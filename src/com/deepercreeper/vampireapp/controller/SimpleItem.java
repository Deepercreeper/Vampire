package com.deepercreeper.vampireapp.controller;

/**
 * Attributes, abilities and virtues are simple items.<br>
 * They are final and a character has to set all of them.
 * 
 * @author Vincent
 */
public class SimpleItem implements Item
{
	private static final int	MAX_VALUE	= 6;
	
	private final String		mName;
	
	private final int			mStartValue;
	
	private final int			mMaxStartValue;
	
	private final String		mDescription;
	
	private SimpleItem(final String aName, final int aStartValue, final int aMaxStartValue)
	{
		mName = aName;
		mStartValue = aStartValue;
		mMaxStartValue = aMaxStartValue;
		mDescription = createDescription();
	}
	
	@Override
	public int getMaxStartValue()
	{
		return mMaxStartValue;
	}
	
	@Override
	public int getMaxValue()
	{
		return MAX_VALUE;
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
	 * @return the created simple item.
	 */
	public static SimpleItem create(final String aData, final int aStartValue, final int aMaxStartValue)
	{
		return new SimpleItem(aData, aStartValue, aMaxStartValue);
	}
}
