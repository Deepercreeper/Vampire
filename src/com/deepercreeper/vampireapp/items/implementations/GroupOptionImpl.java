package com.deepercreeper.vampireapp.items.implementations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.util.LanguageUtil;

public class GroupOptionImpl implements GroupOption
{
	private static final String				TAG		= "GroupOption";
	
	private final Map<String, ItemGroup>	mGroups	= new HashMap<String, ItemGroup>();
	
	private final String					mName;
	
	private final int[]						mMaxValues;
	
	private final boolean					mValueGroupOption;
	
	public GroupOptionImpl(final String aName, final int[] aMaxValues)
	{
		mName = aName;
		mValueGroupOption = aMaxValues != null;
		mMaxValues = aMaxValues;
	}
	
	@Override
	public void addGroup(final ItemGroup aGroup)
	{
		mGroups.put(aGroup.getName(), aGroup);
	}
	
	@Override
	public int compareTo(final GroupOption aAnother)
	{
		if (aAnother == null)
		{
			return getName().compareTo(null);
		}
		return getName().compareTo(aAnother.getName());
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof GroupOption)
		{
			final GroupOption groupOption = (GroupOption) aO;
			return getName().equals(groupOption.getName());
		}
		return false;
	}
	
	@Override
	public ItemGroup getGroup(final String aName)
	{
		return mGroups.get(aName);
	}
	
	@Override
	public Collection<ItemGroup> getGroups()
	{
		return mGroups.values();
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
	public String getName()
	{
		return mName;
	}
	
	@Override
	public String getDisplayName()
	{
		return LanguageUtil.instance().getValue(getName());
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
	public boolean isValueGroupOption()
	{
		return mValueGroupOption;
	}
	
	@Override
	public String toString()
	{
		return getDisplayName() + ": " + getGroups().toString();
	}
}
