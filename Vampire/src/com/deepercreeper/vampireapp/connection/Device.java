package com.deepercreeper.vampireapp.connection;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.implementations.Named;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

/**
 * To store Bluetooth devices inside a list this device class contains a named Bluetooth device.
 * 
 * @author vrl
 */
public class Device extends Named
{
	/**
	 * Device states a described in here.
	 * 
	 * @author vrl
	 */
	public static enum State
	{
		/**
		 * Not searching yet.
		 */
		PENDING(R.string.pending),
		
		/**
		 * This device is being searched.
		 */
		SEARCHING(R.string.searching),
		
		/**
		 * The device has no open connection.
		 */
		REFUSED(R.string.refused),
		
		/**
		 * The device is ready to start.
		 */
		AVAILABLE(R.string.available);
		
		private final int mResourceId;
		
		private State(final int aResourceId)
		{
			mResourceId = aResourceId;
		}
		
		/**
		 * @return the resource Id.
		 */
		public int getResourceId()
		{
			return mResourceId;
		}
	}
	
	private final BluetoothDevice mDevice;
	
	private final Context mContext;
	
	private State mState = State.PENDING;
	
	/**
	 * Creates a new device containing the given Bluetooth device.
	 * 
	 * @param aDevice
	 *            The Bluetooth device.
	 * @param aContext
	 *            The underlying context.
	 */
	public Device(final BluetoothDevice aDevice, final Context aContext)
	{
		super(aDevice.getName() == null ? aDevice.getAddress() : aDevice.getName());
		mDevice = aDevice;
		mContext = aContext;
	}
	
	@Override
	public boolean equals(final Object aObj)
	{
		if (aObj instanceof Device)
		{
			final Device dev = (Device) aObj;
			return dev.getAddress().equals(getAddress());
		}
		return false;
	}
	
	/**
	 * @return the device address.
	 */
	public String getAddress()
	{
		return mDevice.getAddress();
	}
	
	/**
	 * @return the original Bluetooth device.
	 */
	public BluetoothDevice getDevice()
	{
		return mDevice;
	}
	
	@Override
	public String getDisplayName()
	{
		return getName();
	}
	
	/**
	 * Sets whether this device is pending, connected, or not reachable.
	 * 
	 * @param aState
	 *            The new state.
	 */
	public void setState(final State aState)
	{
		mState = aState;
	}
	
	/**
	 * @return the current device state.
	 */
	public State getState()
	{
		return mState;
	}
	
	@Override
	public String toString()
	{
		return getName() + " (" + mContext.getString(getState().getResourceId()) + ")";
	}
	
	@Override
	public int hashCode()
	{
		return getAddress().hashCode();
	}
}
