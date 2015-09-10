package com.deepercreeper.vampireapp.items.interfaces.instances.restrictions;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;

/**
 * Restrictions sometimes have conditions. these are represented by this interface.
 * 
 * @author vrl
 */
public interface ConditionInstance
{
	/**
	 * Each condition makes a query that tells whether something is positive of not.
	 * 
	 * @author vrl
	 */
	public abstract class ConditionQueryInstance
	{
		private static final Map<String, ConditionQueryInstance>	CONDITION_QUERIES			= new HashMap<String, ConditionQueryInstance>();
		
		/**
		 * Complies if the value of the item with name {@link ConditionInstance#getItemName()} has a value that approves {@link ConditionInstance#isInRange(int)}.
		 */
		public static final ConditionQueryInstance					ITEM_VALUE					= new ConditionQueryInstance("ItemValue")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final ConditionInstance aCondition)
																									{
																										final ItemInstance item = aController
																												.getItem(aCondition.getItemName());
																										if (item.isValueItem()
																												&& aCondition.isInRange(item
																														.getValue()))
																										{
																											return true;
																										}
																										return false;
																									}
																								};
		
		/**
		 * Complies if {@link ConditionQueryInstance#ITEM_VALUE} does not comply.
		 */
		public static final ConditionQueryInstance					NOT_ITEM_VALUE				= new ConditionQueryInstance("NotItemVale")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final ConditionInstance aCondition)
																									{
																										return !ITEM_VALUE.complied(aController,
																												aCondition);
																									}
																								};
		
		/**
		 * Complies if the given controller has an item with name {@link ConditionInstance#getItemName()}.
		 */
		public static final ConditionQueryInstance					HAS_ITEM					= new ConditionQueryInstance("HasItem")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final ConditionInstance aCondition)
																									{
																										return aController.hasItem(aCondition
																												.getItemName());
																									}
																								};
		
		/**
		 * Complies if {@link ConditionQueryInstance#HAS_ITEM} does not comply.
		 */
		public static final ConditionQueryInstance					NOT_HAS_ITEM				= new ConditionQueryInstance("NotHasItem")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final ConditionInstance aCondition)
																									{
																										return !HAS_ITEM.complied(aController,
																												aCondition);
																									}
																								};
		
		/**
		 * Complies if the item with name {@link ConditionInstance#getItemName()} has a child at index {@link ConditionInstance#getIndex()} and that child has a value that approves
		 * {@link ConditionInstance#isInRange(int)}.
		 */
		public static final ConditionQueryInstance					ITEM_CHILD_VALUE			= new ConditionQueryInstance("ItemChildValueAt")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final ConditionInstance aCondition)
																									{
																										final ItemInstance item = aController
																												.getItem(aCondition.getItemName());
																										if ( !item.isParent())
																										{
																											return false;
																										}
																										if ( !item.hasChildAt(aCondition.getIndex()))
																										{
																											return false;
																										}
																										final ItemInstance child = item
																												.getChildAt(aCondition.getIndex());
																										return child.isValueItem()
																												&& aCondition.isInRange(child
																														.getValue());
																									}
																								};
		
		/**
		 * Complies if {@link ConditionQueryInstance#ITEM_CHILD_VALUE} does not comply.
		 */
		public static final ConditionQueryInstance					NOT_ITEM_CHILD_VALUE_		= new ConditionQueryInstance("NotItemChildValueAt")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final ConditionInstance aCondition)
																									{
																										return !ITEM_CHILD_VALUE.complied(
																												aController, aCondition);
																									}
																								};
		
		/**
		 * Complies if the item with name {@link ConditionInstance#getItemName()} has a child item with a value that approves {@link ConditionInstance#isInRange(int)}.
		 */
		public static final ConditionQueryInstance					ITEM_HAS_CHILD_VALUE		= new ConditionQueryInstance("ItemHasChildValue")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final ConditionInstance aCondition)
																									{
																										final ItemInstance item = aController
																												.getItem(aCondition.getItemName());
																										if ( !item.isParent())
																										{
																											return false;
																										}
																										for (final ItemInstance child : item
																												.getChildrenList())
																										{
																											if (child.isValueItem()
																													&& aCondition.isInRange(child
																															.getValue()))
																											{
																												return true;
																											}
																										}
																										return false;
																									}
																								};
		
		/**
		 * Complies if {@link ConditionQueryInstance#ITEM_HAS_CHILD_VALUE} does not comply.
		 */
		public static final ConditionQueryInstance					NOT_ITEM_HAS_CHILD_VALUE	= new ConditionQueryInstance("NotItemHasChildValue")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final ConditionInstance aCondition)
																									{
																										return !ITEM_HAS_CHILD_VALUE.complied(
																												aController, aCondition);
																									}
																								};
		
		/**
		 * Complies if the group with name {@link ConditionInstance#getItemName()} has a child item with a value that approves {@link ConditionInstance#isInRange(int)}.
		 */
		public static final ConditionQueryInstance					GROUP_HAS_CHILD_VALUE		= new ConditionQueryInstance("GroupHasChildValue")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final ConditionInstance aCondition)
																									{
																										final ItemGroupInstance group = aController
																												.getGroup(aCondition.getItemName());
																										for (final ItemInstance item : group
																												.getItemsList())
																										{
																											if (item.isValueItem()
																													&& aCondition.isInRange(item
																															.getValue()))
																											{
																												return true;
																											}
																										}
																										return false;
																									}
																								};
		
		/**
		 * Complies if {@link ConditionQueryInstance#GROUP_HAS_CHILD_VALUE} does not comply.
		 */
		public static final ConditionQueryInstance					NOT_GROUP_HAS_CHILD_VALUE	= new ConditionQueryInstance("NotGroupHasChildValue")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final ConditionInstance aCondition)
																									{
																										return !GROUP_HAS_CHILD_VALUE.complied(
																												aController, aCondition);
																									}
																								};
		
		private final String										mName;
		
		private ConditionQueryInstance(final String aName)
		{
			mName = aName;
			CONDITION_QUERIES.put(mName, this);
		}
		
		/**
		 * @param aController
		 *            The item controller.
		 * @param aCondition
		 *            The condition that starts this query.
		 * @return {@code true} if this query complies and {@code false} if not.
		 */
		public abstract boolean complied(ItemControllerInstance aController, ConditionInstance aCondition);
		
		/**
		 * @param aName
		 *            The query name.
		 * @return the query with the given name.
		 */
		public static ConditionQueryInstance getQuery(final String aName)
		{
			return CONDITION_QUERIES.get(aName);
		}
	}
	
	/**
	 * @param aController
	 *            The item controller.
	 * @return {@code true} if the query of this condition complies and {@code false} if not.
	 */
	public boolean complied(ItemControllerInstance aController);
	
	/**
	 * @return the item index defined by this condition.
	 */
	public int getIndex();
	
	/**
	 * @return the item name defined by this condition.
	 */
	public String getItemName();
	
	/**
	 * @param aValue
	 *            The value.
	 * @return whether the given value is inside the range defined by this condition.
	 */
	public boolean isInRange(int aValue);
}
