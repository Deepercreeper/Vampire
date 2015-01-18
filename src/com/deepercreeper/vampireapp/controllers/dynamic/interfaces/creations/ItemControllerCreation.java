package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations;

import java.util.List;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.CreationMode;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.GroupOption;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemController;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.restrictions.CreationRestrictionable;

public interface ItemControllerCreation extends CreationRestrictionable
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
	
	public boolean canChangeGroupBy(final ItemGroupCreation aGroup, int aValue);
	
	public void clear();
	
	/**
	 * Closes the widget container. Maybe for refilling with new values.
	 */
	public void close();
	
	public LinearLayout getContainer();
	
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
	
	public boolean hasGroup(String aName);
	
	public ItemGroupCreation getGroup(ItemGroup aGroup);
	
	public ItemGroupCreation getGroup(String aName);
	
	public GroupOptionCreation getGroupOption(GroupOption aGroupOption);
	
	public GroupOptionCreation getGroupOption(String aName);
	
	public List<GroupOptionCreation> getGroupOptionsList();
	
	public List<ItemGroupCreation> getGroupsList();
	
	public int getGroupValue(String aName);
	
	public void addItemName(ItemCreation aItem);
	
	public void removeItemName(String aName);
	
	public ItemCreation getItem(final String aName);
	
	public boolean hasItem(String aName);
	
	/**
	 * @return the controller.
	 */
	public ItemController getItemController();
	
	public int getItemValue(String aName);
	
	public String getName();
	
	public PointHandler getPoints();
	
	public void init();
	
	public int getTempPoints();
	
	public int getValue();
	
	/**
	 * Removes all widgets from their parent container.
	 */
	public void release();
	
	public void resetTempPoints();
	
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
	
	public void setPoints(PointHandler aPoints);
	
	/**
	 * Updates all value groups.
	 * 
	 * @param aUpdateOthers
	 *            Whether all other controllers should also be updated.
	 */
	public void updateGroups();
}
