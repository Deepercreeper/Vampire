package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DisciplineItemGroup implements ItemGroup<DisciplineItem>
{
	private final String							mName;
	
	private final List<DisciplineItem>				mItems		= new ArrayList<DisciplineItem>();
	
	private final HashMap<String, DisciplineItem>	mItemNames	= new HashMap<String, DisciplineItem>();
	
	private DisciplineItemGroup(final String aName)
	{
		mName = aName;
	}
	
	@Override
	public List<DisciplineItem> getItems()
	{
		return mItems;
	}
	
	@Override
	public void addItem(final DisciplineItem aItem)
	{
		mItems.add(aItem);
		mItemNames.put(aItem.getName(), aItem);
		Collections.sort(mItems);
	}
	
	@Override
	public DisciplineItem getItem(final String aName)
	{
		return mItemNames.get(aName);
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
}
