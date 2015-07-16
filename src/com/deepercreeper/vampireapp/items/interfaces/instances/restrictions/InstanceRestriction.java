package com.deepercreeper.vampireapp.items.interfaces.instances.restrictions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.mechanics.Duration.DurationListener;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.mechanics.TimeListener;

/**
 * A character can have several restrictions. They can restrict items, groups or something else.
 * 
 * @author vrl
 */
public interface InstanceRestriction extends Saveable, TimeListener, DurationListener
{
	/**
	 * Each restriction has a type that defines, what is restricted.
	 * 
	 * @author vrl
	 */
	public static class InstanceRestrictionType
	{
		/**
		 * @param aName
		 *            The restriction type name.
		 * @return the restriction type with the given name.
		 */
		public static InstanceRestrictionType get(final String aName)
		{
			return RESTRICTION_TYPES.get(aName);
		}
		
		private static final Map<String, InstanceRestrictionType>	RESTRICTION_TYPES			= new HashMap<String, InstanceRestrictionType>();
		
		/**
		 * The value of a specific item is restricted.
		 */
		public static final InstanceRestrictionType					ITEM_VALUE					= new InstanceRestrictionType("ItemValue");
		
		/**
		 * The normal experience cost for a item is restricted.
		 */
		public static final InstanceRestrictionType					ITEM_EP_COST				= new InstanceRestrictionType("ItemEpCost");
		
		/**
		 * The additional experience depending on the current value of the item is restricted.
		 */
		public static final InstanceRestrictionType					ITEM_EP_COST_MULTI			= new InstanceRestrictionType("ItemEpCostMulti");
		
		/**
		 * The experience cost for the first point of an item is restricted.
		 */
		public static final InstanceRestrictionType					ITEM_EP_COST_NEW			= new InstanceRestrictionType("ItemEpCostNew");
		
		/**
		 * The value depending experience cost of the child at the given position is restricted.
		 */
		public static final InstanceRestrictionType					ITEM_CHILD_EP_COST_MULTI_AT	= new InstanceRestrictionType(
																										"ItemChildEpCostMultiAt");
		
		/**
		 * The experience cost for the first point of the child item at the given position is restricted.
		 */
		public static final InstanceRestrictionType					ITEM_CHILD_EP_COST_NEW		= new InstanceRestrictionType("ItemChildEpCostNew");
		
		private final String										mName;
		
		private InstanceRestrictionType(final String aName)
		{
			mName = aName;
			RESTRICTION_TYPES.put(getName(), this);
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((mName == null) ? 0 : mName.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			InstanceRestrictionType other = (InstanceRestrictionType) obj;
			if (mName == null)
			{
				if (other.mName != null) return false;
			}
			else if ( !mName.equals(other.mName)) return false;
			return true;
		}
		
		/**
		 * @return the restriction type name.
		 */
		public String getName()
		{
			return mName;
		}
	}
	
	/**
	 * Adds the given condition to this restriction. This restriction is only active if all conditions are positive.
	 * 
	 * @param aCondition
	 *            The condition to add.
	 */
	public void addCondition(InstanceCondition aCondition);
	
	/**
	 * Clears this restriction.
	 */
	public void clear();
	
	/**
	 * @return a set of all conditions this restriction has.
	 */
	public Set<InstanceCondition> getConditions();
	
	/**
	 * @return he duration of this restriction.
	 */
	public Duration getDuration();
	
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
	public InstanceRestrictionable getParent();
	
	/**
	 * @return the restriction type of this restriction.
	 */
	public InstanceRestrictionType getType();
	
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
	public boolean isActive(ItemControllerInstance aController);
	
	/**
	 * @param aValue
	 *            The value to test.
	 * @return whether the given value is inside the range of this restriction.
	 */
	public boolean isInRange(int aValue);
	
	/**
	 * Sets the restrictionable parent of this restriction.
	 * 
	 * @param aParent
	 *            the restrictionable parent.
	 */
	public void setParent(InstanceRestrictionable aParent);
	
	/**
	 * Updates this restriction.
	 */
	public void update();
}
