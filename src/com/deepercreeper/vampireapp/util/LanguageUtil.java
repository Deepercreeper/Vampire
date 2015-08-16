package com.deepercreeper.vampireapp.util;

import java.util.Locale;
import java.util.TreeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;

/**
 * This utility is used to translate all defined keywords.
 * 
 * @author vrl
 */
public class LanguageUtil
{
	private static final String TAG = "LanguageUtil";
	
	private static final String LANGUAGE = "language";
	
	private static final LanguageUtil INSTANCE = new LanguageUtil();
	
	private static boolean mInitialized = false;
	
	private Context mContext;
	
	private final TreeMap<String, String> mValues = new TreeMap<String, String>();
	
	private LanguageUtil()
	{}
	
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
	 * Translates all strings inside the given array if the corresponding boolean is true.
	 * 
	 * @param aArgs
	 *            The string argument array.
	 * @param aTranslatedArgs
	 *            Whether the current argument should be translated.
	 * @return an array of translated strings.
	 */
	public String[] translateArray(final String[] aArgs, final boolean[] aTranslatedArgs)
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "LanguageUtil was not initialized yet.");
			return null;
		}
		if (aArgs.length != aTranslatedArgs.length)
		{
			Log.w(TAG, "Tried to translate an array of length" + aArgs.length + " with a flag list of length " + aTranslatedArgs.length + ".");
			return null;
		}
		final String[] arguments = new String[aArgs.length];
		for (int i = 0; i < arguments.length; i++ )
		{
			if (aTranslatedArgs[i])
			{
				try
				{
					final int id = Integer.parseInt(aArgs[i]);
					arguments[i] = mContext.getString(id);
				}
				catch (final NumberFormatException e)
				{
					arguments[i] = LanguageUtil.instance().getValue(aArgs[i]);
				}
			}
			else
			{
				arguments[i] = aArgs[i];
			}
		}
		return arguments;
	}
	
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
		final Document pack = DataUtil.loadDocument(mContext, "language", true);
		if (pack == null)
		{
			mInitialized = true;
			Log.w(TAG, "No language file was detected for the current language.");
			return;
		}
		Element language = DataUtil.getElement(pack, LANGUAGE);
		if (language == null)
		{
			Log.w(TAG, "Could not find language tag.");
			mInitialized = true;
			return;
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
