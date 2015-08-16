package com.deepercreeper.vampireapp.character.inventory;

import com.deepercreeper.vampireapp.character.instance.InventoryControllerInstance;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;

/**
 * Each inventory item has to have at least these methods to be stored and handled inside an inventory controller instnace.
 * 
 * @author vrl
 */
public interface InventoryItem extends Saveable, Viewable
{
	/**
	 * Sets the parent inventory controller.
	 * 
	 * @param aController
	 *            The new parent inventory controller.
	 */
	public void addTo(InventoryControllerInstance aController);
	
	/**
	 * Removes one of this item stack.<br>
	 * If there is no one left, this item is removed from the controller.
	 */
	public void decrease();
	
	/**
	 * @return the underlying context.
	 */
	public Context getContext();
	
	/**
	 * @return The parent inventory controller.
	 */
	public InventoryControllerInstance getController();
	
	/**
	 * @return a summary of this item.
	 */
	public String getInfo();
	
	/**
	 * @param aQuantity
	 *            Whether the quantity of this item should be displayed.
	 * @return an array of strings, that are used to display this item inside a message.
	 */
	public String[] getInfoArray(boolean aQuantity);
	
	/**
	 * @param aQuantity
	 *            Whether the quantity should be displayed.
	 * @return a boolean array that determines which info array entries should be translated.
	 */
	public boolean[] getInfoTranslatedArray(boolean aQuantity);
	
	/**
	 * @return the item name.
	 */
	public String getName();
	
	/**
	 * @return how many items this stack contains.
	 */
	public int getQuantity();
	
	/**
	 * @return the inventory item string identifier.
	 */
	public String getType();
	
	/**
	 * @return the item weight.
	 */
	public int getWeight();
	
	/**
	 * Increases the amount of this items by the given amount.
	 * 
	 * @param aAmount
	 *            The amount.
	 */
	public void increase(final int aAmount);
}
