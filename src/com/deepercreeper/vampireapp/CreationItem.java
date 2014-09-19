package com.deepercreeper.vampireapp;

public class CreationItem
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
	
	public int getValue()
	{
		return mValue;
	}
	
	public boolean increase()
	{
		if (mValue - mItem.getStartValue() < MAX_CREATION_VALUE && mValue < MAX_VALUE)
		{
			mValue++ ;
			return true;
		}
		return false;
	}
	
	public boolean decrease()
	{
		if (mValue > mItem.getStartValue())
		{
			mValue-- ;
			return true;
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return "CreationItem: " + mItem.getParent() + "." + mItem.getName() + ":" + mValue;
	}
}
