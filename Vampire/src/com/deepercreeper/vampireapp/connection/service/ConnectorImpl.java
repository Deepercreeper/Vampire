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
import com.deepercreeper.vampireapp.util.BluetoothReceiver;
import com.deepercreeper.vampireapp.util.BluetoothReceiver.BluetoothListener;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.view.dialogs.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.listeners.ItemSelectionListener;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
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
	
	private class ServerListener extends Thread
	{
		private boolean mListening = true;
		
		private SelectItemDialog<Device> mDialog;
		
		private List<Device> mServerDevices;
		
		private void setDialog(final SelectItemDialog<Device> aDialog)
		{
			mDialog = aDialog;
			mServerDevices = mDialog.getItems();
		}
		
		private void stopListening()
		{
			mListening = false;
		}
		
		@Override
		public void run()
		{
			int index = 0;
			while (mListening)
			{
				while ((mBluetoothAdapter.isDiscovering() || mServerDevices.size() <= index) && mListening)
				{
					try
					{
						Thread.sleep(1);
					}
					catch (final InterruptedException e)
					{}
				}
				if ( !mListening)
				{
					return;
				}
				final Device device = mServerDevices.get(index++ );
				device.setState(Device.State.SEARCHING);
				mDialog.updateUI();
				askForServer(device);
			}
		}
		
		private void askForServer(final Device aDevice)
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
					final BluetoothSocket remoteSocket = socket;
					final Thread thread = Thread.currentThread();
					new Thread()
					{
						@Override
						public void run()
						{
							boolean exception = false;
							try
							{
								remoteSocket.connect();
							}
							catch (final Exception e)
							{
								exception = true;
							}
							if ( !exception)
							{
								thread.interrupt();
							}
						}
					}.start();
					try
					{
						Thread.sleep(TIMEOUT);
					}
					catch (final InterruptedException e)
					{}
					if ( !socket.isConnected())
					{
						socket.close();
					}
				}
				catch (final Exception e)
				{}
				if (socket.isConnected())
				{
					aDevice.setState(Device.State.AVAILABLE);
					mDialog.setOptionEnabled(aDevice, true);
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
				aDevice.setState(Device.State.REFUSED);
				mDialog.setOptionEnabled(aDevice, false);
			}
		}
	}
	
	private static final String TAG = "Connector";
	
	private static final UUID DEFAULT_UUID = UUID.fromString("c155da2d-302d-4f10-bc3b-fa277af3599d");
	
	private static final int TIMEOUT = 3000;
	
	private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private final BluetoothReceiver mBluetoothReceiver = new BluetoothReceiver();
	
	private final List<ConnectedDevice> mDevices = new ArrayList<ConnectedDevice>();
	
	private BluetoothServerSocket mInsecureSocket = null;
	
	private BluetoothServerSocket mSecureSocket = null;
	
	private boolean mCheckingForDevies = false;
	
	private ConnectionType mConnectionType;
	
	private Activity mContext = null;
	
	private Handler mHandler = null;
	
	private ConnectionListener mListener = null;
	
	@Override
	public void bind(final Activity aContext, final ConnectionListener aListener, final Handler aHandler)
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
					checkActiveState();
				}
			};
			mBluetoothReceiver.setBluetoothListener(BluetoothAdapter.STATE_ON, listener);
			mBluetoothReceiver.setBluetoothListener(BluetoothAdapter.STATE_OFF, listener);
			
			setConnectionType(ConnectionType.BLUETOOTH);
		}
		if (hasNetwork())
		{
			if (getConnectionType() == null)
			{
				setConnectionType(ConnectionType.NETWORK);
			}
		}
		if (hasWifi())
		{
			if (getConnectionType() == null)
			{
				setConnectionType(ConnectionType.WIFI);
			}
		}
	}
	
	@Override
	public void checkActiveState()
	{
		if (getConnectionType() == ConnectionType.BLUETOOTH)
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
		else if (getConnectionType() == ConnectionType.NETWORK)
		{
			// TODO Implement
		}
		else if (getConnectionType() == ConnectionType.WIFI)
		{
			// TODO Implement
		}
	}
	
	@Override
	public void connect()
	{
		if ( !isActive())
		{
			makeText(R.string.activate_bluetooth, Toast.LENGTH_SHORT);
			return;
		}
		final ServerListener serverListener = new ServerListener();
		final ItemSelectionListener<Device> listener = new ItemSelectionListener<Device>()
		{
			@Override
			public void cancel()
			{
				mBluetoothReceiver.removeDeviceListener(BluetoothDevice.ACTION_FOUND);
				serverListener.stopListening();
			}
			
			@Override
			public void select(final Device aDevice)
			{
				mBluetoothReceiver.removeDeviceListener(BluetoothDevice.ACTION_FOUND);
				serverListener.stopListening();
				connectTo(aDevice);
			}
		};
		final SelectItemDialog<Device> dialog = SelectItemDialog.createSelectionDialog(Collections.<Device> emptyList(),
				mContext.getString(R.string.choose_host), mContext, listener);
		serverListener.setDialog(dialog);
		
		for (final BluetoothDevice device : mBluetoothAdapter.getBondedDevices())
		{
			dialog.addOption(new Device(device, mContext), false);
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
						dialog.addOption(new Device(aDevice, mContext), false);
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
		serverListener.start();
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
				mListener.disconnectedFrom(aDevice);
			}
		});
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
	public boolean hasNetwork()
	{
		// TODO Implement
		return false;
	}
	
	@Override
	public boolean hasWifi()
	{
		// TODO Implement
		return false;
	}
	
	@Override
	public boolean isActive()
	{
		switch (getConnectionType())
		{
			case BLUETOOTH :
				return mBluetoothAdapter.isEnabled();
			case NETWORK :
				return false;
			case WIFI :
				return false;
		}
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
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				mListener.receiveMessage(aDevice, aType, aArgs);
			}
		});
	}
	
	@Override
	public void sendToAll(final MessageType aType, final String... aArgs)
	{
		for (final ConnectedDevice device : mDevices)
		{
			device.send(aType, aArgs);
		}
	}
	
	@Override
	public void setConnectionType(final ConnectionType aConnectionType)
	{
		boolean typeOk = true;
		if (aConnectionType == ConnectionType.BLUETOOTH)
		{
			if ( !hasBluetooth())
			{
				typeOk = false;
			}
		}
		else if (aConnectionType == ConnectionType.NETWORK)
		{
			if ( !hasNetwork())
			{
				typeOk = false;
			}
		}
		else if (aConnectionType == ConnectionType.BLUETOOTH)
		{
			if ( !hasWifi())
			{
				typeOk = false;
			}
		}
		if (typeOk)
		{
			mConnectionType = aConnectionType;
		}
		else
		{
			Log.w(TAG, "Could not change connection type.");
		}
	}
	
	@Override
	public void startServer()
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
				listenForDevices(true);
			}
		}.start();
		new Thread()
		{
			@Override
			public void run()
			{
				listenForDevices(false);
			}
		}.start();
	}
	
	@Override
	public void unbind()
	{
		mContext.unregisterReceiver(mBluetoothReceiver);
		
		mContext = null;
		mListener = null;
		mHandler = null;
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
	
	private void listenForDevices(final boolean aSecure)
	{
		final BluetoothServerSocket server = aSecure ? mSecureSocket : mInsecureSocket;
		while (mCheckingForDevies && server != null)
		{
			BluetoothSocket socket = null;
			try
			{
				socket = server.accept(1000);
			}
			catch (final IOException e)
			{}
			if (socket != null)
			{
				boolean connected = false;
				try
				{
					connectedTo(new ConnectedDevice(socket, this, false));
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
