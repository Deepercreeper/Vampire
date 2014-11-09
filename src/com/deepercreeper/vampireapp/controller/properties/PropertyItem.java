package com.deepercreeper.vampireapp.controller.properties;

import com.deepercreeper.vampireapp.controller.implementations.ItemImpl;

/**
 * Each character has several properties which can't be changed after the creation.<br>
 * They define some attributes of the character more precisely.
 * 
 * @author Vincent
 */
public class PropertyItem extends ItemImpl
{
	private static final String	NAME_DELIM	= ":", NEGATIVE_PREFIX = "-", VALUE_DELIM = ",";
	
	private static final int	START_VALUE	= 0;
	
	private int[]				mValues;
	
	private final boolean		mNegative;
	
	private PropertyItem(final String aName, final boolean aNegative)
	{
		super(aName);
		mNegative = aNegative;
	}
	
	private void setValues(final int[] aValues)
	{
		mValues = aValues;
	}
	
	/**
	 * Some properties are positive, some negative.<br>
	 * When a character is created the sum of values from the negative properties<br>
	 * has to be at least as large as the sum of values from the positive properties.
	 * 
	 * @return {@code true} if this is a negative property and {@code false} if positive.
	 */
	public boolean isNegative()
	{
		return mNegative;
	}
	
	@Override
	public int getFreePointsCost()
	{
		return -1;
	}
	
	/**
	 * Properties have a specified set of possible values that can be set.<br>
	 * They all have a unique id which is used to increase and decrease the property value.
	 * 
	 * @param aValueId
	 *            The id of the corresponding value.
	 * @return the value with the given id.
	 */
	public int getValue(final int aValueId)
	{
		return mValues[aValueId];
	}
	
	/**
	 * If this is a negative property the value multiplied with negative one is returned.<br>
	 * Otherwise this returns just the value as specified in {@link PropertyItem#getValue(int)}.
	 * 
	 * @param aValueId
	 *            The id of the corresponding value.
	 * @return the final signed value of this property.
	 */
	public int getFinalValue(final int aValueId)
	{
		return mValues[aValueId] * (mNegative ? -1 : 1);
	}
	
	@Override
	public int getMaxStartValue()
	{
		return mValues.length - 1;
	}
	
	@Override
	public int getMaxValue()
	{
		return mValues.length - 1;
	}
	
	@Override
	public int getStartValue()
	{
		return START_VALUE;
	}
	
	@Override
	protected final String createDescription()
	{
		// TODO Implement
		return getName();
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof PropertyItem)
		{
			final PropertyItem item = (PropertyItem) aO;
			return getName().equals(item.getName());
		}
		return false;
	}
	
	/**
	 * Creates a property item out of the given data.
	 * 
	 * @param aData
	 *            The data out of which the property item is created.
	 * @return the created property item.
	 */
	public static PropertyItem create(final String aData)
	{
		PropertyItem item;
		String[] data;
		if (aData.startsWith(NEGATIVE_PREFIX))
		{
			data = aData.substring(1).split(NAME_DELIM);
			item = new PropertyItem(data[0], true);
		}
		else
		{
			data = aData.split(NAME_DELIM);
			item = new PropertyItem(data[0], false);
		}
		final String[] valueData = data[1].split(VALUE_DELIM);
		final int[] values = new int[valueData.length + 1];
		values[0] = 0;
		for (int i = 0; i < valueData.length; i++ )
		{
			values[i + 1] = Integer.parseInt(valueData[i]);
		}
		item.setValues(values);
		return item;
	}
}
