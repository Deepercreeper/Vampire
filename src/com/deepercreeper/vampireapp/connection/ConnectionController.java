package com.deepercreeper.vampireapp.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;
import android.os.Handler;
import android.widget.Toast;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.host.Player;
import com.deepercreeper.vampireapp.util.BluetoothReceiver;
import com.deepercreeper.vampireapp.util.BluetoothReceiver.BluetoothListener;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.view.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.SelectItemDialog.SelectionListener;

/**
 * This controller handles all connection actions like creating an open Bluetooth port<br>
 * for other devices or connecting an searching for open Bluetooth ports.
 * 
 * @author vrl
 */
public class ConnectionController implements ConnectionListener
{
	private static final String			TAG					= "ConnectionController";
	
	private static final UUID			DEFAULT_UUID		= UUID.fromString("c155da2d-302d-4f10-bc3b-fa277af3599d");
	
	private final BluetoothReceiver		mReceiver			= new BluetoothReceiver();
	
	private final Activity				mContext;
	
	private final List<ConnectedDevice>	mDevices			= new ArrayList<ConnectedDevice>();
	
	private final ConnectionListener	mConnectionListener;
	
	private final Handler				mHandler;
	
	private BluetoothAdapter			mBluetoothAdapter;
	
	private BluetoothServerSocket		mInsecureSocket		= null;
	
	private BluetoothServerSocket		mSecureSocket		= null;
	
	private boolean						mBluetooth			= true;
	
	private boolean						mCheckingForDevies	= false;
	
	/**
	 * Creates a new connection controller for the given activity.
	 * 
	 * @param aContext
	 *            The underlying context.
	 * @param aConnectionListener
	 *            The listener for message, Bluetooth and connection events.
	 * @param aHandler
	 *            A UI handler.
	 */
	public ConnectionController(final Activity aContext, final ConnectionListener aConnectionListener, final Handler aHandler)
	{
		mContext = aContext;
		mHandler = aHandler;
		mConnectionListener = aConnectionListener;
		init();
	}
	
	@Override
	public void banned(final Player aPlayer)
	{}
	
