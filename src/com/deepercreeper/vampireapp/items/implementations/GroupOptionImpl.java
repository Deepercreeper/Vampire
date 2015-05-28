package com.deepercreeper.vampireapp.items.implementations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;

/**
 * A group option implementation.
 * 
 * @author vrl
 */
public class GroupOptionImpl extends Named implements GroupOption
{
	private final Map<String, ItemGroup>	mGroups	= new HashMap<String, ItemGroup>();
	
	private final int[]						mMaxValues;
	
	private final boolean					mValueGroupOption;
	
	/**
	 * Creates a new group option.
	 * 
	 * @param aName
	 *            The group option name.
	 * @param aMaxValues
	 *            The maximum group values.
	 */
	public GroupOptionImpl(final String aName, final int[] aMaxValues)
	{
		super(aName);
		mValueGroupOption = aMaxValues != null;
		mMaxValues = aMaxValues;
	}
	
	@Override
	public void addGroup(final ItemGroup aGroup)
	{
		mGroups.put(aGroup.getName(), aGroup);
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		// TODO Default items should inherit the equals and hashcode methods from Named
		// TODO Go through all classes and sort the members
		return super.equals(aO);
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
	public boolean hasMaxValues()
	{
		return mMaxValues != null;
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
