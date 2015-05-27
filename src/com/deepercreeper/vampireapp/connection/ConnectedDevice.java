package com.deepercreeper.vampireapp.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.net.Uri;
import com.deepercreeper.vampireapp.util.Log;

public class ConnectedDevice
{
	public static enum MessageType
	{
		/**
		 * First connection to a host
		 */
		LOGIN,
		
		/**
		 * Accepting a player
		 */
		ACCEPT,
		
		/**
		 * Declining a player
		 */
		DECLINE,
		
		/**
		 * The host is busy and the player needs to wait
		 */
		WAIT,
		
		/**
		 * The player has chosen a name that was already in use
		 */
		NAME_IN_USE,
		
		/**
		 * The host has closed the game
		 */
		CLOSED,
		
		/**
		 * The player was kicked
		 */
		KICKED,
		
		/**
		 * The player left the game
		 */
		LEFT_GAME,
		
		/**
		 * The player took the focus from his application
		 */
		REMOVED_FOCUS
	}
	
	private static final String			TAG				= "ConnectedDevice";
	
	private static final String			ARGS_DELIM		= ",";
	
	private static final char			MESSAGES_DELIM	= '\n';
	
	private final BluetoothSocket		mSocket;
	
	private final OutputStream			mOut;
	
	private final InputStream			mIn;
	
	private final ConnectionListener	mConnectionListener;
	
	private final boolean				mHost;
	
	private boolean						mListeningForMessages;
	
	/**
	 * Creates a new connected device, that listens for messages.
	 * 
	 * @param aSocket
	 *            The Bluetooth socket.
	 * @throws IOException
	 *             if the in-/ or output stream could not be resolved.
	 */
	public ConnectedDevice(final BluetoothSocket aSocket, final ConnectionListener aConnectionListener, boolean aHost) throws IOException
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
	
	public BluetoothDevice getDevice()
	{
		return mSocket.getRemoteDevice();
	}
	
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
			message.append(ARGS_DELIM + Uri.encode(arg));
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
		mListeningForMessages = true;
		while (mListeningForMessages)
		{
			if ( !mSocket.isConnected())
			{
				exit();
			}
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
							args[i] = Uri.decode(args[i]);
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
	}
	
	public void exit()
	{
		mListeningForMessages = false;
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
}
