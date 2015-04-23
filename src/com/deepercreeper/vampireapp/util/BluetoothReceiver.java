package com.deepercreeper.vampireapp.util;

import java.util.HashMap;
import java.util.Map;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothReceiver extends BroadcastReceiver
{
	public static final String[]					ACTIONS		= new String[] { // Console return
																BluetoothDevice.ACTION_FOUND, // Device found
			BluetoothDevice.ACTION_ACL_CONNECTED, // Device now connected
			BluetoothAdapter.ACTION_DISCOVERY_FINISHED, // Done searching
			BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED, // Device is about too disconnect
			BluetoothDevice.ACTION_ACL_DISCONNECTED, // Device has disconnected
			"" + BluetoothAdapter.STATE_OFF, // Off
			"" + BluetoothAdapter.STATE_TURNING_OFF, // Turning off
			"" + BluetoothAdapter.STATE_ON, // On
			"" + BluetoothAdapter.STATE_TURNING_ON // Turning on
																};
	
	private final Map<String, BluetoothListener>	mListeners	= new HashMap<String, BluetoothListener>();
	
	@Override
	public void onReceive(Context aContext, Intent intent)
	{
		String action = intent.getAction();
		BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		
		if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
		{
			final String state = "" + intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
			if (mListeners.containsKey(state))
			{
				mListeners.get(state).action(null);
			}
			return;
		}
		
		if (mListeners.containsKey(action))
		{
			mListeners.get(action).action(device);
		}
	}
	
	public void setDeviceListener(String aAction, BluetoothListener aListener)
	{
		mListeners.put(aAction, aListener);
	}
	
	public void setBluetoothListener(int aState, BluetoothListener aListener)
	{
		mListeners.put("" + aState, aListener);
	}
	
	public interface BluetoothListener
	{
		public void action(BluetoothDevice aDevice);
	}
}
