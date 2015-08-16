package com.deepercreeper.vampireapp.mechanics;

import java.util.HashMap;
import java.util.Map;

/**
 * Everything that is possible to be executed by a character, like abilities, healing or something<br>
 * should implement this interface.
 * 
 * @author vrl
 */
public interface Action extends Comparable<Action>
{
	/**
	 * Each action has exactly one type, that tells, how it should be treated.
	 * 
	 * @author vrl
	 */
	public static class ActionType
	{
		private static final Map<String, ActionType> ACTION_TYPES = new HashMap<String, ActionType>();
		
		/**
		 * Character abilities like discipline parts.
		 */
		public static final ActionType ABILITY = new ActionType("Ability");
		
		private final String mName;
		
		private ActionType(final String aName)
		{
			mName = aName;
			ACTION_TYPES.put(getName(), this);
		}
		
		/**
		 * @return the action type name.
		 */
		public String getName()
		{
			return mName;
		}
		
		/**
		 * @param aName
		 *            The action type name.
		 * @return the action type with the given name.
		 */
		public static ActionType get(final String aName)
		{
			return ACTION_TYPES.get(aName);
		}
	}
	
	/**
	 * @return a list of items that can be spent by a user defined amount to have a special effect.
	 */
	public String[] getCostDiceNames();
	
	/**
	 * @return a map of item names and a value for each. It defines, how many item points<br>
	 *         need to be spent for using this action.
	 */
	public Map<String, Integer> getCostNames();
	
	/**
	 * @return a list of names for the items whose value are added to the minimum dice number
	 */
	public String[] getDiceNames();
	
	/**
	 * @return the action display name.
	 */
	public String getDisplayName();
	
	/**
	 * @return the minimum number of dices that can be used to count hits for this action.
	 */
	public int getMinDices();
	
	/**
	 * @return the minimum item instance level to use this action.
	 */
	public int getMinLevel();
	
	/**
	 * @return the locale depending action name.
	 */
	public String getName();
	
	/**
	 * @return the action type.
	 */
	public ActionType getType();
	
}
