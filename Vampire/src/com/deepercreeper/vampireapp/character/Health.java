package com.deepercreeper.vampireapp.character;

/**
 * Represents default health settings.
 * 
 * @author vrl
 */
public class Health
{
	private final int[]		mSteps;
	
	private final String	mCost;
	
	/**
	 * Creates new default health settings.
	 * 
	 * @param aSteps
	 *            The number of default health steps per character.
	 * @param aCost
	 *            The name of the item that is used to heal health steps.
	 */
	public Health(final int[] aSteps, final String aCost)
	{
		mSteps = aSteps;
		mCost = aCost;
	}
	
	/**
	 * @return the healing item.
	 */
	public String getCost()
	{
		return mCost;
	}
	
	/**
	 * @return the health steps, a chracter usually has from the beginning.
	 */
	public int[] getSteps()
	{
		return mSteps;
	}
}
