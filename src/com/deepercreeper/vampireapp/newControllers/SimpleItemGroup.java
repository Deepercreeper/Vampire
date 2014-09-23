package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SimpleItemGroup implements ItemGroup<SimpleItem>
{
	private final String						mName;
	
	private final List<SimpleItem>				mItems		= new ArrayList<SimpleItem>();
	
	private final HashMap<String, SimpleItem>	mItemNames	= new HashMap<String, SimpleItem>();
	
	public SimpleItemGroup(final String aName)
	{
		mName = aName;
	}
	
	@Override
	public void init(final Set<SimpleItem> aItems)
	{
		for (final SimpleItem item : aItems)
		{
			mItems.add(item);
			mItemNames.put(item.getName(), item);
		}
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
}
