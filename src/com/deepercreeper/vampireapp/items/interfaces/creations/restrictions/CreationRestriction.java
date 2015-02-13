package com.deepercreeper.vampireapp.items.interfaces.creations.restrictions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;

public interface CreationRestriction
{
	public static class CreationRestrictionType
	{
		private static final Map<String, CreationRestrictionType>	RESTRICTION_TYPES		= new HashMap<String, CreationRestrictionType>();
		
		public static final CreationRestrictionType					ITEM_VALUE				= new CreationRestrictionType("ItemValue");
		
		public static final CreationRestrictionType					ITEM_CHILD_VALUE_AT		= new CreationRestrictionType("ItemChildValueAt");
		
		public static final CreationRestrictionType					ITEM_CHILDREN_COUNT		= new CreationRestrictionType("ItemChildrenCount");
		
		public static final CreationRestrictionType					GROUP_CHILDREN			= new CreationRestrictionType("GroupChildren");
		
		public static final CreationRestrictionType					GROUP_CHILDREN_COUNT	= new CreationRestrictionType("GroupChildrenCount");
		
		public static final CreationRestrictionType					GROUP_ITEM_VALUE_AT		= new CreationRestrictionType("GroupItemValueAt");
		
		public static final CreationRestrictionType					INSANITY				= new CreationRestrictionType("Insanity");
		
		public static final CreationRestrictionType					GENERATION				= new CreationRestrictionType("Generation");
		
		private final String								mName;
		
		private CreationRestrictionType(final String aName)
		{
			mName = aName;
			RESTRICTION_TYPES.put(getName(), this);
		}
		
		public String getName()
		{
			return mName;
		}
		
		public static CreationRestrictionType get(final String aName)
		{
			return RESTRICTION_TYPES.get(aName);
		}
		
		@Override
		public boolean equals(final Object aO)
		{
			if (aO instanceof CreationRestrictionType)
			{
				final CreationRestrictionType type = (CreationRestrictionType) aO;
				return type.getName().equals(getName());
			}
			return false;
		}
	}
	
	public int getIndex();
	
	public void update();
	
	public void addCondition(CreationCondition aCondition);
	
	public boolean hasConditions();
	
	public boolean isActive(ItemControllerCreation aController);
	
	public void setParent(CreationRestrictionable aParent);
	
	public CreationRestrictionable getParent();
	
	public void clear();
	
	public CreationRestrictionType getType();
	
	public String getItemName();
	
	public Set<CreationCondition> getConditions();
	
	public boolean isInRange(int aValue);
	
	public int getMinimum();
	
	public int getMaximum();
	
	public List<String> getItems();
}
