package com.deepercreeper.vampireapp.newControllers;

public class SimpleItemValue implements ItemValue<SimpleItem>
{
	private final SimpleItem	mItem;
	
	private int					mValue;
	
	public SimpleItemValue(SimpleItem aItem)
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
}
