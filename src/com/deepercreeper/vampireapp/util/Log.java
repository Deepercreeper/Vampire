package com.deepercreeper.vampireapp.util;

/**
 * A shortcut for logging messages.
 * 
 * @author vrl
 */
public class Log
{
	/**
	 * Logs an error.
	 * 
	 * @param aTag
	 *            The log tag.
	 * @param aMessage
	 *            The message.
	 */
	public static void e(final String aTag, final String aMessage)
	{
		android.util.Log.e(aTag, aMessage);
	}
	
	/**
	 * Logs an information.
	 * 
	 * @param aTag
	 *            The log tag.
	 * @param aMessage
	 *            The message.
	 */
	public static void i(final String aTag, final String aMessage)
	{
		android.util.Log.i(aTag, aMessage);
	}
	
	/**
	 * Logs a warning.
	 * 
	 * @param aTag
	 *            The log tag.
	 * @param aMessage
	 *            The message.
	 */
	public static void w(final String aTag, final String aMessage)
	{
		android.util.Log.w(aTag, aMessage);
	}
}
