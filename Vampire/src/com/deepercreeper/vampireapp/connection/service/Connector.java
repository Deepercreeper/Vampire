package com.deepercreeper.vampireapp.connection.service;

import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionListener;
import com.deepercreeper.vampireapp.connection.service.ConnectorImpl.ConnectionType;
import com.deepercreeper.vampireapp.util.interfaces.Toaster;
import android.app.Activity;
import android.os.Handler;

public interface Connector extends Toaster
{
	public void bind(Activity aContext, ConnectionListener aListener, Handler aHandler);
	
	public void checkActiveState();
	
	public boolean isActive();
	
	public boolean hasNetwork();
	
	public boolean hasWifi();
	
	public void connect();
	
	public void connectedTo(final ConnectedDevice aDevice);
	
	public void disconnect(ConnectedDevice aDevice);
	
	public void disconnectedFrom(ConnectedDevice aDevice);
	
	public ConnectionType getConnectionType();
	
	/**
	 * @return the connected host or {@code null} if no host is connected.
	 */
	public ConnectedDevice getHost();
	
	public boolean hasBluetooth();
	
	/**
	 * @return whether this connection controller is connected to a host.
	 */
	public boolean hasHost();
	
	public void receiveMessage(final ConnectedDevice aDevice, final MessageType aType, final String[] aArgs);
	
	public void sendToAll(final MessageType aType, final String... aArgs);
	
	public void setConnectionType(ConnectionType aConnectionType);
	
	public void startServer();
	
	public void unbind();
}
