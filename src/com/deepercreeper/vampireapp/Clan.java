package com.deepercreeper.vampireapp;

import java.util.HashSet;
import java.util.Set;
import com.deepercreeper.vampireapp.controller.Restriction;
import com.deepercreeper.vampireapp.controller.disciplines.DisciplineItem;
import com.deepercreeper.vampireapp.controller.implementations.Named;

public class Clan extends Named
{
	private final HashSet<DisciplineItem>	mDisciplines	= new HashSet<DisciplineItem>();
	
	private final HashSet<Restriction>		mRestrictions	= new HashSet<Restriction>();
	
	public Clan(final String aName)
	{
		super(aName);
	}
	
	public void addRestriction(final Restriction aRestriction)
	{
		mRestrictions.add(aRestriction);
	}
	
	public Set<Restriction> getRestrictions()
	{
		return mRestrictions;
	}
	
	public void addDiscipline(final DisciplineItem aDiscipline)
	{
		mDisciplines.add(aDiscipline);
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof Clan)
		{
			final Clan c = (Clan) aO;
			return getName().equals(c.getName());
		}
		return false;
	}
	
	public String getDescription()
	{
		final StringBuilder descr = new StringBuilder();
		if (getDisciplines().isEmpty())
		{
			descr.append(getName());
		}
		else
		{
			descr.append(getName() + ": ");
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
	
	public HashSet<DisciplineItem> getDisciplines()
	{
		return mDisciplines;
	}
	
	@Override
	public String toString()
	{
		return getName() + " " + mDisciplines;
	}
}
