package com.deepercreeper.vampireapp.controllers.actions;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Namable;

public interface Action extends Namable
{
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
	
	public ActionType getType();
	
	public int getMinLevel();
}
