package com.deepercreeper.vampireapp;

import java.util.HashSet;

public class Ability
{
	private static final String		PROPERTIES_DELIM	= ",", COST_DELIM = "#", VOLITION_POINTS = "VolitionPoints", BLOOD_POINTS = "BloodPoints",
			DICES = "Dices";
	
	private final String			mName;
	
	private final int				mLevel;
	
	private final HashSet<String>	mCosts				= new HashSet<String>();
	
	private int						mBloodPoints		= 0;
	
	private int						mWillPoints			= 0;
	
	private int						mDices				= 0;
	
	public Ability(final String aData)
	{
		final String[] properties = aData.split(PROPERTIES_DELIM);
		mName = properties[0];
		mLevel = Integer.parseInt(properties[1]);
		if (properties.length > 2)
		{
			int propertyCounter = 3;
			for (final String cost : properties[2].split(COST_DELIM))
			{
				if (cost.equals(BLOOD_POINTS))
				{
					if (properties.length > propertyCounter)
					{
						// For the ability are the given number of blood points to spend
						mBloodPoints = Integer.parseInt(properties[propertyCounter]);
						propertyCounter++ ;
					}
					else
					{
						// For each dice thrown one blood point is to spend
						mBloodPoints = -1;
					}
				}
				else if (cost.equals(VOLITION_POINTS))
				{
					if (properties.length > propertyCounter)
					{
						// For the ability are the given number of will points to spend
						mWillPoints = Integer.parseInt(properties[propertyCounter]);
						propertyCounter++ ;
					}
					else
					{
						// For each dice thrown one will point is to spend
						mWillPoints = -1;
					}
				}
				else if (cost.equals(DICES))
				{
					if (properties.length > propertyCounter)
					{
						// The number of dices that are added to the default number
						mDices = Integer.parseInt(properties[propertyCounter]);
						propertyCounter++ ;
					}
				}
				else
				{
					// The number of dices to throw by default
					mCosts.add(cost);
				}
			}
		}
	}
	
	public String getName()
	{
		return mName;
	}
	
	public int getLevel()
	{
		return mLevel;
	}
	
	public int getDices()
	{
		return mDices;
	}
	
	public int getBloodPoints()
	{
		return mBloodPoints;
	}
	
	public int getWillPoints()
	{
		return mWillPoints;
	}
	
	public HashSet<String> getCosts()
	{
		return mCosts;
	}
	
	@Override
	public String toString()
	{
		String costs = mCosts.toString();
		if (mBloodPoints > 0)
		{
			costs += " Blood: " + mBloodPoints;
		}
		else if (mBloodPoints == -1)
		{
			costs += " Blood: Dices";
		}
		if (mWillPoints > 0)
		{
			costs += " Volition: " + mWillPoints;
		}
		else if (mWillPoints == -1)
		{
			costs += " Volition: Dices";
		}
		return mLevel + "-" + mName + ": " + costs;
	}
}
