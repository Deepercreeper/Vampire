package com.deepercreeper.vampireapp;

import java.util.HashMap;
import java.util.Set;

public class ItemHandler
{
	private static final String				HEADER_DELIM	= ":", ITEM_DELIM = ",";
	
	private final HashMap<String, Item[]>	mAttributes		= new HashMap<>();
	
	private final HashMap<String, Item[]>	mAbilities		= new HashMap<>();
	
	public void init(final String[] aAttributes, final String[] aAbilities)
	{
		for (final String line : aAttributes)
		{
			final String[] splitLine = line.split(HEADER_DELIM);
			final String parent = splitLine[0];
			final String[] attributesData = splitLine[1].split(ITEM_DELIM);
			final Item[] attributes = new Item[attributesData.length];
			mAttributes.put(parent, attributes);
			for (int i = 0; i < attributesData.length; i++ )
			{
				attributes[i] = new Item(attributesData[i], parent, true);
			}
		}
		
		for (final String line : aAbilities)
		{
			final String[] splitLine = line.split(HEADER_DELIM);
			final String parent = splitLine[0];
			final String[] abilitiesData = splitLine[1].split(ITEM_DELIM);
			final Item[] abilities = new Item[abilitiesData.length];
			mAbilities.put(parent, abilities);
			for (int i = 0; i < abilitiesData.length; i++ )
			{
				abilities[i] = new Item(abilitiesData[i], parent, false);
			}
		}
	}
	
	public HashMap<String, HashMap<String, CreationItem>> createItems(final boolean aAttributes)
	{
		final HashMap<String, Item[]> source = aAttributes ? mAttributes : mAbilities;
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
	
	public Item[] getItems(final String aParent, final boolean aAttributes)
	{
		return aAttributes ? mAttributes.get(aParent) : mAbilities.get(aParent);
	}
	
	public Set<String> getParents(final boolean aAttributes)
	{
		return aAttributes ? mAttributes.keySet() : mAbilities.keySet();
	}
}
