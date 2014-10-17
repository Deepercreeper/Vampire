package com.deepercreeper.vampireapp.controller;

/**
 * Attributes, abilities and virtues are simple items.<br>
 * They are final and a character has to set all of them.
 * 
 * @author Vincent
 */
public class SimpleItem implements Item
{
	private final String	mName;
	
	private final int		mMaxValue;
	
	private final int		mFreePointsCost;
	
	private final int		mStartValue;
	
	private final int		mMaxStartValue;
	
	private final String	mDescription;
	
	private SimpleItem(final String aName, final int aStartValue, final int aMaxStartValue, final int aMaxValue, final int aFreePointsCost)
	{
		mName = aName;
		mFreePointsCost = aFreePointsCost;
		mMaxValue = aMaxValue;
		mStartValue = aStartValue;
		mMaxStartValue = aMaxStartValue;
		mDescription = createDescription();
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
	
	private String createDescription()
	{
		// TODO Implement
		return mName;
	}
	
	@Override
	public final String getDescription()
	{
		return mDescription;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	@Override
	public int compareTo(final Item aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	@Override
	public int hashCode()
	{
		return mName.hashCode();
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
		return new SimpleItem(aData, aStartValue, aMaxStartValue, aMaxValue, aFreePointsCost);
	}
}
