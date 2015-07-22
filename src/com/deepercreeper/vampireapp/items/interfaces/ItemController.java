package com.deepercreeper.vampireapp.items.interfaces;

import java.util.List;

/**
 * Each controller handles one or more item group.
 * 
 * @author Vincent
 */
public interface ItemController extends Nameable
{
	/**
	 * Adds the given group.
	 * 
	 * @param aGroup
	 *            The group.
	 */
	public void addGroup(ItemGroup aGroup);
	
	/**
	 * Adds the given group option.
	 * 
	 * @param aGroupOption
	 *            The group option.
	 */
	public void addGroupOption(GroupOption aGroupOption);
	
	/**
	 * @param aName
	 *            The group name.
	 * @return the group with the given name.
	 */
	public ItemGroup getGroup(String aName);
	
	/**
	 * @param aName
	 *            The group option name.
	 * @return the group option with the given name.
	 */
	public GroupOption getGroupOption(String aName);
	
	/**
	 * @return a list of all group options.
	 */
	public List<GroupOption> getGroupOptionsList();
	
	/**
	 * @return a list of all groups.
	 */
	public List<ItemGroup> getGroupsList();
	
	/**
	 * @param aName
	 *            The item name.
	 * @return whether this controller has an item with the given name.
	 */
	public boolean hasItem(String aName);
	
	/**
	 * @param aName
	 *            The item name.
	 * @return the item with the given name.
	 */
	public Item getItem(String aName);
	
	/**
	 * @return a list of all items inside this controller.
	 */
	public List<Item> getItemsList();
}
