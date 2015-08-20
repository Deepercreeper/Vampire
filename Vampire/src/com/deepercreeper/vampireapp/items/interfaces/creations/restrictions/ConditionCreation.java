package com.deepercreeper.vampireapp.items.interfaces.creations.restrictions;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.ConditionInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.ConditionInstance.ConditionQueryInstance;

/**
 * Restrictions sometimes have conditions. these are represented by this interface.
 * 
 * @author vrl
 */
public interface ConditionCreation
{
	/**
	 * Each condition makes a query that tells whether something is positive of not.
	 * 
	 * @author vrl
	 */
	public abstract class ConditionQueryCreation
	{
		private static final Map<String, ConditionQueryCreation> CONDITION_QUERIES = new HashMap<String, ConditionQueryCreation>();
		
		/**
		 * Complies if the value of the item with name {@link ConditionCreation#getItemName()} has a value that approves {@link ConditionCreation#isInRange(int)}.
		 */
		public static final ConditionQueryCreation ITEM_VALUE = new ConditionQueryCreation("ItemValue", ConditionQueryInstance.ITEM_VALUE)
		{
			
			@Override
			public boolean complied(final ItemControllerCreation aController, final ConditionCreation aCondition)
			{
				final ItemCreation item = aController.getItem(aCondition.getItemName());
				if (item.isValueItem() && aCondition.isInRange(item.getValue()))
				{
					return true;
				}
				return false;
			}
		};
		
		/**
		 * Complies if {@link ConditionQueryCreation#ITEM_VALUE} does not comply.
		 */
		public static final ConditionQueryCreation NOT_ITEM_VALUE = new ConditionQueryCreation("NotItemVale", ConditionQueryInstance.NOT_ITEM_VALUE)
		{
			@Override
			public boolean complied(final ItemControllerCreation aController, final ConditionCreation aCondition)
			{
				return !ITEM_VALUE.complied(aController, aCondition);
			}
		};
		
		/**
		 * Complies if the given controller has an item with name {@link ConditionCreation#getItemName()}.
		 */
		public static final ConditionQueryCreation HAS_ITEM = new ConditionQueryCreation("HasItem", ConditionQueryInstance.HAS_ITEM)
		{
			
			@Override
			public boolean complied(final ItemControllerCreation aController, final ConditionCreation aCondition)
			{
				return aController.hasItem(aCondition.getItemName());
			}
		};
		
		/**
		 * Complies if {@link ConditionQueryCreation#HAS_ITEM} does not comply.
		 */
		public static final ConditionQueryCreation NOT_HAS_ITEM = new ConditionQueryCreation("NotHasItem", ConditionQueryInstance.NOT_HAS_ITEM)
		{
			@Override
			public boolean complied(final ItemControllerCreation aController, final ConditionCreation aCondition)
			{
				return !HAS_ITEM.complied(aController, aCondition);
			}
		};
		
		/**
		 * Complies if the item with name {@link ConditionCreation#getItemName()} has a child at index {@link ConditionCreation#getIndex()} and that child has a value that approves
		 * {@link ConditionCreation#isInRange(int)}.
		 */
		public static final ConditionQueryCreation ITEM_CHILD_VALUE_AT = new ConditionQueryCreation("ItemChildValueAt",
				ConditionQueryInstance.ITEM_CHILD_VALUE_AT)
		{
			@Override
			public boolean complied(final ItemControllerCreation aController, final ConditionCreation aCondition)
			{
				final ItemCreation item = aController.getItem(aCondition.getItemName());
				if ( !item.isParent())
				{
					return false;
				}
				if ( !item.hasChildAt(aCondition.getIndex()))
				{
					return false;
				}
				final ItemCreation child = item.getChildAt(aCondition.getIndex());
				return child.isValueItem() && aCondition.isInRange(child.getValue());
			}
		};
		
		/**
		 * Complies if {@link ConditionQueryCreation#ITEM_CHILD_VALUE_AT} does not comply.
		 */
		public static final ConditionQueryCreation NOT_ITEM_CHILD_VALUE_AT = new ConditionQueryCreation("NotItemChildValueAt",
				ConditionQueryInstance.NOT_ITEM_CHILD_VALUE_AT)
		{
			@Override
			public boolean complied(final ItemControllerCreation aController, final ConditionCreation aCondition)
			{
				return !ITEM_CHILD_VALUE_AT.complied(aController, aCondition);
			}
		};
		
