package com.deepercreeper.vampireapp.activities;

import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionController;
import com.deepercreeper.vampireapp.connection.ConnectionListener;
import com.deepercreeper.vampireapp.host.BannedPlayer;
import com.deepercreeper.vampireapp.host.Host;
import com.deepercreeper.vampireapp.host.Message;
import com.deepercreeper.vampireapp.host.Player;
import com.deepercreeper.vampireapp.items.ItemConsumer;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.mechanics.TimeListener.Type;
import com.deepercreeper.vampireapp.util.ConnectionUtil;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.view.dialogs.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.dialogs.SelectItemDialog.SelectionListener;

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
	public void banned(final Player aPlayer)
	{
		mHost.ban(aPlayer);
		final ConnectedDevice device = aPlayer.getDevice();
		device.send(MessageType.BANNED);
		device.exit();
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
		mHost = new Host(getIntent().getStringExtra(HOST), mItems, this);
		
		init();
	}
	
	@Override
	public void disconnectedFrom(final ConnectedDevice aDevice)
	{
		mHost.removePlayer(aDevice);
	}
	
	/**
	 * Closes the host activity after cleaning up connections.
	 */
	public void exit()
	{
		mConnection.sendToAll(MessageType.CLOSED);
		mConnection.exit();
		final Intent intent = new Intent();
		intent.putExtra(HOST, FilesUtil.serialize(mHost));
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
	public boolean onCreateOptionsMenu(final Menu aMenu)
	{
		getMenuInflater().inflate(R.menu.host, aMenu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem aItem)
	{
		final int id = aItem.getItemId();
		switch (id)
		{
			case R.id.unban :
				unban();
				return true;
		}
		
		return super.onOptionsItemSelected(aItem);
	}
	
	private void unban()
	{
		final List<BannedPlayer> players = mHost.getBannedPlayers();
		if (players.isEmpty())
		{
			makeText(R.string.no_banned_players, Toast.LENGTH_SHORT);
			return;
		}
		final SelectionListener<BannedPlayer> action = new SelectionListener<BannedPlayer>()
		{
			@Override
			public void select(final BannedPlayer aPlayer)
			{
				mHost.unban(aPlayer);
			}
			
			@Override
			public void cancel()
			{}
		};
		SelectItemDialog.<BannedPlayer> showSelectionDialog(players, getString(R.string.unban), this, action);
	}
	
	@Override
	public void receiveMessage(final ConnectedDevice aDevice, final MessageType aType, final String[] aArgs)
	{
		final Player player = mHost.getPlayer(aDevice);
		switch (aType)
		{
			case LOGIN :
				login(aDevice, aArgs[0], aArgs[1]);
				break;
			case LEFT_GAME :
				makeText(player.getName() + " " + getString(R.string.left_game), Toast.LENGTH_SHORT);
				mConnection.disconnect(aDevice);
				break;
			case AFK :
				player.setAFK(true);
				break;
			case BACK :
				player.setAFK(false);
				break;
			case UPDATE :
				player.applyChange(aArgs[0], aArgs[1]);
				break;
			case MESSAGE :
				final Message message = Message.deserialize(aArgs[0], this, player);
				mHost.addMessage(message);
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
		mConnection.startServer();
		
		setContentView(R.layout.host_activity);
		
		setTitle(mHost.getName());
		final EditText timeSetter = (EditText) findViewById(R.id.ha_time_text);
		mHost.setPlayersList((LinearLayout) findViewById(R.id.ha_players_list));
		mHost.setPlayersTimeList((LinearLayout) findViewById(R.id.ha_players_time_list));
		mHost.setMessageList((LinearLayout) findViewById(R.id.ha_message_list));
		final Button applyTime = (Button) findViewById(R.id.ha_apply_time_button);
		final Button day = (Button) findViewById(R.id.ha_day_button);
		final Button hour = (Button) findViewById(R.id.ha_hour_button);
		final Button round = (Button) findViewById(R.id.ha_round_button);
		final Button exit = (Button) findViewById(R.id.ha_exit_button);
		
		day.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mHost.time(Type.DAY, 1);
			}
		});
		hour.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mHost.time(Type.HOUR, 1);
			}
		});
		round.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mHost.time(Type.ROUND, 1);
			}
		});
		applyTime.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				int value = -1;
				try
				{
					value = Integer.parseInt(timeSetter.getText().toString());
				}
				catch (final NumberFormatException e)
				{}
				if (value >= 0 && value <= 23)
				{
					mHost.time(Type.SET, value);
				}
				else
				{
					timeSetter.setText("");
				}
			}
		});
		exit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				exit();
			}
		});
	}
	
	private void login(final ConnectedDevice aDevice, final String aNumber, final String aCharacter)
	{
		final Player player = new Player(aCharacter, aNumber, aDevice, this, this, mItems);
		if ( !mHost.addPlayer(player))
		{
			if (mHost.isBanned(player))
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
