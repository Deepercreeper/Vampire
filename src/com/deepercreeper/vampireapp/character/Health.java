package com.deepercreeper.vampireapp.character;

public class Health
{
	private final int[]		mSteps;
	
	private final String	mCost;
	
	public Health(final int[] aSteps, final String aCost)
	{
		mSteps = aSteps;
		mCost = aCost;
	}
	
	public String getCost()
	{
		return mCost;
	}
	
	public int[] getSteps()
	{
		return mSteps;
	}
}
