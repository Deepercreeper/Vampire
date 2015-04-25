package com.deepercreeper.vampireapp.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import com.deepercreeper.vampireapp.util.Log;

public class DeviceServer extends Thread
{
	public static final int			ENABLE_BLUETOOTH_REQUEST	= 4;
	
	private static final String		TAG							= "DeviceServer";
	
	private static final UUID		MY_UUID						= UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	private final String			mMacAdress;
	
	private final Activity			mContext;
	
	private final BluetoothAdapter	mBluetoothAdapter;
	
	private final List<String>		mMessages					= new ArrayList<String>();
	
	private BluetoothSocket			mSocket						= null;
	
	private boolean					mRunning					= false;
	
	public DeviceServer(final Activity aContext)
	{
		mContext = aContext;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mMacAdress = mBluetoothAdapter.getAddress();
	}
	
	public boolean tryConnect()
	{
		if ( !mBluetoothAdapter.isEnabled())
		{
			final Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			mContext.startActivityForResult(bluetoothIntent, ENABLE_BLUETOOTH_REQUEST);
			return false;
		}
		try
		{
			mSocket = mBluetoothAdapter.getRemoteDevice(mMacAdress).createRfcommSocketToServiceRecord(MY_UUID);
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not create listening service.");
			return false;
		}
		if (mSocket == null)
		{
			return false;
		}
		startServer();
		return true;
	}
	
	@Override
	public synchronized void start()
	{
		Log.w(TAG, "Tried to start the server.");
	}
	
	private void startServer()
	{
		mRunning = true;
		super.start();
	}
	
	public void stopServer()
	{
		mRunning = false;
	}
	
	@Override
	public void run()
	{
		InputStream input = null;
		try
		{
			input = mSocket.getInputStream();
		}
		catch (final IOException e)
		{
			Log.w(TAG, "Could not create input stream.");
		}
		while (mRunning)
		{
			final StringBuilder messageBuild = new StringBuilder();
			int c;
			try
			{
				while ((c = input.read()) != -1)
				{
					messageBuild.append((char) c);
				}
				if (messageBuild.length() > 0)
				{
					final String message = messageBuild.toString();
					messageBuild.delete(0, messageBuild.length());
					mMessages.add(message);
					System.out.println(message);
				}
			}
			catch (final IOException e)
			{
				// Do nothing
			}
		}
		try
		{
			input.close();
			mSocket.close();
		}
		catch (final IOException e)
		{
			Log.w(TAG, "Could not close input stream and socket.");
		}
	}
}
