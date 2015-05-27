package com.deepercreeper.vampireapp.items.interfaces.creations;

import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.character.creation.CreationMode;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation.PointHandler;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestrictionable;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * Used to create item groups.
 * 
 * @author vrl
 */
public interface ItemGroupCreation extends Comparable<ItemGroupCreation>, CreationRestrictionable, Viewable
{
	/**
	 * Asks the user to choose an item that should be added to this group.
	 */
	public void addItem();
	
	/**
	 * Adds an item creation with the given item type.
	 * 
	 * @param aItem
	 *            The item type.
	 */
	public void addItem(Item aItem);
	
	/**
	 * @param aValue
	 *            The value.
	 * @return whether this groups value can be changed by the given value.
	 */
	public boolean canChangeBy(int aValue);
	
	/**
	 * @return whether the child items of this group have a specific order.
	 */
	public boolean hasOrder();
	
	/**
	 * Clears all child items and properties of this group.
	 */
	public void clear();
	
	/**
	 * Replaces the given child item with an user chosen one.
	 * 
	 * @param aItem
	 */
	public void editItem(Item aItem);
	
	/**
	 * @return the context of this value group.
	 */
	public Context getContext();
	
	/**
	 * @return whether this group is in creation mode.
	 */
	public CreationMode getCreationMode();
	
	/**
	 * @return a list of all values, that need a description and have more than 0 as value.
	 */
	public List<ItemCreation> getDescriptionItems();
	
	/**
	 * @param aItem
	 *            The item type.
	 * @return the child item with the given item type.
	 */
	public ItemCreation getItem(Item aItem);
	
	/**
	 * @param aName
	 *            The name of the value item.
	 * @return the value with the given name.
	 */
	public ItemCreation getItem(String aName);
	
	/**
	 * @return the parent controller of this group.
	 */
	public ItemControllerCreation getItemController();
	
	/**
	 * @return the item group of this value group.
	 */
	public ItemGroup getItemGroup();
	
	/**
	 * @return a list of all value items.
	 */
	public List<ItemCreation> getItemsList();
	
	/**
	 * @param aName
	 *            The item name.
	 * @return the value of the given child item name.
	 */
	public int getItemValue(String aName);
	
	/**
	 * @return the group name.
	 */
	public String getName();
	
	/**
	 * @return the point handler of this value group.
	 */
	public PointHandler getPoints();
	
	/**
	 * @return the sum of all temporary points inside this value group.
	 */
	public int getTempPoints();
	
	/**
	 * @return the sum of all values inside this value group.
	 */
	public int getValue();
	
	/**
	 * @param aItem
	 *            The item type.
	 * @return whether this group contains an item with the given item type.
	 */
	public boolean hasItem(Item aItem);
	
	/**
	 * @param aItem
	 *            The item.
	 * @return whether this group contains the given item.
	 */
	public boolean hasItem(ItemCreation aItem);
	
	/**
	 * @param aName
	 *            The value name.
	 * @return whether this group contains a value with the given name.
	 */
	public boolean hasItem(String aName);
	
	/**
	 * @param aItem
	 *            The item child.
	 * @return the index of the given item child.
	 */
	public int indexOfItem(ItemCreation aItem);
	
	/**
	 * @return whether this group is mutable.
	 */
	public boolean isMutable();
	
	/**
	 * @return whether this is a value group.
	 */
	public boolean isValueGroup();
	
	/**
	 * Removes the item with the given item type from this group.
	 * 
	 * @param aItem
	 *            The item type.
	 */
	public void removeItem(Item aItem);
	
	/**
	 * Removes all temporary points from all values.
	 */
	public void resetTempPoints();
	
	/**
	 * Sets whether this group is in creation mode.
	 * 
	 * @param aMode
	 *            Whether this group represents the values inside a character creation.
	 */
	public void setCreationMode(CreationMode aMode);
	
	/**
	 * Replaces the item child at the given index with the given item.
	 * 
	 * @param aIndex
	 *            The item index.
	 * @param aItem
	 *            The new item.
	 */
	public void setItemAt(int aIndex, Item aItem);
	
	/**
	 * Sets the current points handler.
	 * 
	 * @param aPoints
	 *            The new points handler.
	 */
	public void setPoints(PointHandler aPoints);
	
	/**
	 * Updates whether the add child button is enabled.
	 */
	public void updateAddButton();
	
	/**
	 * Updates the item controller.
	 */
	public void updateController();
	
	/**
	 * Updates all values and whether they can be increased and decreased.
	 */
	public void updateItems();
}
