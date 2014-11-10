package com.deepercreeper.vampireapp.controller.interfaces;

import android.content.Context;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.controller.CharMode;

/**
 * A controller that handles value groups.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 */
public interface ValueController <T extends Item>
{
	/**
	 * Used to call the number of points from the parent.
	 * 
	 * @author Vincent
	 */
	public static interface PointHandler
	{
		/**
		 * Decreases the current available points by {@code aValue} points.
		 * 
		 * @param aValue
		 *            The points to add.
		 */
		public void decrease(int aValue);
		
		/**
		 * @return the current number of free or experience points from the parent.
		 */
		public int getPoints();
		
		/**
		 * Increases the current available points by {@code aValue} points.
		 * 
		 * @param aValue
		 *            The points to subtract.
		 */
		public void increase(int aValue);
	}
	
	/**
	 * Closes the widget container. Maybe for refilling with new values.
	 */
	public void close();
	
	/**
	 * @return the context of this value controller.
	 */
	public Context getContext();
	
	/**
	 * @return the controller.
	 */
	public Controller<T> getController();
	
	/**
	 * @return whether this controller is in the creation mode.
	 */
	public CharMode getCreationMode();
	
	/**
	 * @return the point handler of this value controller.
	 */
	public PointHandler getPoints();
	
	/**
	 * The given layout is a container for a drop down button and a linear layout<br>
	 * that will be expanded after pushing the button.
	 * This method has to initialize the button and the layout and perform the animation.<br>
	 * Also it has to make the value groups build the content of each layout on demand.
	 * 
	 * @param aLayout
	 *            The layout to put the content into.
	 */
	public void initLayout(LinearLayout aLayout);
	
	/**
	 * Removes all widgets from their parent container.
	 */
	public void release();
	
	/**
	 * Removes all temporary points from all values.
	 */
	public void resetTempPoints();
	
	/**
	 * Sets whether this controller is in the creation mode.
	 * 
	 * @param aMode
	 *            Whether creation mode or not.
	 */
	public void setCreationMode(CharMode aMode);
	
	/**
	 * Enables or disabled the value groups for changing anything.
	 * 
	 * @param aEnabled
	 *            Whether the groups should be enabled or not.
	 */
	public void setEnabled(boolean aEnabled);
	
	/**
	 * @param aPoints
	 *            The new point listener.
	 */
	public void setPoints(PointHandler aPoints);
	
	/**
	 * Updates all value groups.
	 * 
	 * @param aUpdateOthers
	 *            Whether all other controllers should also be updated.
	 */
	public void updateValues(boolean aUpdateOthers);
}
