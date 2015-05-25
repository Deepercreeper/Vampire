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
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageListener;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionController;
import com.deepercreeper.vampireapp.connection.ConnectionController.BluetoothConnectionListener;
import com.deepercreeper.vampireapp.connection.ConnectionController.ConnectionStateListener;
import com.deepercreeper.vampireapp.host.Host;
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
public class HostActivity extends Activity implements ItemConsumer, ConnectionStateListener, BluetoothConnectionListener, MessageListener
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
	public void connectedTo(final ConnectedDevice aDevice)
	{
		// TODO Implement
	}
	
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
	public void receiveMessage(final ConnectedDevice aDevice, final MessageType aType, final String[] aArgs)
	{
		switch (aType)
		{
			case LOGIN :
				if (YesNoDialog.isDialogOpen())
				{
					aDevice.send(MessageType.WAIT);
				}
				else
				{
					final YesNoListener listener = new YesNoListener()
					{
						@Override
						public void select(final boolean aYes)
						{
							aDevice.send(aYes ? MessageType.ACCEPT : MessageType.DECLINE);
						}
					};
					YesNoDialog.showYesNoDialog(aArgs[0], getString(R.string.new_player), this, listener);
				}
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
	protected void onDestroy()
	{
		mConnection.unregister();
		super.onDestroy();
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
	
	private void exit()
	{
		mConnection.stopServer();
		mConnection.unregister();
		final Intent intent = new Intent();
		intent.putExtra(HOST, mHost.serialize());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	private void init()
	{
		mConnection = new ConnectionController(this, this);
		mConnection.addConnectionListener(this);
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
}
