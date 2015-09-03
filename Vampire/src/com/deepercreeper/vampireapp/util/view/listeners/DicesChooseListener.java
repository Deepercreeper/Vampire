package com.deepercreeper.vampireapp.util.view.listeners;

import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;

/**
 * A listener for dice choose dialogs.
 * 
 * @author vrl
 */
public interface DicesChooseListener
{
	/**
	 * The given map of amounts for each item was selected.
	 * 
	 * @param aValues
	 *            A map that assigns a value to each item.
	 */
	public void choseDices(Map<ItemInstance, Integer> aValues);
}
