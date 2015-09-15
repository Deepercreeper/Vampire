package com.deepercreeper.vampireapp.connection.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionListener;
import com.deepercreeper.vampireapp.connection.Device;
import com.deepercreeper.vampireapp.connection.Device.State;
import com.deepercreeper.vampireapp.util.BluetoothReceiver;
import com.deepercreeper.vampireapp.util.BluetoothReceiver.BluetoothListener;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.view.dialogs.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.listeners.ItemSelectionListener;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;
import android.os.Handler;
import android.widget.Toast;

/**
 * The default implementation for a connector.
 * 
 * @author vrl
 */
public class ConnectorImpl implements Connector
{
	/**
	 * Connection types.
	 * 
	 * @author vrl
	 */
	public static enum ConnectionType
	{
		/**
		 * Bluetooth
		 */
		BLUETOOTH,
		
		/**
		 * Network via a server
		 */
		NETWORK,
		
		/**
		 * Direct WiFi
		 */
		WIFI
	}
	
	private static final String TAG = "Connector";
	
	private static final UUID DEFAULT_UUID = UUID.fromString("c155da2d-302d-4f10-bc3b-fa277af3599d");
	
	private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private final BluetoothReceiver mBluetoothReceiver = new BluetoothReceiver();
	
	private final List<ConnectedDevice> mDevices = new ArrayList<ConnectedDevice>();
	
	private ConnectionType mConnectionType = ConnectionType.BLUETOOTH;
	
	private Activity mContext = null;
	
	private Handler mHandler = null;
	
	private ConnectionListener mListener = null;
	
	@Override
	public void bind(Activity aContext, ConnectionListener aListener, Handler aHandler)
	{
		mContext = aContext;
		mListener = aListener;
		mHandler = aHandler;
		
		if (hasBluetooth())
		{
			final IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			final IntentFilter deviceFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			mContext.registerReceiver(mBluetoothReceiver, filter);
			mContext.registerReceiver(mBluetoothReceiver, deviceFilter);
			
			final BluetoothListener listener = new BluetoothListener()
			{
				@Override
				public void action(final BluetoothDevice aDevice)
				{
					bluetoothStateChanged();
				}
			};
			mBluetoothReceiver.setBluetoothListener(BluetoothAdapter.STATE_ON, listener);
			mBluetoothReceiver.setBluetoothListener(BluetoothAdapter.STATE_OFF, listener);
		}
	}
	
	@Override
	public void checkConnectionState()
	{
		// TODO Implement
	}
	
	@Override
	public void connect()
	{
		final ItemSelectionListener<Device> listener = new ItemSelectionListener<Device>()
		{
			@Override
			public void cancel()
			{
				mBluetoothReceiver.removeDeviceListener(BluetoothDevice.ACTION_FOUND);
			}
			
			@Override
			public void select(final Device aDevice)
			{
				mBluetoothReceiver.removeDeviceListener(BluetoothDevice.ACTION_FOUND);
				connectTo(aDevice);
			}
		};
		final SelectItemDialog<Device> dialog = SelectItemDialog.createSelectionDialog(Collections.<Device> emptyList(),
				mContext.getString(R.string.choose_host), mContext, listener);
				
		for (final BluetoothDevice device : mBluetoothAdapter.getBondedDevices())
		{
			dialog.addOption(createDevice(device, dialog));
		}
		mBluetoothReceiver.setDeviceListener(BluetoothDevice.ACTION_FOUND, new BluetoothListener()
		{
			@Override
			public void action(final BluetoothDevice aDevice)
			{
				mHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						if (aDevice.getName() == null)
						{
							Log.i(TAG, "Added no name device.");
						}
						dialog.addOption(createDevice(aDevice, dialog));
					}
				});
			}
		});
		if ( !mBluetoothAdapter.startDiscovery())
		{
			if (mBluetoothAdapter.getBondedDevices().isEmpty())
			{
				makeText(R.string.no_paired_device, Toast.LENGTH_SHORT);
				return;
			}
		}
		dialog.show(mContext.getFragmentManager(), mContext.getString(R.string.choose_host));
	}
	
	@Override
	public void connectedTo(final ConnectedDevice aDevice)
	{
		mDevices.add(aDevice);
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mListener.connectedTo(aDevice);
			}
		});
	}
	
	@Override
	public void disconnect(ConnectedDevice aDevice)
	{
		// TODO Implement
	}
	
	@Override
	public void disconnectedFrom(ConnectedDevice aDevice)
	{
		// TODO Implement
	}
	
	@Override
	public ConnectionType getConnectionType()
	{
		return mConnectionType;
	}
	
	@Override
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
	
	@Override
	public boolean hasBluetooth()
	{
		return mBluetoothAdapter != null;
	}
	
	@Override
	public boolean hasHost()
	{
		return getHost() != null;
	}
	
	@Override
	public void makeText(final int aResId, final int aDuration)
	{
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mListener.makeText(aResId, aDuration);
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
				mListener.makeText(aText, aDuration);
			}
		});
	}
	
	@Override
	public void receiveMessage(final ConnectedDevice aDevice, final MessageType aType, final String[] aArgs)
	{
		// TODO Implement
	}
	
	@Override
	public void sendToAll(MessageType aType, String... aArgs)
	{
		// TODO Implement
	}
	
	@Override
	public void setConnectionType(ConnectionType aConnectionType)
	{
		mConnectionType = aConnectionType;
		// TODO This will have to make many changes
	}
	
	@Override
	public void startServer()
	{
		// TODO Implement
	}
	
	@Override
	public void unbind()
	{
		mContext.unregisterReceiver(mBluetoothReceiver);
		
		mContext = null;
		mListener = null;
		mHandler = null;
	}
	
	private void askForServer(Device aDevice, SelectItemDialog<Device> aDialog)
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
				aDevice.setState(State.AVAILABLE);
				aDialog.setOptionEnabled(aDevice, true);
				connected = true;
				try
				{
					socket.close();
				}
				catch (final IOException e)
				{}
			}
		}
		if ( !connected)
		{
			aDevice.setState(State.REFUSED);
			aDialog.setOptionEnabled(aDevice, false);
		}
	}
	
	private void bluetoothStateChanged()
	{
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mListener.connectionEnabled(mBluetoothAdapter.isEnabled());
			}
		});
	}
	
	private void connectTo(final Device aDevice)
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
					connectedTo(new ConnectedDevice(socket, this, true));
					connected = true;
				}
				catch (final IOException e)
				{}
			}
		}
		if ( !connected)
		{
			makeText("Could not connect to device " + aDevice.getDisplayName(), Toast.LENGTH_SHORT);
		}
	}
	
	private Device createDevice(BluetoothDevice aDevice, final SelectItemDialog<Device> aDialog)
	{
		final Device device = new Device(aDevice, mContext);
		new Thread()
		{
			@Override
			public void run()
			{
				askForServer(device, aDialog);
			}
		}.start();
		return device;
	}
}
