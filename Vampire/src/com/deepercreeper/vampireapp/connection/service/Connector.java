package com.deepercreeper.vampireapp.connection.service;

import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionListener;
import com.deepercreeper.vampireapp.connection.service.ConnectorImpl.ConnectionType;
import com.deepercreeper.vampireapp.util.interfaces.Toaster;
import android.app.Activity;
import android.os.Handler;

/**
 * A connector is used to create connections to other servers and clients.
 * 
 * @author Vincent
 */
public interface Connector extends Toaster
{
	/**
	 * Binds the default connector to the given activity.
	 * 
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The connection listener.
	 * @param aHandler
	 *            The handler for toasting.
	 */
	public void bind(Activity aContext, ConnectionListener aListener, Handler aHandler);
	
	/**
	 * Checks whether the current connection is active.
	 */
	public void checkActiveState();
	
	/**
	 * @return whether the current connection is active.
	 */
	public boolean isActive();
	
	/**
	 * @return whether this device is able to connect via network.
	 */
	public boolean hasNetwork();
	
	/**
	 * @return whether this device is able the connect via WiFi direct.
	 */
	public boolean hasWifi();
	
	/**
	 * Opens a dialog that makes is possible to choose a server.
	 */
	public void connect();
	
	/**
	 * The given device has been connected.
	 * 
	 * @param aDevice
	 *            The connected device.
	 */
	public void connectedTo(final ConnectedDevice aDevice);
	
	/**
	 * Disconnects from the given device.
	 * 
	 * @param aDevice
	 *            The device.
	 */
	public void disconnect(ConnectedDevice aDevice);
	
	/**
	 * The connection to the given device was closed.
	 * 
	 * @param aDevice
	 *            The disconnected device.
	 */
	public void disconnectedFrom(ConnectedDevice aDevice);
	
	/**
	 * @return the current connection type.
	 */
	public ConnectionType getConnectionType();
	
	/**
	 * @return the connected host or {@code null} if no host is connected.
	 */
	public ConnectedDevice getHost();
	
	/**
	 * @return whether this device is able to connect via bluetooth.
	 */
	public boolean hasBluetooth();
	
	/**
	 * @return whether this connection controller is connected to a host.
	 */
	public boolean hasHost();
	
	/**
	 * The given message was sent to this device.
	 * 
	 * @param aDevice
	 *            The sender device.
	 * @param aType
	 *            The message type.
	 * @param aArgs
	 *            The message arguments.
	 */
	public void receiveMessage(final ConnectedDevice aDevice, final MessageType aType, final String[] aArgs);
	
	/**
	 * Sends the given message to all connected devices.
	 * 
	 * @param aType
	 *            The message type.
	 * @param aArgs
	 *            The message arguments.
	 */
	public void sendToAll(final MessageType aType, final String... aArgs);
	
	/**
	 * Sets the new connection type.
	 * 
	 * @param aConnectionType
	 *            The connection type.
	 */
	public void setConnectionType(ConnectionType aConnectionType);
	
	/**
	 * Starts the server.
	 */
	public void startServer();
	
	/**
	 * Unbinds this connector from the current activity.
	 */
	public void unbind();
}
