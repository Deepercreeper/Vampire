package com.deepercreeper.vampireapp;

public class Item
{
	private static final int	ATTRIBUTE_START_VALUE	= 1, ABILITY_START_VALUE = 0;
	
	private final String		mName;
	
	private final String		mParent;
	
	private final int			mStartValue;
	
	private final boolean		mAttribute;
	
	public Item(final String aName, final String aParent, final boolean aAttribute)
	{
		mName = aName;
		mParent = aParent;
		mAttribute = aAttribute;
		mStartValue = aAttribute ? ATTRIBUTE_START_VALUE : ABILITY_START_VALUE;
	}
	
	public boolean isAttribute()
	{
		return mAttribute;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public String getParent()
	{
		return mParent;
	}
	
	public int getStartValue()
	{
		return mStartValue;
	}
}
