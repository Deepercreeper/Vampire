package com.deepercreeper.vampireapp.items.interfaces.instances;

import java.util.List;
import java.util.Set;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.instances.dependencies.DependableInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionableInstance;
import com.deepercreeper.vampireapp.mechanics.ActionInstance;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;

/**
 * This represents a final item that a character can have.
 * 
 * @author vrl
 */
public interface ItemInstance extends RestrictionableInstance, Comparable<ItemInstance>, Saveable, AnimatorUpdateListener, DependableInstance
{
	/**
	 * If something depends on the value of any item this listener can be attached to an item.
	 * 
	 * @author vrl
	 */
	public interface ItemValueListener
	{
		/**
		 * Invoked when the item value changed.
		 */
		public void valueChanged();
	}
	
	/**
	 * Asks the user which item to add.
	 */
	public void addChild();
	
	/**
	 * Adds the given child to this item.
	 * 
	 * @param aItem
	 *            The item to add.
	 * @param aSilent
	 *            Whether a change should be sent or not.
	 */
	public void addChild(Item aItem, boolean aSilent);
	
	/**
	 * Initializes all item actions.
	 */
	public void initActions();
	
	/**
	 * Adds the given value listener to this item.
	 * 
	 * @param aListener
	 *            The value listener.
	 */
	public void addValueListener(ItemValueListener aListener);
	
	/**
	 * @return the calculated experience cost depending on the current item value and restrictions.
	 */
	public int calcEPCost();
	
	/**
	 * @return whether this item can be decreased at all.
	 */
	public boolean canDecrease();
	
	/**
	 * @return how often the user could decrease this item.
	 */
	public int getMaxDecreasure();
	
	/**
	 * @return whether the character has enough experience to increase this item.
	 */
	public boolean canEPIncrease();
	
	/**
	 * @return whether this item can be increased at all.
	 */
	public boolean canIncrease();
	
	/**
	 * Decreases this item if possible.
	 * 
	 * @param aSilent
	 *            Whether a change should be sent.
	 */
	public void decrease(boolean aSilent);
	
	/**
	 * @return the absolute value of this item.
	 */
	public int getAbsoluteValue();
	
	/**
	 * @return a set of all actions this item contains.
	 */
	public Set<ActionInstance> getActions();
	
	/**
	 * @return the sum of all values this and all children of this item have.
	 */
	public int getAllValues();
	
	/**
	 * @param aIndex
	 *            the item child index.
	 * @return the child item at the given index.
	 */
	public ItemInstance getChildAt(int aIndex);
	
	/**
	 * @return a list of all child items of this item.
	 */
	public List<ItemInstance> getChildrenList();
	
	/**
	 * @return the underlying context.
	 */
	public Context getContext();
	
	/**
	 * @return the description for this item.
	 */
	public String getDescription();
	
	/**
	 * @return a list of all child items that have a description.
	 */
	public List<ItemInstance> getDescriptionItems();
	
	/**
	 * @return the default experience cost, each increase of this item costs.
	 */
	public int getEPCost();
	
	/**
	 * @return the experience value that is multiplied with the current item value for increasing this item.
	 */
	public int getEPCostMulti();
	
	/**
	 * @return the experience cost for adding the first point to this item.
	 */
	public int getEPCostNew();
	
	/**
	 * @return the item type.
	 */
	public Item getItem();
	
	/**
	 * @return the parent item group.
	 */
	public ItemGroupInstance getItemGroup();
	
	/**
	 * @return the maximum value reachable as a low level character.
	 */
	public int getMaxLowLevelValue();
	
	/**
	 * @return the current maximum item value.
	 */
	@Override
	public int getMaxValue();
	
	/**
	 * @return the item name.
	 */
	public String getName();
	
	/**
	 * @return the item display name.
	 */
	public String getDisplayName();
	
	/**
	 * @return the parent item if existing.
	 */
	public ItemInstance getParentItem();
	
	/**
	 * @return the current item value.
	 */
	public int getValue();
	
	/**
	 * @param aItem
	 *            The item type.
	 * @return whether this item has a child with the given item type.
	 */
	public boolean hasChild(Item aItem);
	
	/**
	 * @param aIndex
	 *            The item index.
	 * @return whether this item has a child item at the given index.
	 */
	public boolean hasChildAt(int aIndex);
	
	/**
	 * @return whether this item has any child item.
	 */
	public boolean hasChildren();
	
	/**
	 * @return whether this item has a description.
	 */
	public boolean hasDescription();
	
	/**
	 * @return whether the character has enough experience to increase this item.
	 */
	public boolean hasEnoughEP();
	
	/**
	 * @return whether the child items of this item have a specific order.
	 */
	public boolean hasOrder();
	
	/**
	 * @return whether this item has a parent item.
	 */
	public boolean hasParentItem();
	
	/**
	 * Increases this item if possible.
	 * 
	 * @param aAsk
	 *            Whether the host needs to be asked.
	 * @param aCostEP
	 *            Whether the increase costs experience.
	 */
	public void increase(boolean aAsk, boolean aCostEP);
	
	/**
	 * @param aItem
	 *            The child item.
	 * @return the index of the given child item.
	 */
	public int indexOfChild(ItemInstance aItem);
	
	/**
	 * @return whether this item is a parent item.
	 */
	public boolean isParent();
	
	/**
	 * @return whether this item is a value item.
	 */
	public boolean isValueItem();
	
	/**
	 * Removes the given child from this item.
	 * 
	 * @param aItem
	 *            The item type.
	 * @param aSilent
	 *            Whether a change should be sent.
	 */
	public void removeChild(Item aItem, boolean aSilent);
	
	/**
	 * Removes the given value listener from this item.
	 * 
	 * @param aListener
	 *            The listener to remove.
	 */
	public void removeValueListener(ItemValueListener aListener);
	
	/**
	 * Updates the character.
	 */
	public void updateCharacter();
	
	/**
	 * Updates the user interface.
	 */
	@Override
	public void updateUI();
	
	/**
	 * Sets the new value for this item.
	 * 
	 * @param aValue
	 *            The new value.
	 */
	public void updateValue(int aValue);
}
