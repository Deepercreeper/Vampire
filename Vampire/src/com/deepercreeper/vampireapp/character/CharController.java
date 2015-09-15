package com.deepercreeper.vampireapp.character;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.activities.ClientActivity;
import com.deepercreeper.vampireapp.character.instance.CharacterCompound;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.view.CharacterContextMenu.CharacterListener;
import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;

/**
 * This controller is used to control all characters that were created before.
 * 
 * @author vrl
 */
public class CharController implements CharacterListener
{
	private static final String TAG = "CharController";
	
	private static final String CHARACTER_LIST = "Chars.lst";
	
	private final List<CharacterCompound> mCharacterCompoundsList = new ArrayList<CharacterCompound>();
	
	private final Map<String, CharacterCompound> mCharacterCompounds = new HashMap<String, CharacterCompound>();
	
	private final Map<String, String> mCharacterCache = new HashMap<String, String>();
	
	private final Activity mContext;
	
	private LinearLayout mCharsList;
	
	/**
	 * Creates a new character controller.
	 * 
	 * @param aContext
	 *            The underlying context.
	 */
	public CharController(final Activity aContext)
	{
		mContext = aContext;
	}
	
	/**
	 * Adds an already created character to the characters list and saves it.
	 * 
	 * @param aCharacter
	 *            The new character.
	 */
	public void addChar(final CharacterInstance aCharacter)
	{
		saveChar(aCharacter);
		
		final CharacterCompound charCompound = new CharacterCompound(aCharacter, this, mContext);
		// TODO Make sure and remove
		// charCompound.setPlayingEnabled(mConnection.isEnabled());
		mCharacterCompoundsList.add(charCompound);
		mCharacterCompounds.put(charCompound.getName(), charCompound);
		sortChars();
		
		saveCharsList();
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
	
	/**
	 * Deletes all characters.
	 */
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
	
	/**
	 * @return a list of character compounds. Each represents a character.
	 */
	public List<CharacterCompound> getCharacterCompoundsList()
	{
		return mCharacterCompoundsList;
	}
	
	/**
	 * @return an array of character names, that were used already.
	 */
	public String[] getCharNames()
	{
		final String[] charNames = new String[mCharacterCompoundsList.size()];
		for (int i = 0; i < charNames.length; i++ )
		{
			charNames[i] = mCharacterCompoundsList.get(i).getName();
		}
		return charNames;
	}
	
	/**
	 * Loads the character with the given name from the saved character file.
	 * 
	 * @param aName
	 *            The character name.
	 * @return the loaded character.
	 */
	public String loadChar(final String aName)
	{
		String character = mCharacterCache.get(aName);
		if (character != null)
		{
			return character;
		}
		character = DataUtil.loadFile(aName + ".chr", mContext);
		if (character != null)
		{
			mCharacterCache.put(aName, character);
			return character;
		}
		return null;
	}
	
	/**
	 * Loads all saved characters and displays them in sorted order to the characters list.
	 */
	public void loadCharCompounds()
	{
		final String data = DataUtil.loadFile(CHARACTER_LIST, mContext);
		if (data != null && !data.trim().isEmpty())
		{
			for (final String character : data.split("\n"))
			{
				final CharacterCompound charCompound = new CharacterCompound(character, this, mContext);
				// TODO Make sure and remove
				// charCompound.setPlayingEnabled(mConnection.isEnabled());
				mCharacterCompoundsList.add(charCompound);
				mCharacterCompounds.put(charCompound.getName(), charCompound);
			}
			sortChars();
		}
	}
	
	@Override
	public void play(final String aName)
	{
		final String character = loadChar(aName);
		
		final Intent intent = new Intent(mContext, ClientActivity.class);
		intent.putExtra(ClientActivity.CHARACTER, character);
		
		mContext.startActivityForResult(intent, ClientActivity.PLAY_CLIENT_REQUEST);
	}
	
	/**
	 * Saves the given character to a character file.
	 * 
	 * @param aCharacter
	 *            The character to save.
	 */
	public void saveChar(final CharacterInstance aCharacter)
	{
		final String character = DataUtil.serialize(aCharacter);
		DataUtil.saveFile(character, aCharacter.getName() + ".chr", mContext);
		mCharacterCache.put(aCharacter.getName(), character);
	}
	
	/**
	 * Links the linear layout, wherein the character compounds should be placed.
	 * 
	 * @param aCharsList
	 *            The characters list.
	 */
	public void setCharsList(final LinearLayout aCharsList)
	{
		mCharsList = aCharsList;
	}
	
	/**
	 * Sorts the characters inside the list. The first sorting order is the use date.<br>
	 * The second is the character name.
	 */
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
	
	/**
	 * Saves the given character and updates all information inside the character compound inside the characters list.
	 * 
	 * @param aCharacter
	 *            The character to update.
	 */
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
	
	private void deleteCharFile(final String aName)
	{
		final File charFile = new File(mContext.getFilesDir(), aName + ".chr");
		if ( !charFile.delete())
		{
			Log.e(TAG, "Could not delete character file.");
		}
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
		
		DataUtil.saveFile(characterNames.toString(), CHARACTER_LIST, mContext);
	}
}
