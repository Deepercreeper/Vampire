package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ability implements Comparable<Ability>
{
	private static final String	NAME_DELIM	= ",", COST_DELIM = "#";
	
	private static final String	VOLITION_POINTS	= "VolitionPoints", BLOOD_POINTS = "BloodPoints", VOLITION = "Volition", DICES = "Dices";
	
	private final String		mName;
	
	private final int			mLevel;
	
	private boolean				mVolition		= false;
	
	private int					mVolitionPoints	= 0, mBloodPoints = 0, mDices = 0;
	
	private final Set<String>	mCosts			= new HashSet<String>();
	
	private Ability(final String aName, final int aLevel)
	{
		mName = aName;
		mLevel = aLevel;
	}
	
	@Override
	public int compareTo(final Ability aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	public int getBloodPoints()
	{
		return mBloodPoints;
	}
	
	public Set<String> getCosts()
	{
		return mCosts;
	}
	
	public int getDices()
	{
		return mDices;
	}
	
	public int getLevel()
	{
		return mLevel;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public int getVolitionPoints()
	{
		return mVolitionPoints;
	}
	
	public boolean hasVolitionCost()
	{
		return mVolition;
	}
	
	private void addCost(final String aCost, final List<Integer> aNumberData)
	{
		if (aCost.equals(VOLITION))
		{
			mVolition = true;
		}
		else if (aCost.equals(VOLITION_POINTS))
		{
			if (aNumberData.isEmpty())
			{
				mVolitionPoints = -1;
			}
			else
			{
				mVolitionPoints = aNumberData.remove(0);
			}
		}
		else if (aCost.equals(BLOOD_POINTS))
		{
			if (aNumberData.isEmpty())
			{
				mBloodPoints = -1;
			}
			else
			{
				mBloodPoints = aNumberData.remove(0);
			}
		}
		else if (aCost.equals(DICES))
		{
			if (aNumberData.isEmpty())
			{
				mDices = -1;
			}
			else
			{
				mDices = aNumberData.remove(0);
			}
		}
		else
		{
			mCosts.add(aCost);
		}
	}
	
	public static Ability create(final String aData)
	{
		final String[] data = aData.split(NAME_DELIM);
		final Ability ability = new Ability(data[0], Integer.parseInt(data[1]));
		if (data.length > 2)
		{
			final List<Integer> numberData = new ArrayList<Integer>();
			for (int i = 3; i < data.length; i++ )
			{
				numberData.add(Integer.parseInt(data[i]));
			}
			for (final String cost : data[2].split(COST_DELIM))
			{
				ability.addCost(cost, numberData);
			}
		}
		return ability;
	}
}
