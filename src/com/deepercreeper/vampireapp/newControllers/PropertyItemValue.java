package com.deepercreeper.vampireapp.newControllers;

import java.util.Comparator;

public class PropertyItemValue implements ItemValue<PropertyItem>
{
	private final PropertyItem	mItem;
	
	private int					mValueId;
	
	public PropertyItemValue(final PropertyItem aItem)
	{
		mItem = aItem;
		mValueId = aItem.getStartValue();
	}
	
	@Override
	public PropertyItem getItem()
	{
		return mItem;
	}
	
	@Override
	public int getValue()
	{
		return getItem().getValues()[mValueId];
	}
	
	@Override
	public void increase()
	{
		if (mValueId < getItem().getMaxValue())
		{
			mValueId++ ;
		}
	}
	
	@Override
	public void decrease()
	{
		if (mValueId > getItem().getStartValue())
		{
			mValueId-- ;
		}
	}
	
	public static Comparator<? super PropertyItemValue> getComparator()
	{
		return new Comparator<PropertyItemValue>()
		{
			@Override
			public int compare(final PropertyItemValue aLhs, final PropertyItemValue aRhs)
			{
				return aLhs.getItem().compareTo(aRhs.getItem());
			}
		};
	}
}
