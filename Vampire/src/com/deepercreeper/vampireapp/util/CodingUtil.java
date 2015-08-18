package com.deepercreeper.vampireapp.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * This utility is used to encode and decode any string so that it is able to be sent or received.
 * 
 * @author vrl
 */
public class CodingUtil
{
	private static final String TAG = "CodingUtil";
	
	private CodingUtil()
	{}
	
	/**
	 * Decodes the given encoded string using UTF-8.
	 * 
	 * @param aString
	 *            The encoded string to decode.
	 * @return the decoded string.
	 */
	public static String decode(final String aString)
	{
		try
		{
			return URLDecoder.decode(aString, "UTF-8");
		}
		catch (final UnsupportedEncodingException e)
		{
			Log.e(TAG, "Could not decode value.");
		}
		return null;
	}
	
	/**
	 * Encodes the given string using UTF-8.
	 * 
	 * @param aString
	 *            The string to encode.
	 * @return the encoded string.
	 */
	public static String encode(final String aString)
	{
		try
		{
			return URLEncoder.encode(aString, "UTF-8");
		}
		catch (final UnsupportedEncodingException e)
		{
			Log.e(TAG, "Could not encode value.");
		}
		return null;
	}
}
