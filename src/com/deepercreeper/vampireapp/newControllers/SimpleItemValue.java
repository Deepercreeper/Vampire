package com.deepercreeper.vampireapp.newControllers;

import java.util.Comparator;

public class SimpleItemValue implements ItemValue<SimpleItem>
{
	private final SimpleItem	mItem;
	
	private int					mValue;
	
	public SimpleItemValue(final SimpleItem aItem)
	{
		mItem = aItem;
		mValue = mItem.getStartValue();
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
			mValue++ ;
		}
	}
	
	@Override
	public SimpleItem getItem()
	{
		return mItem;
	}
	
	public static Comparator<? super SimpleItemValue> getComparator()
	{
		return new Comparator<SimpleItemValue>()
		{
			@Override
			public int compare(final SimpleItemValue aLhs, final SimpleItemValue aRhs)
			{
				return aLhs.getItem().compareTo(aRhs.getItem());
			}
		};
	}
}
