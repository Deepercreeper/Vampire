package com.deepercreeper.vampireapp.items.interfaces;

import java.util.List;

/**
 * A item group is used to control several items.
 * 
 * @author Vincent
 */
public interface ItemGroup extends Nameable, Dependable
{
	/**
	 * Adds the given item to this group.
	 * 
	 * @param aItem
	 *            The item to add.
	 */
	public void addItem(Item aItem);
	
	/**
	 * @return whether this item group is mutable when the host sees this group.
	 */
	public boolean isHostMutable();
	
	/**
	 * @return the default maximum group values or {@code null} if this is no value group.
	 */
	public int[] getDefaultValues();
	
	/**
	 * @return the group default experience cost.
	 */
	public int getEPCost();
	
	/**
	 * @return the group default experience cost that is multiplied with the current item value.
	 */
	public int getEPCostMultiplicator();
	
	/**
	 * @return the group default experience cost for a new item.
	 */
	public int getEPCostNew();
	
	/**
	 * Returns whether and how many points need to be spent for increasing this item.<br>
	 * If the value is positive it's a fixed value that needs to be spent each time.<br>
	 * If the value is negative it's the factor multiplied to the current value that needs to be spent.<br>
	 * Otherwise (If the value is {@code 0}) this item can't be bought.
	 * 
	 * @return the free points cost.
	 */
	public int getFreePointsCost();
	
	/**
	 * @param aName
	 *            The item name.
	 * @return the item with the given name.
	 */
	public Item getItem(String aName);
	
	/**
	 * @return a list of all items inside this group.
	 */
	public List<Item> getItemsList();
	
	/**
	 * @return the maximum number of items inside this group or {@link Integer#MAX_VALUE} if unlimited.
	 */
	public int getMaxItems();
	
	/**
	 * @return the group default maximum low level item value.
	 */
	public int getMaxLowLevelValue();
	
	/**
	 * @return the group default maximum value for each item.
	 */
	public int getMaxValue();
	
	/**
	 * @return the group default item start value.
	 */
	public int getStartValue();
	
	/**
	 * @param aName
	 *            The item name.
	 * @return whether this group has an item with the given name.
	 */
	public boolean hasItem(String aName);
	
	/**
	 * @return whether this group has a specific order.
	 */
	public boolean hasOrder();
	
	/**
	 * @return whether this group is mutable inside free creation mode.
	 */
	public boolean isFreeMutable();
	
	/**
	 * @return whether this group is mutable.
	 */
	public boolean isMutable();
	
	/**
	 * @return whether this is a value group.
	 */
	public boolean isValueGroup();
}
