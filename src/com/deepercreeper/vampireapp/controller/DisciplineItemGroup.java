package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A group of discipline items.
 * 
 * @author Vincent
 */
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
		for (final DisciplineItem parent : mItems)
		{
			if (parent.isParentItem())
			{
				for (final String subItemName : parent.getSubItemNames())
				{
					final SubDisciplineItem subItem = (SubDisciplineItem) mItemNames.get(subItemName);
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
	
	/**
	 * Creates a new discipline group out of the given data.
	 * 
	 * @param aName
	 *            The group name.
	 * @param aData
	 *            The data out of which the group is created.
	 * @return the created discipline item group.
	 */
	public static DisciplineItemGroup create(final String aName, final String[] aData)
	{
		final DisciplineItemGroup group = new DisciplineItemGroup(aName);
		for (final String discipline : aData)
		{
			group.addItem(DisciplineItem.create(discipline));
		}
		group.initParents();
		return group;
	}
}
