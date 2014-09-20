package com.deepercreeper.vampireapp;

public class CreationItem implements Creation
{
	public static final int	MAX_CREATION_VALUE	= 3, MAX_VALUE = 6;
	
	private final Item		mItem;
	
	private int				mValue;
	
	public CreationItem(final Item aItem)
	{
		mItem = aItem;
		mValue = aItem.getStartValue();
	}
	
	public Item getItem()
	{
		return mItem;
	}
	
	@Override
	public int getValue()
	{
		return mValue;
	}
	
	@Override
	public void increase()
	{
		if (mValue - mItem.getStartValue() < MAX_CREATION_VALUE && mValue < MAX_VALUE)
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
	public String toString()
	{
		return "CreationItem: " + mItem.getParent() + "." + mItem.getName() + ":" + mValue;
	}
}
