package com.deepercreeper.vampireapp.util;

import java.util.HashSet;
import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.widget.Toast;
import com.deepercreeper.vampireapp.util.BluetoothReceiver.BluetoothListener;

public class ConnectionController
{
	private final BluetoothReceiver			mReceiver	= new BluetoothReceiver();
	
	private final Activity					mContext;
	
	private final Set<ConnectionListener>	mListeners	= new HashSet<ConnectionListener>();
	
	private BluetoothAdapter				mBluetoothAdapter;
	
	private boolean							mBluetooth	= true;
	
	public ConnectionController(Activity aContext)
	{
		mContext = aContext;
		
		init();
	}
	
	public void init()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
		IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
		IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		IntentFilter filter4 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		mContext.registerReceiver(mReceiver, filter1);
		mContext.registerReceiver(mReceiver, filter2);
		mContext.registerReceiver(mReceiver, filter3);
		mContext.registerReceiver(mReceiver, filter4);
		
		BluetoothListener listener = new BluetoothListener()
		{
			@Override
			public void action(BluetoothDevice aDevice)
			{
				checkConnection();
			}
		};
		mReceiver.setBluetoothListener(BluetoothAdapter.STATE_ON, listener);
		mReceiver.setBluetoothListener(BluetoothAdapter.STATE_OFF, listener);
	}
	
	public void addListener(ConnectionListener aListener)
	{
		mListeners.add(aListener);
	}
	
	public void removeListener(ConnectionListener aListener)
	{
		mListeners.remove(aListener);
	}
	
	public boolean isBluetooth()
	{
		return mBluetooth;
	}
	
	private void checkBluetooth()
	{
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
		{
			Toast.makeText(mContext, "Bluetooth is not enabled!", Toast.LENGTH_SHORT).show();
			for (ConnectionListener listener : mListeners)
			{
				listener.connectionEnabled(false);
			}
		}
		else
		{
			Toast.makeText(mContext, "Bluetooth is enabled!", Toast.LENGTH_SHORT).show();
			for (ConnectionListener listener : mListeners)
			{
				listener.connectionEnabled(true);
			}
		}
	}
	
	private void checkNetwork()
	{
		Toast.makeText(mContext, "Network connections are not enabled yet!", Toast.LENGTH_SHORT).show();
	}
	
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
	
	public void setBluetooth(boolean aBluetooth)
	{
		mBluetooth = aBluetooth;
		checkConnection();
	}
	
	public boolean hasBluetooth()
	{
		return mBluetoothAdapter != null;
	}
	
	public boolean isActive()
	{
		if (mBluetooth)
		{
			return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
		}
		// TODO Implement network connection
		return false;
	}
	
	public void close()
	{
		mContext.unregisterReceiver(mReceiver);
	}
	
	public interface ConnectionListener
	{
		public void connectionEnabled(boolean aEnabled);
	}
}
