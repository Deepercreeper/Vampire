package com.deepercreeper.vampireapp.util.interfaces;

import java.util.List;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
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
	public ItemInstance findItemInstance(String aName);
	
	/**
	 * @return a list of all items.
	 */
	public List<Item> getItemsList();
	
	/**
	 * @param aName
	 *            The item name.
	 * @return the item with the given name.
	 */
	public Item findItem(String aName);
	
	/**
	 * @param aName
	 *            The group name.
	 * @return the group instance with the given name.
	 */
	public ItemGroupInstance findGroupInstance(String aName);
}
