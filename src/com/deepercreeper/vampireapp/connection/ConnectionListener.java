package com.deepercreeper.vampireapp.connection;

import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.util.view.Toaster;

/**
 * This listener is used to react to Bluetooth events.
 * 
 * @author vrl
 */
public interface ConnectionListener extends Toaster
{
	/**
	 * The given device has been banned or this device has been banned by the given device.
	 * 
	 * @param aDevice
	 *            The banning or banned device.
	 */
	public void banned(ConnectedDevice aDevice);
	
	/**
	 * Sometimes the connection is cancelled by a user action.<br>
	 * This method is invoked if the user doesn't allow a socket connection.
	 */
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
	
	/**
	 * A message was sent.
	 * 
	 * @param aDevice
	 *            The device that sent the message.
	 * @param aType
	 *            The message type.
	 * @param aArgs
	 *            All optional arguments.
	 */
	public void receiveMessage(ConnectedDevice aDevice, MessageType aType, String[] aArgs);
}
