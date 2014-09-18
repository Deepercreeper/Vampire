package com.deepercreeper.vampireapp;

import java.util.HashSet;

public class Clan
{
	private static final String		CLAN_DISCIPLIN_DELIM	= ",";
	
	private final String			mName;
	
	private final HashSet<String>	mDisciplines			= new HashSet<>();
	
	public Clan(String aName)
	{
		mName = aName;
	}
	
	public void addDisciplines(String aDisciplines)
	{
		for (String discipline : aDisciplines.split(CLAN_DISCIPLIN_DELIM))
		{
			mDisciplines.add(discipline);
		}
	}
	
	public String getName()
	{
		return mName;
	}
	
	public HashSet<String> getDisciplines()
	{
		return mDisciplines;
	}
	
	@Override
	public String toString()
	{
		return mName + " " + mDisciplines;
	}
}
