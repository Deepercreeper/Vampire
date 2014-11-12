package com.deepercreeper.vampireapp.controller.interfaces;


/**
 * There are several items that define character property types.
 * 
 * @author Vincent
 */
public interface Item extends Comparable<Item>
{
	/**
	 * Each item has to have a display name.<br>
	 * It is displayed when the user touches any item name.
	 * 
	 * @return the item display name.
	 */
	public String getDisplayName();
	
	/**
	 * @return the number of free points that have to be spent for increasing a value of this item.
	 */
	public int getFreePointsCost();
	
	/**
	 * Each item has a specific maximum start value,<br>
	 * which can be reached when creating a new character.
	 * 
	 * @return the item start value.
	 */
	public int getMaxStartValue();
	
	/**
	 * Each item has a maximum value that can be reached.
	 * 
	 * @return the maximum item value.
	 */
	public int getMaxValue();
	
	/**
	 * @return the item name.
	 */
	public String getName();
	
	/**
	 * Each item has a specific start value, which is set when creating a value of it.
	 * 
	 * @return the item start value.
	 */
	public int getStartValue();
	
	/**
	 * @return whether this item needs a description that has to be given by creating a character.
	 */
	public boolean needsDescription();
}
