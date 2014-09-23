package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PropertyItemGroup implements ItemGroup<PropertyItem>
{
	private final String						mName;
	
	private final List<PropertyItem>			mItems		= new ArrayList<PropertyItem>();
	
	private final HashMap<String, PropertyItem>	mItemNames	= new HashMap<String, PropertyItem>();
	
	public PropertyItemGroup(final String aName)
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
	
	@Override
	public void init(final Set<PropertyItem> aItems)
	{
		for (final PropertyItem item : aItems)
		{
			mItems.add(item);
			mItemNames.put(item.getName(), item);
		}
		Collections.sort(mItems);
	}
}
