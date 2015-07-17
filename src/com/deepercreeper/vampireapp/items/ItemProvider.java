package com.deepercreeper.vampireapp.items;

import java.util.List;
import com.deepercreeper.vampireapp.character.Health;
import com.deepercreeper.vampireapp.character.inventory.Inventory;
import com.deepercreeper.vampireapp.character.Currency;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.lists.controllers.ClanController;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionController;
import com.deepercreeper.vampireapp.lists.controllers.NatureController;

/**
 * The item provider is a loader, or connector, that is able to give information<br>
 * about default settings of the vampire application.
 * 
 * @author vrl
 */
public interface ItemProvider
{
	/**
	 * @return the clan controller.
	 */
	public ClanController getClans();
	
	/**
	 * @param aName
	 *            The item controller name.
	 * @return the item controller with the given name.
	 */
	public ItemController getController(String aName);
	
	/**
	 * @return a list of all item controllers.
	 */
	public List<ItemController> getControllers();
	
	/**
	 * @return the description controller.
	 */
	public DescriptionController getDescriptions();
	
	/**
	 * @return the name of the generation defining item.
	 */
	public String getGenerationItem();
	
	/**
	 * @return the health controller.
	 */
	public Health getHealth();
	
	/**
	 * @return the inventory controller.
	 */
	public Inventory getInventory();
	
	/**
	 * @return the money controller.
	 */
	public Currency getCurrency();
	
	/**
	 * @return the nature controller.
	 */
	public NatureController getNatures();
}