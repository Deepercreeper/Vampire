package com.deepercreeper.vampireapp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.deepercreeper.vampireapp.character.Currency;
import com.deepercreeper.vampireapp.character.Health;
import com.deepercreeper.vampireapp.character.inventory.Inventory;
import com.deepercreeper.vampireapp.items.implementations.ItemControllerImpl;
import com.deepercreeper.vampireapp.items.implementations.ItemGroupImpl;
import com.deepercreeper.vampireapp.items.implementations.ItemImpl;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.ConditionCreationImpl;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.RestrictionCreationImpl;
import com.deepercreeper.vampireapp.items.implementations.dependencies.DependencyImpl;
import com.deepercreeper.vampireapp.items.interfaces.Dependable;
import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.DestinationType;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.ConditionCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.ConditionCreation.ConditionQueryCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation.RestrictionCreationType;
import com.deepercreeper.vampireapp.lists.controllers.ClanController;
import com.deepercreeper.vampireapp.lists.items.Clan;
import com.deepercreeper.vampireapp.mechanics.Action;
import com.deepercreeper.vampireapp.mechanics.Action.ActionType;
import com.deepercreeper.vampireapp.mechanics.ActionImpl;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;

/**
 * Used to load all needed information for the item provider from the local data.
 * 
 * @author vrl
 */
public class DataUtil
{
	private static final String FILE_ENDING = ".xml";
	
	private static final String TAG = "DataUtil";
	
	private static final String CONTROLLER = "controller";
	
	private static final String CONTROLLERS = "controllers";
	
	private static final String ITEM = "item";
	
	private static final String DEPENDENCY = "dependency";
	
	private static final String GROUP = "group";
	
	private static final String CLAN = "clan";
	
	private static final String CLANS = "clans";
	
	private static final String CONDITION = "condition";
	
	private static final String RESTRICTION = "restriction";
	
	private static final String ACTION = "action";
	
	private static final String ACTIONS = "actions";
	
	private static final String X_MATCHER = ".*\\{x\\}.*";
	
	private static final String NUMBER_MATCHER = ".*\\{[0-9]+\\}.*";
	
	private static final String RANGE_STRING = "-?[0-9]+\\.\\.\\.-?[0-9]+";
	
	private static Document sData;
	
	/**
	 * @param aMessageId
	 *            The message id.
	 * @param aArgs
	 *            The arguments.
	 * @param aContext
	 *            The underlying context.
	 * @return the given string with <code>{0}, {1}, ...</code> replaced with the given arguments.
	 */
	public static String buildMessage(final int aMessageId, final String[] aArgs, final Context aContext)
	{
		return buildMessage(aContext.getString(aMessageId), aArgs);
	}
	
	/**
	 * @param aMessage
	 *            The message.
	 * @param aArgs
	 *            The arguments.
	 * @return the given string with <code>{0}, {1}, ...</code> replaced with the given arguments.
	 */
	public static String buildMessage(final String aMessage, final String[] aArgs)
	{
		if (aMessage == null || aArgs == null)
		{
			Log.w(TAG, "Message or arguments is null.");
			return null;
		}
		String result = aMessage;
		for (int i = 0; i < aArgs.length; i++ )
		{
			result = result.replace("{" + i + "}", aArgs[i]);
		}
		if (result.contains("{x}"))
		{
			final StringBuilder args = new StringBuilder();
			for (final String arg : aArgs)
			{
				args.append(arg);
			}
			result = result.replace("{x}", args.toString());
		}
		if (result.matches(X_MATCHER) || result.matches(NUMBER_MATCHER))
		{
			Log.w(TAG, "Some unfilled wildcards were found inside the message: " + result);
		}
		return result;
	}
	
	/**
	 * @param aStart
	 *            The first value.
	 * @param aEnd
	 *            The last value.
	 * @return a values array that is filled with all integers from {@code aStart} to {@code aEnd}.
	 */
	public static int[] createDefaultValues(final int aStart, final int aEnd)
	{
		final int[] values = new int[aEnd - aStart + 1];
		for (int i = aStart; i <= aEnd; i++ )
		{
			values[i - aStart] = i;
		}
		return values;
	}
	
	/**
	 * @return a new empty XML document.
	 */
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
	
	/**
	 * @param aDoc
	 *            The parent document.
	 * @param aTagName
	 *            The tag name. May be {@code null} if all children should be added.
	 * @return a list of child elements with the given tag name. No children children are added.
	 */
	public static List<Element> getChildren(final Document aDoc, final String aTagName)
	{
		return getChildren(aDoc, aTagName, false);
	}
	
