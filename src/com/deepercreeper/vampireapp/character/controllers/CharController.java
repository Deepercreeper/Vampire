package com.deepercreeper.vampireapp.character.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.activities.PlayActivity;
import com.deepercreeper.vampireapp.character.instance.CharacterCompound;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.ConnectionController;
import com.deepercreeper.vampireapp.util.ConnectionController.BluetoothConnectionListener;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.view.CharacterContextMenu.CharacterListener;

public class CharController implements CharacterListener
{
	private static final String						TAG						= "CharController";
	
	private static final String						CHARACTER_LIST			= "Chars.lst";
	
	private final ConnectionController				mConnection;
	
	private final List<CharacterCompound>			mCharacterCompoundsList	= new ArrayList<CharacterCompound>();
	
	private final Map<String, CharacterCompound>	mCharacterCompounds		= new HashMap<String, CharacterCompound>();
	
	private final Map<String, CharacterInstance>	mCharacterCache			= new HashMap<String, CharacterInstance>();
	
	private final Activity							mContext;
	
	private final ItemProvider						mItems;
	
	private LinearLayout							mCharsList;
	
	public CharController(final Activity aContext, final ItemProvider aItems, final ConnectionController aConnection)
	{
		mContext = aContext;
		mItems = aItems;
		mConnection = aConnection;
	}
	
	public void setCharsList(final LinearLayout aCharsList)
	{
		mCharsList = aCharsList;
	}
	
	public String[] getCharNames()
	{
		final String[] charNames = new String[mCharacterCompoundsList.size()];
		for (int i = 0; i < charNames.length; i++ )
		{
			charNames[i] = mCharacterCompoundsList.get(i).getName();
		}
		return charNames;
	}
	
	public void loadCharCompounds()
	{
		final String data = FilesUtil.loadFile(CHARACTER_LIST, mContext);
		if (data != null && !data.trim().isEmpty())
		{
			for (final String character : data.split("\n"))
			{
				final CharacterCompound charCompound = new CharacterCompound(character, this, mContext);
				mCharacterCompoundsList.add(charCompound);
				mCharacterCompounds.put(charCompound.getName(), charCompound);
			}
			sortChars();
		}
	}
	
	public void sortChars()
	{
		for (final CharacterCompound charCompound : mCharacterCompoundsList)
		{
			charCompound.release();
		}
		
		Collections.sort(mCharacterCompoundsList);
		
		boolean first = true;
		
		for (final CharacterCompound charCompound : mCharacterCompoundsList)
		{
			charCompound.setFirst(first);
			if (first)
			{
				first = false;
			}
			mCharsList.addView(charCompound.getContainer());
		}
	}
	
	public List<CharacterCompound> getCharacterCompoundsList()
	{
		return mCharacterCompoundsList;
	}
	
	public void addChar(final CharacterInstance aCharacter)
	{
		saveChar(aCharacter);
		
		final CharacterCompound charCompound = new CharacterCompound(aCharacter, this, mContext);
		charCompound.getPlayButton().setEnabled(mConnection.isActive());
		mCharacterCompoundsList.add(charCompound);
		mCharacterCompounds.put(charCompound.getName(), charCompound);
		sortChars();
		
		saveCharsList();
	}
	
	private void saveCharsList()
	{
		final StringBuilder characterNames = new StringBuilder();
		for (int i = 0; i < mCharacterCompoundsList.size(); i++ )
		{
			if (i > 0)
			{
				characterNames.append("\n");
			}
			characterNames.append(mCharacterCompoundsList.get(i));
		}
		
		FilesUtil.saveFile(characterNames.toString(), CHARACTER_LIST, mContext);
	}
	
	@Override
	public void play(final String aName)
	{
		final BluetoothConnectionListener listener = new BluetoothConnectionListener()
		{
			@Override
			public void connectedTo(final String aDevice)
			{
				final CharacterInstance character = loadChar(aName);
				
				final Intent intent = new Intent(mContext, PlayActivity.class);
				intent.putExtra(PlayActivity.CHARACTER, character.serialize());
				
				mContext.startActivityForResult(intent, PlayActivity.PLAY_CHAR_REQUEST);
			}
		};
		
		mConnection.connect(listener);
	}
	
	public CharacterInstance loadChar(final String aName)
	{
		CharacterInstance character = mCharacterCache.get(aName);
		if (character == null)
		{
			final String data = FilesUtil.loadFile(aName + ".chr", mContext);
			if (data != null)
			{
				character = new CharacterInstance(data, mItems, mContext);
			}
		}
		if (character == null)
		{
			return null;
		}
		mCharacterCache.put(character.getName(), character);
		return character;
	}
	
	@Override
	public void deleteChar(final String aName)
	{
		deleteCharFile(aName);
		final CharacterCompound charCompound = mCharacterCompounds.get(aName);
		charCompound.release();
		mCharacterCompoundsList.remove(charCompound);
		mCharacterCompounds.remove(aName);
		
		saveCharsList();
	}
	
	public void saveChar(final CharacterInstance aCharacter)
	{
		FilesUtil.saveFile(aCharacter.serialize(), aCharacter.getName() + ".chr", mContext);
		mCharacterCache.put(aCharacter.getName(), aCharacter);
	}
	
	public void updateChar(final CharacterInstance aCharacter)
	{
		saveChar(aCharacter);
		
		CharacterCompound charCompound = mCharacterCompounds.get(aCharacter.getName());
		
		charCompound.release();
		
		mCharacterCompounds.remove(aCharacter.getName());
		mCharacterCompoundsList.remove(charCompound);
		
		charCompound = new CharacterCompound(aCharacter, this, mContext);
		
		mCharacterCompounds.put(aCharacter.getName(), charCompound);
		mCharacterCompoundsList.add(charCompound);
		
		charCompound.use();
		saveCharsList();
		
		sortChars();
	}
	
	public void deleteChars()
	{
		for (final File file : mContext.getFilesDir().listFiles())
		{
			if (file.getName().endsWith(".chr") || file.getName().endsWith("lst"))
			{
				if ( !file.delete())
				{
					Log.e(TAG, "Could not delete file: " + file.getName());
				}
			}
		}
		for (final CharacterCompound charCompound : mCharacterCompoundsList)
		{
			charCompound.release();
		}
		mCharacterCompoundsList.clear();
		mCharacterCompounds.clear();
	}
	
	private void deleteCharFile(final String aName)
	{
		final File charFile = new File(mContext.getFilesDir(), aName + ".chr");
		if ( !charFile.delete())
		{
			Log.e(TAG, "Could not delete character file.");
		}
	}
}
