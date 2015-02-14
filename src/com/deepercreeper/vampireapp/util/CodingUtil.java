package com.deepercreeper.vampireapp.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CodingUtil
{
	private static final String	TAG	= "CodingUtil";
	
	private CodingUtil()
	{}
	
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
	
	public static String decode(final String aString)
	{
		try
		{
			return URLDecoder.decode(aString, "UTF-8");
		}
		catch (final UnsupportedEncodingException e)
		{
			Log.e(TAG, "Could not encode value.");
		}
		return null;
	}
}
