package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A group of simple items.
 * 
 * @author Vincent
 */
public class SimpleItemGroup implements ItemGroup<SimpleItem>
{
	private static final String					NAME_DELIM	= ":", ITEMS_DELIM = ",";
	
	private final String						mName;
	
	private final List<SimpleItem>				mItems		= new ArrayList<SimpleItem>();
	
	private final HashMap<String, SimpleItem>	mItemNames	= new HashMap<String, SimpleItem>();
	
	private SimpleItemGroup(final String aName)
	{
		mName = aName;
	}
	
	private void addItem(final SimpleItem aItem)
	{
		mItems.add(aItem);
		mItemNames.put(aItem.getName(), aItem);
		Collections.sort(mItems);
	}
	
	@Override
	public List<SimpleItem> getItems()
	{
		return mItems;
	}
	
	@Override
	public SimpleItem getItem(final String aName)
	{
		return mItemNames.get(aName);
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	/**
	 * Creates a new simple item group out of the given data.
	 * 
	 * @param aData
	 *            The data out of which the group is created.
	 * @param aStartValue
	 *            The character creation start value for all values inside this group.
	 * @param aMaxStartValue
	 *            The maximum character creation value for all values inside this group.
	 * @param aMaxValue
	 *            The maximum value for all values inside this group.
	 * @param aFreePointsCost
	 *            The number of experience of free points needed to spend for increasing values of this group.
	 * @return the created simple item group.
	 */
	public static SimpleItemGroup create(final String aData, final int aStartValue, final int aMaxStartValue, final int aMaxValue,
			final int aFreePointsCost)
	{
		final String[] data = aData.split(NAME_DELIM);
		final SimpleItemGroup group = new SimpleItemGroup(data[0]);
		if (data.length > 1)
		{
			for (final String item : data[1].split(ITEMS_DELIM))
			{
				group.addItem(SimpleItem.create(item, aStartValue, aMaxStartValue, aMaxValue, aFreePointsCost));
			}
		}
		return group;
	}
}
