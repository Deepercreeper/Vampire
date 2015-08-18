package com.deepercreeper.vampireapp.util.view.listeners;

import com.deepercreeper.vampireapp.character.inventory.Artifact;

/**
 * The inventory item created listener.
 * 
 * @author Vincent
 */
public interface InventoryItemCreationListener
{
	/**
	 * Invoked, when an inventory item was created.
	 * 
	 * @param aItem
	 *            The newly created item.
	 */
	public void itemCreated(Artifact aItem);
}
