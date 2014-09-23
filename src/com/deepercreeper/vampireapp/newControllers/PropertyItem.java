package com.deepercreeper.vampireapp.newControllers;

public class PropertyItem implements Item
{
	private static final int	START_VALUE	= 0;
	
	private final String		mName;
	
	private final String		mDescription;
	
	private final int[]			mValues;
	
	public PropertyItem(final String aName, final int[] aValues)
	{
		mName = aName;
		mValues = aValues;
		mDescription = createDescription();
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
}
