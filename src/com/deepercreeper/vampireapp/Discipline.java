package com.deepercreeper.vampireapp;

import java.util.HashSet;

public class Discipline
{
	private static final String		ABILITIES_DELIM	= ";";
	
	private final String			mName;
	
	private final HashSet<Ability>	mAbilities		= new HashSet<>();
	
	public Discipline(String aName, String aData)
	{
		mName = aName;
		String[] abilities = aData.split(ABILITIES_DELIM);
		for (String ability : abilities)
		{
			mAbilities.add(new Ability(ability));
		}
	}
	
	public String getName()
	{
		return mName;
	}
	
	public HashSet<Ability> getAbilities()
	{
		return mAbilities;
	}
	
	@Override
	public String toString()
	{
		return mName + " " + mAbilities;
	}
}
