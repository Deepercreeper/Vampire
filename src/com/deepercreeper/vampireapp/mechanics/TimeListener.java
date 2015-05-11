package com.deepercreeper.vampireapp.mechanics;

/**
 * Everything that is affected by the game time should implement this interface.
 * 
 * @author vrl
 */
public interface TimeListener
{
	/**
	 * Called when a day has passed.
	 */
	public void day();
	
	/**
	 * Called when an hour has passed.
	 */
	public void hour();
	
	/**
	 * Called when a round has passed.
	 */
	public void round();
}
