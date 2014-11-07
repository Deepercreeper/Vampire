package com.deepercreeper.vampireapp.controller;


/**
 * A group of simple items.
 * 
 * @author Vincent
 */
public class SimpleItemGroup extends ItemGroupImpl<SimpleItem>
{
	private static final String	NAME_DELIM	= ":", ITEMS_DELIM = ",";
	
	private SimpleItemGroup(final String aName)
	{
		super(aName);
	}
	
	/**
	 * Creates a new simple item group out of the given data.
	 * 
	 * @param aData
	 *            The data out of which the group is created.
	 * @param aStartValue
	 *            The character creation start value for all values inside this group.
	 * @param aMaxStartValue
	 *            The maximum character creation value for all values inside this group.
	 * @param aMaxValue
	 *            The maximum value for all values inside this group.
	 * @param aFreePointsCost
	 *            The number of experience of free points needed to spend for increasing values of this group.
	 * @return the created simple item group.
	 */
	public static SimpleItemGroup create(final String aData, final int aStartValue, final int aMaxStartValue, final int aMaxValue,
			final int aFreePointsCost)
	{
		final String[] data = aData.split(NAME_DELIM);
		final SimpleItemGroup group = new SimpleItemGroup(data[0]);
		if (data.length > 1)
		{
			for (final String item : data[1].split(ITEMS_DELIM))
			{
				group.addItem(SimpleItem.create(item, aStartValue, aMaxStartValue, aMaxValue, aFreePointsCost));
			}
		}
		return group;
	}
}
