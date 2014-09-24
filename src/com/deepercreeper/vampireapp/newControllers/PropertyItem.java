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
	
	private void setValues(int[] aValues)
	{
		mValues = aValues;
	}
	
	public boolean isNegative()
	{
		return mNegative;
	}
	
	public int[] getValues()
	{
		return mValues;
	}
	
	@Override
	public int getMaxValue()
	{
		return mValues.length;
	}
	
	@Override
	public PropertyItemValue createValue()
	{
		return new PropertyItemValue(this);
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
	
	public static PropertyItem create(String aData)
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
		String[] valueData = data[1].split(VALUE_DELIM);
		int[] values = new int[valueData.length + 1];
		values[0] = 0;
		for (int i = 0; i < valueData.length; i++ )
		{
			values[i + 1] = Integer.parseInt(valueData[i]);
		}
		item.setValues(values);
		return item;
	}
}
