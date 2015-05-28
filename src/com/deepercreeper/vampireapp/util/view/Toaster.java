package com.deepercreeper.vampireapp.util.view;

/**
 * Everything that is able to make toast messages should implement this interface.
 * 
 * @author Vincent
 */
public interface Toaster
{
	/**
	 * Displays the given text for the given time.
	 * 
	 * @param aText
	 *            The text to display.
	 * @param aDuration
	 *            The display length.
	 */
	public void makeText(String aText, int aDuration);
	
	/**
	 * Displays the resource string with the given id for the given time.
	 * 
	 * @param aResId
	 *            The resource id.
	 * @param aDuration
	 *            The display length.
	 */
	public void makeText(int aResId, int aDuration);
}
