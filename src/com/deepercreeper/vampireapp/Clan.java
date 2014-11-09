package com.deepercreeper.vampireapp;

import java.util.HashSet;
import com.deepercreeper.vampireapp.controller.disciplines.DisciplineItem;

public class Clan
{
	private final String					mName;
	
	private final HashSet<DisciplineItem>	mDisciplines	= new HashSet<DisciplineItem>();
	
	private int								mGeneration		= -1;
	
	public Clan(final String aName)
	{
		mName = aName;
	}
	
	public void addDiscipline(final DisciplineItem aDiscipline)
	{
		mDisciplines.add(aDiscipline);
	}
	
	public void setGeneration(final int aGeneration)
	{
		mGeneration = aGeneration;
	}
	
	public boolean hasGeneration()
	{
		return mGeneration != -1;
	}
	
	public int getGeneration()
	{
		return mGeneration;
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
	public boolean equals(final Object aO)
	{
		if (aO instanceof Clan)
		{
			final Clan c = (Clan) aO;
			return mName.equals(c.mName);
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return mName + " " + mDisciplines;
	}
}
