package com.deepercreeper.vampireapp.lists.items;


public class DescriptionInstance
{
	private final Description	mDescription;
	
	private final String		mValue;
	
	public DescriptionInstance(final Description aDescription, final String aValue)
	{
		mDescription = aDescription;
		mValue = aValue;
	}
	
	public Description getDescription()
	{
		return mDescription;
	}
	
	public String getValue()
	{
		return mValue;
	}
}
