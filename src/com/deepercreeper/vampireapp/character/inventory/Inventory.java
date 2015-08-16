package com.deepercreeper.vampireapp.character.inventory;

/**
 * Represents default inventory settings.
 * 
 * @author vrl
 */
public class Inventory
{
	private final int[]		mMaxWeights;
	
	private final String	mMaxWeightItem;
	
	/**
	 * Creates new default inventory settings.
	 * 
	 * @param aMaxWeights
	 *            The maximum weight values depending on the strength item of the character.
	 * @param aMaxWeightItem
	 *            The maximum weight item. Its state says how much weight can be ported.
	 */
	public Inventory(final int[] aMaxWeights, final String aMaxWeightItem)
	{
		mMaxWeights = aMaxWeights;
		mMaxWeightItem = aMaxWeightItem;
	}
	
	/**
	 * @return the name of the weight carry defining item.
	 */
	public String getMaxWeightItem()
	{
		return mMaxWeightItem;
	}
	
	/**
	 * @param aValue
	 *            The weight item value.
	 * @return the maximum weight of the given weight item value.
	 */
	public int getMaxWeightOf(final int aValue)
	{
		return mMaxWeights[aValue];
	}
}
