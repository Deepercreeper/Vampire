package com.deepercreeper.vampireapp.util.view.listeners;

import com.deepercreeper.vampireapp.items.interfaces.Item;

/**
 * A listener for item choosing actions.
 * 
 * @author Vincent
 */
@Deprecated
public interface ItemChooseListener
{
	/**
	 * The given item was chosen.
	 * 
	 * @param aItem
	 *            The item.
	 */
	public void chose(Item aItem);
}
