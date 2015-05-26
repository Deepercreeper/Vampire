package com.deepercreeper.vampireapp.connection;

/**
 * This listener is used to react to Bluetooth events.
 * 
 * @author vrl
 */
public interface BluetoothConnectionListener
{
	/**
	 * Called when this device was connected to another device with the given name.
	 * 
	 * @param aDevice
	 *            The connected device name.
	 */
	public void connectedTo(ConnectedDevice aDevice);
	
	public void disconnectedFrom(ConnectedDevice aDevice);
}
