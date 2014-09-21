package com.deepercreeper.vampireapp;

/**
 * Each character has specific attributes and abilities with a value for each.<br>
 * Creation items are used to represent the item and to give it a value.
 * 
 * @author Vincent
 */
public class CreationItem implements Creation
{
	private static final int	MAX_CREATION_VALUE	= 3;
	
	/**
	 * The maximum value of each attribute or ability for a character.
	 */
	public static final int		MAX_VALUE			= 6;
	
	private final Item			mItem;
	
	private int					mValue;
	
	/**
	 * Creates an attribute or ability that is able to contain a value of points.
	 * 
	 * @param aItem
	 *            The item whose identity is used to save the value.
	 */
	public CreationItem(final Item aItem)
	{
		mItem = aItem;
		mValue = aItem.getStartValue();
	}
	
	/**
	 * @return the overlaying item.
	 */
	public Item getItem()
	{
		return mItem;
	}
	
	@Override
	public int getValue()
	{
		return mValue;
	}
	
	@Override
	public void increase()
	{
		if (mValue - mItem.getStartValue() < MAX_CREATION_VALUE && mValue < MAX_VALUE)
		{
			mValue++ ;
		}
	}
	
	@Override
	public void decrease()
	{
		if (mValue > mItem.getStartValue())
		{
			mValue-- ;
		}
	}
	
	@Override
	public String toString()
	{
		return "CreationItem: " + mItem.getParent() + "." + mItem.getName() + ":" + mValue;
	}
}
