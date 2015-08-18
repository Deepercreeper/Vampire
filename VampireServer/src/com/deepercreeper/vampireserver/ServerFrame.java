package com.deepercreeper.vampireserver;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

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
	
	private final JLabel mStatusLabel = new JLabel("Ready");
	
	public ServerFrame()
	{
		mEngine = new ServerEngine(this);
		setSize(WIDTH, HEIGHT);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				mEngine.stop();
				System.exit(0);
			};
		});
		init();
		setVisible(true);
	}
	
	public void setStatus(String aStatus)
	{
		mStatusLabel.setText(aStatus);
	}
	
	private void init()
	{
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(WIDTH, 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		mStatusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(mStatusLabel);
	}
}
