package com.deepercreeper.vampireapp.character.creation;

/**
 * The creation of a character has several states.<br>
 * Each is declared by one CreationMode.
 * 
 * @author Vincent
 */
public enum CreationMode
{
	/**
	 * This is the first creation mode. Here are the start points and other main options registered.
	 */
	MAIN(true, false),
	
	/**
	 * Here are the bonus points set, that can be given to each character.
	 */
	POINTS(false, true),
	
	/**
	 * Here are descriptions created. For items and character own descriptions.
	 */
	DESCRIPTIONS(false, false),
	
	/**
	 * This is the free version of the main mode. Used for creating free characters.
	 */
	FREE_MAIN();
	
	private final boolean mValueMode;
	
	private final boolean mTempPointsMode;
	
	private final boolean mFreeMode;
	
	private CreationMode()
	{
		mValueMode = true;
		mTempPointsMode = false;
		mFreeMode = true;
	}
	
	private CreationMode(final boolean aValueMode, final boolean aTempPointsMode)
	{
		mValueMode = aValueMode;
		mTempPointsMode = aTempPointsMode;
		mFreeMode = false;
		if (mValueMode && mTempPointsMode)
		{
			throw new IllegalArgumentException("Can't change value and temporary points in one mode!");
		}
	}
	
	/**
	 * @return whether this is a free creation mode.
	 */
	public boolean isFreeMode()
	{
		return mFreeMode;
	}
	
	/**
	 * @return whether this mode is for temporary point changes.
	 */
	public boolean isTempPointsMode()
	{
		return mTempPointsMode;
	}
	
	/**
	 * @return whether this mode is for value changes.
	 */
	public boolean isValueMode()
	{
		return mValueMode;
	}
}
