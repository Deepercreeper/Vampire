package com.deepercreeper.vampireapp.items.interfaces;

import java.util.List;

/**
 * A item group is used to control several items.
 * 
 * @author Vincent
 */
public interface ItemGroup extends Nameable
{
	public void addItem(Item aItem);
	
	public int[] getDefaultValues();
	
	public int getEPCost();
	
	public int getEPCostNew();
	
	public int getEPCostMultiplicator();
	
	public boolean hasOrder();
	
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
	
	public int getMaxItems();
	
	public int getMaxLowLevelValue();
	
	public int getMaxValue();
	
	public int getStartValue();
	
	public boolean hasItem(String aName);
	
	public boolean isFreeMutable();
	
	public boolean isMutable();
	
	public boolean isValueGroup();
}
