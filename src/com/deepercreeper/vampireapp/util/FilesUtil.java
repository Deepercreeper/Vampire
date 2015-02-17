package com.deepercreeper.vampireapp.util;

import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import android.content.Context;

public class FilesUtil
{
	private static final String	FILE_ENDING	= ".xml";
	
	private FilesUtil()
	{}
	
	public static Document loadDocument(final Context aContext, final String aName, final boolean aLocale)
	{
		Document doc = null;
		try
		{
			final DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			final String postfix = aLocale ? "-" + Locale.getDefault().getLanguage() : "";
			doc = DOMbuilder.parse(aContext.getAssets().open(aName + postfix + FILE_ENDING));
		}
		catch (final Exception e)
		{
			return null;
		}
		return doc;
	}
}
