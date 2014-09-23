package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BackgroundItemGroup implements ItemGroup<BackgroundItem>
{
	private final String							mName;
	
	private final List<BackgroundItem>				mItems		= new ArrayList<BackgroundItem>();
	
	private final HashMap<String, BackgroundItem>	mItemNames	= new HashMap<String, BackgroundItem>();
	
	public BackgroundItemGroup(String aName)
	{
		mName = aName;
	}
	
	@Override
	public List<BackgroundItem> getItems()
	{
		return mItems;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	@Override
	public BackgroundItem getItem(String aName)
	{
		return mItemNames.get(aName);
	}
	
	@Override
	public void init(Set<BackgroundItem> aItems)
	{
		for (final BackgroundItem item : aItems)
		{
			mItems.add(item);
			mItemNames.put(item.getName(), item);
		}
		Collections.sort(mItems);
	}
}
