package com.deepercreeper.vampireapp.controller.interfaces;

/**
 * All value groups that provide adding and deleting values should implement this interface.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 * @param <S>
 *            The item value type.
 */
public interface VariableValueGroup <T extends Item, S extends ItemValue<T>>
{
	/**
	 * Adds a value of the given item type to this group.
	 * 
	 * @param aItem
	 *            The item type.
	 */
	public void addItem(T aItem);
	
	/**
	 * Removes all values and resets the widgets.
	 */
	public void clear();
	
	/**
	 * Resizes the widget container of this group.
	 */
	public void resize();
}
