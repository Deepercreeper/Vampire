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

public class ConnectionController
{
	private static final String				TAG					= "ConnectionController";
	
	private final BluetoothReceiver			mReceiver			= new BluetoothReceiver();
	
	private final Activity					mContext;
	
	private final Set<ConnectionListener>	mListeners			= new HashSet<ConnectionListener>();
	
	private BluetoothAdapter				mBluetoothAdapter;
	
	private boolean							mBluetooth			= true;
	
	private boolean							mCheckingForDevies	= false;
	
	public ConnectionController(final Activity aContext)
	{
		mContext = aContext;
		init();
	}
	
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
	
	public void addListener(final ConnectionListener aListener)
	{
		mListeners.add(aListener);
	}
	
	public void removeListener(final ConnectionListener aListener)
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
	
	public void setBluetooth(final boolean aBluetooth)
	{
		mBluetooth = aBluetooth;
		checkConnection();
	}
	
	public boolean hasBluetooth()
	{
		return mBluetoothAdapter != null;
	}
	
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
	
	public void connect(final String aDevice, final BluetoothConnectionListener aListener)
	{
		Log.i(TAG, "Tried to connect to " + aDevice);
		aListener.connectedTo(aDevice);
	}
	
	public void openConnection()
	{
		mCheckingForDevies = true;
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
	
	public interface BluetoothConnectionListener
	{
		public void connectedTo(String aDevice);
	}
}
