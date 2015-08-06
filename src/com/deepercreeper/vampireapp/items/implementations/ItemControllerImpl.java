package com.deepercreeper.vampireapp.items.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;

/**
 * A item controller implementation.
 * 
 * @author vrl
 */
public class ItemControllerImpl extends Named implements ItemController
{
	private final Map<String, ItemGroup> mGroups = new HashMap<String, ItemGroup>();
	
	private final List<ItemGroup> mGroupList = new ArrayList<ItemGroup>();
	
	private final int[] mMaxValues;
	
	/**
	 * Creates a new item controller.
	 * 
	 * @param aName
	 *            The item controller name.
	 * @param aMaxValues
	 *            The maximum group values or {@code null} if no restrictions given.
	 */
	public ItemControllerImpl(final String aName, final int[] aMaxValues)
	{
		super(aName);
		mMaxValues = aMaxValues;
	}
	
	@Override
	public void addGroups(final List<ItemGroup> aGroups)
	{
		for (final ItemGroup group : aGroups)
		{
			mGroups.put(group.getName(), group);
		}
		getGroupsList().addAll(aGroups);
		Collections.sort(getGroupsList());
	}
	
	@Override
	public ItemGroup getGroup(final String aName)
	{
		return mGroups.get(aName);
	}
	
	@Override
	public List<ItemGroup> getGroupsList()
	{
		return mGroupList;
	}
	
	@Override
	public int[] getMaxValues()
	{
		return mMaxValues;
	}
	
	@Override
	public boolean hasMaxValues()
	{
		return mMaxValues != null;
	}
	
	@Override
	public boolean hasGroup(final ItemGroup aGroup)
	{
		return hasGroup(aGroup.getName());
	}
	
	@Override
	public boolean hasGroup(final String aName)
	{
		return mGroups.containsKey(aName);
	}
	
	@Override
	public boolean hasItem(final String aName)
	{
		for (final ItemGroup group : getGroupsList())
		{
			if (group.hasItem(aName))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Item getItem(final String aName)
	{
		for (final ItemGroup group : getGroupsList())
		{
			if (group.hasItem(aName))
			{
				return group.getItem(aName);
			}
		}
		return null;
	}
	
	@Override
	public List<Item> getItemsList()
	{
		final List<Item> items = new ArrayList<Item>();
		for (final ItemGroup group : getGroupsList())
		{
			items.addAll(group.getItemsList());
		}
		return items;
	}
	
	@Override
	public String toString()
	{
		return getDisplayName() + ": " + getGroupsList().toString();
	}
}
