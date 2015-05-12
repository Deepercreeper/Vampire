package com.deepercreeper.vampireapp.mechanics;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.util.ItemFinder;

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
		private static final Map<String, ActionType>	ACTION_TYPES	= new HashMap<String, ActionType>();
		
		/**
		 * Character abilities like discipline parts.
		 */
		public static final ActionType					ABILITY			= new ActionType("Ability");
		
		private final String							mName;
		
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
	 * @return the action Id.
	 */
	public String getId();
	
	/**
	 * @return the action type.
	 */
	public ActionType getType();
	
	/**
	 * @return the action display name.
	 */
	public String getDisplayName();
	
	/**
	 * @return the locale depending action name.
	 */
	public String getName();
	
	/**
	 * @return the minimum item instance level to use this action.
	 */
	public int getMinLevel();
	
	/**
	 * @return the minimum number of dices that can be used to count hits for this action.
	 */
	public int getMinDices();
	
	/**
	 * By default the number of dices is the number of minimum dices added to all necessary<br>
	 * item values for this action.
	 * 
	 * @return the default number of dices.
	 */
	public int getDefaultDices();
	
	/**
	 * @param aLevel
	 *            The action item value.
	 * @return whether this action can be used at the current item level and item costs.
	 */
	public boolean canUse(int aLevel);
	
	/**
	 * Initializes this action, so that the item values can be calculated.
	 * 
	 * @param aFinder
	 *            The item finder.
	 */
	public void init(ItemFinder aFinder);
}
