package com.deepercreeper.vampireapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
	
	private Handler					mHandler;
	
	private ConnectionController	mConnection;
	
	private Host					mHost;
	
	private ItemProvider			mItems;
	
	@Override
	public void banned(final ConnectedDevice aDevice)
	{
		mHost.ban(aDevice);
		aDevice.send(MessageType.BANNED);
		aDevice.exit();
	}
	
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
	
	/**
	 * Closes the host activity after cleaning up connections.
	 */
	public void exit()
	{
		mConnection.exit();
		final Intent intent = new Intent();
		intent.putExtra(HOST, mHost.serialize());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	@Override
	public void makeText(final int aResId, final int aDuration)
	{
		Toast.makeText(HostActivity.this, aResId, aDuration).show();
	}
	
	@Override
	public void makeText(final String aText, final int aDuration)
	{
		Toast.makeText(HostActivity.this, aText, aDuration).show();
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
			case LEFT_GAME :
				makeText(mHost.getPlayer(aDevice).getName() + " " + getString(R.string.left_game), Toast.LENGTH_SHORT);
				mConnection.disconnect(aDevice);
				break;
			case AFK :
				mHost.getPlayer(aDevice).setAFK(true);
				break;
			case BACK :
				mHost.getPlayer(aDevice).setAFK(false);
				break;
			default :
				break;
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
		mConnection = new ConnectionController(this, this, mHandler);
		mConnection.startServer(this);
		
		setContentView(R.layout.host);
		
		final TextView name = (TextView) findViewById(R.id.host_name);
		mHost.setPlayersList((LinearLayout) findViewById(R.id.players_list));
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
		if ( !mHost.addPlayer(new Player(aPlayer, aDevice, this, this)))
		{
			if (mHost.isBanned(aDevice))
			{
				aDevice.send(MessageType.BANNED);
			}
			else
			{
				aDevice.send(MessageType.NAME_IN_USE);
			}
			mConnection.disconnect(aDevice);
		}
		else
		{
			aDevice.send(MessageType.ACCEPT);
		}
	}
}
