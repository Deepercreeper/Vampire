package com.deepercreeper.vampireapp.items.interfaces.creations.restrictions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction.InstanceRestrictionType;

/**
 * A restriction that is created and used inside the character creation.<br>
 * Maybe this restriction will exist further to the character instance.
 * 
 * @author vrl
 */
public interface CreationRestriction
{
	/**
	 * The type of restriction. It tells what is restricted.
	 * 
	 * @author vrl
	 */
	public static class CreationRestrictionType
	{
		/**
		 * @param aName
		 *            The restriction type name.
		 * @return the restriction type with the given name.
		 */
		public static CreationRestrictionType get(final String aName)
		{
			return RESTRICTION_TYPES.get(aName);
		}
		
		private static final Map<String, CreationRestrictionType>	RESTRICTION_TYPES			= new HashMap<String, CreationRestrictionType>();
		
		/**
		 * The value of a specific item is restricted.
		 */
		public static final CreationRestrictionType					ITEM_VALUE					= new CreationRestrictionType("ItemValue",
																										InstanceRestrictionType.ITEM_VALUE);
		
		/**
		 * The value of the child item at the given position is restricted.
		 */
		public static final CreationRestrictionType					ITEM_CHILD_VALUE_AT			= new CreationRestrictionType("ItemChildValueAt");
		
		/**
		 * The number of children of one specific item is restricted.
		 */
		public static final CreationRestrictionType					ITEM_CHILDREN_COUNT			= new CreationRestrictionType("ItemChildrenCount");
		
		/**
		 * Restricts, which items can be inside a group.
		 */
		public static final CreationRestrictionType					GROUP_CHILDREN				= new CreationRestrictionType("GroupChildren");
		
		/**
		 * Restricts the number of children inside a group.
		 */
		public static final CreationRestrictionType					GROUP_CHILDREN_COUNT		= new CreationRestrictionType("GroupChildrenCount");
		
		/**
		 * The value of the child at the given position inside a group is restricted.
		 */
		public static final CreationRestrictionType					GROUP_ITEM_VALUE_AT			= new CreationRestrictionType("GroupItemValueAt");
		
		/**
		 * The normal experience cost for a item is restricted.
		 */
		public static final CreationRestrictionType					ITEM_EP_COST				= new CreationRestrictionType("ItemEpCost",
																										InstanceRestrictionType.ITEM_EP_COST);
		
		/**
		 * The additional experience depending on the current value of the item is restricted.
		 */
		public static final CreationRestrictionType					ITEM_EP_COST_MULTI			= new CreationRestrictionType("ItemEpCostMulti",
																										InstanceRestrictionType.ITEM_EP_COST_MULTI);
		
		/**
		 * The experience cost for the first point of an item is restricted.
		 */
		public static final CreationRestrictionType					ITEM_EP_COST_NEW			= new CreationRestrictionType("ItemEpCostNew",
																										InstanceRestrictionType.ITEM_EP_COST_NEW);
		
		/**
		 * The value depending experience cost of the child at the given position is restricted.
		 */
		public static final CreationRestrictionType					ITEM_CHILD_EP_COST_MULTI_AT	= new CreationRestrictionType(
																										"ItemChildEpCostMultiAt",
																										InstanceRestrictionType.ITEM_CHILD_EP_COST_MULTI_AT);
		
		/**
		 * The experience cost for the first point of the child item at the given position is restricted.
		 */
		public static final CreationRestrictionType					ITEM_CHILD_EP_COST_NEW		= new CreationRestrictionType(
																										"ItemChildEpCostNew",
																										InstanceRestrictionType.ITEM_CHILD_EP_COST_NEW);
		
		/**
		 * The number of insanities a character has to have is restricted.
		 */
		public static final CreationRestrictionType					INSANITY					= new CreationRestrictionType("Insanity");
		
		/**
		 * The generation of a character is restricted.
		 */
		public static final CreationRestrictionType					GENERATION					= new CreationRestrictionType("Generation");
		
		private final String										mName;
		
		private final InstanceRestrictionType						mInstanceType;
		
		private CreationRestrictionType(final String aName)
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
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			CreationRestrictionType other = (CreationRestrictionType) obj;
			if (mName == null)
			{
				if (other.mName != null) return false;
			}
			else if ( !mName.equals(other.mName)) return false;
			return true;
		}
		
		/**
		 * @return the corresponding restriction type for non creation restrictions if existing.
		 */
		public InstanceRestrictionType getInstanceType()
		{
			return mInstanceType;
		}
		
		/**
		 * @return the restriction type name.
		 */
		public String getName()
		{
			return mName;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((mName == null) ? 0 : mName.hashCode());
			return result;
		}
		
		/**
		 * @return whether this restriction type can be transferred into a instance restriction type.
		 */
		public boolean isPersistent()
		{
			return mInstanceType != null;
		}
	}
	
	/**
	 * Adds the given condition to this restriction. This restriction is only active if all conditions are positive.
	 * 
	 * @param aCondition
	 *            The condition to add.
	 */
	public void addCondition(CreationCondition aCondition);
	
	/**
	 * Clears this restriction.
	 */
	public void clear();
	
	/**
	 * @return the instance restriction for this creation restriction.
	 */
	public InstanceRestriction createInstance();
	
	/**
	 * @return a set of all conditions this restriction has.
	 */
	public Set<CreationCondition> getConditions();
	
	/**
	 * @return the index of anything defined inside the restriction type.
	 */
	public int getIndex();
	
	/**
	 * @return the item name defined inside the restriction type.
	 */
	public String getItemName();
	
	/**
	 * @return the list of items defined inside the restriction type.
	 */
	public List<String> getItems();
	
	/**
	 * @return the maximum value defined inside the restriction type.
	 */
	public int getMaximum();
	
	/**
	 * @return the minimum value defined inside the restriction type.
	 */
	public int getMinimum();
	
	/**
	 * @return the current restrictionable parent, that is restricted by this restriction.
	 */
	public CreationRestrictionable getParent();
	
	/**
	 * @return the restriction type of this restriction.
	 */
	public CreationRestrictionType getType();
	
	/**
	 * @return the value defined inside the restriction type.
	 */
	public int getValue();
	
	/**
	 * @return whether this restriction gas any condition.
	 */
	public boolean hasConditions();
	
	/**
	 * Tests all conditions.
	 * 
	 * @param aController
	 *            The item controller.
	 * @return this restriction is active.
	 */
	public boolean isActive(ItemControllerCreation aController);
	
	/**
	 * @return whether this restriction is not allowed to be transferred into a instance restriction.
	 */
	public boolean isCreationRestriction();
	
	/**
	 * @param aValue
	 *            The value to test.
	 * @return whether the given value is inside the range of this restriction.
	 */
	public boolean isInRange(int aValue);
	
	/**
	 * @return whether this restriction can be transferred into a instance restriction.
	 */
	public boolean isPersistent();
	
	/**
	 * Sets the restrictionable parent of this restriction.
	 * 
	 * @param aParent
	 *            the restrictionable parent.
	 */
	public void setParent(CreationRestrictionable aParent);
	
	/**
	 * Updates this restriction.
	 */
	public void update();
}
