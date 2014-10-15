package com.deepercreeper.vampireapp.controller;

import android.widget.LinearLayout;

/**
 * Each item can be instantiated. That creates an item value.<br>
 * Item values have a specified value that can be changed.
 * 
 * @author Vincent
 * @param <T>
 *            The parent item type.
 */
public interface ItemValue <T extends Item>
{
	/**
	 * When a value is changed, added, removed or updated,<br>
	 * all values inside one value controller have to be updated.<br>
	 * An update action defines which action is made when the action is done.
	 * 
	 * @author Vincent
	 */
	public interface UpdateAction
	{
		/**
		 * Updates the value controller.
		 */
		public void update();
	}
	
	/**
	 * @return whether this value can be decreased.
	 */
	public boolean canDecrease();
	
	/**
	 * If the value has special decrease properties this defines whether it is able to be decreased.
	 * 
	 * @param aMode
	 *            Whether the value is used when a character is created.
	 * @return whether this value is able to be decreased.
	 */
	public boolean canDecrease(CreationMode aMode);
	
	/**
	 * @return whether this value can be increased.
	 */
	public boolean canIncrease();
	
	/**
	 * If the value has special increase properties this defines whether it is able to be increased.
	 * 
	 * @param aMode
	 *            Whether the value is used when a character is created.
	 * @return whether this value is able to be increased.
	 */
	public boolean canIncrease(CreationMode aMode);
	
	/**
	 * Increases this value if possible.
	 */
	public void increase();
	
	/**
	 * Decreases this value if possible.
	 */
	public void decrease();
	
	/**
	 * Enables or disabled the increase button.
	 * 
	 * @param aEnabled
	 *            Whether the increase button should be enabled.
	 */
	public void setIncreasable(boolean aEnabled);
	
	/**
	 * Enables or disabled the decrease button.
	 * 
	 * @param aEnabled
	 *            Whether the decrease button should be enabled.
	 */
	public void setDecreasable(boolean aEnabled);
	
	/**
	 * Removes all temporary points.
	 */
	public void resetTempPoints();
	
	/**
	 * Free bonus points are handled as temporary points. They need to be saved separately.
	 * 
	 * @return the number of temporary points.
	 */
	public int getTempPoints();
	
	/**
	 * Removes all widgets from their parent containers.
	 */
	public void release();
	
	/**
	 * Each value has a specified widget that contains some buttons and text views to handle its value.
	 * 
	 * @return the value container.
	 */
	public LinearLayout getContainer();
	
	/**
	 * @return the item that defines the type of this value.
	 */
	public T getItem();
	
	/**
	 * @return the current item value.
	 */
	public int getValue();
}
