package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class ItemGroupImpl <T extends Item> implements ItemGroup<T>
{
	private final String				mName;
	
	private final List<T>				mItems		= new ArrayList<T>();
	
	private final HashMap<String, T>	mItemNames	= new HashMap<String, T>();
	
	public ItemGroupImpl(final String aName)
	{
		mName = aName;
	}
	
	@Override
	public T getItem(final String aName)
	{
		return mItemNames.get(aName);
	}
	
	@Override
	public List<T> getItems()
	{
		return mItems;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	protected void addItem(final T aItem)
	{
		mItems.add(aItem);
		mItemNames.put(aItem.getName(), aItem);
		Collections.sort(mItems);
	}
}
