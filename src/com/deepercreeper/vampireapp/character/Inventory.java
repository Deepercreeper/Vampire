package com.deepercreeper.vampireapp.character;

public class Inventory
{
	private final int[]		mMaxWeights;
	
	private final String	mMaxWeightItem;
	
	public Inventory(final int[] aMaxWeights, final String aMaxWeightItem)
	{
		mMaxWeights = aMaxWeights;
		mMaxWeightItem = aMaxWeightItem;
	}
	
	public int getMaxWeightOf(final int aValue)
	{
		return mMaxWeights[aValue];
	}
	
	public String getMaxWeightItem()
	{
		return mMaxWeightItem;
	}
}
