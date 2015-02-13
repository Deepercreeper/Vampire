package com.deepercreeper.vampireapp.character;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.view.CharacterContextMenu.CharacterListener;

public class CharController implements CharacterListener
{
	private static final String						TAG						= "CharController";
	
	private static final String						CHARACTER_LIST			= "Chars.lst";
	
	private final List<CharacterCompound>			mCharacterCompoundsList	= new ArrayList<CharacterCompound>();
	
	private final Map<String, CharacterCompound>	mCharacterCompounds		= new HashMap<String, CharacterCompound>();
	
	private final Map<String, CharacterInstance>	mCharacterCache			= new HashMap<String, CharacterInstance>();
	
	private final Activity							mContext;
	
	private final ItemProvider						mItems;
	
	private LinearLayout							mCharsList;
	
	public CharController(final Activity aContext, final ItemProvider aItems)
	{
		mContext = aContext;
		mItems = aItems;
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
		String data = null;
		InputStreamReader reader = null;
		try
		{
			reader = new InputStreamReader(mContext.openFileInput(CHARACTER_LIST));
			final StringBuilder list = new StringBuilder();
			int c;
			while ((c = reader.read()) != -1)
			{
				list.append((char) c);
			}
			data = list.toString();
		}
		catch (final FileNotFoundException e)
		{
			Log.i(TAG, "No characters saved.");
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not load characters list.");
		}
		try
		{
			if (reader != null)
			{
				reader.close();
			}
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not close reader.");
		}
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
		for (final CharacterCompound charCompound : mCharacterCompoundsList)
		{
			mCharsList.addView(charCompound.getContainer());
		}
	}
	
	public void addChar(final CharacterInstance aCharacter)
	{
		try
		{
			final String xml = aCharacter.serialize();
			final PrintWriter writer = new PrintWriter(mContext.openFileOutput(aCharacter.getName() + ".chr", Context.MODE_PRIVATE));
			writer.append(xml);
			writer.flush();
			writer.close();
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not open file stream.");
		}
		mCharacterCache.put(aCharacter.getName(), aCharacter);
		
		final CharacterCompound charCompound = new CharacterCompound(aCharacter, this, mContext);
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
		OutputStreamWriter writer = null;
		try
		{
			writer = new OutputStreamWriter(mContext.openFileOutput(CHARACTER_LIST, Context.MODE_PRIVATE));
			writer.append(characterNames.toString());
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not save characters list.");
		}
		if (writer != null)
		{
			try
			{
				writer.close();
			}
			catch (final IOException e)
			{
				Log.e(TAG, "Could not close stream.");
			}
		}
	}
	
	@Override
	public void play(final String aName)
	{
		CharacterInstance character = mCharacterCache.get(aName);
		if (character == null)
		{
			try
			{
				final StringBuilder xml = new StringBuilder();
				final FileInputStream stream = mContext.openFileInput(aName + ".chr");
				int c;
				while ((c = stream.read()) != -1)
				{
					xml.append((char) c);
				}
				character = new CharacterInstance(xml.toString(), mItems, mContext);
			}
			catch (final IOException e)
			{
				Log.e(TAG, "Could not load character.");
			}
		}
		if (character == null)
		{
			return;
		}
		mCharacterCache.put(character.getName(), character);
		Toast.makeText(mContext, character.getConcept(), Toast.LENGTH_SHORT).show();
		sortChars();
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
