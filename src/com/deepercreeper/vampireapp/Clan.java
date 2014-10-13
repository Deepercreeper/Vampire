package com.deepercreeper.vampireapp;

import java.util.HashSet;
import com.deepercreeper.vampireapp.newControllers.DisciplineItem;

public class Clan
{
	public static final String				CLAN_DISCIPLIN_DELIM	= ",";
	
	private final String					mName;
	
	private final HashSet<DisciplineItem>	mDisciplines			= new HashSet<DisciplineItem>();
	
	public Clan(final String aName)
	{
		mName = aName;
	}
	
	public void addDiscipline(final DisciplineItem aDiscipline)
	{
		mDisciplines.add(aDiscipline);
	}
	
	public String getName()
	{
		return mName;
	}
	
	public HashSet<DisciplineItem> getDisciplines()
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
			for (final DisciplineItem discipline : getDisciplines())
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
