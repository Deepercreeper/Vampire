package com.deepercreeper.vampireapp.activities;

import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.CharacterInstance;
import com.deepercreeper.vampireapp.items.ItemConsumer;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.ConnectionUtil;

public class PlayActivity extends Activity implements ItemConsumer
{
	private static final String	TAG					= "PlayActivity";
	
	public static final String	CHARACTER			= "CHARACTER";
	
	public static final int		PLAY_CHAR_REQUEST	= 2;
	
	private ItemProvider		mItems;
	
	private CharacterInstance	mChar;
	
	@Override
	protected void onCreate(final Bundle aSavedInstanceState)
	{
		super.onCreate(aSavedInstanceState);
		
		ConnectionUtil.loadItems(this, this);
	}
	
	@Override
	public void consumeItems(final ItemProvider aItems)
	{
		mItems = aItems;
		
		init();
	}
	
	private void init()
	{
		final String xml = getIntent().getStringExtra(CreateCharActivity.CHARACTER);
		CharacterInstance character = null;
		try
		{
			character = new CharacterInstance(xml, mItems, this);
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not create character from xml.");
			setResult(RESULT_CANCELED);
			finish();
		}
		if (character != null)
		{
			mChar = character;
		}
		
		setContentView(R.layout.play_lobby);
		
		final TextView charName = (TextView) findViewById(R.id.char_name);
		charName.setText(mChar.getName());
		
		final Button exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				exit();
			}
		});
	}
	
	private void exit()
	{
		final Intent intent = new Intent();
		intent.putExtra(CHARACTER, mChar.serialize());
		setResult(RESULT_OK, intent);
		finish();
	}
}
