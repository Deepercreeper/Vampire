package com.deepercreeper.vampireapp.newControllers;

public class SimpleItem implements Item
{
	private static final int	MAX_VALUE	= 6;
	
	private final String		mName;
	
	private final int			mStartValue;
	
	private final String		mDescription;
	
	public SimpleItem(final String aName, final int aStartValue)
	{
		mName = aName;
		mStartValue = aStartValue;
		mDescription = createDescription();
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
	public SimpleItemValue createValue()
	{
		return new SimpleItemValue(this);
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
	
	public static SimpleItem create(final String aData, final int aStartValue)
	{
		return new SimpleItem(aData, aStartValue);
	}
}
