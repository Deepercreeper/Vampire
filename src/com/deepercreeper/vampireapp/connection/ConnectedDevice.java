package com.deepercreeper.vampireapp.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.Log;

/**
 * Each device that was connected and is able to send and receive messages is stored inside a ConnectedDevice.
 * 
 * @author vrl
 */
public class ConnectedDevice
{
	/**
	 * Each message that is sent has a type. The type tells, what information is transferred.
	 * 
	 * @author vrl
	 */
	public static enum MessageType
	{
		/**
		 * First connection to a host
		 */
		LOGIN,
		
		/**
		 * The player has paused his activity.
		 */
		AFK,
		
		/**
		 * The player has opened his activity again.
		 */
		BACK,
		
		/**
		 * Accepting a player
		 */
		ACCEPT,
		
		/**
		 * The player has chosen a name that was already in use
		 */
		NAME_IN_USE,
		
		/**
		 * The host has closed the game
		 */
		CLOSED,
		
		/**
		 * The player was banned
		 */
		BANNED,
		
		/**
		 * The player was kicked
		 */
		KICKED,
		
		/**
		 * The player left the game
		 */
		LEFT_GAME,
		
		/**
		 * The given time has passed
		 */
		TIME,
		
		/**
		 * Updates the players health
		 */
		UPDATE_HEALTH
	}
	
	private static final String			TAG				= "ConnectedDevice";
	
	private static final String			ARGS_DELIM		= ",";
	
	private static final char			MESSAGES_DELIM	= '\n';
	
	private final BluetoothSocket		mSocket;
	
	private final OutputStream			mOut;
	
	private final InputStream			mIn;
	
	private final ConnectionListener	mConnectionListener;
	
	private final boolean				mHost;
	
	private boolean						mClosing		= false;
	
	/**
	 * Creates a new connected device, that listens for messages.
	 * 
	 * @param aSocket
	 *            The Bluetooth socket.
	 * @param aConnectionListener
	 *            The listener for Bluetooth, connection and message events.
	 * @param aHost
	 *            Whether this is a host device or not.
	 * @throws IOException
	 *             if the in-/ or output stream could not be resolved.
	 */
	public ConnectedDevice(final BluetoothSocket aSocket, final ConnectionListener aConnectionListener, final boolean aHost) throws IOException
	{
		mSocket = aSocket;
		mOut = aSocket.getOutputStream();
		mIn = aSocket.getInputStream();
		mHost = aHost;
		mConnectionListener = aConnectionListener;
		
		new Thread()
		{
			@Override
			public void run()
			{
				listenForMessages();
			}
		}.start();
	}
	
	/**
	 * Stops listening for messages and closes the socket. Afterwards {@link ConnectionListener#disconnectedFrom(ConnectedDevice)} is invoked.
	 */
	public void exit()
	{
		mClosing = true;
		if (mSocket.isConnected())
		{
			try
			{
				mSocket.close();
			}
			catch (final IOException e)
			{
				Log.e(TAG, "Could not close the socket.");
			}
		}
		mConnectionListener.disconnectedFrom(this);
	}
	
	/**
	 * @return the underlying Bluetooth device.
	 */
	public BluetoothDevice getDevice()
	{
		return mSocket.getRemoteDevice();
	}
	
	/**
	 * @return whether this is a host device.
	 */
	public boolean isHost()
	{
		return mHost;
	}
	
	/**
	 * Sends a message with the given type and arguments to the connected device.
	 * 
	 * @param aType
	 *            The message type.
	 * @param aArgs
	 *            An array of arguments.
	 * @return whether the message could be sent.
	 */
	public boolean send(final MessageType aType, final String... aArgs)
	{
		final StringBuilder message = new StringBuilder();
		message.append(aType);
		for (final String arg : aArgs)
		{
			message.append(ARGS_DELIM + CodingUtil.encode(arg));
		}
		message.append(MESSAGES_DELIM);
		boolean success = true;
		try
		{
			mOut.write(message.toString().getBytes(Charset.defaultCharset()));
		}
		catch (final IOException e)
		{
			success = false;
		}
		try
		{
			mOut.flush();
		}
		catch (final IOException e)
		{
			success = false;
		}
		return success;
		// TODO Check whether message has been sent
	}
	
	private void listenForMessages()
	{
		while (mSocket.isConnected())
		{
			final StringBuilder messageBuilder = new StringBuilder();
			int c;
			try
			{
				while ((c = mIn.read()) != -1)
				{
					final char a = (char) c;
					if (a == MESSAGES_DELIM)
					{
						final String[] message = messageBuilder.toString().split(ARGS_DELIM);
						final String[] args = Arrays.copyOfRange(message, 1, message.length);
						for (int i = 0; i < args.length; i++ )
						{
							args[i] = CodingUtil.decode(args[i]);
						}
						mConnectionListener.receiveMessage(this, MessageType.valueOf(message[0]), args);
						messageBuilder.delete(0, messageBuilder.length());
					}
					else
					{
						messageBuilder.append((char) c);
					}
				}
			}
			catch (final IOException e)
			{}
		}
		if ( !mClosing)
		{
			exit();
		}
	}
}
