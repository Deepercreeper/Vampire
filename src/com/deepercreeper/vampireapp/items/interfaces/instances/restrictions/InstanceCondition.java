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
public interface InstanceCondition
{
	/**
	 * Each condition makes a query that tells whether something is positive of not.
	 * 
	 * @author vrl
	 */
	public abstract class InstanceConditionQuery
	{
		/**
		 * @param aName
		 *            The query name.
		 * @return the query with the given name.
		 */
		public static InstanceConditionQuery getQuery(final String aName)
		{
			return CONDITION_QUERIES.get(aName);
		}
		
		private static final Map<String, InstanceConditionQuery>	CONDITION_QUERIES			= new HashMap<String, InstanceConditionQuery>();
		
		/**
		 * Complies if the value of the item with name {@link InstanceCondition#getItemName()} has a value that approves {@link InstanceCondition#isInRange(int)}.
		 */
		public static final InstanceConditionQuery					ITEM_VALUE					= new InstanceConditionQuery("ItemValue")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final InstanceCondition aCondition)
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
		 * Complies if {@link InstanceConditionQuery#ITEM_VALUE} does not comply.
		 */
		public static final InstanceConditionQuery					NOT_ITEM_VALUE				= new InstanceConditionQuery("NotItemVale")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final InstanceCondition aCondition)
																									{
																										return !ITEM_VALUE.complied(aController,
																												aCondition);
																									}
																								};
		
		/**
		 * Complies if the given controller has an item with name {@link InstanceCondition#getItemName()}.
		 */
		public static final InstanceConditionQuery					HAS_ITEM					= new InstanceConditionQuery("HasItem")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final InstanceCondition aCondition)
																									{
																										return aController.hasItem(aCondition
																												.getItemName());
																									}
																								};
		
		/**
		 * Complies if {@link InstanceConditionQuery#HAS_ITEM} does not comply.
		 */
		public static final InstanceConditionQuery					NOT_HAS_ITEM				= new InstanceConditionQuery("NotHasItem")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final InstanceCondition aCondition)
																									{
																										return !HAS_ITEM.complied(aController,
																												aCondition);
																									}
																								};
		
		/**
		 * Complies if the item with name {@link InstanceCondition#getItemName()} has a child at index {@link InstanceCondition#getIndex()} and that child has a value that approves
		 * {@link InstanceCondition#isInRange(int)}.
		 */
		public static final InstanceConditionQuery					ITEM_CHILD_VALUE_AT			= new InstanceConditionQuery("ItemChildValueAt")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final InstanceCondition aCondition)
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
		 * Complies if {@link InstanceConditionQuery#ITEM_CHILD_VALUE_AT} does not comply.
		 */
		public static final InstanceConditionQuery					NOT_ITEM_CHILD_VALUE_AT		= new InstanceConditionQuery("NotItemChildValueAt")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final InstanceCondition aCondition)
																									{
																										return !ITEM_CHILD_VALUE_AT.complied(
																												aController, aCondition);
																									}
																								};
		
		/**
		 * Complies if the item with name {@link InstanceCondition#getItemName()} has a child item with a value that approves {@link InstanceCondition#isInRange(int)}.
		 */
		public static final InstanceConditionQuery					ITEM_HAS_CHILD_VALUE		= new InstanceConditionQuery("ItemHasChildValue")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final InstanceCondition aCondition)
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
		 * Complies if {@link InstanceConditionQuery#ITEM_HAS_CHILD_VALUE} does not comply.
		 */
		public static final InstanceConditionQuery					NOT_ITEM_HAS_CHILD_VALUE	= new InstanceConditionQuery("NotItemHasChildValue")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final InstanceCondition aCondition)
																									{
																										return !ITEM_HAS_CHILD_VALUE.complied(
																												aController, aCondition);
																									}
																								};
		
		/**
		 * Complies if the group with name {@link InstanceCondition#getItemName()} has a child item with a value that approves {@link InstanceCondition#isInRange(int)}.
		 */
		public static final InstanceConditionQuery					GROUP_HAS_CHILD_VALUE		= new InstanceConditionQuery("GroupHasChildValue")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final InstanceCondition aCondition)
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
		 * Complies if {@link InstanceConditionQuery#GROUP_HAS_CHILD_VALUE} does not comply.
		 */
		public static final InstanceConditionQuery					NOT_GROUP_HAS_CHILD_VALUE	= new InstanceConditionQuery("NotGroupHasChildValue")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerInstance aController,
																											final InstanceCondition aCondition)
																									{
																										return !GROUP_HAS_CHILD_VALUE.complied(
																												aController, aCondition);
																									}
																								};
		
		private final String										mName;
		
		private InstanceConditionQuery(final String aName)
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
		public abstract boolean complied(ItemControllerInstance aController, InstanceCondition aCondition);
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
