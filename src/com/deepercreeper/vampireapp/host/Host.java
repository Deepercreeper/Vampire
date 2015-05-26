package com.deepercreeper.vampireapp.host;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.FilesUtil;

/**
 * This host represents a whole vampire game, that accepts users and handles the game flow.
 * 
 * @author vrl
 */
public class Host
{
	private final String		mName;
	
	private final ItemProvider	mItems;
	
	private final List<Player>	mPlayers	= new ArrayList<Player>();
	
	private final String		mLocation;
	
	/**
	 * Creates a new host with the given name and location.
	 * 
	 * @param aName
	 *            The host name.
	 * @param aItems
	 *            The item provider.
	 * @param aLocation
	 *            The host location.
	 */
	public Host(final String aName, final ItemProvider aItems, final String aLocation)
	{
		mName = aName;
		mItems = aItems;
		mLocation = aLocation;
	}
	
	/**
	 * Creates a host out of the given XML data.
	 * 
	 * @param aXML
	 *            The XML data.
	 * @param aItems
	 *            The item provider.
	 */
	public Host(final String aXML, final ItemProvider aItems)
	{
		mItems = aItems;
		
		final Document doc = FilesUtil.loadDocument(aXML);
		if (doc == null)
		{
			throw new IllegalArgumentException();
		}
		
		// Root element
		final Element root = (Element) doc.getElementsByTagName("host").item(0);
		
		// Meta data
		final Element meta = (Element) root.getElementsByTagName("meta").item(0);
		mName = CodingUtil.decode(meta.getAttribute("name"));
		mLocation = meta.getAttribute("location");
	}
	
	public boolean addPlayer(Player aPlayer)
	{
		if (mPlayers.contains(aPlayer))
		{
			return false;
		}
		mPlayers.add(aPlayer);
		return true;
	}
	
	/**
	 * @return a serialized version of this host, that is possible to be sent and saved.
	 */
	public String serialize()
	{
		final Document doc = FilesUtil.createDocument();
		if (doc == null)
		{
			return null;
		}
		
		// Root element
		final Element root = doc.createElement("host");
		doc.appendChild(root);
		
		// Meta data
		final Element meta = doc.createElement("meta");
		meta.setAttribute("name", CodingUtil.encode(getName()));
		meta.setAttribute("location", mLocation);
		root.appendChild(meta);
		
		return FilesUtil.readDocument(doc);
	}
	
	/**
	 * @return the host name.
	 */
	public String getName()
	{
		return mName;
	}
}
