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
	 * Invoked when the user hit the back button or has touched beside the dialog.
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