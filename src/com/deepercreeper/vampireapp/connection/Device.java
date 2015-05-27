package com.deepercreeper.vampireapp.connection;

import android.bluetooth.BluetoothDevice;
import com.deepercreeper.vampireapp.items.implementations.Named;

/**
 * To store Bluetooth devices inside a list this device class contains a named Bluetooth device.
 * 
 * @author vrl
 */
public class Device extends Named
{
	private final BluetoothDevice	mDevice;
	
	/**
	 * Creates a new device containing the given Bluetooth device.
	 * 
	 * @param aDevice
	 *            The Bluetooth device.
	 */
	public Device(final BluetoothDevice aDevice)
	{
		super(aDevice.getName());
		mDevice = aDevice;
	}
	
	/**
	 * @return the device address.
	 */
	public String getAddress()
	{
		return mDevice.getAddress();
	}
	
	@Override
	public String getDisplayName()
	{
		return getName();
	}
	
	/**
	 * @return the original Bluetooth device.
	 */
	public BluetoothDevice getDevice()
	{
		return mDevice;
	}
}
