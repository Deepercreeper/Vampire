package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.List;

public class DisciplineItemValue implements ItemValue<DisciplineItem>
{
	private final DisciplineItem			mItem;
	
	private int								mValue;
	
	private final List<DisciplineItemValue>	mSubValues	= new ArrayList<DisciplineItemValue>();
	
	public DisciplineItemValue(DisciplineItem aItem)
	{
		mItem = aItem;
		mValue = mItem.getStartValue();
	}
	
	public DisciplineItemValue getSubValue(int aPos)
	{
		if (aPos >= mSubValues.size()) { return null; }
		return mSubValues.get(aPos);
	}
	
	public void setSubValue(int aPos, DisciplineItemValue aSubValue)
	{
		mSubValues.add(aPos, aSubValue);
	}
	
	public boolean hasSubDiscipline(int aPos)
	{
		return mSubValues.size() > aPos && mSubValues.get(aPos) != null;
	}
	
	@Override
	public DisciplineItem getItem()
	{
		return mItem;
	}
	
	public List<DisciplineItemValue> getSubValues()
	{
		return mSubValues;
	}
	
	@Override
	public int getValue()
	{
		return mValue;
	}
	
	@Override
	public void increase()
	{
		if (mValue < mItem.getMaxValue())
		{
			mValue++ ;
		}
	}
	
	@Override
	public void decrease()
	{
		if (mValue > mItem.getStartValue())
		{
			mValue-- ;
		}
	}
	
	@Override
	public int compareTo(DisciplineItemValue aAnother)
	{
		return mItem.compareTo();
	}
}
