package com.deepercreeper.vampireapp.mechanics;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.items.implementations.Named;

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
	public static class ActionType extends Named
	{
		private static final Map<String, ActionType> ACTION_TYPES = new HashMap<String, ActionType>();
		
		/**
		 * Character abilities like discipline parts.
		 */
		public static final ActionType ABILITY = new ActionType("Ability");
		
		/**
		 * Default actions like punching and jumping.
		 */
		public static final ActionType DEFAULT = new ActionType("Default");
		
		/**
		 * Weapon uses like swinging a sword.
		 */
		public static final ActionType WEAPON = new ActionType("Weapon");
		
		private ActionType(final String aName)
		{
			super(aName);
			ACTION_TYPES.put(getName(), this);
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
	 * @return the number of instant success dices that are added to this actions dice amount.
	 */
	public int getInstantSuccess();
	
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
