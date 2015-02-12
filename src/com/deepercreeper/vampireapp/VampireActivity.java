package com.deepercreeper.vampireapp;

import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.CharController;
import com.deepercreeper.vampireapp.character.CharacterInstance;
import com.deepercreeper.vampireapp.util.ConnectionUtil;

/**
 * The main activity is the start class for the vampire app.<br>
 * This just handles inputs and passes them to the vampire.
 * 
 * @author vrl
 */
public class VampireActivity extends Activity
{
	private static final String	TAG	= "VampireActivity";
	
	private CharController		mChars;
	
	private ItemProvider		mItems;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mItems = ConnectionUtil.loadItems(this);
		
		mChars = new CharController(this, mItems);
		
		setContentView(R.layout.activity_main);
		
		final Button createChar = (Button) findViewById(R.id.create_character_button);
		createChar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				createChar();
			}
		});
		
		mChars.setCharsList((LinearLayout) findViewById(R.id.characters_list));
		mChars.loadCharCompounds();
		mChars.sortChars();
	}
	
	@Override
	protected void onActivityResult(final int aRequestCode, final int aResultCode, final Intent aData)
	{
		if (aRequestCode == CreateCharActivity.CREATE_CHAR_REQUEST && aResultCode == RESULT_OK)
		{
			final String xml = aData.getStringExtra(CreateCharActivity.CHARACTER);
			CharacterInstance character = null;
			try
			{
				character = new CharacterInstance(xml, getItems(), this);
			}
			catch (final IOException e)
			{
				Log.e(TAG, "Could not create character from xml.");
			}
			if (character != null)
			{
				mChars.addChar(character);
			}
		}
	}
	
	private void createChar()
	{
		final Intent intent = new Intent(this, CreateCharActivity.class);
		intent.putExtra(CreateCharActivity.CHAR_NAMES, mChars.getCharNames());
		startActivityForResult(intent, CreateCharActivity.CREATE_CHAR_REQUEST);
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		final int id = item.getItemId();
		if (id == R.id.delete_chars)
		{
			mChars.deleteChars();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public ItemProvider getItems()
	{
		return mItems;
	}
}