	/**
	 * @param aDoc
	 *            The parent document.
	 * @param aTagName
	 *            The tag name. May be {@code null} if all children should be added.
	 * @param aChildren
	 *            Whether the children of children should be added also.
	 * @return a list of child elements with the given tag name.
	 */
	public static List<Element> getChildren(final Document aDoc, final String aTagName, final boolean aChildren)
	{
		if (aDoc == null)
		{
			Log.w(TAG, "Document is null.");
			return null;
		}
		final List<Element> children = new ArrayList<Element>();
		NodeList nodes;
		nodes = aChildren && aTagName != null ? aDoc.getElementsByTagName(aTagName) : aDoc.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++ )
		{
			if (nodes.item(i) instanceof Element)
			{
				if (aTagName == null || ((Element) nodes.item(i)).getTagName().equals(aTagName))
				{
					children.add((Element) nodes.item(i));
				}
			}
		}
		return children;
	}
	
	/**
	 * @param aElement
	 *            The parent element.
	 * @param aTagName
	 *            The tag name. May be {@code null} if all children should be added.
	 * @return a list of child elements with the given tag name. No children children are added.
	 */
	public static List<Element> getChildren(final Element aElement, final String aTagName)
	{
		return getChildren(aElement, aTagName, false);
	}
	
	/**
	 * @param aElement
	 *            The parent element.
	 * @param aTagName
	 *            The tag name. May be {@code null} if all children should be added.
	 * @param aChildren
	 *            Whether the children of children should be added also.
	 * @return a list of child elements with the given tag name.
	 */
	public static List<Element> getChildren(final Element aElement, final String aTagName, final boolean aChildren)
	{
		if (aElement == null)
		{
			Log.w(TAG, "Parent element is null.");
			return null;
		}
		final List<Element> children = new ArrayList<Element>();
		NodeList nodes;
		nodes = aChildren && aTagName != null ? aElement.getElementsByTagName(aTagName) : aElement.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++ )
		{
			if (nodes.item(i) instanceof Element)
			{
				if (aTagName == null || ((Element) nodes.item(i)).getTagName().equals(aTagName))
				{
					children.add((Element) nodes.item(i));
				}
			}
		}
		return children;
	}
	
	/**
	 * @param aTagName
	 *            The element tag name.
	 * @param aParent
	 *            The parent document.
	 * @return the first child element with the given tag name.
	 */
	public static Element getElement(final Document aParent, final String aTagName)
	{
		if (aParent == null)
		{
			Log.w(TAG, "Parent is null.");
			return null;
		}
		final NodeList children = aParent.getElementsByTagName(aTagName);
		for (int i = 0; i < children.getLength(); i++ )
		{
			final Node node = children.item(i);
			if (node instanceof Element)
			{
				return (Element) node;
			}
		}
		Log.w(TAG, "Can't find any child element of the given document with name " + aTagName + ".");
		return null;
	}
	
	/**
	 * @param aTagName
	 *            The element tag name.
	 * @param aParent
	 *            The parent element.
	 * @return the first child element with the given tag name.
	 */
	public static Element getElement(final Element aParent, final String aTagName)
	{
		if (aParent == null)
		{
			Log.w(TAG, "Parent is null.");
			return null;
		}
		final NodeList children = aParent.getElementsByTagName(aTagName);
		for (int i = 0; i < children.getLength(); i++ )
		{
			final Node node = children.item(i);
			if (node instanceof Element)
			{
				return (Element) node;
			}
		}
		Log.w(TAG, "Can't find any child element of the tag " + aParent.getTagName() + " with name " + aTagName + ".");
		return null;
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return the clan controller.
	 */
	public static ClanController loadClans(final Context aContext)
	{
		return loadClans(getData(aContext));
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return the money settings.
	 */
	public static Currency loadCurrency(final Context aContext)
	{
		final Element element = getSpecialItems(aContext);
		if (element != null)
		{
			final String[] currencies = parseArray(element.getAttribute("currencies"));
			if (currencies.length == 0)
			{
				Log.w(TAG, "Currencies are empty.");
			}
			return new Currency(currencies);
			
		}
		Log.w(TAG, "Can't find special items element.");
		return null;
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return a set of all default actions.
	 */
	public static Set<Action> loadDefaultActions(final Context aContext)
	{
		final Element element = getElement(getData(aContext), ACTIONS);
		if (element != null)
		{
			return loadActions(element);
		}
		Log.w(TAG, "Can't find default actions element.");
		return null;
	}
	
	/**
	 * Loads the given document from the file system.
	 * 
	 * @param aContext
	 *            The underlying context.
	 * @param aName
	 *            The file name.
	 * @param aLocale
	 *            The language type of the file if existing.
	 * @return the language depending document.
	 */
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
			Log.e(TAG, "Can't load given document " + aName + ".");
			return null;
		}
		return doc;
	}
	
	/**
	 * @param aXML
	 *            The XML data.
	 * @return a document created out of the given XML data.
	 */
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
	
	/**
	 * Loads the given file and returns it as a string.
	 * 
	 * @param aFile
	 *            The file name.
	 * @param aContext
	 *            The underlying context.
	 * @return a string containing all contents of the given file.
	 */
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
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return the name of the generation item.
	 */
	public static String loadGenerationItem(final Context aContext)
	{
		final Element element = getSpecialItems(aContext);
		if (element != null)
		{
			final String generationItem = element.getAttribute("generationItem");
			if ( !generationItem.isEmpty())
			{
				return generationItem;
			}
		}
		Log.w(TAG, "Can't load generation item.");
		return null;
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return the health settings.
	 */
	public static Health loadHealth(final Context aContext)
	{
		final Element element = getSpecialItems(aContext);
		if (element != null)
		{
			final int[] defaultHealth = parseValues(element.getAttribute("defaultHealth"));
			final String healthCost = element.getAttribute("healthCost");
			if (defaultHealth.length > 0 && !healthCost.isEmpty())
			{
				return new Health(defaultHealth, healthCost);
			}
		}
		Log.w(TAG, "Can't load health.");
		return null;
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return the inventory settings.
	 */
	public static Inventory loadInventory(final Context aContext)
	{
		final Element element = getSpecialItems(aContext);
		if (element != null)
		{
			final int[] maxWeightSteps = parseValues(element.getAttribute("maxWeightSteps"));
			final String maxWeightItem = element.getAttribute("maxWeightItem");
			if (maxWeightSteps.length > 0 && !maxWeightItem.isEmpty())
			{
				return new Inventory(maxWeightSteps, maxWeightItem);
			}
		}
		Log.w(TAG, "Can't load inventory.");
		return null;
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return a list of all item controllers.
	 */
	public static List<ItemController> loadItems(final Context aContext)
	{
		return loadControllers(getData(aContext));
	}
	
	/**
	 * Creates an array of string out of the given string.
	 * 
	 * @param aList
	 *            The string that should be parsed into an array.
	 * @return an array of strings, contained inside the given string.
	 */
	public static String[] parseArray(final String aList)
	{
		if (aList == null)
		{
			Log.w(TAG, "Array is null.");
			return null;
		}
		final String[] result = aList.split(",");
		for (int i = 0; i < result.length; i++ )
		{
			result[i] = CodingUtil.decode(result[i]);
			if (result[i] == null)
			{
				Log.w(TAG, "Can't parse array: " + aList);
				return null;
			}
		}
		return result;
	}
	
	/**
	 * Writes all string inside the given array into one string.
	 * 
	 * @param aList
	 *            The array of strings.
	 * @return one string containing all strings of the given array.
	 */
	public static String parseArray(final String[] aList)
	{
		if (aList == null)
		{
			Log.w(TAG, "Array is null.");
			return null;
		}
		final StringBuilder list = new StringBuilder();
		for (int i = 0; i < aList.length; i++ )
		{
			if (i != 0)
			{
				list.append(",");
			}
			final String item = CodingUtil.encode(aList[i]);
			if (item == null)
			{
				Log.w(TAG, "Can't parse array: " + list.toString() + " ... ");
				return null;
			}
			list.append(item);
		}
		return list.toString();
	}
	
	/**
	 * @param aFlags
	 *            The boolean array.
	 * @return a string of <code>0</code> and <code>1</code> representing the array.
	 */
	public static String parseFlags(final boolean[] aFlags)
	{
		if (aFlags == null)
		{
			Log.w(TAG, "Array is null.");
			return null;
		}
		final StringBuilder result = new StringBuilder();
		for (final boolean flag : aFlags)
		{
			result.append(flag ? '1' : '0');
		}
		return result.toString();
	}
	
	/**
	 * @param aFlags
	 *            a string of <code>0</code> and <code>1</code>.
	 * @return the boolean array represented by the given string.
	 */
	public static boolean[] parseFlags(final String aFlags)
	{
		if (aFlags == null)
		{
			Log.w(TAG, "Array is null.");
			return null;
		}
		final boolean[] result = new boolean[aFlags.length()];
		for (int i = 0; i < aFlags.length(); i++ )
		{
			result[i] = aFlags.charAt(i) == '1';
		}
		return result;
	}
	
	/**
	 * Writes all strings of the given list into one string.
	 * 
	 * @param aList
	 *            The string list.
	 * @return one string containing all strings inside the list.
	 */
	public static String parseList(final List<String> aList)
	{
		if (aList == null)
		{
			Log.w(TAG, "List is null.");
			return null;
		}
		final StringBuilder list = new StringBuilder();
		for (int i = 0; i < aList.size(); i++ )
		{
			if (i != 0)
			{
				list.append(",");
			}
			final String string = aList.get(i);
			if (string == null)
			{
				Log.w(TAG, "List contains null: " + aList.toString());
				return null;
			}
			list.append(string);
		}
		return list.toString();
	}
	
	/**
	 * Parses the given string into a list of strings.
	 * 
	 * @param aList
	 *            The string to parse.
	 * @return a list of all strings contained inside the given string.
	 */
	public static List<String> parseList(final String aList)
	{
		return Arrays.asList(parseArray(aList));
	}
	
	/**
	 * @param aMap
	 *            The string to integer map.
	 * @return a string representing the given map.
	 */
	public static String parseMap(final Map<String, Integer> aMap)
	{
		if (aMap == null)
		{
			Log.w(TAG, "Map is null.");
			return null;
		}
		final StringBuilder string = new StringBuilder();
		boolean first = true;
		for (final String key : aMap.keySet())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				string.append(",");
			}
			if (key == null || aMap.get(key) == null)
			{
				Log.w(TAG, "Map contains null: " + aMap.toString());
				return null;
			}
			final String encodedKey = CodingUtil.encode(key);
			if (encodedKey == null)
			{
				Log.w(TAG, "Map contains unencodable key: " + key);
				return null;
			}
			string.append(encodedKey + "=" + aMap.get(key));
		}
		return string.toString();
	}
	
	/**
	 * @param aMap
	 *            A string that represents a string to integer map.
	 * @return a map out of the given string.
	 */
	public static Map<String, Integer> parseMap(final String aMap)
	{
		if (aMap == null)
		{
			Log.w(TAG, "Map is null.");
			return null;
		}
		final Map<String, Integer> map = new HashMap<String, Integer>();
		final String[] entries = aMap.split(",");
		for (final String entry : entries)
		{
			final String[] keyAndValue = entry.split("=");
			if (keyAndValue.length != 2)
			{
				Log.w(TAG, "Can't parse map entry: " + entry);
				return null;
			}
			final String key = CodingUtil.decode(keyAndValue[0]);
			Integer value = null;
			try
			{
				value = Integer.parseInt(keyAndValue[1]);
			}
			catch (final NumberFormatException e)
			{}
			if (key == null || value == null)
			{
				Log.w(TAG, "Can't decode key or parse value: " + entry);
				return null;
			}
			map.put(key, value);
		}
		return map;
	}
	
	/**
	 * @param aMap
	 *            The map to parse.
	 * @return a sparse integer array that has integer keys and values.
	 */
	public static SparseIntArray parseValueMap(final String aMap)
	{
		if (aMap == null)
		{
			Log.w(TAG, "Map is null.");
			return null;
		}
		final SparseIntArray map = new SparseIntArray();
		final String[] entries = aMap.split(",");
		for (final String entry : entries)
		{
			final String[] keyAndValue = entry.split("=");
			if (keyAndValue.length != 2)
			{
				Log.w(TAG, "Can't parse map entry: " + entry);
				return null;
			}
			Integer key = null;
			Integer value = null;
			try
			{
				key = Integer.parseInt(keyAndValue[0]);
				value = Integer.parseInt(keyAndValue[1]);
			}
			catch (final NumberFormatException e)
			{}
			if (key == null || value == null)
			{
				Log.w(TAG, "Can't parse key or value: " + entry);
				return null;
			}
			map.put(key, value);
		}
		return map;
	}
	
	/**
	 * Writes the given integer array into one string.
	 * 
	 * @param aValues
	 *            The array of integers.
	 * @return a string, containing all integers.
	 */
	public static String parseValues(final int[] aValues)
	{
		if (aValues == null)
		{
			Log.w(TAG, "Array is null.");
			return null;
		}
		final StringBuilder values = new StringBuilder();
		for (int i = 0; i < aValues.length; i++ )
		{
			if (i != 0)
			{
				values.append(",");
			}
			values.append(aValues[i]);
		}
		return values.toString();
	}
	
	/**
	 * Parses the given string into an array of integers.
	 * 
	 * @param aValues
	 *            The string to parse.
	 * @return an array of integers, contained inside the given string.
	 */
	public static int[] parseValues(final String aValues)
	{
		if (aValues == null)
		{
			Log.w(TAG, "Array is null.");
			return null;
		}
		final String[] integers = aValues.split(",");
		final int[] values = new int[integers.length];
		for (int i = 0; i < integers.length; i++ )
		{
			try
			{
				values[i] = Integer.parseInt(integers[i]);
			}
			catch (final NumberFormatException e)
			{
				Log.w(TAG, "Can't parse value: " + integers[i]);
				return null;
			}
		}
		return values;
	}
	
	/**
	 * @param aMap
	 *            The map.
	 * @return a map that returns an integer array for each value.
	 */
	public static SparseArray<int[]> parseValuesMap(final String aMap)
	{
		if (aMap == null)
		{
			Log.w(TAG, "Map is null.");
			return null;
		}
		final SparseArray<int[]> map = new SparseArray<int[]>();
		final String[] entries = aMap.split(";");
		for (final String entry : entries)
		{
			final String[] keyAndValue = entry.split("=");
			if (keyAndValue.length != 2)
			{
				Log.w(TAG, "Can't parse key and values: " + entry);
				return null;
			}
			int key = -1;
			try
			{
				key = Integer.parseInt(keyAndValue[0]);
			}
			catch (final NumberFormatException e)
			{
				Log.w(TAG, "Can't parse key: " + keyAndValue[0]);
				return null;
			}
			final String[] valueStrings = keyAndValue[1].split(",");
			if (valueStrings.length == 0)
			{
				Log.w(TAG, "Can't parse values: " + keyAndValue[1]);
				return null;
			}
			int[] values;
			if (valueStrings.length == 1 && valueStrings[0].matches(RANGE_STRING))
			{
				final String[] startAndEnd = valueStrings[0].split("\\.\\.\\.");
				values = createDefaultValues(Integer.parseInt(startAndEnd[0]), Integer.parseInt(startAndEnd[1]));
			}
			else
			{
				values = new int[valueStrings.length];
				for (int i = 0; i < valueStrings.length; i++ )
				{
					try
					{
						values[i] = Integer.parseInt(valueStrings[i]);
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse value: " + valueStrings[i]);
						return null;
					}
				}
			}
			map.put(key, values);
		}
		return map;
	}
	
	/**
	 * @param aDoc
	 *            A XML document.
	 * @return a string containing the whole document.
	 */
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
	
	/**
	 * Saves the given data to the given file.
	 * 
	 * @param aData
	 *            The data that should be saved.
	 * @param aFile
	 *            The file where to store the data.
	 * @param aContext
	 *            The underlying context.
	 */
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
	
	/**
	 * @param aSaveable
	 *            The item to serialize.
	 * @return a string containing the given saveable.
	 */
	public static String serialize(final Saveable aSaveable)
	{
		if (aSaveable == null)
		{
			Log.w(TAG, "Can't serialize null.");
			return null;
		}
		final Document doc = createDocument();
		if (doc == null)
		{
			Log.w(TAG, "Can't create new document.");
			return null;
		}
		
		doc.appendChild(aSaveable.asElement(doc));
		
		return readDocument(doc);
	}
	
	private static Document getData(final Context aContext)
	{
		if (sData == null)
		{
			sData = loadDocument(aContext, "data", false);
		}
		return sData;
	}
	
	private static Element getSpecialItems(final Context aContext)
	{
		return getElement(getData(aContext), "special-items");
	}
	
	private static Set<Action> loadActions(final Element aItem)
	{
		if (aItem == null)
		{
			Log.w(TAG, "Parent node is null.");
			return null;
		}
		final Set<Action> actions = new HashSet<Action>();
		for (final Element child : getChildren(aItem, ACTION))
		{
			final ActionType type = Action.ActionType.get(child.getAttribute("type"));
			if (type == null)
			{
				Log.w(TAG, "Can't find action type " + child.getAttribute("type"));
				continue;
			}
			final String name = child.getAttribute("name");
			if (name.isEmpty())
			{
				Log.w(TAG, "No action name defined.");
				continue;
			}
			int minLevel = 0;
			int minDices = 0;
			int instantSuccess = 0;
			String[] dices = new String[0];
			String[] costDices = new String[0];
			String[] cost = new String[0];
			
			if (child.hasAttribute("minDices"))
			{
				try
				{
					minDices = Integer.parseInt(child.getAttribute("minDices"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse minimum dices of " + name + ".");
				}
			}
			if (child.hasAttribute("instantSuccess"))
			{
				try
				{
					instantSuccess = Integer.parseInt(child.getAttribute("instantSuccess"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse instant success of " + name + ".");
				}
			}
			if (child.hasAttribute("dices"))
			{
				dices = parseArray(child.getAttribute("dices"));
				if (dices.length == 0)
				{
					Log.w(TAG, "Dices of " + name + " is empty.");
				}
			}
			if (child.hasAttribute("costDices"))
			{
				costDices = parseArray(child.getAttribute("costDices"));
				if (costDices.length == 0)
				{
					Log.w(TAG, "Cost dices of " + name + " is empty.");
				}
			}
			if (child.hasAttribute("costs"))
			{
				cost = parseArray(child.getAttribute("cost"));
				if (cost.length == 0)
				{
					Log.w(TAG, "Costs of " + name + " is empty.");
				}
			}
			if (child.hasAttribute("minLevel"))
			{
				try
				{
					minLevel = Integer.parseInt(child.getAttribute("minLevel"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse minimum level of " + name + ".");
				}
			}
			actions.add(new ActionImpl(name, type, minLevel, minDices, instantSuccess, dices, costDices, cost));
		}
		return actions;
	}
	
	private static ClanController loadClans(final Document aDoc)
	{
		if (aDoc == null)
		{
			Log.w(TAG, "The document is null.");
			return null;
		}
		final ClanController controller = new ClanController();
		final List<Clan> clansList = new ArrayList<Clan>();
		for (final Element clanNode : getChildren(getElement(aDoc, CLANS), CLAN))
		{
			final String name = clanNode.getAttribute("name");
			if (name.isEmpty())
			{
				Log.w(TAG, "Clan with empty name.");
				continue;
			}
			final Clan clan = new Clan(name);
			clan.addRestrictions(loadRestrictions(clanNode));
			clansList.add(clan);
		}
		controller.init(clansList);
		return controller;
	}
	
	private static Set<ConditionCreation> loadConditions(final Element aRestrictionNode)
	{
		if (aRestrictionNode == null)
		{
			Log.w(TAG, "Parent node is null.");
			return null;
		}
		final Set<ConditionCreation> conditions = new HashSet<ConditionCreation>();
		for (final Element child : getChildren(aRestrictionNode, CONDITION))
		{
			final ConditionQueryCreation query = ConditionQueryCreation.getQuery(child.getAttribute("query"));
			if (query == null)
			{
				Log.w(TAG, "Can't find condition query " + child.getAttribute("query") + ".");
				continue;
			}
			final boolean persistent = Boolean.valueOf(child.getAttribute("persistent"));
			String itemName = null;
			int minimum = Integer.MIN_VALUE;
			int maximum = Integer.MAX_VALUE;
			int index = 0;
			if (child.hasAttribute("range"))
			{
				final String range = child.getAttribute("range");
				
				if (range.startsWith("="))
				{
					try
					{
						minimum = maximum = Integer.parseInt(range.substring(1));
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse range " + range + ".");
					}
				}
				else if (range.startsWith("<"))
				{
					try
					{
						maximum = Integer.parseInt(range.substring(1)) - 1;
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse range " + range + ".");
					}
				}
				else if (range.startsWith(">"))
				{
					try
					{
						minimum = Integer.parseInt(range.substring(1)) + 1;
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse range " + range + ".");
					}
				}
				else if (range.contains("-"))
				{
					try
					{
						minimum = Integer.parseInt(range.split("-")[0]);
						maximum = Integer.parseInt(range.split("-")[1]);
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse range " + range + ".");
						minimum = Integer.MIN_VALUE;
					}
				}
			}
			if (child.hasAttribute("itemName"))
			{
				itemName = child.getAttribute("itemName");
				if (itemName.isEmpty())
				{
					itemName = null;
					Log.w(TAG, "Item name is empty.");
				}
			}
			if (child.hasAttribute("index"))
			{
				try
				{
					index = Integer.parseInt(child.getAttribute("index"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse index " + child.getAttribute("index") + ".");
					continue;
				}
			}
			conditions.add(new ConditionCreationImpl(query, itemName, minimum, maximum, index, persistent));
		}
		return conditions;
	}
	
	private static List<ItemController> loadControllers(final Document aDoc)
	{
		if (aDoc == null)
		{
			Log.w(TAG, "Document is null.");
			return null;
		}
		final List<ItemController> controllersList = new ArrayList<ItemController>();
		for (final Element controller : getChildren(getElement(aDoc, CONTROLLERS), CONTROLLER))
		{
			final String name = controller.getAttribute("name");
			int[] maxValues = null;
			if (controller.hasAttribute("maxValues"))
			{
				maxValues = parseValues(controller.getAttribute("maxValues"));
			}
			final ItemController itemController = new ItemControllerImpl(name, maxValues);
			itemController.addGroups(loadGroups(controller));
			controllersList.add(itemController);
		}
		if ( !checkDependencies(controllersList))
		{
			Log.w(TAG, "Found cyclic dependencies!");
			return null;
		}
		return controllersList;
	}
	
	private static boolean checkDependencies(final List<ItemController> aControllers)
	{
		final Map<Dependency, Dependable> dependencies = new HashMap<Dependency, Dependable>();
		for (final ItemController controller : aControllers)
		{
			// Use when dependencies are able to have a controller or group destination.
			// for (Dependency dependency : controller.getDependencies())
			// {
			// dependencies.put(dependency, controller);
			// }
			for (final ItemGroup group : controller.getGroupsList())
			{
				// for (Dependency dependency : group.getDependencies())
				// {
				// dependencies.put(dependency, group);
				// }
				for (final Item item : group.getItemsList())
				{
					for (final Dependency dependency : item.getDependencies())
					{
						dependencies.put(dependency, item);
					}
				}
			}
		}
		final Set<Dependency> seen = new HashSet<Dependency>();
		final Set<Dependency> done = new HashSet<Dependency>();
		for (final Dependency dependency : dependencies.keySet())
		{
			if (done.contains(dependency))
			{
				continue;
			}
			seen.clear();
			final Set<Dependency> currents = new HashSet<Dependency>();
			currents.add(dependency);
			boolean hasItemDependensies;
			do
			{
				final Set<Dependency> newCurrents = new HashSet<Dependency>();
				for (final Dependency current : currents)
				{
					seen.add(current);
					for (final Dependable dependable : dependencies.values())
					{
						if (dependable instanceof Item)
						{
							final Item item = (Item) dependable;
							if (item.getName().equals(current.getItem()))
							{
								for (final Dependency newDependency : item.getDependencies())
								{
									if (seen.contains(newDependency))
									{
										Log.w(TAG, "Cyclic dependency: " + newDependency.getItem());
										return false;
									}
									newCurrents.add(newDependency);
								}
							}
						}
					}
				}
				currents.clear();
				currents.addAll(newCurrents);
				hasItemDependensies = false;
				for (final Dependency current : currents)
				{
					if (current.getDestinationType().equals(DestinationType.ITEM))
					{
						hasItemDependensies = true;
					}
				}
			}
			while (hasItemDependensies);
			done.addAll(seen);
		}
		return true;
	}
	
	private static List<Dependency> loadDependencies(final Element aParentNode)
	{
		if (aParentNode == null)
		{
			Log.w(TAG, "Parent element is null.");
			return null;
		}
		final List<Dependency> dependencies = new ArrayList<Dependency>();
		for (final Element child : getChildren(aParentNode, DEPENDENCY))
		{
			final Dependency.Type type = Dependency.Type.get(child.getAttribute("type"));
			final Dependency.DestinationType destinationType = Dependency.DestinationType.get(child.getAttribute("destinationType"));
			if (type == null || destinationType == null)
			{
				Log.w(TAG, "Can't load dependency type or destination type: " + child.getAttribute("type") + ", "
						+ child.getAttribute("destinationType"));
				continue;
			}
			final boolean creationDependency = Boolean.parseBoolean(child.getAttribute("creationDependency"));
			SparseIntArray value = null;
			SparseArray<int[]> values = null;
			final String item = null;
			if (child.hasAttribute("value"))
			{
				value = parseValueMap(child.getAttribute("value"));
			}
			if (child.hasAttribute("values"))
			{
				values = parseValuesMap(child.getAttribute("values"));
			}
			final DependencyImpl dependency = new DependencyImpl(type, destinationType, item, value, values, creationDependency);
			dependencies.add(dependency);
		}
		return dependencies;
	}
	
	private static List<ItemGroup> loadGroups(final Element aController)
	{
		if (aController == null)
		{
			Log.w(TAG, "Parent element is null.");
			return null;
		}
		final List<ItemGroup> groupsList = new ArrayList<ItemGroup>();
		for (final Element child : getChildren(aController, GROUP))
		{
			final String name = child.getAttribute("name");
			final boolean mutable = Boolean.parseBoolean(child.getAttribute("mutable"));
			final boolean freeMutable = Boolean.parseBoolean(child.getAttribute("freeMutable"));
			final boolean hostMutable = Boolean.parseBoolean(child.getAttribute("hostMutable"));
			final boolean valueGroup = Boolean.parseBoolean(child.getAttribute("value"));
			final boolean order = Boolean.parseBoolean(child.getAttribute("order"));
			int maxValue = Integer.MAX_VALUE;
			int startValue = 0;
			int maxLowLevelValue = 0;
			int freePointsCost = 0;
			int epCost = 0;
			int epCostMultiplicator = 0;
			int epCostNew = 0;
			int maxItems = Integer.MAX_VALUE;
			if (child.hasAttribute("maxItems"))
			{
				try
				{
					maxItems = Integer.parseInt(child.getAttribute("maxItems"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse " + child.getAttribute("maxItems"));
					continue;
				}
			}
			if (valueGroup)
			{
				if (child.hasAttribute("epCost"))
				{
					try
					{
						epCost = Integer.parseInt(child.getAttribute("epCost"));
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse " + child.getAttribute("epCost"));
						continue;
					}
				}
				if (child.hasAttribute("epCostMulti"))
				{
					try
					{
						epCostMultiplicator = Integer.parseInt(child.getAttribute("epCostMulti"));
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse " + child.getAttribute("epCostMulti"));
						continue;
					}
				}
				if (child.hasAttribute("epCostNew"))
				{
					try
					{
						epCostNew = Integer.parseInt(child.getAttribute("epCostNew"));
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse " + child.getAttribute("epCostNew"));
						continue;
					}
				}
				if (child.hasAttribute("maxValue"))
				{
					try
					{
						maxValue = Integer.parseInt(child.getAttribute("maxValue"));
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse " + child.getAttribute("maxValue"));
						continue;
					}
				}
				if (child.hasAttribute("freePointsCost"))
				{
					try
					{
						freePointsCost = Integer.parseInt(child.getAttribute("freePointsCost"));
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse " + child.getAttribute("freePointsCost"));
						continue;
					}
				}
				if (child.hasAttribute("maxLowLevelValue"))
				{
					try
					{
						maxLowLevelValue = Integer.parseInt(child.getAttribute("maxLowLevelValue"));
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse " + child.getAttribute("maxLowLevelValue"));
						continue;
					}
				}
				else
				{
					maxLowLevelValue = maxValue;
				}
				if (child.hasAttribute("startValue"))
				{
					try
					{
						startValue = Integer.parseInt(child.getAttribute("startValue"));
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse " + child.getAttribute("startValue"));
						continue;
					}
				}
			}
			final ItemGroup group = new ItemGroupImpl(name, mutable, order, freeMutable, hostMutable, maxLowLevelValue, startValue, maxValue,
					freePointsCost, valueGroup, maxItems, epCost, epCostNew, epCostMultiplicator);
			for (final Dependency dependency : loadDependencies(child))
			{
				group.addDependency(dependency);
			}
			for (final Item item : loadItems(child, group, null))
			{
				group.addItem(item);
			}
			groupsList.add(group);
		}
		return groupsList;
	}
	
	private static List<Item> loadItems(final Element aParentNode, final ItemGroup aParentGroup, final Item aParentItem)
	{
		if (aParentNode == null)
		{
			Log.w(TAG, "Parent element is null.");
			return null;
		}
		final List<Item> itemsList = new ArrayList<Item>();
		for (final Element child : getChildren(aParentNode, ITEM))
		{
			final String name = child.getAttribute("name");
			if (name.isEmpty())
			{
				Log.w(TAG, "Name is empty.");
				continue;
			}
			final boolean needsDescription = Boolean.parseBoolean(child.getAttribute("needsDescription"));
			final boolean parent = Boolean.parseBoolean(child.getAttribute("parent"));
			final boolean order = Boolean.parseBoolean(child.getAttribute("order"));
			final boolean mutableParent = Boolean.parseBoolean(child.getAttribute("mutableParent"));
			boolean valueItem = aParentGroup.isValueGroup();
			int[] values = null;
			int maxLowLevelValue = Integer.MAX_VALUE;
			int startValue = -1;
			int epCost = -1;
			int epCostNew = -1;
			int epCostMultiplicator = -1;
			
			if (child.hasAttribute("maxLowLevelValue"))
			{
				try
				{
					maxLowLevelValue = Integer.parseInt(child.getAttribute("maxLowLevelValue"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse " + child.getAttribute("maxLowLevelValue"));
					continue;
				}
			}
			if (child.hasAttribute("epCost"))
			{
				try
				{
					epCost = Integer.parseInt(child.getAttribute("epCost"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse " + child.getAttribute("epCost"));
					continue;
				}
			}
			if (child.hasAttribute("epCostNew"))
			{
				try
				{
					epCostNew = Integer.parseInt(child.getAttribute("epCostNew"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse " + child.getAttribute("epCostNew"));
					continue;
				}
			}
			if (child.hasAttribute("epCostMulti"))
			{
				try
				{
					epCostMultiplicator = Integer.parseInt(child.getAttribute("epCostMulti"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse " + child.getAttribute("epCostMulti"));
					continue;
				}
			}
			if (child.hasAttribute("startValue"))
			{
				try
				{
					startValue = Integer.parseInt(child.getAttribute("startValue"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse " + child.getAttribute("startValue"));
					continue;
				}
			}
			if (child.hasAttribute("valueItem"))
			{
				valueItem = Boolean.parseBoolean(child.getAttribute("valueItem"));
			}
			if (valueItem)
			{
				if (child.hasAttribute("values"))
				{
					values = parseValues(child.getAttribute("values"));
				}
				else
				{
					values = aParentGroup.getDefaultValues();
				}
			}
			
			final Item item;
			item = new ItemImpl(name, aParentGroup, needsDescription, parent, mutableParent, order, maxLowLevelValue, values, startValue, epCost,
					epCostNew, epCostMultiplicator, aParentItem);
			for (final Item childItem : loadItems(child, aParentGroup, item))
			{
				item.addChild(childItem);
			}
			for (final Dependency dependency : loadDependencies(child))
			{
				item.addDependency(dependency);
			}
			for (final Action action : loadActions(child))
			{
				item.addAction(action);
			}
			itemsList.add(item);
		}
		return itemsList;
	}
	
	private static Set<RestrictionCreation> loadRestrictions(final Element aClanNode)
	{
		if (aClanNode == null)
		{
			Log.w(TAG, "Parent element is null.");
			return null;
		}
		final Set<RestrictionCreation> restrictions = new HashSet<RestrictionCreation>();
		for (final Element child : getChildren(aClanNode, RESTRICTION))
		{
			final RestrictionCreationType type = RestrictionCreationType.get(child.getAttribute("type"));
			
			if (type == null)
			{
				Log.w(TAG, "Restriction type could not be found: " + child.getAttribute("type"));
				continue;
			}
			
			final boolean creationRestriction = Boolean.parseBoolean(child.getAttribute("creationRestriction"));
			String itemName = null;
			int minimum = Integer.MIN_VALUE;
			int maximum = Integer.MAX_VALUE;
			int index = 0;
			int value = 0;
			List<String> items = null;
			
			if (child.hasAttribute("itemName"))
			{
				itemName = child.getAttribute("itemName");
				if (itemName.isEmpty())
				{
					Log.w(TAG, "Item name is empty.");
				}
			}
			if (child.hasAttribute("range"))
			{
				final String range = child.getAttribute("range");
				
				if (range.startsWith("="))
				{
					try
					{
						minimum = maximum = Integer.parseInt(range.substring(1));
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse range " + range + ".");
						continue;
					}
				}
				else if (range.startsWith("<"))
				{
					try
					{
						maximum = Integer.parseInt(range.substring(1)) - 1;
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse range " + range + ".");
						continue;
					}
				}
				else if (range.startsWith(">"))
				{
					try
					{
						minimum = Integer.parseInt(range.substring(1)) + 1;
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse range " + range + ".");
						continue;
					}
				}
				else if (range.contains("-"))
				{
					try
					{
						minimum = Integer.parseInt(range.split("-")[0]);
						maximum = Integer.parseInt(range.split("-")[1]);
					}
					catch (final NumberFormatException e)
					{
						Log.w(TAG, "Can't parse range " + range + ".");
						continue;
					}
				}
			}
			if (child.hasAttribute("items"))
			{
				items = Arrays.asList(parseArray(child.getAttribute("items")));
			}
			if (child.hasAttribute("index"))
			{
				try
				{
					index = Integer.parseInt(child.getAttribute("index"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse " + child.getAttribute("index") + ".");
					continue;
				}
			}
			if (child.hasAttribute("value"))
			{
				try
				{
					value = Integer.parseInt(child.getAttribute("value"));
				}
				catch (final NumberFormatException e)
				{
					Log.w(TAG, "Can't parse " + child.getAttribute("value") + ".");
					continue;
				}
			}
			
			final RestrictionCreation restriction = new RestrictionCreationImpl(type, itemName, minimum, maximum, items, index, value,
					creationRestriction);
					
			for (final ConditionCreation condition : loadConditions(child))
			{
				restriction.addCondition(condition);
			}
			restrictions.add(restriction);
		}
		return restrictions;
	}
}
