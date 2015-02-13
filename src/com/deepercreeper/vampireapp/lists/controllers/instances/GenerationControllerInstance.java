package com.deepercreeper.vampireapp.lists.controllers.instances;

public class GenerationControllerInstance
{
	private static final int	MAX_LEVEL_POINTS	= 7;
	
	private int					mGeneration;
	
	public GenerationControllerInstance(final int aGeneration)
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
	
	public boolean isLowLevel()
	{
		return mGeneration <= MAX_LEVEL_POINTS;
	}
}
