package com.deepercreeper.vampireapp.items.interfaces;

import java.util.Collection;

/**
 * Each item controller contains several group options. Each group option contains one or more groups.
 * 
 * @author vrl
 */
public interface GroupOption extends Nameable
{
	/**
	 * Adds the given group.
	 * 
	 * @param aGroup
	 *            The group to add.
	 */
	public void addGroup(ItemGroup aGroup);
	
	/**
	 * @param aName
	 *            The group name.
	 * @return the group with the given name.
	 */
	public ItemGroup getGroup(String aName);
	
	/**
	 * @return a collection of all groups.
	 */
	public Collection<ItemGroup> getGroups();
	
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
	 * @return whether this group option defines maximum values for its groups.
	 */
	public boolean hasMaxValues();
	
	/**
	 * @return whether this is a value group option.
	 */
	public boolean isValueGroupOption();
}
