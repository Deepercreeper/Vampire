package com.deepercreeper.vampireserver;

import java.applet.Applet;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class ServerApplet extends Applet
{
	private final List<String> mMessages = new ArrayList<>();
	
	@Override
	public void start()
	{
		new Printer().start();
	}
	
	@Override
	public void paint(final Graphics aG)
	{
		for (int i = 0; i < mMessages.size(); i++ )
		{
			aG.drawString(mMessages.get(i), 0, i * 10 + 10);
		}
	}
	
	public void println(final String aMessage)
	{
		mMessages.add(aMessage);
		repaint();
	}
	
	private class Printer extends Thread
	{
		
		@Override
		public void run()
		{
			println("Halllllooo");
			final ServerEngine server = new ServerEngine(ServerApplet.this);
			try
			{
				Thread.sleep(20000);
			}
			catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
			server.stop();
		}
	}
}
