package com.deepercreeper.vampireapp.controllers.dynamic.interfaces;

import java.util.Collection;

public interface GroupOption extends Comparable<GroupOption>
{
	public void addGroup(ItemGroup aGroup);
	
	public ItemGroup getGroup(String aName);
	
	public Collection<ItemGroup> getGroups();
	
	public int[] getMaxValues();
	
	public boolean hasMaxValues();
	
	public String getName();
	
	public boolean hasGroup(ItemGroup aGroup);
	
	public boolean hasGroup(String aName);
	
	public boolean isValueGroupOption();
}
