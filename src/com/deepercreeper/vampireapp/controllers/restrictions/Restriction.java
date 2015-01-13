package com.deepercreeper.vampireapp.controllers.restrictions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemControllerCreation;

public interface Restriction
{
	public static class RestrictionType
	{
		private static final Map<String, RestrictionType>	RESTRICTION_TYPES		= new HashMap<String, RestrictionType>();
		
		public static final RestrictionType					ITEM_VALUE				= new RestrictionType("ItemValue");
		
		public static final RestrictionType					ITEM_CHILD_VALUE_AT		= new RestrictionType("ItemChildValueAt");
		
		public static final RestrictionType					ITEM_CHILDREN_COUNT		= new RestrictionType("ItemChildrenCount");
		
		public static final RestrictionType					GROUP_CHILDREN			= new RestrictionType("GroupChildren");
		
		public static final RestrictionType					GROUP_CHILDREN_COUNT	= new RestrictionType("GroupChildrenCount");
		
		public static final RestrictionType					INSANITY				= new RestrictionType("Insanity");
		
		public static final RestrictionType					GENERATION				= new RestrictionType("Generation");
		
		private final String								mName;
		
		private RestrictionType(final String aName)
		{
			mName = aName;
			RESTRICTION_TYPES.put(getName(), this);
		}
		
		public String getName()
		{
			return mName;
		}
		
		public static RestrictionType get(final String aName)
		{
			return RESTRICTION_TYPES.get(aName);
		}
		
		@Override
		public boolean equals(final Object aO)
		{
			if (aO instanceof RestrictionType)
			{
				final RestrictionType type = (RestrictionType) aO;
				return type.getName().equals(getName());
			}
			return false;
		}
	}
	
	public int getIndex();
	
	public void update();
	
	public void addCondition(Condition aCondition);
	
	public boolean hasConditions();
	
	public boolean isActive(ItemControllerCreation aController);
	
	public void setParent(Restrictionable aParent);
	
	public Restrictionable getParent();
	
	public void clear();
	
	public RestrictionType getRestrictionType();
	
	public String getItemName();
	
	public Set<Condition> getConditions();
	
	public boolean isInRange(int aValue);
	
	public int getMinimum();
	
	public int getMaximum();
	
	public List<String> getItems();
}
