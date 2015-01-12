package com.deepercreeper.vampireapp.controllers.descriptions;


public class DescriptionValue
{
	private final Description	mDescription;
	
	private final String		mValue;
	
	public DescriptionValue(final Description aDescription, final String aValue)
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
