package com.deepercreeper.vampireapp.controllers;

public class GenerationValueController
{
	private static final int	MAX_LEVEL_POINTS	= 7;
	
	private int					mGeneration;
	
	public GenerationValueController(final int aGeneration)
	{
		mGeneration = aGeneration;
	}
	
	public void increase()
	{
		if (mGeneration > 3)
		{
			mGeneration-- ;
		}
	}
	
	public int getGeneration()
	{
		return mGeneration;
	}
	
	public void setGeneration(final int aGeneration)
	{
		mGeneration = aGeneration;
	}
	
	public boolean allowsMaxLevel()
	{
		return mGeneration <= MAX_LEVEL_POINTS;
	}
}
