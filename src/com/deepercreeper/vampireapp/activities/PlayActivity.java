package com.deepercreeper.vampireapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionController;
import com.deepercreeper.vampireapp.connection.ConnectionListener;
import com.deepercreeper.vampireapp.items.ItemConsumer;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.util.ConnectionUtil;

/**
 * This activity is used to play a character, that was created before and connect to a host.<br>
 * This is the real play process that a non master user can do with this application.
 * 
 * @author vrl
 */
public class PlayActivity extends Activity implements ItemConsumer, ConnectionListener
{
	private static final String		TAG					= "PlayActivity";
	
	/**
	 * The extra key for the character that is going to be played.
	 */
	public static final String		CHARACTER			= "CHARACTER";
	
	/**
	 * The request code for playing a character.
	 */
	public static final int			PLAY_CHAR_REQUEST	= 2;
	
	private ConnectionController	mConnection;
	
	private ItemProvider			mItems;
	
	private CharacterInstance		mChar;
	
	@Override
	public void cancel()
	{
		exit(true);
	}
	
	@Override
	public void connectedTo(final ConnectedDevice aDevice)
	{
		aDevice.send(MessageType.LOGIN, mChar.getName());
	}
	
	@Override
	public void connectionEnabled(final boolean aEnabled)
	{
		if ( !aEnabled)
		{
			exit(true);
		}
	}
	
	@Override
	public void consumeItems(final ItemProvider aItems)
	{
		mItems = aItems;
		
		init();
	}
	
	@Override
	public void disconnectedFrom(final ConnectedDevice aDevice)
	{
		// TODO Tell user that he was disconnected
		exit(true);
	}
	
	public void exit(final boolean aSaveCharacter)
	{
		// TODO Maybe add a negative exit
		mConnection.exit();
		final Intent intent = new Intent();
		intent.putExtra(CHARACTER, mChar.serialize());
		setResult(aSaveCharacter ? RESULT_OK : RESULT_CANCELED, intent);
		finish();
	}
	
	@Override
	public void onBackPressed()
	{
		exit(true);
	}
	
	@Override
	public void receiveMessage(final ConnectedDevice aDevice, final MessageType aType, final String[] aArgs)
	{
		switch (aType)
		{
			case DECLINE :
				exit(true);
				break;
			case WAIT :
				Toast.makeText(this, R.string.host_busy, Toast.LENGTH_SHORT).show();
				exit(true);
				break;
			case NAME_IN_USE :
				Toast.makeText(this, R.string.name_in_use, Toast.LENGTH_SHORT).show();
				exit(true);
				break;
			default :
				break;
		}
		// TODO Implement
	}
	
	@Override
	protected void onCreate(final Bundle aSavedInstanceState)
	{
		super.onCreate(aSavedInstanceState);
		
		ConnectionUtil.loadItems(this, this);
	}
	
	private void init()
	{
		mConnection = new ConnectionController(this, this);
		mConnection.connect(this);
		
		final String xml = getIntent().getStringExtra(CreateCharActivity.CHARACTER);
		CharacterInstance character = null;
		try
		{
			character = new CharacterInstance(xml, mItems, this);
		}
		catch (final IllegalArgumentException e)
		{
			Log.e(TAG, "Could not create character from xml.");
			exit(false);
		}
		if (character != null)
		{
			mChar = character;
		}
		
		mChar.release();
		
		setContentView(R.layout.play_lobby);
		
		final TextView charName = (TextView) findViewById(R.id.char_name);
		final LinearLayout controllersPanel = (LinearLayout) findViewById(R.id.controllers_panel);
		final Button exit = (Button) findViewById(R.id.exit);
		
		mChar.update();
		mChar.init();
		
		controllersPanel.addView(mChar.getEPHandler().getContainer());
		
		controllersPanel.addView(mChar.getHealth().getContainer());
		
		controllersPanel.addView(mChar.getMoney().getContainer());
		
		for (final ItemControllerInstance controller : mChar.getControllers())
		{
			if (controller.hasAnyItem())
			{
				controllersPanel.addView(controller.getContainer());
				controller.close();
			}
		}
		
		controllersPanel.addView(mChar.getInventory().getContainer());
		
		charName.setText(mChar.getName());
		
		exit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				exit(true);
			}
		});
	}
}
