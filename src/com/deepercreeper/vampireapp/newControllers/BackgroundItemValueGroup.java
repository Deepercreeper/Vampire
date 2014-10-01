package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.widget.LinearLayout;

public class BackgroundItemValueGroup implements ItemValueGroup<BackgroundItem>, VariableItemValueGroup<BackgroundItem, BackgroundItemValue>
{
	private boolean												mCreation;
	
	private final BackgroundItemGroup							mGroup;
	
	private final List<BackgroundItemValue>						mValuesList		= new ArrayList<BackgroundItemValue>();
	
	private final HashMap<BackgroundItem, BackgroundItemValue>	mValues	= new HashMap<BackgroundItem, BackgroundItemValue>();
	
	public BackgroundItemValueGroup(final BackgroundItemGroup aGroup, final boolean aCreation)
	{
		mGroup = aGroup;
		mCreation = aCreation;
	}
	
	@Override
	public void addValue(final BackgroundItemValue aValue)
	{
		mValuesList.add(aValue);
		mValues.put(aValue.getItem(), aValue);
		Collections.sort(mValuesList, BackgroundItemValue.getComparator());
	}
	
	@Override
	public void addValue(final BackgroundItem aItem)
	{
		addValue(aItem.createValue());
	}
	
	@Override
	public void addValue(final String aName)
	{
		addValue(getGroup().getItem(aName));
	}
	
	@Override
	public void removeValue(final BackgroundItemValue aValue)
	{
		mValuesList.remove(aValue);
		mValues.remove(aValue.getItem());
	}
	
	@Override
	public void removeValue(final BackgroundItem aItem)
	{
		removeValue(aItem.createValue());
	}
	
	@Override
	public void removeValue(final String aName)
	{
		removeValue(getGroup().getItem(aName));
	}
	
	@Override
	public ItemGroup<BackgroundItem> getGroup()
	{
		return mGroup;
	}
	
	@Override
	public List<BackgroundItemValue> getValues()
	{
		return mValuesList;
	}
	
	@Override
	public BackgroundItemValue getValue(final String aName)
	{
		return getValue(getGroup().getItem(aName));
	}
	
	@Override
	public BackgroundItemValue getValue(final BackgroundItem aItem)
	{
		return mValues.get(aItem);
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
