package com.deepercreeper.vampireapp.character;

public class ExperienceController
{
	private int	mEP	= 0;
	
	public int getExperience()
	{
		return mEP;
	}
	
	public void increaseBy(final int aValue)
	{
		mEP += aValue;
	}
	
	public void decreaseBy(final int aValue)
	{
		mEP -= aValue;
		if (mEP < 0)
		{
			mEP = 0;
		}
	}
}
