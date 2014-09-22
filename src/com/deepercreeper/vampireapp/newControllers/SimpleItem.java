package com.deepercreeper.vampireapp.newControllers;

public class SimpleItem implements Item
{
	private final String	mName;
	
	private final String	mDescription;
	
	public SimpleItem(final String aName)
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
	public final String getDescription()
	{
		return mDescription;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	public static SimpleItem read(final String aData)
	{
		return new SimpleItem(aData);
	}
	
	@Override
	public int compareTo(final Item aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
}
