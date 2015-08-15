package com.deepercreeper.vampireapp.items.interfaces.creations;

import java.util.List;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionableCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependableInstance;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;

/**
 * This controller controls several groups and a few group options.
 * 
 * @author vrl
 */
public interface ItemControllerCreation extends RestrictionableCreation, Viewable, DependableInstance
{
	/**
	 * Adds a name shortcut for the given item.
	 * 
	 * @param aItem
	 *            The item.
	 */
	public void addItem(ItemCreation aItem);
	
	/**
	 * @param aGroup
	 *            The group.
	 * @param aValue
	 *            The value.
	 * @return whether the value of the given group can be changed by the given value.
	 */
	public boolean canChangeGroupBy(final ItemGroupCreation aGroup, int aValue);
	
	/**
	 * Clears the whole controller.
	 */
	public void clear();
	
	/**
	 * Closes the widget container. Maybe for refilling with new values.
	 */
	public void close();
	
	/**
	 * @return the context of this value controller.
	 */
	public Context getContext();
	
	/**
	 * @return whether this controller has currently maximum values.
	 */
	public boolean hasMaxValues();
	
	/**
	 * @return a list of all values, that need a description and have more than 0 as value.
	 */
	public List<ItemCreation> getDescriptionValues();
	
	/**
	 * @param aGroup
	 *            The group type.
	 * @return the group with the given group type.
	 */
	public ItemGroupCreation getGroup(ItemGroup aGroup);
	
	/**
	 * @param aName
	 *            The group name.
	 * @return the group with the given name.
	 */
	public ItemGroupCreation getGroup(String aName);
	
	/**
	 * @return a list of all groups.
	 */
	public List<ItemGroupCreation> getGroupsList();
	
	/**
	 * @param aName
	 *            The item name.
	 * @return The item with the given name.
	 */
	public ItemCreation getItem(final String aName);
	
	/**
	 * @return the controller.
	 */
	public ItemController getItemController();
	
	/**
	 * @return the controller name.
	 */
	public String getName();
	
	/**
	 * @return the number of temporary points spent inside this controller.
	 */
	public int getTempPoints();
	
	/**
	 * @return all values of this controller.
	 */
	public int getValue();
	
	/**
	 * @param aName
	 *            The group name.
	 * @return whether this controller contains a group with the given name.
	 */
	public boolean hasGroup(String aName);
	
	/**
	 * @param aName
	 * @return whether this controller contains an item with the given name.
	 */
	public boolean hasItem(String aName);
	
	/**
	 * Removes the item with the given name.
	 * 
	 * @param aName
	 *            The item name.
	 */
	public void removeItem(String aName);
	
	/**
	 * @return whether this controller has any group with items or that is mutable.
	 */
	public boolean isEmpty();
	
	/**
	 * resets all temporary points spent in this controller.
	 */
	public void resetTempPoints();
	
	/**
	 * Resizes the view of this controller to the real size.
	 */
	public void resize();
	
	/**
	 * Enables or disabled the value groups for changing anything.
	 * 
	 * @param aEnabled
	 *            Whether the groups should be enabled or not.
	 */
	public void setEnabled(boolean aEnabled);
	
	/**
	 * Updates the user interface for all views. Invoked after the update method.
	 */
	public void updateUI();
}
