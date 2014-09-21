package com.deepercreeper.vampireapp;

public class CreationProperty implements Creation
{
	private final Property	mProperty;
	
	private int				mValueId;
	
	public CreationProperty(final Property aProperty)
	{
		mProperty = aProperty;
		mValueId = 0;
	}
	
	@Override
	public void increase()
	{
		if (mValueId < mProperty.getValues().length - 1)
		{
			mValueId++ ;
		}
	}
	
	@Override
	public void decrease()
	{
		if (mValueId > 0)
		{
			mValueId-- ;
		}
	}
	
	@Override
	public int getValue()
	{
		return mProperty.getValues()[mValueId];
	}
	
	public int getValueId()
	{
		return mValueId;
	}
	
	public Property getProperty()
	{
		return mProperty;
	}
}
