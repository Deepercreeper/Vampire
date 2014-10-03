package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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
	
	public static SimpleItemGroup create(final String aData, final int aStartValue, final int aMaxStartValue)
	{
		final String[] data = aData.split(NAME_DELIM);
		final SimpleItemGroup group = new SimpleItemGroup(data[0]);
		if (data.length > 1)
		{
			for (final String item : data[1].split(ITEMS_DELIM))
			{
				group.addItem(SimpleItem.create(item, aStartValue, aMaxStartValue));
			}
		}
		return group;
	}
	
	public static Comparator<SimpleItemGroup> getComparator()
	{
		return new Comparator<SimpleItemGroup>()
		{
			@Override
			public int compare(final SimpleItemGroup aLhs, final SimpleItemGroup aRhs)
			{
				return aLhs.getName().compareTo(aRhs.getName());
			}
		};
	}
}
