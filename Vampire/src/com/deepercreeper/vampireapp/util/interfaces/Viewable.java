package com.deepercreeper.vampireapp.util.interfaces;

import android.view.View;

/**
 * Everything that needs to be initialized and can be released should implement this interface.
 * 
 * @author vrl
 */
public interface Viewable
{
	/**
	 * @return the main container, that contains all views of this viewable.
	 */
	public View getContainer();
	
	/**
	 * Releases all views from their parent, so they can be added again.
	 */
	public void release();
	
	/**
	 * Updates the user interface.
	 */
	public void updateUI();
}
