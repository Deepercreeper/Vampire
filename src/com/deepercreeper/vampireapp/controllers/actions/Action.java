package com.deepercreeper.vampireapp.controllers.actions;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.ItemInstance;

public interface Action extends Comparable<Action>
{
	public static interface ItemFinder
	{
		public ItemInstance findItem(String aName);
	}
	
	public static class ActionType
	{
		private static final Map<String, ActionType>	ACTION_TYPES	= new HashMap<String, ActionType>();
		
		public static final ActionType					ABILITY			= new ActionType("Ability");
		
		private final String							mName;
		
		public ActionType(final String aName)
		{
			mName = aName;
			ACTION_TYPES.put(getName(), this);
		}
		
		public String getName()
		{
			return mName;
		}
		
		public static ActionType get(final String aName)
		{
			return ACTION_TYPES.get(aName);
		}
	}
	
	public String getId();
	
	public ActionType getType();
	
	public String getDisplayName();
	
	public String getName();
	
	public int getMinLevel();
	
	public int getMinDices();
	
	public int getDefaultDices();
	
	public boolean canUse(int aLevel);
	
	public void init(ItemFinder aFinder);
}
