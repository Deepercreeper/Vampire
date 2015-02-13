package com.deepercreeper.vampireapp.items.interfaces.instances.restrictions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;

public interface InstanceRestriction
{
	public static class InstanceRestrictionType
	{
		private static final Map<String, InstanceRestrictionType>	RESTRICTION_TYPES		= new HashMap<String, InstanceRestrictionType>();
		
		public static final InstanceRestrictionType					ITEM_VALUE				= new InstanceRestrictionType("ItemValue");
		
		public static final InstanceRestrictionType					ITEM_CHILD_VALUE_AT		= new InstanceRestrictionType("ItemChildValueAt");
		
		public static final InstanceRestrictionType					ITEM_CHILDREN_COUNT		= new InstanceRestrictionType("ItemChildrenCount");
		
		public static final InstanceRestrictionType					GROUP_CHILDREN			= new InstanceRestrictionType("GroupChildren");
		
		public static final InstanceRestrictionType					GROUP_CHILDREN_COUNT	= new InstanceRestrictionType("GroupChildrenCount");
		
		public static final InstanceRestrictionType					GROUP_ITEM_VALUE_AT		= new InstanceRestrictionType("GroupItemValueAt");
		
		public static final InstanceRestrictionType					INSANITY				= new InstanceRestrictionType("Insanity");
		
		public static final InstanceRestrictionType					GENERATION				= new InstanceRestrictionType("Generation");
		
		private final String								mName;
		
		private InstanceRestrictionType(final String aName)
		{
			mName = aName;
			RESTRICTION_TYPES.put(getName(), this);
		}
		
		public String getName()
		{
			return mName;
		}
		
		public static InstanceRestrictionType get(final String aName)
		{
			return RESTRICTION_TYPES.get(aName);
		}
		
		@Override
		public boolean equals(final Object aO)
		{
			if (aO instanceof InstanceRestrictionType)
			{
				final InstanceRestrictionType type = (InstanceRestrictionType) aO;
				return type.getName().equals(getName());
			}
			return false;
		}
	}
	
	public int getIndex();
	
	public void update();
	
	public void addCondition(InstanceCondition aCondition);
	
	public boolean hasConditions();
	
	public boolean isActive(ItemControllerInstance aController);
	
	public void setParent(InstanceRestrictionable aParent);
	
	public InstanceRestrictionable getParent();
	
	public void clear();
	
	public InstanceRestrictionType getType();
	
	public String getItemName();
	
	public Set<InstanceCondition> getConditions();
	
	public boolean isInRange(int aValue);
	
	public int getMinimum();
	
	public int getMaximum();
	
	public List<String> getItems();
}
