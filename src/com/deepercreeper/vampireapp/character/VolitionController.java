package com.deepercreeper.vampireapp.character;

public class VolitionController
{
	private static final int	MIN_VALUE	= 0, MAX_VALUE = 20;
	
	private int					mVolition;
	
	public VolitionController(final int aVolition)
	{
		mVolition = aVolition;
	}
	
	public void increase()
	{
		if (mVolition < MAX_VALUE)
		{
			mVolition++ ;
		}
	}
	
	public void decrease()
	{
		if (mVolition > MIN_VALUE)
		{
			mVolition-- ;
		}
	}
	
	public int getEPCost()
	{
		return getVolition();
	}
	
	public int getVolition()
	{
		return mVolition;
	}
}
