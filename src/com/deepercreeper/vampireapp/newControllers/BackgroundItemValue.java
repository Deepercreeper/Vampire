package com.deepercreeper.vampireapp.newControllers;

import java.util.Comparator;

public class BackgroundItemValue implements ItemValue<BackgroundItem>
{
	private final BackgroundItem	mItem;
	
	private int						mValue;
	
	public BackgroundItemValue(final BackgroundItem aItem)
	{
		mItem = aItem;
		mValue = mItem.getStartValue();
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
	public int getValue()
	{
		return mValue;
	}
	
	@Override
	public BackgroundItem getItem()
	{
		return mItem;
	}
	
	public static Comparator<? super BackgroundItemValue> getComparator()
	{
		return new Comparator<BackgroundItemValue>()
		{
			@Override
			public int compare(final BackgroundItemValue aLhs, final BackgroundItemValue aRhs)
			{
				return aLhs.getItem().compareTo(aRhs.getItem());
			}
		};
	}
}
