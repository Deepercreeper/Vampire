package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BackgroundItemGroup implements ItemGroup<BackgroundItem>
{
	private final String							mName;
	
	private final List<BackgroundItem>				mItems		= new ArrayList<BackgroundItem>();
	
	private final HashMap<String, BackgroundItem>	mItemNames	= new HashMap<String, BackgroundItem>();
	
	private BackgroundItemGroup(final String aName)
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
	public BackgroundItem getItem(final String aName)
	{
		return mItemNames.get(aName);
	}
	
	private void addItem(final BackgroundItem aItem)
	{
		mItems.add(aItem);
		mItemNames.put(aItem.getName(), aItem);
		Collections.sort(mItems);
	}
	
	public static BackgroundItemGroup create(String aName, String[] aData)
	{
		BackgroundItemGroup group = new BackgroundItemGroup(aName);
		for (String item : aData)
		{
			group.addItem(BackgroundItem.create(item));
		}
		return group;
	}
}
