package com.deepercreeper.vampireapp.connection;

import android.bluetooth.BluetoothDevice;
import com.deepercreeper.vampireapp.items.implementations.Named;

public class Device extends Named
{
	private final BluetoothDevice	mDevice;
	
	public Device(final BluetoothDevice aDevice)
	{
		super(aDevice.getName());
		mDevice = aDevice;
	}
	
	public String getAddress()
	{
		return mDevice.getAddress();
	}
	
	@Override
	public String getDisplayName()
	{
		return getName();
	}
	
	public BluetoothDevice getDevice()
	{
		return mDevice;
	}
}
