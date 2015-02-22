package com.deepercreeper.vampireapp.items.interfaces.instances.restrictions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.util.Saveable;

public interface InstanceRestriction extends Saveable
{
	public static class InstanceRestrictionType
	{
		private static final Map<String, InstanceRestrictionType>	RESTRICTION_TYPES			= new HashMap<String, InstanceRestrictionType>();
		
		public static final InstanceRestrictionType					ITEM_VALUE					= new InstanceRestrictionType("ItemValue");
		
		public static final InstanceRestrictionType					ITEM_EP_COST				= new InstanceRestrictionType("ItemEpCost");
		
		public static final InstanceRestrictionType					ITEM_EP_COST_MULTI			= new InstanceRestrictionType("ItemEpCostMulti");
		
		public static final InstanceRestrictionType					ITEM_EP_COST_NEW			= new InstanceRestrictionType("ItemEpCostNew");
		
		public static final InstanceRestrictionType					ITEM_CHILD_EP_COST_MULTI_AT	= new InstanceRestrictionType(
																										"ItemChildEpCostMultiAt");
		
		public static final InstanceRestrictionType					ITEM_CHILD_EP_COST_NEW		= new InstanceRestrictionType("ItemChildEpCostNew");
		
		private final String										mName;
		
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
	
	public int getValue();
	
	public void clear();
	
	public InstanceRestrictionType getType();
	
	public String getItemName();
	
	public Set<InstanceCondition> getConditions();
	
	public boolean isInRange(int aValue);
	
	public int getMinimum();
	
	public int getMaximum();
	
	public List<String> getItems();
}
