package com.deepercreeper.vampireserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerEngine
{
	private static final int PORT = 123;
	
	private final List<Socket> mSockets = new ArrayList<>();
	
	private final ServerFrame mFrame;
	
	private boolean mListening;
	
	private Listener mListener;
	
	private ServerSocket mSocket;
	
	public ServerEngine(ServerFrame aFrame)
	{
		mFrame = aFrame;
		try
		{
			mSocket = new ServerSocket(PORT);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (mSocket != null)
		{
			mListener = new Listener();
			mListener.start();
		}
	}
	
	private void addSocket(Socket aSocket)
	{
		mSockets.add(aSocket);
		mFrame.setStatus("Added socket: " + aSocket.getInetAddress().getHostAddress());
		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		mFrame.setStatus("Ready");
	}
	
	public void stop()
	{
		mListening = false;
		try
		{
			mSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private class Listener extends Thread
	{
		@Override
		public void run()
		{
			while (mListening)
			{
				try
				{
					addSocket(mSocket.accept());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
