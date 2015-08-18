package com.deepercreeper.vampireapp.items.interfaces.creations;

import java.util.List;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionableCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependableInstance;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;

/**
 * Each item can be instantiated. That creates an item value.<br>
 * Item values have a specified value that can be changed.
 * 
 * @author Vincent
 *         The parent item type.
 */
public interface ItemCreation extends Comparable<ItemCreation>, RestrictionableCreation, AnimatorUpdateListener, DependableInstance
{
	/**
	 * Asks the user to choose a child item that is going to be added to this item.
	 */
	public void addChild();
	
	/**
	 * Adds a creation of the give item as child to this item.
	 * 
	 * @param aItem
	 *            The item type.
	 */
	public void addChild(Item aItem);
	
	/**
	 * @return whether the user can decrease this item.
	 */
	public boolean canDecrease();
	
	/**
	 * @return whether the user can increase this item.
	 */
	public boolean canIncrease();
	
	/**
	 * Clears all children and properties of this item.
	 */
	public void clear();
	
	/**
	 * Decreases this item if possible.
	 */
	public void decrease();
	
	/**
	 * Asks the user to replace the given item with any other.
	 * 
	 * @param aItem
	 *            The item to replace.
	 */
	public void editChild(Item aItem);
	
	/**
	 * @return the absolute value of this item.
	 */
	public int getAbsoluteValue();
	
	/**
	 * @return all temporary points that were spent into this and its child items.
	 */
	public int getAllTempPoints();
	
	/**
	 * @return all values that were spent into this and its child items.
	 */
	public int getAllValues();
	
	/**
	 * @return the parent character.
	 */
	public CharacterCreation getCharacter();
	
	/**
	 * @param aIndex
	 *            The child index.
	 * @return the child item at the given index.
	 */
	public ItemCreation getChildAt(int aIndex);
	
	/**
	 * @return a list of all child items.
	 */
	public List<ItemCreation> getChildrenList();
	
	/**
	 * @return the context of this value.
	 */
	public Context getContext();
	
	/**
	 * @return the next value this item will have if it's decreased.
	 */
	public int getDecreasedValue();
	
	/**
	 * @return the user defined description for this value.
	 */
	public String getDescription();
	
	/**
	 * @return a list of all description items created by this item.
	 */
	public List<ItemCreation> getDescriptionItems();
	
	/**
	 * @return the number of free points needed to increase this item.
	 */
	public int getFreePointsCost();
	
	/**
	 * @return the next value this item will have if it's increased.
	 */
	public int getIncreasedValue();
	
	/**
	 * @return the item that defines the type of this value.
	 */
	public Item getItem();
	
	/**
	 * @return The parent item group.
	 */
	public ItemGroupCreation getItemGroup();
	
	/**
	 * @return the item name.
	 */
	public String getName();
	
	/**
	 * @return the parent item if existing.
	 */
	public ItemCreation getParentItem();
	
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
	
	/**
	 * @return the current value id.
	 */
	public int getValueId();
	
	/**
	 * @param aItem
	 *            The item type.
	 * @return whether this item has a child with the given item type.
	 */
	public boolean hasChild(Item aItem);
	
	/**
	 * @param aIndex
	 *            The item index.
	 * @return whether this item has a child at the given index.
	 */
	public boolean hasChildAt(int aIndex);
	
	/**
	 * @return whether this item has children.
	 */
	public boolean hasChildren();
	
	/**
	 * @return whether the user has enough free points to increase this item.
	 */
	public boolean hasEnoughPoints();
	
	/**
	 * @return whether the children of this item have a specific order.
	 */
	public boolean hasOrder();
	
	/**
	 * @return whether this item has a parent item.
	 */
	public boolean hasParentItem();
	
	/**
	 * Increases this item if possible.
	 */
	public void increase();
	
	/**
	 * @param aItem
	 *            The child item.
	 * @return the index of the given child.
	 */
	public int indexOfChild(ItemCreation aItem);
	
	/**
	 * @return whether this item will be added to the character even if it has no value.
	 */
	public boolean isImportant();
	
	/**
	 * @return whether the children of this item are mutable.
	 */
	public boolean isMutableParent();
	
	/**
	 * @return whether this item is a parent item.
	 */
	public boolean isParent();
	
	/**
	 * @return whether this item is a value item.
	 */
	public boolean isValueItem();
	
	/**
	 * @return whether this item needs a description.
	 */
	public boolean needsDescription();
	
	/**
	 * Removes the given child from this item.
	 * 
	 * @param aItem
	 *            The child item.
	 */
	public void removeChild(Item aItem);
	
	/**
	 * Removes all temporary points.
	 */
	public void resetTempPoints();
	
	/**
	 * Replaces the child at the given index with the given item.
	 * 
	 * @param aIndex
	 *            The child index.
	 * @param aItem
	 *            The new item.
	 */
	public void setChildAt(int aIndex, Item aItem);
	
	/**
	 * Updates whether this item can be decreased.
	 */
	public void setDecreasable();
	
	/**
	 * Sets the user defined description for this value.
	 * 
	 * @param aDescription
	 */
	public void setDescription(String aDescription);
	
	/**
	 * Updates whether this item can be increased.
	 */
	public void setIncreasable();
	
	/**
	 * Updates all item controllers.
	 */
	public void updateControllerUI();
}
