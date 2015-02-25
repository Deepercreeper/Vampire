package com.deepercreeper.vampireapp.host;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.FilesUtil;

public class Host
{
	private final String		mName;
	
	private final ItemProvider	mItems;
	
	private final String		mLocation;
	
	public Host(final String aName, final ItemProvider aItems, final String aLocation)
	{
		mName = aName;
		mItems = aItems;
		mLocation = aLocation;
	}
	
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
	
	public String getName()
	{
		return mName;
	}
}
