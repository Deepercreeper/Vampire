package com.deepercreeper.vampireapp.newControllers;

public class PropertyItem implements Item
{
	private static final String	NAME_DELIM	= ":", NEGATIVE_PREFIX = "-", VALUE_DELIM = ",";
	
	private static final int	START_VALUE	= 0;
	
	private final String		mName;
	
	private final String		mDescription;
	
	private int[]				mValues;
	
	private final boolean		mNegative;
	
	private PropertyItem(final String aName, final boolean aNegative)
	{
		mName = aName;
		mNegative = aNegative;
		mDescription = createDescription();
	}
	
	private void setValues(final int[] aValues)
	{
		mValues = aValues;
	}
	
	public boolean isNegative()
	{
		return mNegative;
	}
	
	public int getValue(final int aValueId)
	{
		return mValues[aValueId];
	}
	
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
	public int compareTo(final Item aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	@Override
	public int getStartValue()
	{
		return START_VALUE;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	@Override
	public String getDescription()
	{
		return mDescription;
	}
	
	private final String createDescription()
	{
		// TODO Implement
		return mName;
	}
	
	@Override
	public int hashCode()
	{
		return mName.hashCode();
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
