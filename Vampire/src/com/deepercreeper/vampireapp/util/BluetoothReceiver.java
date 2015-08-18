package com.deepercreeper.vampireapp.util;

import java.util.HashMap;
import java.util.Map;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * A receiver for all Bluetooth and network actions.
 * 
 * @author vrl
 */
public class BluetoothReceiver extends BroadcastReceiver
{
	/**
	 * A listener for exactly one Bluetooth action.
	 * 
	 * @author vrl
	 */
	public interface BluetoothListener
	{
		/**
		 * The defined action was invoked.
		 * 
		 * @param aDevice
		 *            The optional Bluetooth device if existing.
		 */
		public void action(BluetoothDevice aDevice);
	}
	
	/**
	 * This array isn't used really. It's just a list of possible listen actions.
	 */
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
	
	/**
	 * Removes the listener for the given action.
	 * 
	 * @param aAction
	 *            The Bluetooth action.
	 */
	public void removeDeviceListener(String aAction)
	{
		mListeners.remove(aAction);
	}
	
	/**
	 * Sets the Bluetooth listener for the given Bluetooth state.
	 * 
	 * @param aState
	 *            The Bluetooth state. One of {@link BluetoothAdapter#STATE_ON}, {@link BluetoothAdapter#STATE_OFF}<br>
	 *            or one of their turning states.
	 * @param aListener
	 */
	public void setBluetoothListener(int aState, BluetoothListener aListener)
	{
		mListeners.put("" + aState, aListener);
	}
	
	/**
	 * Sets the listener for the given action.
	 * 
	 * @param aAction
	 *            The action that invokes the listener.
	 * @param aListener
	 *            The Bluetooth listener.
	 */
	public void setDeviceListener(String aAction, BluetoothListener aListener)
	{
		mListeners.put(aAction, aListener);
	}
}
