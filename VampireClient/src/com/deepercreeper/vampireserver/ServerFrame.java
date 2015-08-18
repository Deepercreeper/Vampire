package com.deepercreeper.vampireserver;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * This server handles the communication between vampire clients and hosts.
 * 
 * @author vrl
 */
public class ServerFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 800;
	
	private static final int HEIGHT = 600;
	
	private final ServerEngine mEngine;
	
	public ServerFrame()
	{
		mEngine = new ServerEngine();
		setSize(WIDTH, HEIGHT);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			};
		});
		setVisible(true);
	}
}
