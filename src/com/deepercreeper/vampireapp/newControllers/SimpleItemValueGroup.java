package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.LinearLayout;

public class SimpleItemValueGroup implements ItemValueGroup<SimpleItem>
{
	private boolean										mCreation;
	
	private final SimpleItemGroup						mGroup;
	
	private final List<SimpleItemValue>					mValues		= new ArrayList<SimpleItemValue>();
	
	private final HashMap<SimpleItem, SimpleItemValue>	mValueItems	= new HashMap<SimpleItem, SimpleItemValue>();
	
	public SimpleItemValueGroup(final SimpleItemGroup aGroup, final boolean aCreation)
	{
		mCreation = aCreation;
		mGroup = aGroup;
		for (final SimpleItem item : mGroup.getItems())
		{
			addValue(item.createValue());
		}
	}
	
	private void addValue(final SimpleItemValue aValue)
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
	public SimpleItemValue getValue(final SimpleItem aItem)
	{
		return mValueItems.get(aItem);
	}
	
	@Override
	public SimpleItemValue getValue(final String aName)
	{
		return getValue(getGroup().getItem(aName));
	}
	
	@Override
	public void setCreation(final boolean aCreation)
	{
		mCreation = aCreation;
	}
	
	@Override
	public boolean isCreation()
	{
		return mCreation;
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{
		// TODO Implement
	}
}
