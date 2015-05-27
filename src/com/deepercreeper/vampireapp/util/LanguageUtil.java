package com.deepercreeper.vampireapp.util;

import java.util.Locale;
import java.util.TreeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.content.Context;

/**
 * This utility is used to translate all defined keywords.
 * 
 * @author vrl
 */
public class LanguageUtil
{
	private static final String				TAG				= "LanguageUtil";
	
	private static final String				LANGUAGE		= "language";
	
	private static final LanguageUtil		INSTANCE		= new LanguageUtil();
	
	private static boolean					mInitialized	= false;
	
	private Context							mContext;
	
	private final TreeMap<String, String>	mValues			= new TreeMap<String, String>();
	
	private LanguageUtil()
	{}
	
	private void setContext(final Context aContext)
	{
		if (mContext != null)
		{
			return;
		}
		mInitialized = false;
		mContext = aContext;
		mValues.clear();
		if (Locale.getDefault().getLanguage().equals("en"))
		{
			mInitialized = true;
			return;
		}
		final Document pack = FilesUtil.loadDocument(mContext, "language", true);
		if (pack == null)
		{
			mInitialized = true;
			Log.w(TAG, "No language file was detected for the current language.");
			return;
		}
		Element language = null;
		{
			final NodeList list = pack.getElementsByTagName(LANGUAGE);
			for (int i = 0; i < list.getLength(); i++ )
			{
				final Node node = list.item(i);
				if (node instanceof Element)
				{
					language = (Element) node;
					break;
				}
			}
			if (language == null)
			{
				Log.w(TAG, "Could not find language tag.");
				mInitialized = true;
				return;
			}
		}
		final String content = language.getTextContent().trim();
		for (String line : content.split("\n"))
		{
			line = line.replace("\r", "").replace("\n", "").trim();
			if ( !line.isEmpty())
			{
				final String[] keyAndValue = line.split("=");
				if (keyAndValue.length < 2)
				{
					Log.w(TAG, "The following line is no statement: " + line);
				}
				else
				{
					mValues.put(keyAndValue[0], keyAndValue[1]);
				}
			}
		}
		mInitialized = true;
	}
	
	/**
	 * @param aKey
	 * @return the display string if different from the given one.<br>
	 *         Otherwise it just returns the given one.
	 * @note {@link LanguageUtil#init(Context)} needs to be invoked before any string can be translated.
	 */
	public String getValue(final String aKey)
	{
		final String value = mValues.get(aKey);
		if (value == null)
		{
			return aKey;
		}
		return value;
	}
	
	/**
	 * Loads all needed data from the local language file depending on the current locale.
	 * 
	 * @param aContext
	 *            The underlying context.
	 */
	public static void init(final Context aContext)
	{
		INSTANCE.setContext(aContext);
	}
	
	/**
	 * @return the current language instance.
	 */
	public static LanguageUtil instance()
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "LanguageUtil was not initialized yet.");
			return null;
		}
		return INSTANCE;
	}
}
