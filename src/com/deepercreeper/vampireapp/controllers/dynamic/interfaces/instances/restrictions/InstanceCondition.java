package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.restrictions;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.ItemInstance;

public interface InstanceCondition
{
	public abstract class ConditionQuery
	{
		private static final Map<String, ConditionQuery>	CONDITION_QUERIES			= new HashMap<String, ConditionQuery>();
		
		public static final ConditionQuery					ITEM_VALUE					= new ConditionQuery("ItemValue")
																						{
																							
																							@Override
																							public boolean complied(
																									final ItemControllerInstance aController,
																									final InstanceCondition aCondition)
																							{
																								final ItemInstance item = aController
																										.getItem(aCondition.getItemName());
																								if (item.isValueItem()
																										&& aCondition.isInRange(item.getValue()))
																								{
																									return true;
																								}
																								return false;
																							}
																						};
		
		public static final ConditionQuery					NOT_ITEM_VALUE				= new ConditionQuery("NotItemVale")
																						{
																							@Override
																							public boolean complied(
																									final ItemControllerInstance aController,
																									final InstanceCondition aCondition)
																							{
																								return !ITEM_VALUE.complied(aController, aCondition);
																							}
																						};
		
		public static final ConditionQuery					HAS_ITEM					= new ConditionQuery("HasItem")
																						{
																							
																							@Override
																							public boolean complied(
																									final ItemControllerInstance aController,
																									final InstanceCondition aCondition)
																							{
																								return aController.hasItem(aCondition.getItemName());
																							}
																						};
		
		public static final ConditionQuery					NOT_HAS_ITEM				= new ConditionQuery("NotHasItem")
																						{
																							@Override
																							public boolean complied(
																									final ItemControllerInstance aController,
																									final InstanceCondition aCondition)
																							{
																								return !HAS_ITEM.complied(aController, aCondition);
																							}
																						};
		
		public static final ConditionQuery					ITEM_CHILD_VALUE_AT			= new ConditionQuery("ItemChildValueAt")
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
																								final ItemInstance child = item.getChildAt(aCondition
																										.getIndex());
																								return child.isValueItem()
																										&& aCondition.isInRange(child.getValue());
																							}
																						};
		
		public static final ConditionQuery					NOT_ITEM_CHILD_VALUE_AT		= new ConditionQuery("NotItemChildValueAt")
																						{
																							@Override
																							public boolean complied(
																									final ItemControllerInstance aController,
																									final InstanceCondition aCondition)
																							{
																								return !ITEM_CHILD_VALUE_AT.complied(aController,
																										aCondition);
																							}
																						};
		
		public static final ConditionQuery					ITEM_HAS_CHILD_VALUE		= new ConditionQuery("ItemHasChildValue")
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
																											&& aCondition.isInRange(child.getValue()))
																									{
																										return true;
																									}
																								}
																								return false;
																							}
																						};
		
		public static final ConditionQuery					NOT_ITEM_HAS_CHILD_VALUE	= new ConditionQuery("NotItemHasChildValue")
																						{
																							@Override
																							public boolean complied(
																									final ItemControllerInstance aController,
																									final InstanceCondition aCondition)
																							{
																								return !ITEM_HAS_CHILD_VALUE.complied(aController,
																										aCondition);
																							}
																						};
		
		public static final ConditionQuery					GROUP_HAS_CHILD_VALUE		= new ConditionQuery("GroupHasChildValue")
																						{
																							
																							@Override
																							public boolean complied(
																									final ItemControllerInstance aController,
																									final InstanceCondition aCondition)
																							{
																								final ItemGroupInstance group = aController
																										.getGroup(aCondition.getItemName());
																								for (final ItemInstance item : group.getItemsList())
																								{
																									if (item.isValueItem()
																											&& aCondition.isInRange(item.getValue()))
																									{
																										return true;
																									}
																								}
																								return false;
																							}
																						};
		
		public static final ConditionQuery					NOT_GROUP_HAS_CHILD_VALUE	= new ConditionQuery("NotGroupHasChildValue")
																						{
																							@Override
																							public boolean complied(
																									final ItemControllerInstance aController,
																									final InstanceCondition aCondition)
																							{
																								return !GROUP_HAS_CHILD_VALUE.complied(aController,
																										aCondition);
																							}
																						};
		
		private final String								mName;
		
		public ConditionQuery(final String aName)
		{
			mName = aName;
			CONDITION_QUERIES.put(mName, this);
		}
		
		public static ConditionQuery getQuery(final String aName)
		{
			return CONDITION_QUERIES.get(aName);
		}
		
		public abstract boolean complied(ItemControllerInstance aController, InstanceCondition aCondition);
	}
	
	public int getIndex();
	
	public String getItemName();
	
	public boolean isInRange(int aValue);
	
	public boolean complied(ItemControllerInstance aController);
}
