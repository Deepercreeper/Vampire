package com.deepercreeper.vampireapp.newControllers;

public class BackgroundItem implements Item
{
	private static final int	MAX_VALUE		= 6, MAX_START_VALUE = 5, START_VALUE = 0;
	
	public static final int		MAX_BACKGROUNDS	= 5;
	
	private final String		mName;
	
	private final String		mDescription;
	
	private BackgroundItem(final String aName)
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
	public int getMaxStartValue()
	{
		return MAX_START_VALUE;
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
	public int compareTo(final Item aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	@Override
	public int hashCode()
	{
		return mName.hashCode();
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof BackgroundItem)
		{
			final BackgroundItem item = (BackgroundItem) aO;
			return getName().equals(item.getName());
		}
		return false;
	}
	
	public static BackgroundItem create(final String aData)
	{
		return new BackgroundItem(aData);
	}
}
