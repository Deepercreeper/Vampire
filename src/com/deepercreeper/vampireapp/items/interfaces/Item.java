package com.deepercreeper.vampireapp.items.interfaces;

import java.util.List;
import com.deepercreeper.vampireapp.mechanics.Action;

/**
 * There are several items that define character property types.
 * 
 * @author Vincent
 */
public interface Item extends Nameable
{
	/**
	 * Adds the given action to the list of item specific actions.
	 * 
	 * @param aAction
	 *            The action for this item.
	 */
	public void addAction(Action aAction);
	
	/**
	 * Adds a child item.
	 * 
	 * @param aItem
	 *            The child item.
	 */
	public void addChild(Item aItem);
	
	/**
	 * @param aName
	 *            The action name.
	 * @return the action with the given name.
	 */
	public Action getAction(String aName);
	
	/**
	 * @return a list of all actions of this item.
	 */
	public List<Action> getActionsList();
	
	/**
	 * @param aName
	 *            The child item name.
	 * @return the child with the given name.
	 */
	public Item getChild(String aName);
	
	/**
	 * @return a list of all child items.
	 */
	public List<Item> getChildrenList();
	
	/**
	 * Each item has to have a description.<br>
	 * It is displayed when the user touches any item name.
	 * 
	 * @return the item description.
	 */
	public String getDescription();
	
	/**
	 * @return the default experience amount that is needed to increase this item.
	 */
	public int getEPCost();
	
	/**
	 * @return the number of experience points per item value, that are needed to increase this item.
	 */
	public int getEPCostMultiplicator();
	
	/**
	 * @return the number of experience points that are needed to add the first value to this item.
	 */
	public int getEPCostNew();
	
	/**
	 * @return the number of free points that are needed to increase this item.
	 */
	public int getFreePointsCost();
	
	/**
	 * @return the parent item group.
	 */
	public ItemGroup getItemGroup();
	
	/**
	 * @return the maximum value for this item, if the character is still a low level character.
	 */
	public int getMaxLowLevelValue();
	
	/**
	 * @return the real maximum value.
	 */
	public int getMaxValue();
	
	/**
	 * @return the parent item or {@code null} if this item doesn't have a parent item.
	 */
	public Item getParentItem();
	
	/**
	 * @return the start value, instances of this item have from beginning.
	 */
	public int getStartValue();
	
	/**
	 * @return the values that are used and displayed, when the item is increased or decreased.
	 */
	public int[] getValues();
	
	/**
	 * @return whether this item contains actions.
	 */
	public boolean hasActions();
	
	/**
	 * @param aItem
	 *            The child item.
	 * @return whether this item has the given item as child.
	 */
	public boolean hasChild(Item aItem);
	
	/**
	 * @param aName
	 *            The child item name.
	 * @return whether this item has a child item with the given name.
	 */
	public boolean hasChild(String aName);
	
	/**
	 * @return whether child items of this item have a mutable order.
	 */
	public boolean hasOrder();
	
	/**
	 * @return whether this item has a parent item.
	 */
	public boolean hasParentItem();
	
	/**
	 * @return whether this item allows adding, changing and removing child items.
	 */
	public boolean isMutableParent();
	
	/**
	 * @return whether this item has child items.
	 */
	public boolean isParent();
	
	/**
	 * @return whether this item contains a value.
	 */
	public boolean isValueItem();
	
	/**
	 * @return whether this item needs a description that has to be given by creating a character.
	 */
	public boolean needsDescription();
}
