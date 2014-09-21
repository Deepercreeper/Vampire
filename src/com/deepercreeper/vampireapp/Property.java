package com.deepercreeper.vampireapp;

public class Property
{
	public static final String	NEGATIVE_PREFIX	= "-", DELIM = ",";
	
	private final String		mName;
	
	private final boolean		mNegative;
	
	private final int[]			mValues;
	
	public Property(final String aName, final String aData, final boolean aNegative)
	{
		mName = aName;
		mNegative = aNegative;
		final String[] values = aData.split(DELIM);
		mValues = new int[values.length + 1];
		mValues[0] = 0;
		for (int i = 0; i < values.length; i++ )
		{
			mValues[i + 1] = Integer.parseInt(values[i]);
		}
	}
	
	public boolean isNegative()
	{
		return mNegative;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public String getDescription()
	{
		final StringBuilder descr = new StringBuilder();
		descr.append(mName + ": ");
		boolean first = true;
		for (final int value : mValues)
		{
			if (first)
			{
				descr.append(value);
				first = false;
			}
			else
			{
				descr.append(", " + value);
			}
		}
		return descr.toString();
	}
	
	public int[] getValues()
	{
		return mValues;
	}
}
