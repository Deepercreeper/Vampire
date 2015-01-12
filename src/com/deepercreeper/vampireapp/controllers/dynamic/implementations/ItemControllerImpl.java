package com.deepercreeper.vampireapp.controllers.dynamic.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.GroupOption;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemController;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;

public class ItemControllerImpl implements ItemController
{
	private static final String				TAG					= "ItemController";
	
	private final Map<String, ItemGroup>	mGroups				= new HashMap<String, ItemGroup>();
	
	private final List<ItemGroup>			mGroupList			= new ArrayList<ItemGroup>();
	
	private final Map<String, GroupOption>	mGroupOptions		= new HashMap<String, GroupOption>();
	
	private final List<GroupOption>			mGroupOptionList	= new ArrayList<GroupOption>();
	
	private final String					mName;
	
	public ItemControllerImpl(final String aName)
	{
		mName = aName;
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
	public boolean equals(final Object aO)
	{
		if (aO instanceof ItemController)
		{
			final ItemController controller = (ItemController) aO;
			return getName().equals(controller.getName());
		}
		return false;
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
	public String getName()
	{
		return mName;
	}
	
	@Override
	public String toString()
	{
		return getName() + ": " + getGroupOptionsList().toString();
	}
}
