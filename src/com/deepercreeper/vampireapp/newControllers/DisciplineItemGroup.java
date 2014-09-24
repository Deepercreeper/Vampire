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
	
	private void addItem(final DisciplineItem aItem)
	{
		mItems.add(aItem);
		mItemNames.put(aItem.getName(), aItem);
		Collections.sort(mItems);
	}
	
	private void initParents()
	{
		for (DisciplineItem parent : mItems)
		{
			if (parent.isParentItem())
			{
				for (String subItemName : parent.getSubItemNames())
				{
					DisciplineItem subItem = mItemNames.get(subItemName);
					parent.addSubItem(subItem);
					subItem.setParent(parent);
				}
			}
		}
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
	
	public static DisciplineItemGroup create(String aName, String[] aData)
	{
		DisciplineItemGroup group = new DisciplineItemGroup(aName);
		for (String discipline : aData)
		{
			group.addItem(DisciplineItem.create(discipline));
		}
		group.initParents();
		return group;
	}
}
