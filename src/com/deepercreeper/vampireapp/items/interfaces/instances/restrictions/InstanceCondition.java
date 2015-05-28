package com.deepercreeper.vampireapp.items.interfaces.instances.restrictions;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;

public interface InstanceCondition
{
	public abstract class InstanceConditionQuery
	{
		private static final Map<String, InstanceConditionQuery>	CONDITION_QUERIES			= new HashMap<String, InstanceConditionQuery>();
		
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
																										&& aCondition.isInRange(item.getValue()))
																								{
																									return true;
																								}
																								return false;
																							}
																						};
		
		public static final InstanceConditionQuery					NOT_ITEM_VALUE				= new InstanceConditionQuery("NotItemVale")
																						{
																							@Override
																							public boolean complied(
																									final ItemControllerInstance aController,
																									final InstanceCondition aCondition)
																							{
																								return !ITEM_VALUE.complied(aController, aCondition);
																							}
																						};
		
		public static final InstanceConditionQuery					HAS_ITEM					= new InstanceConditionQuery("HasItem")
																						{
																							
																							@Override
																							public boolean complied(
																									final ItemControllerInstance aController,
																									final InstanceCondition aCondition)
																							{
																								return aController.hasItem(aCondition.getItemName());
																							}
																						};
		
		public static final InstanceConditionQuery					NOT_HAS_ITEM				= new InstanceConditionQuery("NotHasItem")
																						{
																							@Override
																							public boolean complied(
																									final ItemControllerInstance aController,
																									final InstanceCondition aCondition)
																							{
																								return !HAS_ITEM.complied(aController, aCondition);
																							}
																						};
		
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
																								final ItemInstance child = item.getChildAt(aCondition
																										.getIndex());
																								return child.isValueItem()
																										&& aCondition.isInRange(child.getValue());
																							}
																						};
		
		public static final InstanceConditionQuery					NOT_ITEM_CHILD_VALUE_AT		= new InstanceConditionQuery("NotItemChildValueAt")
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
																											&& aCondition.isInRange(child.getValue()))
																									{
																										return true;
																									}
																								}
																								return false;
																							}
																						};
		
		public static final InstanceConditionQuery					NOT_ITEM_HAS_CHILD_VALUE	= new InstanceConditionQuery("NotItemHasChildValue")
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
		
		public static final InstanceConditionQuery					GROUP_HAS_CHILD_VALUE		= new InstanceConditionQuery("GroupHasChildValue")
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
		
		public static final InstanceConditionQuery					NOT_GROUP_HAS_CHILD_VALUE	= new InstanceConditionQuery("NotGroupHasChildValue")
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
		
		public InstanceConditionQuery(final String aName)
		{
			mName = aName;
			CONDITION_QUERIES.put(mName, this);
		}
		
		public static InstanceConditionQuery getQuery(final String aName)
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
