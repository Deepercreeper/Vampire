package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A group of background items.
 * 
 * @author Vincent
 */
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
	
	/**
	 * Creates a new background item group out of the given data.
	 * 
	 * @param aName
	 *            The group name.
	 * @param aData
	 *            The data out of which the group is created.
	 * @return the created group.
	 */
	public static BackgroundItemGroup create(final String aName, final String[] aData)
	{
		final BackgroundItemGroup group = new BackgroundItemGroup(aName);
		for (final String item : aData)
		{
			group.addItem(BackgroundItem.create(item));
		}
		return group;
	}
}
