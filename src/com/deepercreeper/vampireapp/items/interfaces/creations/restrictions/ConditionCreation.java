package com.deepercreeper.vampireapp.items.interfaces.creations.restrictions;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;

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
	public abstract class CreationConditionQuery
	{
		/**
		 * @param aName
		 *            The query name.
		 * @return the query with the given name.
		 */
		public static CreationConditionQuery getQuery(final String aName)
		{
			return CONDITION_QUERIES.get(aName);
		}
		
		private static final Map<String, CreationConditionQuery>	CONDITION_QUERIES			= new HashMap<String, CreationConditionQuery>();
		
		/**
		 * Complies if the value of the item with name {@link ConditionCreation#getItemName()} has a value that approves {@link ConditionCreation#isInRange(int)}.
		 */
		public static final CreationConditionQuery					ITEM_VALUE					= new CreationConditionQuery("ItemValue")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerCreation aController,
																											final ConditionCreation aCondition)
																									{
																										final ItemCreation item = aController
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
		 * Complies if {@link CreationConditionQuery#ITEM_VALUE} does not comply.
		 */
		public static final CreationConditionQuery					NOT_ITEM_VALUE				= new CreationConditionQuery("NotItemVale")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerCreation aController,
																											final ConditionCreation aCondition)
																									{
																										return !ITEM_VALUE.complied(aController,
																												aCondition);
																									}
																								};
		
		/**
		 * Complies if the given controller has an item with name {@link ConditionCreation#getItemName()}.
		 */
		public static final CreationConditionQuery					HAS_ITEM					= new CreationConditionQuery("HasItem")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerCreation aController,
																											final ConditionCreation aCondition)
																									{
																										return aController.hasItem(aCondition
																												.getItemName());
																									}
																								};
		
		/**
		 * Complies if {@link CreationConditionQuery#HAS_ITEM} does not comply.
		 */
		public static final CreationConditionQuery					NOT_HAS_ITEM				= new CreationConditionQuery("NotHasItem")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerCreation aController,
																											final ConditionCreation aCondition)
																									{
																										return !HAS_ITEM.complied(aController,
																												aCondition);
																									}
																								};
		
		/**
		 * Complies if the item with name {@link ConditionCreation#getItemName()} has a child at index {@link ConditionCreation#getIndex()} and that child has a value that approves
		 * {@link ConditionCreation#isInRange(int)}.
		 */
		public static final CreationConditionQuery					ITEM_CHILD_VALUE_AT			= new CreationConditionQuery("ItemChildValueAt")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerCreation aController,
																											final ConditionCreation aCondition)
																									{
																										final ItemCreation item = aController
																												.getItem(aCondition.getItemName());
																										if ( !item.isParent())
																										{
																											return false;
																										}
																										if ( !item.hasChildAt(aCondition.getIndex()))
																										{
																											return false;
																										}
																										final ItemCreation child = item
																												.getChildAt(aCondition.getIndex());
																										return child.isValueItem()
																												&& aCondition.isInRange(child
																														.getValue());
																									}
																								};
		
		/**
		 * Complies if {@link CreationConditionQuery#ITEM_CHILD_VALUE_AT} does not comply.
		 */
		public static final CreationConditionQuery					NOT_ITEM_CHILD_VALUE_AT		= new CreationConditionQuery("NotItemChildValueAt")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerCreation aController,
																											final ConditionCreation aCondition)
																									{
																										return !ITEM_CHILD_VALUE_AT.complied(
																												aController, aCondition);
																									}
																								};
		
		/**
		 * Complies if the item with name {@link ConditionCreation#getItemName()} has a child item with a value that approves {@link ConditionCreation#isInRange(int)}.
		 */
		public static final CreationConditionQuery					ITEM_HAS_CHILD_VALUE		= new CreationConditionQuery("ItemHasChildValue")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerCreation aController,
																											final ConditionCreation aCondition)
																									{
																										final ItemCreation item = aController
																												.getItem(aCondition.getItemName());
																										if ( !item.isParent())
																										{
																											return false;
																										}
																										for (final ItemCreation child : item
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
		 * Complies if {@link CreationConditionQuery#ITEM_HAS_CHILD_VALUE} does not comply.
		 */
		public static final CreationConditionQuery					NOT_ITEM_HAS_CHILD_VALUE	= new CreationConditionQuery("NotItemHasChildValue")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerCreation aController,
																											final ConditionCreation aCondition)
																									{
																										return !ITEM_HAS_CHILD_VALUE.complied(
																												aController, aCondition);
																									}
																								};
		
		/**
		 * Complies if the group with name {@link ConditionCreation#getItemName()} has a child item with a value that approves {@link ConditionCreation#isInRange(int)}.
		 */
		public static final CreationConditionQuery					GROUP_HAS_CHILD_VALUE		= new CreationConditionQuery("GroupHasChildValue")
																								{
																									
																									@Override
																									public boolean complied(
																											final ItemControllerCreation aController,
																											final ConditionCreation aCondition)
																									{
																										final ItemGroupCreation group = aController
																												.getGroup(aCondition.getItemName());
																										for (final ItemCreation item : group
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
		 * Complies if {@link CreationConditionQuery#GROUP_HAS_CHILD_VALUE} does not comply.
		 */
		public static final CreationConditionQuery					NOT_GROUP_HAS_CHILD_VALUE	= new CreationConditionQuery("NotGroupHasChildValue")
																								{
																									@Override
																									public boolean complied(
																											final ItemControllerCreation aController,
																											final ConditionCreation aCondition)
																									{
																										return !GROUP_HAS_CHILD_VALUE.complied(
																												aController, aCondition);
																									}
																								};
		
		private final String										mName;
		
		private CreationConditionQuery(final String aName)
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
		public abstract boolean complied(ItemControllerCreation aController, ConditionCreation aCondition);
	}
	
	/**
	 * @param aController
	 *            The item controller.
	 * @return {@code true} if the query of this condition complies and {@code false} if not.
	 */
	public boolean complied(ItemControllerCreation aController);
	
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
