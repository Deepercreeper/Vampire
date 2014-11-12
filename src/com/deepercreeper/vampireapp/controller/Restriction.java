package com.deepercreeper.vampireapp.controller;

import java.util.HashSet;
import java.util.Set;

public class Restriction
{
	public static final String			RESTRICTIONS_DELIM	= ",", SIMPLE = "Simple", BACKGROUND = "Background", DISCIPLINE = "Discipline",
			PROPERTY = "Property", INSANITY = "Insanity", VOLITION = "Volition", PATH = "Path", GENERATION = "Generation";
	
	private static final String			KEY_DELIM			= "@", EQUALS = "=", SMALLER = "<", LARGER = ">", BETWEEN = "?", BETWEEN_DELIM = "-";
	
	private final String				mKey;
	
	private final int					mMinValue;
	
	private final int					mMaxValue;
	
	private final Set<Restrictionable>	mParents			= new HashSet<Restrictionable>();
	
	private Restriction(final String aKey, final int aMinValue, final int aMaxValue)
	{
		mKey = aKey;
		mMinValue = aMinValue;
		mMaxValue = aMaxValue;
	}
	
	public boolean hasGroup()
	{
		return mKey.contains(KEY_DELIM);
	}
	
	public String getGroup()
	{
		return mKey.split(KEY_DELIM)[0];
	}
	
	public void addParent(final Restrictionable aParent)
	{
		mParents.add(aParent);
	}
	
	public void clear()
	{
		for (final Restrictionable parent : mParents)
		{
			parent.removeRestriction(this);
		}
	}
	
	public String getKey()
	{
		if (hasGroup())
		{
			return mKey.substring(mKey.indexOf(KEY_DELIM) + 1);
		}
		return mKey;
	}
	
	public int getMaxValue()
	{
		return mMaxValue;
	}
	
	public int getMinValue()
	{
		return mMinValue;
	}
	
	@Override
	public String toString()
	{
		return mKey + ": " + mMinValue + "-" + mMaxValue;
	}
	
	public static Restriction create(final String aData)
	{
		String[] data;
		int min = -1;
		int max = Integer.MAX_VALUE;
		if (aData.contains(SMALLER))
		{
			data = aData.split(SMALLER);
			max = Integer.parseInt(data[1]) - 1;
		}
		else if (aData.contains(LARGER))
		{
			data = aData.split(LARGER);
			min = Integer.parseInt(data[1]) + 1;
		}
		else if (aData.contains(BETWEEN))
		{
			data = aData.split(BETWEEN);
			final String[] range = data[1].split(BETWEEN_DELIM);
			min = Integer.parseInt(range[0]);
			max = Integer.parseInt(range[1]);
		}
		else
		{
			data = aData.split(EQUALS);
			max = min = Integer.parseInt(data[1]);
		}
		return new Restriction(data[0], min, max);
	}
}
