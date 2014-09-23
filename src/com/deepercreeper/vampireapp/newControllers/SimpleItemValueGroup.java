package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleItemValueGroup implements ItemValueGroup<SimpleItem>
{
	private final SimpleItemGroup						mGroup;
	
	private final List<SimpleItemValue>					mValues		= new ArrayList<SimpleItemValue>();
	
	private final HashMap<SimpleItem, SimpleItemValue>	mValueItems	= new HashMap<SimpleItem, SimpleItemValue>();
	
	public SimpleItemValueGroup(SimpleItemGroup aGroup)
	{
		mGroup = aGroup;
		for (SimpleItem item : mGroup.getItems())
		{
			addValue(item.createValue());
		}
	}
	
	private void addValue(SimpleItemValue aValue)
	{
		mValues.add(aValue);
		mValueItems.put(aValue.getItem(), aValue);
	}
	
	@Override
	public SimpleItemGroup getGroup()
	{
		return mGroup;
	}
	
	@Override
	public List<SimpleItemValue> getValues()
	{
		return mValues;
	}
	
	@Override
	public SimpleItemValue getValue(SimpleItem aItem)
	{
		return mValueItems.get(aItem);
	}
	
	@Override
	public SimpleItemValue getValue(String aName)
	{
		return getValue(getGroup().getItem(aName));
	}
}
