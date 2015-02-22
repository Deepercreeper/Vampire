package com.deepercreeper.vampireapp.items.interfaces.creations.restrictions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction.InstanceRestrictionType;

public interface CreationRestriction
{
	public static class CreationRestrictionType
	{
		private static final Map<String, CreationRestrictionType>	RESTRICTION_TYPES			= new HashMap<String, CreationRestrictionType>();
		
		public static final CreationRestrictionType					ITEM_VALUE					= new CreationRestrictionType("ItemValue",
																										InstanceRestrictionType.ITEM_VALUE);
		
		public static final CreationRestrictionType					ITEM_CHILD_VALUE_AT			= new CreationRestrictionType("ItemChildValueAt");
		
		public static final CreationRestrictionType					ITEM_CHILDREN_COUNT			= new CreationRestrictionType("ItemChildrenCount");
		
		public static final CreationRestrictionType					GROUP_CHILDREN				= new CreationRestrictionType("GroupChildren");
		
		public static final CreationRestrictionType					GROUP_CHILDREN_COUNT		= new CreationRestrictionType("GroupChildrenCount");
		
		public static final CreationRestrictionType					GROUP_ITEM_VALUE_AT			= new CreationRestrictionType("GroupItemValueAt");
		
		public static final CreationRestrictionType					ITEM_EP_COST				= new CreationRestrictionType("ItemEpCost",
																										InstanceRestrictionType.ITEM_EP_COST);
		
		public static final CreationRestrictionType					ITEM_EP_COST_MULTI			= new CreationRestrictionType("ItemEpCostMulti",
																										InstanceRestrictionType.ITEM_EP_COST_MULTI);
		
		public static final CreationRestrictionType					ITEM_EP_COST_NEW			= new CreationRestrictionType("ItemEpCostNew",
																										InstanceRestrictionType.ITEM_EP_COST_NEW);
		
		public static final CreationRestrictionType					ITEM_CHILD_EP_COST_MULTI_AT	= new CreationRestrictionType(
																										"ItemChildEpCostMultiAt",
																										InstanceRestrictionType.ITEM_CHILD_EP_COST_MULTI_AT);
		
		public static final CreationRestrictionType					ITEM_CHILD_EP_COST_NEW		= new CreationRestrictionType(
																										"ItemChildEpCostNew",
																										InstanceRestrictionType.ITEM_CHILD_EP_COST_NEW);
		
		public static final CreationRestrictionType					INSANITY					= new CreationRestrictionType("Insanity");
		
		public static final CreationRestrictionType					GENERATION					= new CreationRestrictionType("Generation");
		
		private final String										mName;
		
		private final InstanceRestrictionType						mInstanceType;
		
		public CreationRestrictionType(final String aName)
		{
			mName = aName;
			mInstanceType = null;
			RESTRICTION_TYPES.put(getName(), this);
		}
		
		private CreationRestrictionType(final String aName, final InstanceRestrictionType aInstanceType)
		{
			mName = aName;
			mInstanceType = aInstanceType;
			RESTRICTION_TYPES.put(getName(), this);
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
		
		public InstanceRestrictionType getInstanceType()
		{
			return mInstanceType;
		}
		
		public String getName()
		{
			return mName;
		}
		
		public boolean isPersistent()
		{
			return mInstanceType != null;
		}
		
		public static CreationRestrictionType get(final String aName)
		{
			return RESTRICTION_TYPES.get(aName);
		}
	}
	
	public void addCondition(CreationCondition aCondition);
	
	public void clear();
	
	public InstanceRestriction createInstance();
	
	public Set<CreationCondition> getConditions();
	
	public int getIndex();
	
	public String getItemName();
	
	public List<String> getItems();
	
	public int getMaximum();
	
	public int getMinimum();
	
	public CreationRestrictionable getParent();
	
	public CreationRestrictionType getType();
	
	public int getValue();
	
	public boolean hasConditions();
	
	public boolean isActive(ItemControllerCreation aController);
	
	public boolean isCreationRestriction();
	
	public boolean isInRange(int aValue);
	
	public boolean isPersistent();
	
	public void setParent(CreationRestrictionable aParent);
	
	public void update();
}
