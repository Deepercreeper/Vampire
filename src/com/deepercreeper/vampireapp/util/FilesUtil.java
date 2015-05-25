package com.deepercreeper.vampireapp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import android.content.Context;

public class FilesUtil
{
	private static final String	TAG			= "FilesUtil";
	
	private static final String	FILE_ENDING	= ".xml";
	
	private FilesUtil()
	{}
	
	public static Document createDocument()
	{
		Document doc = null;
		try
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		}
		catch (final ParserConfigurationException e)
		{
			Log.e(TAG, "Could not create a XML document.");
		}
		return doc;
	}
	
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
	
	public static Document loadDocument(final String aXML)
	{
		final InputStream stream = new ByteArrayInputStream(aXML.getBytes(Charset.defaultCharset()));
		Document doc = null;
		try
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
		}
		catch (final Exception e)
		{
			Log.e(TAG, "Could not read input stream.");
		}
		return doc;
	}
	
	public static String loadFile(final String aFile, final Context aContext)
	{
		String data = null;
		InputStreamReader reader = null;
		try
		{
			reader = new InputStreamReader(aContext.openFileInput(aFile));
			final StringBuilder list = new StringBuilder();
			int c;
			while ((c = reader.read()) != -1)
			{
				list.append((char) c);
			}
			data = list.toString();
		}
		catch (final FileNotFoundException e)
		{
			Log.i(TAG, "No characters saved.");
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not load characters list.");
		}
		try
		{
			if (reader != null)
			{
				reader.close();
			}
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not close reader.");
		}
		return data;
	}
	
	public static String readDocument(final Document aDoc)
	{
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		final StreamResult result = new StreamResult(stream);
		try
		{
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(aDoc), result);
		}
		catch (final TransformerException e)
		{
			Log.e(TAG, "Could not write document into stream.");
		}
		try
		{
			stream.close();
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not close stream.");
		}
		
		return new String(stream.toByteArray(), Charset.defaultCharset());
	}
	
	public static void saveFile(final String aData, final String aFile, final Context aContext)
	{
		try
		{
			final PrintWriter writer = new PrintWriter(aContext.openFileOutput(aFile, Context.MODE_PRIVATE));
			writer.append(aData);
			writer.flush();
			writer.close();
		}
		catch (final IOException e)
		{
			Log.e(TAG, "Could not open file stream.");
		}
	}
}
