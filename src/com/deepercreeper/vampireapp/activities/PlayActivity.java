package com.deepercreeper.vampireapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
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
import com.deepercreeper.vampireapp.host.Player;
import com.deepercreeper.vampireapp.items.ItemConsumer;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.mechanics.TimeListener.Type;
import com.deepercreeper.vampireapp.util.ConnectionUtil;
import com.deepercreeper.vampireapp.util.ContactsUtil;

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
	
	private Handler					mHandler;
	
	private ConnectionController	mConnection;
	
	private ItemProvider			mItems;
	
	private CharacterInstance		mChar;
	
	@Override
	public void banned(final Player aPlayer)
	{
		makeText(R.string.banned, Toast.LENGTH_SHORT);
		exit(true);
	}
	
	@Override
	public void cancel()
	{
		if (mConnection.hasHost())
		{
			mConnection.sendToAll(MessageType.LEFT_GAME);
		}
		exit(true);
	}
	
	@Override
	public void connectedTo(final ConnectedDevice aDevice)
	{
		aDevice.send(MessageType.LOGIN, ContactsUtil.getPhoneNumber(this), mChar.serialize());
	}
	
	@Override
	public void connectionEnabled(final boolean aEnabled)
	{
		if ( !aEnabled)
		{
			if (mConnection.hasHost())
			{
				mConnection.sendToAll(MessageType.LEFT_GAME);
			}
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
	{}
	
	/**
	 * Closes the play activity after closing the connection.
	 * 
	 * @param aSaveCharacter
	 *            Whether the current character should be saved.
	 */
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
	public void makeText(final int aResId, final int aDuration)
	{
		Toast.makeText(PlayActivity.this, aResId, aDuration).show();
	}
	
	@Override
	public void makeText(final String aText, final int aDuration)
	{
		Toast.makeText(PlayActivity.this, aText, aDuration).show();
	}
	
	@Override
	public void onBackPressed()
	{
		if (mConnection.hasHost())
		{
			mConnection.sendToAll(MessageType.LEFT_GAME);
		}
		exit(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(final Menu aMenu)
	{
		getMenuInflater().inflate(R.menu.play, aMenu);
		return true;
	}
	
	@Override
	public void receiveMessage(final ConnectedDevice aDevice, final MessageType aType, final String[] aArgs)
	{
		switch (aType)
		{
			case ACCEPT :
				setEnabled(true);
				break;
			case NAME_IN_USE :
				makeText(R.string.name_in_use, Toast.LENGTH_SHORT);
				exit(true);
				break;
			case KICKED :
				makeText(R.string.kicked, Toast.LENGTH_SHORT);
				exit(true);
				break;
			case CLOSED :
				makeText(R.string.host_closed, Toast.LENGTH_SHORT);
				exit(true);
				break;
			case BANNED :
				banned(null);
				break;
			case TIME :
				makeText("Time changed: " + aArgs[0] + " " + aArgs[1], Toast.LENGTH_SHORT);
				mChar.time(Type.valueOf(aArgs[0]), Integer.parseInt(aArgs[1]));
				break;
			default :
				break;
		}
		// TODO Implement
	}
	
	/**
	 * Disables or enables the whole activity.
	 * 
	 * @param aEnabled
	 *            Whether the user should be able to do anything.
	 */
	public void setEnabled(final boolean aEnabled)
	{
		if ( !aEnabled)
		{
			mHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
					findViewById(R.id.play_container).setBackgroundColor(Color.DKGRAY);
				}
			});
		}
		else
		{
			mHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
					findViewById(R.id.play_container).setBackgroundColor(Color.BLACK);
				}
			});
		}
	}
	
	@Override
	protected void onCreate(final Bundle aSavedInstanceState)
	{
		super.onCreate(aSavedInstanceState);
		
		mHandler = new Handler();
		
		ConnectionUtil.loadItems(this, this);
	}
	
	@Override
	protected void onPause()
	{
		if (mConnection != null && mConnection.hasHost())
		{
			mConnection.getHost().send(MessageType.AFK);
		}
		
		super.onPause();
	}
	
	@Override
	protected void onResume()
	{
		if (mConnection != null && mConnection.hasHost())
		{
			mConnection.getHost().send(MessageType.BACK);
		}
		
		super.onResume();
	}
	
	private void init()
	{
		mConnection = new ConnectionController(this, this, mHandler);
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
		}
		if (character != null)
		{
			mChar = character;
		}
		else
		{
			exit(false);
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
				if (mConnection.hasHost())
				{
					mConnection.sendToAll(MessageType.LEFT_GAME);
				}
				exit(true);
			}
		});
		
		setEnabled(false);
	}
}
