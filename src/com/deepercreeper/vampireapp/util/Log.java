package com.deepercreeper.vampireapp.util;

public class Log
{
	public static void w(final String aTag, final String aMessage)
	{
		android.util.Log.w(aTag, aMessage);
	}
	
	public static void e(final String aTag, final String aMessage)
	{
		android.util.Log.e(aTag, aMessage);
	}
	
	public static void i(final String aTag, final String aMessage)
	{
		android.util.Log.i(aTag, aMessage);
	}
}
