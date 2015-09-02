package com.deepercreeper.vampireapp.items.interfaces.instances.restrictions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.items.interfaces.Nameable;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.mechanics.Duration.DurationListener;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;

/**
 * A character can have several restrictions. They can restrict items, groups or something else.
 * 
 * @author vrl
 */
public interface RestrictionInstance extends Saveable, TimeListener, DurationListener, Viewable
{
	/**
	 * Each restriction has a type that defines, what is restricted.
	 * 
	 * @author vrl
	 */
	public static class RestrictionInstanceType
	{
		private static final Map<String, RestrictionInstanceType> RESTRICTION_TYPES = new HashMap<String, RestrictionInstanceType>();
		
		/**
		 * The value of a specific item is restricted.
		 */
		public static final RestrictionInstanceType ITEM_VALUE = new RestrictionInstanceType("ItemValue", R.string.item_value, true, true, false,
				false);
				
		/**
		 * The normal experience cost for a item is restricted.
		 */
		public static final RestrictionInstanceType ITEM_EP_COST = new RestrictionInstanceType("ItemEpCost", R.string.item_ep_cost, true, false,
				false, true);
				
		/**
		 * The additional experience depending on the current value of the item is restricted.
		 */
		public static final RestrictionInstanceType ITEM_EP_COST_MULTI = new RestrictionInstanceType("ItemEpCostMulti", R.string.item_ep_cost_multi,
				true, false, false, true);
				
		/**
		 * The experience cost for the first point of an item is restricted.
		 */
		public static final RestrictionInstanceType ITEM_EP_COST_NEW = new RestrictionInstanceType("ItemEpCostNew", R.string.item_ep_cost_new, true,
				false, false, true);
				
		/**
		 * The value depending experience cost of the child at the given position is restricted.
		 */
		public static final RestrictionInstanceType ITEM_CHILD_EP_COST_MULTI = new RestrictionInstanceType("ItemChildEpCostMulti",
				R.string.item_child_ep_cost_multi, true, false, true, true);
				
		/**
		 * The experience cost for the first point of the child item at the given position is restricted.
		 */
		public static final RestrictionInstanceType ITEM_CHILD_EP_COST_NEW = new RestrictionInstanceType("ItemChildEpCostNew",
				R.string.item_child_ep_cost_new, true, false, true, true);
				
		private final String mName;
		
		private final int mId;
		
		private final boolean mHasItemName;
		
		private final boolean mHasRange;
		
		private final boolean mHasIndex;
		
		private final boolean mHasValue;
		
		private RestrictionInstanceType(final String aName, final int aId, final boolean aHasItemName, final boolean aHasRange,
				final boolean aHasIndex, final boolean aHasValue)
		{
			mName = aName;
			mId = aId;
			mHasItemName = aHasItemName;
			mHasRange = aHasRange;
			mHasIndex = aHasIndex;
			mHasValue = aHasValue;
			RESTRICTION_TYPES.put(getName(), this);
		}
		
		@Override
		public boolean equals(final Object aObj)
		{
			if (aObj instanceof RestrictionInstanceType)
			{
				RestrictionInstanceType type = (RestrictionInstanceType) aObj;
				return getName().equals(type.getName());
			}
			return false;
		}
		
		/**
		 * @return whether this restriction needs a index value.
		 */
		public boolean hasIndex()
		{
			return mHasIndex;
		}
		
		/**
		 * @return whether this restriction needs a item name value.
		 */
		public boolean hasItemName()
		{
			return mHasItemName;
		}
		
		/**
		 * @return whether this restriction needs a range value.
		 */
		public boolean hasRange()
		{
			return mHasRange;
		}
		
		/**
		 * @return whether this restriction needs a value value.
		 */
		public boolean hasValue()
		{
			return mHasValue;
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
		 * @param aType
		 *            The type nameable.
		 * @return the type of the given nameable.
		 */
		public static RestrictionInstanceType getTypeOf(final Nameable aType)
		{
			return get(aType.getName());
		}
		
		/**
		 * @param aName
		 *            The restriction type name.
		 * @return the restriction type with the given name.
		 */
		public static RestrictionInstanceType get(final String aName)
		{
			return RESTRICTION_TYPES.get(aName);
		}
		
		/**
		 * @param aContext
		 *            The underlying context.
		 * @return the duration type name.
		 */
		public String getName(final Context aContext)
		{
			if (mId == -1)
			{
				return getName();
			}
			return aContext.getString(mId);
		}
		
		/**
		 * @param aContext
		 *            The underlying context.
		 * @return a list of nameables for each restriction instance type.
		 */
		public static List<Nameable> getTypesList(final Context aContext)
		{
			final List<Nameable> list = new ArrayList<Nameable>();
			for (final RestrictionInstanceType type : RESTRICTION_TYPES.values())
			{
				list.add(new Named(type.getName())
				{
					@Override
					public String getDisplayName()
					{
						return type.getName(aContext);
					}
				});
			}
			Collections.sort(list);
			return list;
		}
	}
	
	/**
	 * Adds the given condition to this restriction. This restriction is only active if all conditions are positive.
	 * 
	 * @param aCondition
	 *            The condition to add.
	 */
	public void addCondition(ConditionInstance aCondition);
	
	/**
	 * Clears this restriction.
	 */
	public void clear();
	
	/**
	 * @return a set of all conditions this restriction has.
	 */
	public Set<ConditionInstance> getConditions();
	
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
	public RestrictionableInstance getParent();
	
	/**
	 * @return the restriction type of this restriction.
	 */
	public RestrictionInstanceType getType();
	
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
	public void setParent(RestrictionableInstance aParent);
	
	/**
	 * Updates this restrictions user interface.
	 */
	@Override
	public void updateUI();
}
