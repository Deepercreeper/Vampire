package com.deepercreeper.vampireapp.items.interfaces.instances;

import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.EPControllerInstance;
import com.deepercreeper.vampireapp.character.instance.Mode;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * A group that contains several items.
 * 
 * @author vrl
 */
public interface ItemGroupInstance extends Comparable<ItemGroupInstance>, Saveable, Viewable
{
	/**
	 * @return the underlying context.
	 */
	public Context getContext();
	
	/**
	 * @return a list of all items that have a description.
	 */
	public List<ItemInstance> getDescriptionItems();
	
	/**
	 * @return whether the child items of this group have a specific order.
	 */
	public boolean hasOrder();
	
	/**
	 * @return the experience controller.
	 */
	public EPControllerInstance getEP();
	
	/**
	 * @param aItem
	 *            The item type.
	 * @return the item with the given item type.
	 */
	public ItemInstance getItem(Item aItem);
	
	/**
	 * @param aName
	 *            The item name.
	 * @return the item with the given name.
	 */
	public ItemInstance getItem(String aName);
	
	/**
	 * @return The item controller.
	 */
	public ItemControllerInstance getItemController();
	
	/**
	 * @return the item group type.
	 */
	public ItemGroup getItemGroup();
	
	/**
	 * @return a list of all child items.
	 */
	public List<ItemInstance> getItemsList();
	
	/**
	 * @return the character.
	 */
	public CharacterInstance getCharacter();
	
	/**
	 * @return the character mode.
	 */
	public Mode getMode();
	
	/**
	 * @return the group name.
	 */
	public String getName();
	
	/**
	 * @return a sum of all item values inside this group.
	 */
	public int getValue();
	
	/**
	 * @param aItem
	 *            the item type.
	 * @return whether this group contains an item with the given item type.
	 */
	public boolean hasItem(Item aItem);
	
	/**
	 * @param aItem
	 *            The item.
	 * @return whether this group contains the given item.
	 */
	public boolean hasItem(ItemInstance aItem);
	
	/**
	 * @param aName
	 *            The item name.
	 * @return whether this group contains a item with the given name.
	 */
	public boolean hasItem(String aName);
	
	/**
	 * @param aItem
	 *            The item.
	 * @return the index of the given item inside this group.
	 */
	public int indexOfItem(ItemInstance aItem);
	
	/**
	 * @return whether this is a value group.
	 */
	public boolean isValueGroup();
	
	/**
	 * Sets the character mode.
	 * 
	 * @param aMode
	 *            The new mode.
	 */
	public void setMode(Mode aMode);
	
	/**
	 * Updates the controller.
	 */
	public void updateController();
	
	/**
	 * Updates all items.
	 */
	public void updateItems();
}
