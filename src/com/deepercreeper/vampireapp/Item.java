package com.deepercreeper.vampireapp;

/**
 * Attributes and abilities are handled equal.<br>
 * Each entity corresponds to one item and is used to create creation items.
 * 
 * @author Vincent
 */
public class Item
{
	private static final int	ATTRIBUTE_START_VALUE	= 1, ABILITY_START_VALUE = 0;
	
	private static final int[]	MAX_ATTRIBUTE_POINTS	= new int[] { 6, 8, 10 }, MAX_ABILITIE_POINTS = new int[] { 5, 9, 13 };
	
	private final String		mName;
	
	private final String		mParent;
	
	private final boolean		mAttribute;
	
	/**
	 * Creates a attribute or ability with a name and the given parent.
	 * 
	 * @param aName
	 *            The item name.
	 * @param aParent
	 *            The parent name.
	 * @param aAttribute
	 *            Whether this is an attribute or ability.
	 */
	public Item(final String aName, final String aParent, final boolean aAttribute)
	{
		mName = aName;
		mParent = aParent;
		mAttribute = aAttribute;
	}
	
	/**
	 * @return whether this is an attribute or ability.
	 */
	public boolean isAttribute()
	{
		return mAttribute;
	}
	
	/**
	 * @return the number of points that can be spent per parent.
	 */
	public int[] getMaxPoints()
	{
		return mAttribute ? MAX_ATTRIBUTE_POINTS : MAX_ABILITIE_POINTS;
	}
	
	/**
	 * @return the name of this item.
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * @return the parent name.
	 */
	public String getParent()
	{
		return mParent;
	}
	
	/**
	 * At the creation of any character the items have a specific value.
	 * 
	 * @return the start value of this item.
	 */
	public int getStartValue()
	{
		return mAttribute ? ATTRIBUTE_START_VALUE : ABILITY_START_VALUE;
	}
	
	@Override
	public String toString()
	{
		return "Item: " + mParent + "." + mName;
	}
}
