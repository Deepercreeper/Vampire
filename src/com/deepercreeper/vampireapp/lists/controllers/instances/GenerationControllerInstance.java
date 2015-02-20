package com.deepercreeper.vampireapp.lists.controllers.instances;

import com.deepercreeper.vampireapp.character.instance.CharacterInstance;

public class GenerationControllerInstance
{
	private static final int		MAX_LEVEL_POINTS	= 7;
	
	private static final int		MIN_GENERATION		= 3;
	
	private final CharacterInstance	mChar;
	
	private int						mGeneration;
	
	public GenerationControllerInstance(final int aGeneration, final CharacterInstance aChar)
	{
		mChar = aChar;
		mGeneration = aGeneration;
	}
	
	public void increase()
	{
		if (mGeneration > MIN_GENERATION)
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
		mChar.update();
	}
	
	public boolean isLowLevel()
	{
		return mGeneration > MAX_LEVEL_POINTS;
	}
}