		/**
		 * Complies if the item with name {@link ConditionCreation#getItemName()} has a child item with a value that approves {@link ConditionCreation#isInRange(int)}.
		 */
		public static final ConditionQueryCreation ITEM_HAS_CHILD_VALUE = new ConditionQueryCreation("ItemHasChildValue",
				ConditionQueryInstance.ITEM_HAS_CHILD_VALUE)
		{
			
			@Override
			public boolean complied(final ItemControllerCreation aController, final ConditionCreation aCondition)
			{
				final ItemCreation item = aController.getItem(aCondition.getItemName());
				if ( !item.isParent())
				{
					return false;
				}
				for (final ItemCreation child : item.getChildrenList())
				{
					if (child.isValueItem() && aCondition.isInRange(child.getValue()))
					{
						return true;
					}
				}
				return false;
			}
		};
		
		/**
		 * Complies if {@link ConditionQueryCreation#ITEM_HAS_CHILD_VALUE} does not comply.
		 */
		public static final ConditionQueryCreation NOT_ITEM_HAS_CHILD_VALUE = new ConditionQueryCreation("NotItemHasChildValue",
				ConditionQueryInstance.NOT_ITEM_HAS_CHILD_VALUE)
		{
			@Override
			public boolean complied(final ItemControllerCreation aController, final ConditionCreation aCondition)
			{
				return !ITEM_HAS_CHILD_VALUE.complied(aController, aCondition);
			}
		};
		
		/**
		 * Complies if the group with name {@link ConditionCreation#getItemName()} has a child item with a value that approves {@link ConditionCreation#isInRange(int)}.
		 */
		public static final ConditionQueryCreation GROUP_HAS_CHILD_VALUE = new ConditionQueryCreation("GroupHasChildValue",
				ConditionQueryInstance.GROUP_HAS_CHILD_VALUE)
		{
			
			@Override
			public boolean complied(final ItemControllerCreation aController, final ConditionCreation aCondition)
			{
				final ItemGroupCreation group = aController.getGroup(aCondition.getItemName());
				for (final ItemCreation item : group.getItemsList())
				{
					if (item.isValueItem() && aCondition.isInRange(item.getValue()))
					{
						return true;
					}
				}
				return false;
			}
		};
		
		/**
		 * Complies if {@link ConditionQueryCreation#GROUP_HAS_CHILD_VALUE} does not comply.
		 */
		public static final ConditionQueryCreation NOT_GROUP_HAS_CHILD_VALUE = new ConditionQueryCreation("NotGroupHasChildValue",
				ConditionQueryInstance.NOT_GROUP_HAS_CHILD_VALUE)
		{
			@Override
			public boolean complied(final ItemControllerCreation aController, final ConditionCreation aCondition)
			{
				return !GROUP_HAS_CHILD_VALUE.complied(aController, aCondition);
			}
		};
		
		private final String mName;
		
		private final ConditionQueryInstance mInstanceQuery;
		
		private ConditionQueryCreation(final String aName, ConditionQueryInstance aInstanceQuery)
		{
			mName = aName;
			mInstanceQuery = aInstanceQuery;
			CONDITION_QUERIES.put(mName, this);
		}
		
		/**
		 * @param aController
		 *            The item controller.
		 * @param aCondition
		 *            The condition that starts this query.
		 * @return {@code true} if this query complies and {@code false} if not.
		 */
		public abstract boolean complied(ItemControllerCreation aController, ConditionCreation aCondition);
		
		/**
		 * @return the instance query.
		 */
		public ConditionQueryInstance getInstanceQuery()
		{
			return mInstanceQuery;
		}
		
		/**
		 * @param aName
		 *            The query name.
		 * @return the query with the given name.
		 */
		public static ConditionQueryCreation getQuery(final String aName)
		{
			return CONDITION_QUERIES.get(aName);
		}
	}
	
	/**
	 * @param aController
	 *            The item controller.
	 * @return {@code true} if the query of this condition complies and {@code false} if not.
	 */
	public boolean complied(ItemControllerCreation aController);
	
	/**
	 * @return a condition instance that represents this condition or {@code null} if this condition is not persistent.
	 */
	public ConditionInstance createInstance();
	
	/**
	 * @return the item index defined by this condition.
	 */
	public int getIndex();
	
	/**
	 * @return the item name defined by this condition.
	 */
	public String getItemName();
	
	/**
	 * @return whether this condition will be ported into a instance condition.
	 */
	public boolean isPersistent();
	
	/**
	 * @param aValue
	 *            The value.
	 * @return whether the given value is inside the range defined by this condition.
	 */
	public boolean isInRange(int aValue);
}
