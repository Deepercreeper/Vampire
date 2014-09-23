package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BackgroundItemValueGroup implements ItemValueGroup<BackgroundItem>, VariableItemValueGroup<BackgroundItem, BackgroundItemValue>
{
	private final BackgroundItemGroup							mGroup;
	
	private final List<BackgroundItemValue>						mValues		= new ArrayList<BackgroundItemValue>();
	
	private final HashMap<BackgroundItem, BackgroundItemValue>	mValueItems	= new HashMap<BackgroundItem, BackgroundItemValue>();
	
	public BackgroundItemValueGroup(BackgroundItemGroup aGroup)
	{
		mGroup = aGroup;
	}
	
	@Override
	public void addValue(BackgroundItemValue aValue)
	{
		mValues.add(aValue);
		mValueItems.put(aValue.getItem(), aValue);
	}
	
	@Override
	public void addValue(BackgroundItem aItem)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addValue(String aName)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ItemGroup<BackgroundItem> getGroup()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<? extends ItemValue<BackgroundItem>> getValues()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ItemValue<BackgroundItem> getValue(String aName)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ItemValue<BackgroundItem> getValue(BackgroundItem aItem)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
