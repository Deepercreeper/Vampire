package com.deepercreeper.vampireapp.util.view.listeners;

import com.deepercreeper.vampireapp.items.interfaces.Nameable;

/**
 * A listener that is invoked when a selection was made.
 * 
 * @author Vincent
 * @param <S>
 *            The type of nameable that is selected.
 */
public interface ItemSelectionListener <S extends Nameable>
{
	/**
	 * The dialog was cancelled.
	 */
	public void cancel();
	
	/**
	 * Invoked when any option was selected.
	 * 
	 * @param aItem
	 *            The selected item.
	 */
	public void select(S aItem);
}