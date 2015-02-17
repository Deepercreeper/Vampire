package com.deepercreeper.vampireapp.items.interfaces.creations;

import java.util.List;
import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.character.creation.CreationMode;
import com.deepercreeper.vampireapp.items.implementations.creations.ItemCreationImpl.ChangeAction;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation.PointHandler;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestrictionable;

/**
 * Each item can be instantiated. That creates an item value.<br>
 * Item values have a specified value that can be changed.
 * 
 * @author Vincent
 * @param <T>
 *            The parent item type.
 */
public interface ItemCreation extends Comparable<ItemCreation>, CreationRestrictionable
{
	public void addChild();
	
	public void addChild(Item aItem);
	
	public boolean canDecrease();
	
	public boolean canIncrease();
	
	public boolean hasOrder();
	
	public void clear();
	
	public void decrease();
	
	public void editChild(Item aItem);
	
	public int getAbsoluteValue();
	
	public int getAllTempPoints();
	
	public int getAllValues();
	
	public ChangeAction getChangeTempPoints();
	
	public boolean isImportant();
	
	public ChangeAction getChangeValue();
	
	public ItemCreation getChildAt(int aIndex);
	
	public List<ItemCreation> getChildrenList();
	
	/**
	 * Each value has a specified widget that contains some buttons and text views to handle its value.
	 * 
	 * @return the value container.
	 */
	public LinearLayout getContainer();
	
	/**
	 * @return the context of this value.
	 */
	public Context getContext();
	
	/**
	 * @return whether this group is in creation mode.
	 */
	public CreationMode getCreationMode();
	
	public int getDecreasedValue();
	
	/**
	 * @return the user defined description for this value.
	 */
	public String getDescription();
	
	public List<ItemCreation> getDescriptionItems();
	
	public int getFreePointsCost();
	
	public int getIncreasedValue();
	
	/**
	 * @return the item that defines the type of this value.
	 */
	public Item getItem();
	
	public ItemGroupCreation getItemGroup();
	
	public String getName();
	
	public ItemCreation getParentItem();
	
	/**
	 * @return the current point handler of this value.
	 */
	public PointHandler getPoints();
	
	/**
	 * Free bonus points are handled as temporary points. They need to be saved separately.
	 * 
	 * @return the number of temporary points.
	 */
	public int getTempPoints();
	
	/**
	 * @return the current item value.
	 */
	public int getValue();
	
	public int getValueId();
	
	public boolean hasChild(Item aItem);
	
	public boolean hasChildAt(int aIndex);
	
	public boolean hasChildren();
	
	public boolean hasEnoughPoints();
	
	public boolean hasParentItem();
	
	public void increase();
	
	public int indexOfChild(ItemCreation aItem);
	
	public void init();
	
	public boolean isMutableParent();
	
	public boolean isParent();
	
	public boolean isValueItem();
	
	public boolean needsDescription();
	
	/**
	 * Applies the value to the value display again.
	 */
	public void refreshValue();
	
	/**
	 * Removes all widgets from their parent containers.
	 */
	public void release();
	
	public void removeChild(Item aItem);
	
	/**
	 * Removes all temporary points.
	 */
	public void resetTempPoints();
	
	public void setChildAt(int aIndex, Item aItem);
	
	/**
	 * Sets whether this group is in creation mode.
	 * 
	 * @param aMode
	 *            Whether this group represents the values inside a character creation.
	 */
	public void setCreationMode(CreationMode aMode);
	
	/**
	 * Enables or disabled the decrease button.
	 * 
	 * @param aEnabled
	 *            Whether the decrease button should be enabled.
	 */
	public void setDecreasable();
	
	/**
	 * Sets the user defined description for this value.
	 * 
	 * @param aDescription
	 */
	public void setDescription(String aDescription);
	
	/**
	 * Enables or disabled the increase button.
	 * 
	 * @param aEnabled
	 *            Whether the increase button should be enabled.
	 */
	public void setIncreasable();
	
	/**
	 * Sets the current points handler.
	 * 
	 * @param aPoints
	 *            The new points handler.
	 */
	public void setPoints(PointHandler aPoints);
	
	public void updateButtons();
	
	public void updateController();
}
