package com.deepercreeper.vampireapp.connection;

import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.service.Connector;
import com.deepercreeper.vampireapp.host.Player;
import com.deepercreeper.vampireapp.util.interfaces.Toaster;

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
	 * @param aPlayer
	 *            The banned player.
	 */
	public void banned(Player aPlayer);
	
	/**
	 * Sets the connector.
	 * 
	 * @param aConnector
	 *            The connector.
	 */
	public void setConnector(Connector aConnector);
	
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
