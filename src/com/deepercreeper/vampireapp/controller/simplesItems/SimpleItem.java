package com.deepercreeper.vampireapp.controller.simplesItems;

import com.deepercreeper.vampireapp.controller.implementations.ItemImpl;

/**
 * Attributes, abilities and virtues are simple items.<br>
 * They are final and a character has to set all of them.
 * 
 * @author Vincent
 */
public class SimpleItem extends ItemImpl
{
	private static final String	DESCRIPTION_PREFIX	= "#";
	
	private final int			mMaxValue;
	
	private final int			mFreePointsCost;
	
	private final int			mStartValue;
	
	private final int			mMaxStartValue;
	
	private SimpleItem(final String aName, final int aStartValue, final int aMaxStartValue, final int aMaxValue, final int aFreePointsCost,
			final boolean aNeedsDescription)
	{
		super(aName, aNeedsDescription);
		mFreePointsCost = aFreePointsCost;
		mMaxValue = aMaxValue;
		mStartValue = aStartValue;
		mMaxStartValue = aMaxStartValue;
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof SimpleItem)
		{
			final SimpleItem item = (SimpleItem) aO;
			return getName().equals(item.getName());
		}
		return false;
	}
	
	@Override
	public int getFreePointsCost()
	{
		return mFreePointsCost;
	}
	
	@Override
	public int getMaxStartValue()
	{
		return mMaxStartValue;
	}
	
	@Override
	public int getMaxValue()
	{
		return mMaxValue;
	}
	
	@Override
	public int getStartValue()
	{
		return mStartValue;
	}
	
	@Override
	protected String createDisplayName()
	{
		// TODO Implement
		return getName();
	}
	
	/**
	 * Creates a simple item out of the given data and other specifications.
	 * 
	 * @param aData
	 *            The data out of which the simple item is created.
	 * @param aStartValue
	 *            The start value for this item, which is set at character creation.
	 * @param aMaxStartValue
	 *            The maximum value that can be set when creating a new character.
	 * @param aMaxValue
	 *            The maximum value for this item.
	 * @param aFreePointsCost
	 *            The number of free points needed to increase values of this item.
	 * @return the created simple item.
	 */
	public static SimpleItem create(final String aData, final int aStartValue, final int aMaxStartValue, final int aMaxValue,
			final int aFreePointsCost)
	{
		if (aData.startsWith(DESCRIPTION_PREFIX))
		{
			return new SimpleItem(aData.substring(1), aStartValue, aMaxStartValue, aMaxValue, aFreePointsCost, true);
		}
		return new SimpleItem(aData, aStartValue, aMaxStartValue, aMaxValue, aFreePointsCost, false);
	}
}
