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
	 * Enables or disabled the value groups for changing anything.
	 * 
	 * @param aEnabled
	 *            Whether the groups should be enabled or not.
	 */
	public void setEnabled(boolean aEnabled);
	
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
	public boolean isCreation();
	
	/**
	 * Sets whether this controller is in the creation mode.
	 * 
	 * @param aCreation
	 *            Whether creation mode or not.
	 */
	public void setCreation(boolean aCreation);
	
	/**
	 * Updates all value groups.
	 */
	public void updateValues();
}
