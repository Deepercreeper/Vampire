package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations;

import java.util.List;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemControllerCreation.PointHandler;
import com.deepercreeper.vampireapp.controllers.restrictions.Restrictionable;
import com.deepercreeper.vampireapp.creation.CreationMode;

public interface ItemGroupCreation extends Comparable<ItemGroupCreation>, Restrictionable
{
	public void addItem();
	
	public void addItem(Item aItem);
	
	public boolean canChangeBy(int aValue);
	
	public void clear();
	
	public void editItem(Item aItem);
	
	public LinearLayout getContainer();
	
	public void init();
	
	public int indexOfItem(ItemCreation aItem);
	
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
	
	public int getItemValue(String aName);
	
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
	
	public boolean hasItem(Item aItem);
	
	public boolean hasItem(ItemCreation aItem);
	
	/**
	 * @param aName
	 *            The value name.
	 * @return whether this group contains a value with the given name.
	 */
	public boolean hasItem(String aName);
	
	public boolean isMutable();
	
	public boolean isValueGroup();
	
	/**
	 * Removes all widgets from their parent containers.
	 */
	public void release();
	
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
	
	public void setItemAt(int aIndex, Item aItem);
	
	/**
	 * Sets the current points handler.
	 * 
	 * @param aPoints
	 *            The new points handler.
	 */
	public void setPoints(PointHandler aPoints);
	
	/**
	 * Updates all values and whether they can be increased and decreased.
	 * 
	 * @param aCanIncrease
	 *            Whether values can be increased.
	 * @param aCanDecrease
	 *            Whether values can be decreased.
	 */
	public void updateItems();
	
	public void updateController();
}
