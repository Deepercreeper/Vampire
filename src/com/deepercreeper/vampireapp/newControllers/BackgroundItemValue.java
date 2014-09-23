package com.deepercreeper.vampireapp.newControllers;

public class BackgroundItemValue implements ItemValue<BackgroundItem>
{
	private final BackgroundItem	mItem;
	
	private int						mValue;
	
	public BackgroundItemValue(BackgroundItem aItem)
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
}
