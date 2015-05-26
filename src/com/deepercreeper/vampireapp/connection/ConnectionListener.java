package com.deepercreeper.vampireapp.connection;

import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;

/**
 * This listener is used to react to Bluetooth events.
 * 
 * @author vrl
 */
public interface ConnectionListener
{
	public void cancel();
	
	/**
	 * Called when this device was connected to another device with the given name.
	 * 
	 * @param aDevice
	 *            The connected device name.
	 */
	public void connectedTo(ConnectedDevice aDevice);
	
	/**
	 * Called when the connection state has changed.
	 * 
	 * @param aEnabled
	 *            Whether the connection is enabled.
	 */
	public void connectionEnabled(boolean aEnabled);
	
	/**
	 * Invoked, when the given device has been disconnected from this device.
	 * 
	 * @param aDevice
	 *            The disconnected device.
	 */
	public void disconnectedFrom(ConnectedDevice aDevice);
	
	public void receiveMessage(ConnectedDevice aDevice, MessageType aType, String[] aArgs);
}
