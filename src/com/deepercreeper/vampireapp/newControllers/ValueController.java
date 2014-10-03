package com.deepercreeper.vampireapp.newControllers;

import android.widget.LinearLayout;

public interface ValueController <T extends Item>
{
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
	
	public boolean isCreation();
	
	public void setCreation(boolean aCreation);
	
	public void updateValues();
}
