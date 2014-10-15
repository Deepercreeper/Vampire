package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A group for property items.
 * 
 * @author Vincent
 */
public class PropertyItemGroup implements ItemGroup<PropertyItem>
{
	private final String						mName;
	
	private final List<PropertyItem>			mItems		= new ArrayList<PropertyItem>();
	
	private final HashMap<String, PropertyItem>	mItemNames	= new HashMap<String, PropertyItem>();
	
	private PropertyItemGroup(final String aName)
	{
		mName = aName;
	}
	
	@Override
	public List<PropertyItem> getItems()
	{
		return mItems;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	@Override
	public PropertyItem getItem(final String aName)
	{
		return mItemNames.get(aName);
	}
	
	private void addItem(final PropertyItem aItem)
	{
		mItems.add(aItem);
		mItemNames.put(aItem.getName(), aItem);
		Collections.sort(mItems);
	}
	
	/**
	 * Creates a new property item group out of the given data.
	 * 
	 * @param aName
	 *            The group name.
	 * @param aData
	 *            The data out of which is the group created.
	 * @return the created property group.
	 */
	public static PropertyItemGroup create(final String aName, final String[] aData)
	{
		final PropertyItemGroup group = new PropertyItemGroup(aName);
		for (final String property : aData)
		{
			group.addItem(PropertyItem.create(property));
		}
		return group;
	}
}
