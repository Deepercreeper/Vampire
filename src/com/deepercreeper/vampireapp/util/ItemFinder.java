package com.deepercreeper.vampireapp.util;

import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;

/**
 * Used to search for item instances by their names.
 * 
 * @author vrl
 */
public interface ItemFinder
{
	/**
	 * @param aName
	 *            The item name.
	 * @return the item instance with the given name.
	 */
	public ItemInstance findItem(String aName);
}
