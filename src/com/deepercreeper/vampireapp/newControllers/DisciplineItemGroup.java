package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DisciplineItemGroup implements ItemGroup<DisciplineItem>
{
	private final String							mName;
	
	private final List<DisciplineItem>				mItems		= new ArrayList<DisciplineItem>();
	
	private final HashMap<String, DisciplineItem>	mItemNames	= new HashMap<String, DisciplineItem>();
	
	public DisciplineItemGroup(String aName)
	{
		mName = aName;
	}
	
	@Override
	public List<DisciplineItem> getItems()
	{
		return mItems;
	}
	
	@Override
	public void init(Set<DisciplineItem> aItems)
	{
		for (final DisciplineItem item : aItems)
		{
			mItems.add(item);
			mItemNames.put(item.getName(), item);
		}
		Collections.sort(mItems);
	}
	
	@Override
	public DisciplineItem getItem(String aName)
	{
		return mItemNames.get(aName);
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
}
