package com.deepercreeper.vampireapp;

import java.util.Collections;
import java.util.HashMap;

public class CharCreator
{
	public static final int											MIN_GENERATION	= 8, DEFAULT_GENERATION = 12, MAX_GENERATION = 12;
	
	private static int[]											sMaxAttributes, sMaxAbilities;
	
	private String													mName			= "";
	
	private String													mConcept		= "";
	
	private String													mNature;
	
	private String													mBehavior;
	
	private Clan													mClan;
	
	private int														mGeneration		= DEFAULT_GENERATION;
	
	private final HashMap<String, HashMap<String, CreationItem>>	mAttributes;
	
	private final HashMap<String, HashMap<String, CreationItem>>	mAbilities;
	
	public CharCreator(final HashMap<String, HashMap<String, CreationItem>> aAttributes,
			final HashMap<String, HashMap<String, CreationItem>> aAbilities, final String aNature, final String aBehavior, final Clan aClan)
	{
		mAttributes = aAttributes;
		mAbilities = aAbilities;
		mNature = aNature;
		mBehavior = aBehavior;
		mClan = aClan;
	}
	
	public static void init(final int[] aMaxAttributes, final int[] aMaxAbilities)
	{
		sMaxAttributes = aMaxAttributes;
		sMaxAbilities = aMaxAbilities;
	}
	
	public void increaseItem(final Item aItem)
	{
		if (canIncreaseItem(aItem))
		{
			CreationItem item;
			if (aItem.isAttribute())
			{
				item = mAttributes.get(aItem.getParent()).get(aItem.getName());
			}
			else
			{
				item = mAbilities.get(aItem.getParent()).get(aItem.getName());
			}
			item.increase();
		}
	}
	
	public void decreaseItem(final Item aItem)
	{
		CreationItem item;
		if (aItem.isAttribute())
		{
			item = mAttributes.get(aItem.getParent()).get(aItem.getName());
		}
		else
		{
			item = mAbilities.get(aItem.getParent()).get(aItem.getName());
		}
		item.decrease();
	}
	
	public void setName(final String aName)
	{
		mName = aName;
	}
	
	public void setConcept(final String aConcept)
	{
		mConcept = aConcept;
	}
	
	public void setNature(final String aNature)
	{
		mNature = aNature;
	}
	
	public void setBehavior(final String aBehavior)
	{
		mBehavior = aBehavior;
	}
	
	public void setClan(final Clan aClan)
	{
		mClan = aClan;
	}
	
	public void setGeneration(final int aGeneration)
	{
		mGeneration = aGeneration;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public String getConcept()
	{
		return mConcept;
	}
	
	public String getNature()
	{
		return mNature;
	}
	
	public String getBehavior()
	{
		return mBehavior;
	}
	
	public Clan getClan()
	{
		return mClan;
	}
	
	public int getGeneration()
	{
		return mGeneration;
	}
	
	public CreationItem getItem(final Item aItem)
	{
		if (aItem.isAttribute())
		{
			return mAttributes.get(aItem.getParent()).get(aItem.getName());
		}
		return mAbilities.get(aItem.getParent()).get(aItem.getName());
	}
	
	private HashMap<String, Integer> getValues(final HashMap<String, HashMap<String, CreationItem>> aParents)
	{
		final HashMap<String, Integer> values = new HashMap<>();
		for (final String parent : aParents.keySet())
		{
			int value = 0;
			for (final CreationItem item : aParents.get(parent).values())
			{
				value += item.getValue();
			}
			values.put(parent, value);
		}
		return values;
	}
	
	private boolean canIncreaseItem(final Item aItem)
	{
		final int[] maxPoints = aItem.isAttribute() ? sMaxAttributes : sMaxAbilities;
		final HashMap<String, Integer> values = getValues(aItem.isAttribute() ? mAttributes : mAbilities);
		final int parentValue = values.get(aItem.getParent());
		values.remove(aItem.getParent());
		if (parentValue < maxPoints[0])
		{
			return true;
		}
		boolean maxDone = false, midDone = false;
		if (Collections.max(values.values()) > maxPoints[1])
		{
			maxDone = true;
		}
		if (Collections.min(values.values()) > maxPoints[0])
		{
			midDone = true;
		}
		if (parentValue == maxPoints[0] && midDone)
		{
			return false;
		}
		if (parentValue == maxPoints[1] && maxDone)
		{
			return false;
		}
		return parentValue < maxPoints[2];
	}
}
