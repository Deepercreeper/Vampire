package com.deepercreeper.vampireapp.lists.controllers.instances;

import com.deepercreeper.vampireapp.character.instance.CharacterInstance;

/**
 * A controller for the character generation.
 * 
 * @author vrl
 */
public class GenerationControllerInstance
{
	private static final int		MAX_LEVEL_POINTS	= 7;
	
	private static final int		MIN_GENERATION		= 3;
	
	private final CharacterInstance	mChar;
	
	private int						mGeneration;
	
	/**
	 * Creates a new generation controller.
	 * 
	 * @param aGeneration
	 *            The character creation.
	 * @param aChar
	 *            The character.
	 */
	public GenerationControllerInstance(final int aGeneration, final CharacterInstance aChar)
	{
		mChar = aChar;
		mGeneration = aGeneration;
	}
	
	/**
	 * Lets the character increase its generation. (The generation value will decrease)
	 */
	public void increase()
	{
		if (mGeneration > MIN_GENERATION)
		{
			mGeneration-- ;
		}
	}
	
	/**
	 * @return the current generation.
	 */
	public int getGeneration()
	{
		return mGeneration;
	}
	
	/**
	 * Sets the generation of the character.
	 * 
	 * @param aGeneration
	 *            The new generation.
	 */
	public void setGeneration(final int aGeneration)
	{
		mGeneration = aGeneration;
		mChar.update();
	}
	
	/**
	 * @return whether the character is a low level character.
	 */
	public boolean isLowLevel()
	{
		return mGeneration > MAX_LEVEL_POINTS;
	}
}
