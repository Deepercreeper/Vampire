package com.deepercreeper.vampireapp.activities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import com.deepercreeper.vampireapp.host.Message;
import com.deepercreeper.vampireapp.host.Player;
import com.deepercreeper.vampireapp.host.change.CharacterChange;
import com.deepercreeper.vampireapp.host.change.EPChange;
import com.deepercreeper.vampireapp.host.change.GenerationChange;
import com.deepercreeper.vampireapp.host.change.HealthChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.host.change.MoneyChange;
import com.deepercreeper.vampireapp.items.ItemConsumer;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.mechanics.TimeListener.Type;
import com.deepercreeper.vampireapp.util.ConnectionUtil;
import com.deepercreeper.vampireapp.util.ContactsUtil;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.view.ResizeListener;

/**
 * This activity is used to play a character, that was created before and connect to a host.<br>
 * This is the real play process that a non master user can do with this application.
 * 
 * @author vrl
 */
public class PlayActivity extends Activity implements ItemConsumer, ConnectionListener, MessageListener, ResizeListener
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
	
	private LinearLayout			mMessageList;
	
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
		aDevice.send(MessageType.LOGIN, ContactsUtil.getPhoneNumber(this), FilesUtil.serialize(mChar));
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
	public void sendMessage(final Message aMessage)
	{
		mConnection.getHost().send(MessageType.MESSAGE, FilesUtil.serialize(aMessage));
	}
	
	@Override
	public void applyMessage(final Message aMessage)
	{
		// TODO Implement
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
		intent.putExtra(CHARACTER, FilesUtil.serialize(mChar));
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
			case UPDATE :
				applyChange(aArgs[0], aArgs[1]);
				break;
			case MESSAGE :
				final Message message = Message.deserialize(aArgs[0], this, this);
				mMessageList.addView(message.getContainer());
				applyMessage(message);
			default :
				break;
		}
		// TODO Implement
	}
	
	@Override
	public void applyChange(final String aChange, final String aType)
	{
		final Document doc = FilesUtil.loadDocument(aChange);
		final Element element = (Element) doc.getElementsByTagName(aType).item(0);
		CharacterChange change = null;
		if (aType.equals(HealthChange.TAG_NAME))
		{
			change = new HealthChange(element);
		}
		else if (aType.equals(EPChange.TAG_NAME))
		{
			change = new EPChange(element);
		}
		else if (aType.equals(GenerationChange.TAG_NAME))
		{
			change = new GenerationChange(element);
		}
		else if (aType.equals(MoneyChange.TAG_NAME))
		{
			change = new MoneyChange(element);
		}
		
		// TODO Add other changes
		
		if (change != null)
		{
			change.applyChange(mChar);
		}
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
					findViewById(android.R.id.content).setBackgroundColor(Color.DKGRAY);
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
					findViewById(android.R.id.content).setBackgroundColor(Color.BLACK);
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
	
	@Override
	public void sendChange(final CharacterChange aChange)
	{
		mConnection.getHost().send(MessageType.UPDATE, FilesUtil.serialize(aChange), aChange.getType());
	}
	
	@Override
	public void resize()
	{}
	
	private void init()
	{
		mConnection = new ConnectionController(this, this, mHandler);
		mConnection.connect(this);
		
		final String xml = getIntent().getStringExtra(CreateCharActivity.CHARACTER);
		CharacterInstance character = null;
		try
		{
			character = new CharacterInstance(xml, mItems, this, this, null, false);
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
		
		setContentView(R.layout.play_activity);
		
		final TextView charName = (TextView) findViewById(R.id.pa_char_name_label);
		mMessageList = (LinearLayout) findViewById(R.id.pa_message_list);
		final LinearLayout controllersPanel = (LinearLayout) findViewById(R.id.pa_controllers_list);
		final Button exit = (Button) findViewById(R.id.pa_exit_button);
		
		mChar.update();
		mChar.init();
		
		controllersPanel.addView(mChar.getEPController().getContainer());
		
		controllersPanel.addView(mChar.getHealth().getContainer());
		
		controllersPanel.addView(mChar.getGenerationController().getContainer());
		
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
