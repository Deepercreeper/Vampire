package com.deepercreeper.vampireapp.items.interfaces.creations;

import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.character.creation.CreationMode;
import com.deepercreeper.vampireapp.items.interfaces.GroupOption;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestrictionable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;

/**
 * This controller controls several groups and a few group options.
 * 
 * @author vrl
 */
public interface ItemControllerCreation extends CreationRestrictionable, Viewable
{
	/**
	 * Used to call the number of points from the parent.
	 * 
	 * @author Vincent
	 */
	public static interface PointHandler
	{
		/**
		 * Decreases the current available points by {@code aValue} points.
		 * 
		 * @param aValue
		 *            The points to add.
		 */
		public void decrease(int aValue);
		
		/**
		 * @return the current number of free or experience points from the parent.
		 */
		public int getPoints();
		
		/**
		 * Increases the current available points by {@code aValue} points.
		 * 
		 * @param aValue
		 *            The points to subtract.
		 */
		public void increase(int aValue);
	}
	
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
	 * @return whether this controller is in the creation mode.
	 */
	public CreationMode getCreationMode();
	
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
	 * @param aGroupOption
	 *            The group option type.
	 * @return the group option with the given group option type.
	 */
	public GroupOptionCreation getGroupOption(GroupOption aGroupOption);
	
	/**
	 * @param aName
	 *            The group option name.
	 * @return the group option with the given name.
	 */
	public GroupOptionCreation getGroupOption(String aName);
	
	/**
	 * @return a list of all group options.
	 */
	public List<GroupOptionCreation> getGroupOptionsList();
	
	/**
	 * @return a list of all groups.
	 */
	public List<ItemGroupCreation> getGroupsList();
	
	/**
	 * @param aName
	 *            The group name.
	 * @return the value of he group with the given name.
	 */
	public int getGroupValue(String aName);
	
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
	 * @param aName
	 *            The item name.
	 * @return the value of the item with the given name.
	 */
	public int getItemValue(String aName);
	
	/**
	 * @return the controller name.
	 */
	public String getName();
	
	/**
	 * @return the points handler.
	 */
	public PointHandler getPoints();
	
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
	 * resets all temporary points spent in this controller.
	 */
	public void resetTempPoints();
	
	/**
	 * Resizes the view of this controller to the real size.
	 */
	public void resize();
	
	/**
	 * Sets whether this controller is in the creation mode.
	 * 
	 * @param aMode
	 *            Whether creation mode or not.
	 */
	public void setCreationMode(CreationMode aMode);
	
	/**
	 * Enables or disabled the value groups for changing anything.
	 * 
	 * @param aEnabled
	 *            Whether the groups should be enabled or not.
	 */
	public void setEnabled(boolean aEnabled);
	
	/**
	 * Sets the points handler.
	 * 
	 * @param aPoints
	 *            The new points handler.
	 */
	public void setPoints(PointHandler aPoints);
	
	/**
	 * Updates all value groups.
	 */
	public void updateGroups();
}
