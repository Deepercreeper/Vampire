package com.deepercreeper.vampireapp.items.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
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
	
	private final Map<String, GroupOption> mGroupOptions = new HashMap<String, GroupOption>();
	
	private final List<GroupOption> mGroupOptionList = new ArrayList<GroupOption>();
	
	/**
	 * Creates a new item controller.
	 * 
	 * @param aName
	 *            The item controller name.
	 */
	public ItemControllerImpl(final String aName)
	{
		super(aName);
	}
	
	@Override
	public void addGroup(final ItemGroup aGroup)
	{
		mGroups.put(aGroup.getName(), aGroup);
		getGroupsList().add(aGroup);
		Collections.sort(getGroupsList());
	}
	
	@Override
	public void addGroupOption(final GroupOption aGroupOption)
	{
		mGroupOptions.put(aGroupOption.getName(), aGroupOption);
		mGroupOptionList.add(aGroupOption);
		Collections.sort(mGroupOptionList);
	}
	
	@Override
	public ItemGroup getGroup(final String aName)
	{
		return mGroups.get(aName);
	}
	
	@Override
	public GroupOption getGroupOption(final String aName)
	{
		return mGroupOptions.get(aName);
	}
	
	@Override
	public List<GroupOption> getGroupOptionsList()
	{
		return mGroupOptionList;
	}
	
	@Override
	public List<ItemGroup> getGroupsList()
	{
		return mGroupList;
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
		List<Item> items = new ArrayList<Item>();
		for (ItemGroup group : getGroupsList())
		{
			items.addAll(group.getItemsList());
		}
		return items;
	}
	
	@Override
	public String toString()
	{
		return getDisplayName() + ": " + getGroupOptionsList().toString();
	}
}
