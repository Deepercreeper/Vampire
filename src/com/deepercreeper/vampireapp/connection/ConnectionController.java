package com.deepercreeper.vampireapp.connection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.widget.Toast;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.BluetoothReceiver;
import com.deepercreeper.vampireapp.util.BluetoothReceiver.BluetoothListener;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.view.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.SelectItemDialog.StringSelectionListener;

/**
 * This controller handles all connection actions like creating an open Bluetooth port<br>
 * for other devices or connecting an searching for open Bluetooth ports.
 * 
 * @author vrl
 */
public class ConnectionController
{
	private static final String				TAG					= "ConnectionController";
	
	private final BluetoothReceiver			mReceiver			= new BluetoothReceiver();
	
	private final Activity					mContext;
	
	private final Set<ConnectionListener>	mListeners			= new HashSet<ConnectionListener>();
	
	private BluetoothAdapter				mBluetoothAdapter;
	
	private boolean							mBluetooth			= true;
	
	private boolean							mCheckingForDevies	= false;
	
	/**
	 * Creates a new connection controller for the given activity.
	 * 
	 * @param aContext
	 *            The underlying context.
	 */
	public ConnectionController(final Activity aContext)
	{
		mContext = aContext;
		init();
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
					checkConnection();
				}
			};
			mReceiver.setBluetoothListener(BluetoothAdapter.STATE_ON, listener);
			mReceiver.setBluetoothListener(BluetoothAdapter.STATE_OFF, listener);
		}
	}
	
	/**
	 * Adds the given connection listener.
	 * 
	 * @param aListener
	 *            The listener that should be called when the connection state changes.
	 */
	public void addListener(final ConnectionListener aListener)
	{
		mListeners.add(aListener);
	}
	
	/**
	 * Removes the given listener.
	 * 
	 * @param aListener
	 *            The listener to remove.
	 */
	public void removeListener(final ConnectionListener aListener)
	{
		mListeners.remove(aListener);
	}
	
	/**
	 * @return whether currently Bluetooth is the used connection.
	 */
	public boolean isBluetooth()
	{
		return mBluetooth;
	}
	
	private void checkBluetooth()
	{
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
		{
			Toast.makeText(mContext, "Bluetooth is not enabled! Activate Bluetooth!", Toast.LENGTH_SHORT).show();
			for (final ConnectionListener listener : mListeners)
			{
				listener.connectionEnabled(false);
			}
		}
		else
		{
			for (final ConnectionListener listener : mListeners)
			{
				listener.connectionEnabled(true);
			}
		}
	}
	
	private void checkNetwork()
	{
		Toast.makeText(mContext, "Network connections are not enabled yet!", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Checks the used connection even if nothing changed.
	 */
	public void checkConnection()
	{
		if (mBluetooth)
		{
			checkBluetooth();
		}
		else
		{
			checkNetwork();
		}
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
		checkConnection();
	}
	
	/**
	 * @return whther this device is able to create Bluetooth connections.
	 */
	public boolean hasBluetooth()
	{
		return mBluetoothAdapter != null;
	}
	
	/**
	 * Tries to connect to any of the user paired devices and logs in if possible.
	 * 
	 * @param aListener
	 *            The listener that is called if the connection was successful.
	 */
	public void connect(final BluetoothConnectionListener aListener)
	{
		final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		if (pairedDevices.isEmpty())
		{
			Toast.makeText(mContext, R.string.no_paired_device, Toast.LENGTH_SHORT).show();
			return;
		}
		final List<String> deviceNames = new ArrayList<String>();
		for (final BluetoothDevice device : pairedDevices)
		{
			deviceNames.add(device.getName());
		}
		final StringSelectionListener listener = new StringSelectionListener()
		{
			@Override
			public void select(final String aItem)
			{
				connect(aItem, aListener);
			}
		};
		SelectItemDialog.showSelectionDialog(deviceNames, mContext.getString(R.string.choose_host), mContext, listener);
	}
	
	/**
	 * Connects to the device with the given name.
	 * 
	 * @param aDevice
	 *            The device name.
	 * @param aListener
	 *            The Bluetooth listener that is called if the device was connected.
	 */
	public void connect(final String aDevice, final BluetoothConnectionListener aListener)
	{
		Log.i(TAG, "Tried to connect to " + aDevice);
		aListener.connectedTo(aDevice);
	}
	
	/**
	 * Allows other devices to connect to this device.
	 */
	public void openConnection()
	{
		mCheckingForDevies = true;
	}
	
	/**
	 * @return whether the used connection is active.
	 */
	public boolean isActive()
	{
		if (mBluetooth)
		{
			return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
		}
		// TODO Implement network connection
		return false;
	}
	
	/**
	 * Unregisters all receivers and stops the connection.
	 */
	public void close()
	{
		mContext.unregisterReceiver(mReceiver);
	}
	
	/**
	 * This listener is used to react to connection events.
	 * 
	 * @author vrl
	 */
	public interface ConnectionListener
	{
		/**
		 * Called when the connection state has changed.
		 * 
		 * @param aEnabled
		 *            Whether the connection is enabled.
		 */
		public void connectionEnabled(boolean aEnabled);
	}
	
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
		public void connectedTo(String aDevice);
	}
}
