package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
	
	public static PropertyItemGroup create(String aName, String[] aData)
	{
		PropertyItemGroup group = new PropertyItemGroup(aName);
		for (String property : aData)
		{
			group.addItem(PropertyItem.create(property));
		}
		return group;
	}
}
