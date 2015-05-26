package com.deepercreeper.vampireapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionController;
import com.deepercreeper.vampireapp.connection.ConnectionListener;
import com.deepercreeper.vampireapp.host.Host;
import com.deepercreeper.vampireapp.host.Player;
import com.deepercreeper.vampireapp.items.ItemConsumer;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.ConnectionUtil;
import com.deepercreeper.vampireapp.util.view.YesNoDialog;
import com.deepercreeper.vampireapp.util.view.YesNoDialog.YesNoListener;

/**
 * This activity represents the running host game. It is able to accept new players<br>
 * and handles the whole vampire game.
 * 
 * @author Vincent
 */
public class HostActivity extends Activity implements ItemConsumer, ConnectionListener
{
	/**
	 * The request code for starting a host game.
	 */
	public static final int			PLAY_HOST_REQUEST	= 3;
	
	/**
	 * The extra key for the hosts name.
	 */
	public static final String		HOST				= "HOST";
	
	private ConnectionController	mConnection;
	
	private Host					mHost;
	
	private ItemProvider			mItems;
	
	@Override
	public void cancel()
	{}
	
	@Override
	public void connectedTo(final ConnectedDevice aDevice)
	{}
	
	@Override
	public void connectionEnabled(final boolean aEnabled)
	{
		if ( !aEnabled)
		{
			exit();
		}
	}
	
	@Override
	public void consumeItems(final ItemProvider aItems)
	{
		mItems = aItems;
		mHost = new Host(getIntent().getStringExtra(HOST), mItems);
		
		init();
	}
	
	@Override
	public void disconnectedFrom(final ConnectedDevice aDevice)
	{
		mHost.removePlayer(aDevice);
		// TODO clean up
	}
	
	public void exit()
	{
		mConnection.exit();
		final Intent intent = new Intent();
		intent.putExtra(HOST, mHost.serialize());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	@Override
	public void onBackPressed()
	{
		exit();
	}
	
	@Override
	public void receiveMessage(final ConnectedDevice aDevice, final MessageType aType, final String[] aArgs)
	{
		switch (aType)
		{
			case LOGIN :
				login(aDevice, aArgs[0]);
				break;
			
			default :
				break;
		}
	}
	
	@Override
	protected void onCreate(final Bundle aSavedInstanceState)
	{
		super.onCreate(aSavedInstanceState);
		
		ConnectionUtil.loadItems(this, this);
	}
	
	@Override
	protected void onResume()
	{
		if (mConnection != null)
		{
			mConnection.checkConnectionState();
		}
		super.onResume();
	}
	
	private void init()
	{
		mConnection = new ConnectionController(this, this);
		mConnection.startServer(this);
		
		setContentView(R.layout.host);
		
		final TextView name = (TextView) findViewById(R.id.host_name);
		final Button exit = (Button) findViewById(R.id.exit);
		
		name.setText(mHost.getName());
		exit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				exit();
			}
		});
	}
	
	private void login(final ConnectedDevice aDevice, final String aPlayer)
	{
		if (YesNoDialog.isDialogOpen())
		{
			aDevice.send(MessageType.WAIT);
			return;
		}
		
		final YesNoListener listener = new YesNoListener()
		{
			@Override
			public void select(final boolean aYes)
			{
				if (aYes)
				{
					if ( !mHost.addPlayer(new Player(aPlayer, aDevice)))
					{
						aDevice.send(MessageType.NAME_IN_USE);
					}
					else
					{
						aDevice.send(MessageType.ACCEPT);
					}
				}
				else
				{
					aDevice.send(MessageType.DECLINE);
				}
			}
		};
		YesNoDialog.showYesNoDialog(aPlayer, getString(R.string.new_player), this, listener);
	}
}
