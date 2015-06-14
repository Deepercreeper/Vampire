package com.deepercreeper.vampireapp.mechanics;


/**
 * Everything that is affected by the game time should implement this interface.
 * 
 * @author vrl
 */
public interface TimeListener
{
	/**
	 * There are different duration types.
	 * 
	 * @author vrl
	 */
	public enum Type
	{
		/**
		 * A round is equal to a very short time, that is used to make the game flow.<br>
		 * Typically a round is around a second, but does not count up to hours.
		 */
		ROUND,
		
		/**
		 * Represents an hour.
		 */
		HOUR,
		
		/**
		 * Represents a day. By default a day is around 10 hours long.
		 */
		DAY
	}
	
	/**
	 * The given time has passed.
	 * 
	 * @param aType
	 *            The time type.
	 * @param aAmount
	 *            The amount of time that has passed.
	 */
	public void time(Type aType, int aAmount);
}
