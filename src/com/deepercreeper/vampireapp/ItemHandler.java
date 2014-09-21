package com.deepercreeper.vampireapp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ItemHandler
{
	private static final String					HEADER_DELIM	= ":", ITEM_DELIM = ",";
	
	private final HashMap<String, Item[]>		mAttributes		= new HashMap<>();
	
	private final HashMap<String, Item[]>		mAbilities		= new HashMap<>();
	
	private final HashMap<String, Background>	mBackgrounds	= new HashMap<>();
	
	private final HashMap<String, Property>		mProperties		= new HashMap<>();
	
	private final HashMap<String, Item>			mAllItems		= new HashMap<>();
	
	public void init(final String[] aAttributes, final String[] aAbilities, final String[] aBackgrounds, final String[] aProperties)
	{
		fillItems(aAttributes, true);
		fillItems(aAbilities, false);
		
		for (final String background : aBackgrounds)
		{
			mBackgrounds.put(background, new Background(background));
		}
		
		for (String propertyLine : aProperties)
		{
			boolean negative = false;
			if (propertyLine.startsWith(Property.NEGATIVE_PREFIX))
			{
				negative = true;
				propertyLine = propertyLine.substring(1);
			}
			final String[] propertyData = propertyLine.split(HEADER_DELIM);
			mProperties.put(propertyData[0], new Property(propertyData[0], propertyData[1], negative));
		}
	}
	
	private void fillItems(final String[] aSource, final boolean aAttribute)
	{
		final HashMap<String, Item[]> folder = aAttribute ? mAttributes : mAbilities;
		for (final String line : aSource)
		{
			final String[] splitLine = line.split(HEADER_DELIM);
			final String parent = splitLine[0];
			final String[] itemsData = splitLine[1].split(ITEM_DELIM);
			final Item[] items = new Item[itemsData.length];
			folder.put(parent, items);
			for (int i = 0; i < itemsData.length; i++ )
			{
				final Item item = new Item(itemsData[i], parent, aAttribute);
				items[i] = item;
				mAllItems.put(item.getName(), item);
			}
		}
	}
	
	public HashMap<String, HashMap<String, CreationItem>> createItems(final boolean aAttribute)
	{
		final HashMap<String, Item[]> source = aAttribute ? mAttributes : mAbilities;
		final HashMap<String, HashMap<String, CreationItem>> parents = new HashMap<>();
		for (final String parent : source.keySet())
		{
			final HashMap<String, CreationItem> attributes = new HashMap<>();
			for (final Item attribute : source.get(parent))
			{
				attributes.put(attribute.getName(), new CreationItem(attribute));
			}
			parents.put(parent, attributes);
		}
		return parents;
	}
	
	public Property getProperty(final String aName)
	{
		return mProperties.get(aName);
	}
	
	public Collection<Property> getProperties()
	{
		return mProperties.values();
	}
	
	public Background getBackground(final String aName)
	{
		return mBackgrounds.get(aName);
	}
	
	public Collection<Background> getBackgrounds()
	{
		return mBackgrounds.values();
	}
	
	public Item getItem(final String aName)
	{
		return mAllItems.get(aName);
	}
	
	public Item[] getItems(final String aParent, final boolean aAttribute)
	{
		if (aAttribute)
		{
			return mAttributes.get(aParent);
		}
		return mAbilities.get(aParent);
	}
	
	public Set<String> getParents(final boolean aAttribute)
	{
		if (aAttribute)
		{
			return mAttributes.keySet();
		}
		return mAbilities.keySet();
	}
}
