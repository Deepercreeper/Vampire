package com.deepercreeper.vampireapp.controller;

import java.util.List;

/**
 * A item group is used to control several items.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 */
public interface ItemGroup <T extends Item>
{
	/**
	 * @param aName
	 *            The item name.
	 * @return the item with the given name.
	 */
	public T getItem(String aName);
	
	/**
	 * @return a list of all items inside this group.
	 */
	public List<T> getItems();
	
	/**
	 * @return the group name.
	 */
	public String getName();
}