	@Override
	public void cancel()
	{
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mConnectionListener.cancel();
			}
		});
	}
	
	/**
	 * Checks the used connection even if nothing changed.
	 */
	public void checkConnectionState()
	{
		if (mBluetooth)
		{
			checkBluetoothState();
		}
		else
		{
			checkNetworkState();
		}
	}
	
	/**
	 * Tries to connect to any of the user paired devices and logs in if possible.
	 * 
	 * @param aListener
	 *            The listener that is called if the connection was successful.
	 */
	public void connect(final ConnectionListener aListener)
	{
		// TODO Make this whole fuck'n thing stable
		final List<Device> devices = new ArrayList<Device>();
		for (final BluetoothDevice device : mBluetoothAdapter.getBondedDevices())
		{
			devices.add(new Device(device));
			// TODO Render the given bluetooth name
		}
		final SelectionListener<Device> listener = new SelectionListener<Device>()
		{
			@Override
			public void cancel()
			{
				mReceiver.removeDeviceListener(BluetoothDevice.ACTION_FOUND);
				ConnectionController.this.cancel();
			}
			
			@Override
			public void select(final Device aDevice)
			{
				mReceiver.removeDeviceListener(BluetoothDevice.ACTION_FOUND);
				connectTo(aDevice, aListener);
			}
		};
		final SelectItemDialog<Device> dialog = SelectItemDialog.createSelectionDialog(devices, mContext.getString(R.string.choose_host), mContext,
				listener);
		mReceiver.setDeviceListener(BluetoothDevice.ACTION_FOUND, new BluetoothListener()
		{
			@Override
			public void action(final BluetoothDevice aDevice)
			{
				dialog.addOption(new Device(aDevice));
			}
		});
		if ( !mBluetoothAdapter.startDiscovery())
		{
			for (final BluetoothDevice device : mBluetoothAdapter.getBondedDevices())
			{
				devices.add(new Device(device));
			}
			if (devices.isEmpty())
			{
				mConnectionListener.makeText(R.string.no_paired_device, Toast.LENGTH_SHORT);
				return;
			}
		}
		dialog.show(mContext.getFragmentManager(), mContext.getString(R.string.choose_host));
	}
	
	@Override
	public void connectedTo(final ConnectedDevice aDevice)
	{
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mConnectionListener.connectedTo(aDevice);
			}
		});
	}
	
	@Override
	public void connectionEnabled(final boolean aEnabled)
	{
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mConnectionListener.connectionEnabled(aEnabled);
			}
		});
	}
	
	/**
	 * Connects to the device with the given name.
	 * 
	 * @param aDevice
	 *            The device name.
	 * @param aListener
	 *            The Bluetooth listener that is called if the device was connected.
	 */
	public void connectTo(final Device aDevice, final ConnectionListener aListener)
	{
		final BluetoothDevice device = aDevice.getDevice();
		BluetoothSocket socket = null;
		try
		{
			socket = device.createInsecureRfcommSocketToServiceRecord(DEFAULT_UUID);
		}
		catch (final IOException e)
		{}
		if (socket == null)
		{
			try
			{
				socket = device.createRfcommSocketToServiceRecord(DEFAULT_UUID);
			}
			catch (final IOException e)
			{}
		}
		boolean connected = false;
		if (socket != null)
		{
			try
			{
				socket.connect();
			}
			catch (final IOException e)
			{}
			if (socket.isConnected())
			{
				try
				{
					final ConnectedDevice connectedDevice = new ConnectedDevice(socket, this, true);
					mDevices.add(connectedDevice);
					aListener.connectedTo(connectedDevice);
					connected = true;
				}
				catch (final IOException e)
				{}
			}
		}
		if ( !connected)
		{
			cancel();
			Log.w(TAG, "Could not connect to device.");
		}
	}
	
	/**
	 * Disconnects the given device. Just invokes {@link ConnectedDevice#exit()}.
	 * 
	 * @param aDevice
	 *            The device to disconnect.
	 */
	public void disconnect(final ConnectedDevice aDevice)
	{
		aDevice.exit();
	}
	
	@Override
	public void disconnectedFrom(final ConnectedDevice aDevice)
	{
		mDevices.remove(aDevice);
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mConnectionListener.disconnectedFrom(aDevice);
			}
		});
	}
	
	/**
	 * Unregisters all receivers and stops the connection.
	 */
	public void exit()
	{
		final Set<ConnectedDevice> devices = new HashSet<ConnectedDevice>();
		devices.addAll(mDevices);
		mDevices.clear();
		for (final ConnectedDevice device : devices)
		{
			if (device.isHost())
			{
				device.send(MessageType.LEFT_GAME);
			}
			else
			{
				device.send(MessageType.CLOSED);
			}
			disconnect(device);
		}
		stopServer();
		try
		{
			mContext.unregisterReceiver(mReceiver);
		}
		catch (final IllegalArgumentException e)
		{}
	}
	
	/**
	 * @return the connected host or {@code null} if no host is connected.
	 */
	public ConnectedDevice getHost()
	{
		for (final ConnectedDevice device : mDevices)
		{
			if (device.isHost())
			{
				return device;
			}
		}
		return null;
	}
	
	/**
	 * @return whether this device is able to create Bluetooth connections.
	 */
	public boolean hasBluetooth()
	{
		return mBluetoothAdapter != null;
	}
	
	/**
	 * @return whether this connection controller is connected to a host.
	 */
	public boolean hasHost()
	{
		for (final ConnectedDevice device : mDevices)
		{
			if (device.isHost())
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Initializes all adapters and adds listeners for device changes.
	 */
	public void init()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (mBluetoothAdapter != null)
		{
			final IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			mContext.registerReceiver(mReceiver, filter);
			
			final BluetoothListener listener = new BluetoothListener()
			{
				@Override
				public void action(final BluetoothDevice aDevice)
				{
					checkConnectionState();
				}
			};
			mReceiver.setBluetoothListener(BluetoothAdapter.STATE_ON, listener);
			mReceiver.setBluetoothListener(BluetoothAdapter.STATE_OFF, listener);
		}
	}
	
	/**
	 * @return whether currently Bluetooth is the used connection.
	 */
	public boolean isBluetooth()
	{
		return mBluetooth;
	}
	
	/**
	 * @return whether the used connection is active.
	 */
	public boolean isEnabled()
	{
		if (mBluetooth)
		{
			return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
		}
		// TODO Implement network connection
		return false;
	}
	
	@Override
	public void makeText(final int aResId, final int aDuration)
	{
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mConnectionListener.makeText(aResId, aDuration);
			}
		});
	}
	
	@Override
	public void makeText(final String aText, final int aDuration)
	{
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mConnectionListener.makeText(aText, aDuration);
			}
		});
	}
	
	@Override
	public void receiveMessage(final ConnectedDevice aDevice, final MessageType aType, final String[] aArgs)
	{
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mConnectionListener.receiveMessage(aDevice, aType, aArgs);
			}
		});
	}
	
	/**
	 * Sets whether Bluetooth or the network connection should be used to connect.
	 * 
	 * @param aBluetooth
	 *            {@code true} if Bluetooth should be used and {@code false} if the network should be the connection.
	 */
	public void setBluetooth(final boolean aBluetooth)
	{
		mBluetooth = aBluetooth;
		checkConnectionState();
	}
	
	/**
	 * Allows other devices to connect to this device.
	 * 
	 * @param aListener
	 *            The listener for devices that are being connected.
	 */
	public void startServer(final ConnectionListener aListener)
	{
		mCheckingForDevies = true;
		try
		{
			mInsecureSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(mBluetoothAdapter.getName(), DEFAULT_UUID);
		}
		catch (final IOException e)
		{}
		try
		{
			mSecureSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(mBluetoothAdapter.getName(), DEFAULT_UUID);
		}
		catch (final IOException e)
		{}
		new Thread()
		{
			@Override
			public void run()
			{
				listenForDevices(aListener, true);
			}
		}.start();
		new Thread()
		{
			@Override
			public void run()
			{
				listenForDevices(aListener, false);
			}
		}.start();
	}
	
	/**
	 * Stops listening for devices.
	 */
	public void stopServer()
	{
		mCheckingForDevies = false;
	}
	
	private void checkBluetoothState()
	{
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
		{
			mConnectionListener.makeText("Bluetooth is not enabled! Activate Bluetooth!", Toast.LENGTH_SHORT);
			connectionEnabled(false);
		}
		else
		{
			connectionEnabled(true);
		}
	}
	
	private void checkNetworkState()
	{
		mConnectionListener.makeText("Network connections are not enabled yet!", Toast.LENGTH_SHORT);
	}
	
	private void listenForDevices(final ConnectionListener aListener, final boolean aSecure)
	{
		final BluetoothServerSocket server = aSecure ? mSecureSocket : mInsecureSocket;
		while (mCheckingForDevies && server != null)
		{
			BluetoothSocket socket = null;
			try
			{
				socket = server.accept();
			}
			catch (final IOException e)
			{}
			if (socket != null)
			{
				boolean connected = false;
				try
				{
					final ConnectedDevice connectedDevice = new ConnectedDevice(socket, this, false);
					mDevices.add(connectedDevice);
					aListener.connectedTo(connectedDevice);
					connected = true;
				}
				catch (final IOException e)
				{}
				if ( !connected)
				{
					Log.i(TAG, "Connection listening failed.");
				}
			}
		}
		if (server != null)
		{
			try
			{
				server.close();
			}
			catch (final IOException e)
			{
				Log.e(TAG, "Could not close insecure socket.");
			}
		}
		if (aSecure)
		{
			mSecureSocket = null;
		}
		else
		{
			mInsecureSocket = null;
		}
	}
}
