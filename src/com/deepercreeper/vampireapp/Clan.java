package com.deepercreeper.vampireapp;

import java.util.HashSet;

public class Clan
{
	public static final String			CLAN_DISCIPLIN_DELIM	= ",";
	
	private final String				mName;
	
	private final HashSet<Discipline>	mDisciplines			= new HashSet<>();
	
	public Clan(final String aName)
	{
		mName = aName;
	}
	
	public void addDisciplines(final Discipline aDisciplines)
	{
		mDisciplines.add(aDisciplines);
	}
	
	public String getName()
	{
		return mName;
	}
	
	public HashSet<Discipline> getDisciplines()
	{
		return mDisciplines;
	}
	
	@Override
	public String toString()
	{
		return mName + " " + mDisciplines;
	}
}
