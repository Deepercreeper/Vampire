package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DisciplineItemValue implements ItemValue<DisciplineItem>
{
	private final DisciplineItem			mItem;
	
	private int								mValue;
	
	private final List<DisciplineItemValue>	mSubValues	= new ArrayList<DisciplineItemValue>();
	
	public DisciplineItemValue(final DisciplineItem aItem)
	{
		mItem = aItem;
		mValue = mItem.getStartValue();
	}
	
	public DisciplineItemValue getSubValue(final int aPos)
	{
		if (aPos >= mSubValues.size())
		{
			return null;
		}
		return mSubValues.get(aPos);
	}
	
	public void setSubValue(final int aPos, final DisciplineItemValue aSubValue)
	{
		mSubValues.add(aPos, aSubValue);
	}
	
	public boolean hasSubDiscipline(final int aPos)
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
		if (mValue < getItem().getMaxValue())
		{
			mValue++ ;
		}
	}
	
	@Override
	public void decrease()
	{
		if (mValue > getItem().getStartValue())
		{
			mValue-- ;
		}
	}
	
	public static Comparator<? super DisciplineItemValue> getComparator()
	{
		return new Comparator<DisciplineItemValue>()
		{
			@Override
			public int compare(final DisciplineItemValue aLhs, final DisciplineItemValue aRhs)
			{
				return aLhs.getItem().compareTo(aRhs.getItem());
			}
		};
	}
}
