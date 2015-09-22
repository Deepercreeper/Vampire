package com.deepercreeper.vampireapp.connection.connector;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionListener;
import com.deepercreeper.vampireapp.util.BluetoothReceiver;
import com.deepercreeper.vampireapp.util.BluetoothReceiver.BluetoothListener;
import com.deepercreeper.vampireapp.util.Log;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
	
	private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private final BluetoothReceiver mBluetoothReceiver = new BluetoothReceiver();
	
	private final DeviceController mDeviceController = new DeviceController(this);
	
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
		mDeviceController.findServer(mContext);
	}
	
	@Override
	public BluetoothAdapter getBluetoothAdapter()
	{
		return mBluetoothAdapter;
	}
	
	@Override
	public BluetoothReceiver getBluetoothReceiver()
	{
		return mBluetoothReceiver;
	}
	
	@Override
	public Handler getHandler()
	{
		return mHandler;
	}
	
	@Override
	public void connectedTo(final ConnectedDevice aDevice)
	{
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
		mDeviceController.disconnectedFrom(aDevice);
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
		return mDeviceController.getHost();
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
		for (final ConnectedDevice device : mDeviceController.getConnectedDevices())
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
		mDeviceController.startServer();
	}
	
	@Override
	public void unbind()
	{
		mContext.unregisterReceiver(mBluetoothReceiver);
		
		mContext = null;
		mListener = null;
		mHandler = null;
	}
}
