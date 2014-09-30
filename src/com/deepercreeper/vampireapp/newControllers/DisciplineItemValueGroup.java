package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.widget.LinearLayout;

public class DisciplineItemValueGroup implements ItemValueGroup<DisciplineItem>, VariableItemValueGroup<DisciplineItem, DisciplineItemValue>
{
	private boolean												mCreation;
	
	private final DisciplineItemGroup							mGroup;
	
	private final List<DisciplineItemValue>						mValues		= new ArrayList<DisciplineItemValue>();
	
	private final HashMap<DisciplineItem, DisciplineItemValue>	mValueItems	= new HashMap<DisciplineItem, DisciplineItemValue>();
	
	public DisciplineItemValueGroup(final DisciplineItemGroup aGroup, final boolean aCreation)
	{
		mGroup = aGroup;
		mCreation = aCreation;
	}
	
	@Override
	public DisciplineItemGroup getGroup()
	{
		return mGroup;
	}
	
	@Override
	public List<DisciplineItemValue> getValues()
	{
		return mValues;
	}
	
	@Override
	public DisciplineItemValue getValue(final String aName)
	{
		return getValue(getGroup().getItem(aName));
	}
	
	@Override
	public DisciplineItemValue getValue(final DisciplineItem aItem)
	{
		return mValueItems.get(aItem);
	}
	
	@Override
	public void addValue(final DisciplineItemValue aValue)
	{
		mValues.add(aValue);
		mValueItems.put(aValue.getItem(), aValue);
		Collections.sort(mValues, DisciplineItemValue.getComparator());
	}
	
	@Override
	public void addValue(final DisciplineItem aItem)
	{
		addValue(aItem.createValue());
	}
	
	@Override
	public void addValue(final String aName)
	{
		addValue(getGroup().getItem(aName));
	}
	
	@Override
	public void removeValue(final DisciplineItemValue aValue)
	{
		mValues.remove(aValue);
		mValueItems.remove(aValue.getItem());
	}
	
	@Override
	public void removeValue(final DisciplineItem aItem)
	{
		removeValue(aItem.createValue());
	}
	
	@Override
	public void removeValue(final String aName)
	{
		removeValue(getGroup().getItem(aName));
	}
	
	@Override
	public boolean isCreation()
	{
		return mCreation;
	}
	
	@Override
	public void setCreation(final boolean aCreation)
	{
		mCreation = aCreation;
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{
		// TODO Implement
	}
}
