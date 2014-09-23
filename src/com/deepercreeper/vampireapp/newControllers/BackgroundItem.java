package com.deepercreeper.vampireapp.newControllers;

public class BackgroundItem implements Item
{
	private static final int	MAX_VALUE	= 6, START_VALUE = 0;
	
	private final String		mName;
	
	private final String		mDescription;
	
	public BackgroundItem(String aName)
	{
		mName = aName;
		mDescription = createDescription();
	}
	
	private String createDescription()
	{
		// TODO Implement
		return mName;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	@Override
	public BackgroundItemValue createValue()
	{
		return new BackgroundItemValue(this);
	}
	
	@Override
	public int getMaxValue()
	{
		return MAX_VALUE;
	}
	
	@Override
	public int getStartValue()
	{
		return START_VALUE;
	}
	
	@Override
	public String getDescription()
	{
		return mDescription;
	}
	
	@Override
	public int compareTo(Item aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
}
