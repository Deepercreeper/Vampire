package com.deepercreeper.vampireapp.items;

/**
 * Default item providers have to generate, load or connect the item bunch and<br>
 * afterwards send it to the request giving activity. To make sure, that will happen,<br>
 * it has to implement this interface.
 * 
 * @author vrl
 */
public interface ItemConsumer
{
	/**
	 * Called when the item provider has done the item request and delivers them.
	 * 
	 * @param aItems
	 *            The consumable item bunch.
	 */
	public void consumeItems(ItemProvider aItems);
}
