package com.deepercreeper.vampireapp.lists;

import java.util.List;
import com.deepercreeper.vampireapp.items.interfaces.Nameable;

/**
 * A dynamic list controller that handles a list of items with type {@code T}.
 * 
 * @author vrl
 * @param <T>
 *            The item type.
 */
public interface ListController <T extends Nameable>
{
	/**
	 * @param aValue
	 *            The item.
	 * @return the display name index of the given item.
	 */
	public int displayIndexOf(T aValue);
	
	/**
	 * @return a list of all display names.
	 */
	public List<String> getDisplayNames();
	
	/**
	 * @return the first item.
	 */
	public T getFirst();
	
	/**
	 * @param aPos
	 *            The display name position.
	 * @return the item at the given display name position.
	 */
	public T getItemAtDisplayNamePosition(final int aPos);
	
	/**
	 * @param aPos
	 *            The item position.
	 * @return the item at the given position.
	 */
	public T getItemAtPosition(int aPos);
	
	/**
	 * @param aName
	 *            the display name of the item.
	 * @return the name of the item with the given display name.
	 */
	public T getItemWithDisplayName(String aName);
	
	/**
	 * @param aName
	 *            The item name.
	 * @return the item with the given name.
	 */
	public T getItemWithName(String aName);
	
	/**
	 * @return a list of all item names.
	 */
	public List<String> getNames();
	
	/**
	 * @return a list of all items.
	 */
	public List<T> getValuesList();
	
	/**
	 * @param aValue
	 *            The item.
	 * @return the index of the given item.
	 */
	public int indexOf(T aValue);
	
	/**
	 * Initializes the list with all items inside the given list.
	 * 
	 * @param aValues
	 *            The list of all items.
	 */
	public void init(List<T> aValues);
}
