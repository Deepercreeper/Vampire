package com.deepercreeper.vampireapp.character;

import com.deepercreeper.vampireapp.controllers.lists.Path;

public class PathController
{
	private static final int	MIN_VALUE	= 0, MAX_VALUE = 10;
	
	private final Path			mPath;
	
	private int					mValue;
	
	public PathController(final Path aPath, final int aValue)
	{
		mPath = aPath;
		mValue = aValue;
	}
	
	public Path getPath()
	{
		return mPath;
	}
	
	public int getValue()
	{
		return mValue;
	}
	
	public int getExperienceCost()
	{
		return getValue() * 2;
	}
	
	public void increase()
	{
		if (mValue < MAX_VALUE)
		{
			mValue++ ;
		}
	}
	
	public void decrease()
	{
		if (mValue > MIN_VALUE)
		{
			mValue-- ;
		}
	}
}
