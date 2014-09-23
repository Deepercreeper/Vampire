package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PropertyItemValueGroup implements ItemValueGroup<PropertyItem>, VariableItemValueGroup<PropertyItem, PropertyItemValue>
{
	private final PropertyItemGroup							mGroup;
	
	private final List<PropertyItemValue>					mValues		= new ArrayList<PropertyItemValue>();
	
	private final HashMap<PropertyItem, PropertyItemValue>	mValueItems	= new HashMap<PropertyItem, PropertyItemValue>();
	
	public PropertyItemValueGroup(final PropertyItemGroup aGroup)
	{
		mGroup = aGroup;
	}
	
	@Override
	public void addValue(final PropertyItemValue aValue)
	{
		mValues.add(aValue);
		mValueItems.put(aValue.getItem(), aValue);
		Collections.sort(mValues, PropertyItemValue.getComparator());
	}
	
	@Override
	public void addValue(final PropertyItem aItem)
	{
		addValue(aItem.createValue());
	}
	
	@Override
	public void addValue(final String aName)
	{
		addValue(getGroup().getItem(aName));
	}
	
	@Override
	public void removeValue(final PropertyItemValue aValue)
	{
		mValues.remove(aValue);
		mValueItems.remove(aValue.getItem());
	}
	
	@Override
	public void removeValue(final PropertyItem aItem)
	{
		removeValue(aItem.createValue());
	}
	
	@Override
	public void removeValue(final String aName)
	{
		removeValue(getGroup().getItem(aName));
	}
	
	@Override
	public PropertyItemValue getValue(final PropertyItem aItem)
	{
		return mValueItems.get(aItem);
	}
	
	@Override
	public PropertyItemValue getValue(final String aName)
	{
		return getValue(getGroup().getItem(aName));
	}
	
	@Override
	public List<PropertyItemValue> getValues()
	{
		return mValues;
	}
	
	@Override
	public PropertyItemGroup getGroup()
	{
		return mGroup;
	}
}
