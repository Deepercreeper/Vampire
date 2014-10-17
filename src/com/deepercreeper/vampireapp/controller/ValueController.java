package com.deepercreeper.vampireapp.controller;

import android.widget.LinearLayout;

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
		 * @return the current number of free or experience points from the parent.
		 */
		public int getPoints();
		
		/**
		 * Decreases the current available points by {@code aValue} points.
		 * 
		 * @param aValue
		 *            The points to add.
		 */
		public void decrease(int aValue);
		
		/**
		 * Increases the current available points by {@code aValue} points.
		 * 
		 * @param aValue
		 *            The points to subtract.
		 */
		public void increase(int aValue);
	}
	
	/**
	 * @param aPoints
	 *            The new point listener.
	 */
	public void setPoints(PointHandler aPoints);
	
	/**
	 * Enables or disabled the value groups for changing anything.
	 * 
	 * @param aEnabled
	 *            Whether the groups should be enabled or not.
	 */
	public void setEnabled(boolean aEnabled);
	
	/**
	 * Removes all temporary points from all values.
	 */
	public void resetTempPoints();
	
	/**
	 * Removes all widgets from their parent container.
	 */
	public void release();
	
	/**
	 * Closes the widget container. Maybe for refilling with new values.
	 */
	public void close();
	
	/**
	 * @return the controller.
	 */
	public Controller<T> getController();
	
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
	 * @return whether this controller is in the creation mode.
	 */
	public CreationMode getCreationMode();
	
	/**
	 * Sets whether this controller is in the creation mode.
	 * 
	 * @param aMode
	 *            Whether creation mode or not.
	 */
	public void setCreationMode(CreationMode aMode);
	
	/**
	 * Updates all value groups.
	 */
	public void updateValues();
}
