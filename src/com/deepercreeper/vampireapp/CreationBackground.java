package com.deepercreeper.vampireapp;

public class CreationBackground implements Creation
{
	private static final int	MAX_VALUE	= 6;
	
	private final Background	mBackground;
	
	private int					mValue;
	
	public CreationBackground(final Background aBackground)
	{
		mBackground = aBackground;
	}
	
	public Background getBackground()
	{
		return mBackground;
	}
	
	@Override
	public int getValue()
	{
		return mValue;
	}
	
	@Override
	public void increase()
	{
		if (mValue < MAX_VALUE)
		{
			mValue++ ;
		}
	}
	
	@Override
	public void decrease()
	{
		if (mValue > 0)
		{
			mValue-- ;
		}
	}
}
