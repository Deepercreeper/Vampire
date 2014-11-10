package com.deepercreeper.vampireapp.controller.interfaces;

import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;

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
	public boolean canDecrease(CharMode aMode);
	
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
	public boolean canIncrease(CharMode aMode);
	
	/**
	 * Decreases this value if possible.
	 */
	public void decrease();
	
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
	public CharMode getCreationMode();
	
	/**
	 * @return the item that defines the type of this value.
	 */
	public T getItem();
	
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
	 * @return the update action of this value.
	 */
	public UpdateAction getUpdateAction();
	
	/**
	 * @return the current item value.
	 */
	public int getValue();
	
	/**
	 * Increases this value if possible.
	 */
	public void increase();
	
	/**
	 * Applies the value to the value display again.
	 */
	public void refreshValue();
	
	/**
	 * Removes all widgets from their parent containers.
	 */
	public void release();
	
	/**
	 * Removes all temporary points.
	 */
	public void resetTempPoints();
	
	/**
	 * Sets whether this group is in creation mode.
	 * 
	 * @param aMode
	 *            Whether this group represents the values inside a character creation.
	 */
	public void setCreationMode(CharMode aMode);
	
	/**
	 * Enables or disabled the decrease button.
	 * 
	 * @param aEnabled
	 *            Whether the decrease button should be enabled.
	 */
	public void setDecreasable(boolean aEnabled);
	
	/**
	 * Enables or disabled the increase button.
	 * 
	 * @param aEnabled
	 *            Whether the increase button should be enabled.
	 */
	public void setIncreasable(boolean aEnabled);
	
	/**
	 * Sets the current points handler.
	 * 
	 * @param aPoints
	 *            The new points handler.
	 */
	public void setPoints(PointHandler aPoints);
}
