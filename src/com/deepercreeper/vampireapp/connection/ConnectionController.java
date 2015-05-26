package com.deepercreeper.vampireapp.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;
import android.widget.Toast;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.util.BluetoothReceiver;
import com.deepercreeper.vampireapp.util.BluetoothReceiver.BluetoothListener;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.view.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.SelectItemDialog.NamableSelectionListener;

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
	 */
	public ConnectionController(final Activity aContext, final ConnectionListener aConnectionListener)
	{
		mContext = aContext;
		mConnectionListener = aConnectionListener;
		init();
	}
	
	@Override
	public void cancel()
	{
		mConnectionListener.cancel();
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
		}
		final NamableSelectionListener<Device> listener = new NamableSelectionListener<Device>()
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
				Toast.makeText(mContext, R.string.no_paired_device, Toast.LENGTH_SHORT).show();
				return;
			}
		}
		dialog.show(mContext.getFragmentManager(), mContext.getString(R.string.choose_host));
	}
	
	@Override
	public void connectedTo(final ConnectedDevice aDevice)
	{}
	
	@Override
	public void connectionEnabled(final boolean aEnabled)
	{
		mConnectionListener.connectionEnabled(aEnabled);
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
					final ConnectedDevice connectedDevice = new ConnectedDevice(socket, this);
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
			Log.w(TAG, "Could not connect to device.");
		}
	}
	
	@Override
	public void disconnectedFrom(final ConnectedDevice aDevice)
	{
		aDevice.stopListening();
		mDevices.remove(aDevice);
		mConnectionListener.disconnectedFrom(aDevice);
	}
	
	/**
	 * Unregisters all receivers and stops the connection.
	 */
	public void exit()
	{
		stopServer();
		mContext.unregisterReceiver(mReceiver);
	}
	
	/**
	 * @return whether this device is able to create Bluetooth connections.
	 */
	public boolean hasBluetooth()
	{
		return mBluetoothAdapter != null;
	}
	
	/**
	 * Initializes all adapters and adds listeners for device changes.
	 */
	public void init()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if (mBluetoothAdapter != null)
		{
			// TODO Remove all unnecessary receivers
			// final IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
			// final IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
			// final IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
			final IntentFilter filter4 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			// final IntentFilter filter5 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			// mContext.registerReceiver(mReceiver, filter1);
			// mContext.registerReceiver(mReceiver, filter2);
			// mContext.registerReceiver(mReceiver, filter3);
			mContext.registerReceiver(mReceiver, filter4);
			// mContext.registerReceiver(mReceiver, filter5);
			
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
	public void receiveMessage(final ConnectedDevice aDevice, final MessageType aType, final String[] aArgs)
	{
		mConnectionListener.receiveMessage(aDevice, aType, aArgs);
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
				listenForDevices(aListener);
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
			Toast.makeText(mContext, "Bluetooth is not enabled! Activate Bluetooth!", Toast.LENGTH_SHORT).show();
			connectionEnabled(false);
		}
		else
		{
			connectionEnabled(true);
		}
	}
	
	private void checkNetworkState()
	{
		Toast.makeText(mContext, "Network connections are not enabled yet!", Toast.LENGTH_SHORT).show();
	}
	
	private void listenForDevices(final ConnectionListener aListener)
	{
		while (mCheckingForDevies)
		{
			BluetoothSocket socket = null;
			if (mInsecureSocket != null) try
			{
				socket = mInsecureSocket.accept(10);
			}
			catch (final IOException e)
			{}
			if (mSecureSocket != null && socket != null) try
			{
				socket = mSecureSocket.accept(10);
			}
			catch (final IOException e)
			{}
			if (socket != null)
			{
				boolean connected = false;
				try
				{
					final ConnectedDevice connectedDevice = new ConnectedDevice(socket, this);
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
		mInsecureSocket = null;
		mSecureSocket = null;
	}
}
