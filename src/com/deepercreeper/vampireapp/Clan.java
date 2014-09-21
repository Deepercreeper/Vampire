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
	
	public String getDescription()
	{
		final StringBuilder descr = new StringBuilder();
		if (getDisciplines().isEmpty())
		{
			descr.append(mName);
		}
		else
		{
			descr.append(mName + ": ");
			boolean first = true;
			for (final Discipline discipline : getDisciplines())
			{
				if (first)
				{
					descr.append(discipline.getName());
					first = false;
				}
				else
				{
					descr.append(", " + discipline.getName());
				}
			}
		}
		return descr.toString();
	}
	
	@Override
	public String toString()
	{
		return mName + " " + mDisciplines;
	}
}
