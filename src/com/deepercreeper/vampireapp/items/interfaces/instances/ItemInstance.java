package com.deepercreeper.vampireapp.items.interfaces.instances;

import java.util.List;
import java.util.Set;
import android.content.Context;
import com.deepercreeper.vampireapp.character.controllers.EPController;
import com.deepercreeper.vampireapp.character.instance.Mode;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestrictionable;
import com.deepercreeper.vampireapp.mechanics.Action;
import com.deepercreeper.vampireapp.util.ItemFinder;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * This represents a final item that a character can have.
 * 
 * @author vrl
 */
public interface ItemInstance extends InstanceRestrictionable, Comparable<ItemInstance>, Saveable, Viewable
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
	 * @return whether the character has enough experience to increase this item.
	 */
	public boolean canEPIncrease();
	
	/**
	 * @return whether this item can be increased at all.
	 */
	public boolean canIncrease();
	
	/**
	 * @return the absolute value of this item.
	 */
	public int getAbsoluteValue();
	
	/**
	 * @return a set of all actions this item contains.
	 */
	public Set<Action> getActions();
	
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
	 * @return the experience manager of the character.
	 */
	public EPController getEP();
	
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
	 * @return the character mode.
	 */
	public Mode getMode();
	
	/**
	 * @return the item name.
	 */
	public String getName();
	
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
	 */
	public void increase();
	
	/**
	 * @param aItem
	 *            The child item.
	 * @return the index of the given child item.
	 */
	public int indexOfChild(ItemInstance aItem);
	
	/**
	 * Initializes all actions of this item.
	 * 
	 * @param aFinder
	 *            The item finder.
	 */
	public void initActions(ItemFinder aFinder);
	
	/**
	 * @return whether this item is a parent item.
	 */
	public boolean isParent();
	
	/**
	 * @return whether this item is a value item.
	 */
	public boolean isValueItem();
	
	/**
	 * @return whether the host can decrease this item.
	 */
	public boolean masterCanDecrease();
	
	/**
	 * @return whether the host can increase this item.
	 */
	public boolean masterCanIncrease();
	
	/**
	 * Decreases this item if possible for the host.
	 */
	public void masterDecrease();
	
	/**
	 * Increases this item if possible for the host.
	 */
	public void masterIncrease();
	
	/**
	 * Updates the view shown value and information about this item.
	 */
	public void refreshValue();
	
	/**
	 * Removes the given value listener from this item.
	 * 
	 * @param aListener
	 *            The listener to remove.
	 */
	public void removeValueListener(ItemValueListener aListener);
	
	/**
	 * Calculates whether this item is increasable for the user.
	 */
	public void setIncreasable();
	
	/**
	 * Sets the character mode for this item and all of its child items.
	 * 
	 * @param aMode
	 *            The new character mode.
	 */
	public void setMode(Mode aMode);
	
	/**
	 * Updates all item buttons.
	 */
	public void updateButtons();
	
	/**
	 * Updates the character.
	 */
	public void updateCharacter();
}
