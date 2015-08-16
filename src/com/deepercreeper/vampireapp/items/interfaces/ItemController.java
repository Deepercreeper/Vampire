package com.deepercreeper.vampireapp.items.interfaces;

import java.util.List;

/**
 * Each controller handles one or more item group.
 * 
 * @author Vincent
 */
public interface ItemController extends Nameable, Dependable
{
	/**
	 * Adds the given groups.
	 * 
	 * @param aGroups
	 *            A list of groups.
	 */
	public void addGroups(final List<ItemGroup> aGroups);
	
	/**
	 * @param aName
	 *            The group name.
	 * @return the group with the given name.
	 */
	public ItemGroup getGroup(String aName);
	
	/**
	 * @return a list of all groups.
	 */
	public List<ItemGroup> getGroupsList();
	
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
	
	/**
	 * @return the maximum values that are possible to be used inside this group option.
	 */
	public int[] getMaxValues();
	
	/**
	 * @param aGroup
	 *            The group.
	 * @return whether this group option contains the given group.
	 */
	public boolean hasGroup(ItemGroup aGroup);
	
	/**
	 * @param aName
	 *            The group name.
	 * @return whether this group option contains a group with the given name.
	 */
	public boolean hasGroup(String aName);
	
	/**
	 * @param aName
	 *            The item name.
	 * @return whether this controller has an item with the given name.
	 */
	public boolean hasItem(String aName);
}
