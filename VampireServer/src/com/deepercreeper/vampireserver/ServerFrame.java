package com.deepercreeper.vampireserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerFrame
{
	private final ServerEngine mEngine;
	
	public ServerFrame()
	{
		mEngine = new ServerEngine();
	}
	
	public void stop()
	{
		mEngine.stop();
	}
	
	public static void main(final String[] args)
	{
		System.out.println("Starting...");
		final ServerFrame frame = new ServerFrame();
		System.out.println("Running");
		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			while ( !reader.readLine().equals("exit"))
			{
				// Do nothing
			}
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Exiting...");
		frame.stop();
	}
}
