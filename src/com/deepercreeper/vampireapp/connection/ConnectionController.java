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
import android.widget.Toast;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageListener;
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
public class ConnectionController
{
	/**
	 * This listener is used to react to Bluetooth events.
	 * 
	 * @author vrl
	 */
	public interface BluetoothConnectionListener
	{
		/**
		 * Called when this device was connected to another device with the given name.
		 * 
		 * @param aDevice
		 *            The connected device name.
		 */
		public void connectedTo(ConnectedDevice aDevice);
	}
	
	/**
	 * This listener is used to react to connection events.
	 * 
	 * @author vrl
	 */
	public interface ConnectionStateListener
	{
		/**
		 * Called when the connection state has changed.
		 * 
		 * @param aEnabled
		 *            Whether the connection is enabled.
		 */
		public void connectionEnabled(boolean aEnabled);
	}
	
	private static final String					TAG					= "ConnectionController";
	
	private static final UUID					DEFAULT_UUID		= UUID.fromString("c155da2d-302d-4f10-bc3b-fa277af3599d");
	
	private final BluetoothReceiver				mReceiver			= new BluetoothReceiver();
	
	private final Activity						mContext;
	
	private final Set<ConnectionStateListener>	mListeners			= new HashSet<ConnectionStateListener>();
	
	private final List<ConnectedDevice>			mDevices			= new ArrayList<ConnectedDevice>();
	
	private final MessageListener				mMessageListener;
	
	private BluetoothAdapter					mBluetoothAdapter;
	
	private BluetoothServerSocket				mInsecureSocket		= null;
	
	private BluetoothServerSocket				mSecureSocket		= null;
	
	private boolean								mBluetooth			= true;
	
	private boolean								mCheckingForDevies	= false;
	
	/**
	 * Creates a new connection controller for the given activity.
	 * 
	 * @param aContext
	 *            The underlying context.
	 * @param aMessageListener
	 *            The listener for incoming messages.
	 */
	public ConnectionController(final Activity aContext, final MessageListener aMessageListener)
	{
		mContext = aContext;
		mMessageListener = aMessageListener;
		init();
	}
	
	/**
	 * Adds the given connection listener.
	 * 
	 * @param aListener
	 *            The listener that should be called when the connection state changes.
	 */
	public void addConnectionListener(final ConnectionStateListener aListener)
	{
		mListeners.add(aListener);
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
	public void connect(final BluetoothConnectionListener aListener)
	{
		// TODO Make this whole fuck'n thing stable
		final Set<Device> pairedDevices = new HashSet<Device>();
		for (final BluetoothDevice device : mBluetoothAdapter.getBondedDevices())
		{
			pairedDevices.add(new Device(device));
		}
		if (pairedDevices.isEmpty())
		{
			Toast.makeText(mContext, R.string.no_paired_device, Toast.LENGTH_SHORT).show();
			return;
		}
		final NamableSelectionListener<Device> listener = new NamableSelectionListener<Device>()
		{
			@Override
			public void select(final Device aDevice)
			{
				connectTo(aDevice, aListener);
			}
		};
		SelectItemDialog.showSelectionDialog(pairedDevices.toArray(new Device[pairedDevices.size()]), mContext.getString(R.string.choose_host),
				mContext, listener);
	}
	
	/**
	 * Connects to the device with the given name.
	 * 
	 * @param aDevice
	 *            The device name.
	 * @param aListener
	 *            The Bluetooth listener that is called if the device was connected.
	 */
	public void connectTo(final Device aDevice, final BluetoothConnectionListener aListener)
	{
		Log.i(TAG, "Tried to connect to " + aDevice);
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
			Log.i(TAG, "Could not connect via insecure Rfcomm.");
			try
			{
				socket = device.createRfcommSocketToServiceRecord(DEFAULT_UUID);
			}
			catch (final IOException e)
			{}
		}
		Log.i(TAG, "Socket was created and the connection state is " + (socket == null ? null : socket.isConnected()));
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
					final ConnectedDevice connectedDevice = new ConnectedDevice(socket, mMessageListener);
					mDevices.add(connectedDevice);
					aListener.connectedTo(connectedDevice);
				}
				catch (final IOException e)
				{}
			}
		}
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
			final IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
			final IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
			final IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
			final IntentFilter filter4 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			mContext.registerReceiver(mReceiver, filter1);
			mContext.registerReceiver(mReceiver, filter2);
			mContext.registerReceiver(mReceiver, filter3);
			mContext.registerReceiver(mReceiver, filter4);
			
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
	
	/**
	 * Removes the given listener.
	 * 
	 * @param aListener
	 *            The listener to remove.
	 */
	public void removeConnectionListener(final ConnectionStateListener aListener)
	{
		mListeners.remove(aListener);
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
	public void startServer(final BluetoothConnectionListener aListener)
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
	
	/**
	 * Unregisters all receivers and stops the connection.
	 */
	public void unregister()
	{
		mContext.unregisterReceiver(mReceiver);
	}
	
	private void checkBluetoothState()
	{
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
		{
			Toast.makeText(mContext, "Bluetooth is not enabled! Activate Bluetooth!", Toast.LENGTH_SHORT).show();
			for (final ConnectionStateListener listener : mListeners)
			{
				listener.connectionEnabled(false);
			}
		}
		else
		{
			for (final ConnectionStateListener listener : mListeners)
			{
				listener.connectionEnabled(true);
			}
		}
	}
	
	private void checkNetworkState()
	{
		Toast.makeText(mContext, "Network connections are not enabled yet!", Toast.LENGTH_SHORT).show();
	}
	
	private void listenForDevices(final BluetoothConnectionListener aListener)
	{
		while (mCheckingForDevies)
		{
			BluetoothSocket socket = null;
			if (mInsecureSocket != null) try
			{
				Log.i(TAG, "Trying to accept insecure socket.");
				socket = mInsecureSocket.accept(10);
			}
			catch (final IOException e)
			{}
			if (mSecureSocket != null && socket != null) try
			{
				Log.i(TAG, "Trying to accept secure socket.");
				socket = mSecureSocket.accept(10);
			}
			catch (final IOException e)
			{}
			if (socket != null)
			{
				try
				{
					// TODO Remove most of all info logs
					final ConnectedDevice connectedDevice = new ConnectedDevice(socket, mMessageListener);
					aListener.connectedTo(connectedDevice);
				}
				catch (final IOException e)
				{}
			}
		}
		mInsecureSocket = null;
		mSecureSocket = null;
	}
}
