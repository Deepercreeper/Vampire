package com.deepercreeper.vampireapp.items.interfaces;

import java.util.List;

/**
 * Each controller handles one or more item group.
 * 
 * @author Vincent
 */
public interface ItemController extends Nameable
{
	public void addGroup(ItemGroup aGroup);
	
	public void addGroupOption(GroupOption aGroupOption);
	
	public ItemGroup getGroup(String aName);
	
	public GroupOption getGroupOption(String aName);
	
	public List<GroupOption> getGroupOptionsList();
	
	public List<ItemGroup> getGroupsList();
	
	public Item getItem(String aName);
}
