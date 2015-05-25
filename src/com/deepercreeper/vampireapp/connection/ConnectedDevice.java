package com.deepercreeper.vampireapp.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import android.bluetooth.BluetoothSocket;
import android.net.Uri;

public class ConnectedDevice
{
	public interface MessageListener
	{
		public void receiveMessage(ConnectedDevice aDevice, MessageType aType, String[] aArgs);
	}
	
	public static enum MessageType
	{
		LOGIN, ACCEPT, DECLINE, WAIT
	}
	
	private static final String		ARGS_DELIM		= ",";
	
	private static final char		MESSAGES_DELIM	= '\n';
	
	private final OutputStream		mOut;
	
	private final InputStream		mIn;
	
	private final MessageListener	mListener;
	
	private boolean					mListeningForMessages;
	
	/**
	 * Creates a new connected device, that listens for messages.
	 * 
	 * @param aSocket
	 *            The bluetooth socket.
	 * @param aListener
	 *            The message listener.
	 * @throws IOException
	 *             if the in-/ or output stream could not be resolved.
	 */
	public ConnectedDevice(final BluetoothSocket aSocket, final MessageListener aListener) throws IOException
	{
		mOut = aSocket.getOutputStream();
		mIn = aSocket.getInputStream();
		mListener = aListener;
		
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
	}
	
	/**
	 * Stops listening for messages by other devices.
	 */
	public void stopListening()
	{
		mListeningForMessages = false;
	}
	
	private void listenForMessages()
	{
		mListeningForMessages = true;
		while (mListeningForMessages)
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
							args[i] = Uri.decode(args[i]);
						}
						mListener.receiveMessage(this, MessageType.valueOf(message[0]), args);
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
}
