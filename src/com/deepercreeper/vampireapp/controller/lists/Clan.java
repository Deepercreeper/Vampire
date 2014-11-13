package com.deepercreeper.vampireapp.controller.lists;

import java.util.HashSet;
import java.util.Set;
import com.deepercreeper.vampireapp.controller.disciplines.DisciplineController;
import com.deepercreeper.vampireapp.controller.disciplines.DisciplineItem;
import com.deepercreeper.vampireapp.controller.implementations.Named;
import com.deepercreeper.vampireapp.controller.restrictions.Restriction;

/**
 * Each character has to have a single clan. The clan defines, which disciplines and restrictions<br>
 * are set for the character.
 * 
 * @author vrl
 */
public class Clan extends Named
{
	private static final String				NAME_DELIM		= ":", GENERATION_DELIM = ";", CLAN_DISCIPLIN_DELIM = ",";
	
	private final HashSet<DisciplineItem>	mDisciplines	= new HashSet<DisciplineItem>();
	
	private final HashSet<Restriction>		mRestrictions	= new HashSet<Restriction>();
	
	private Clan(final String aName)
	{
		super(aName);
	}
	
	private void addRestriction(final Restriction aRestriction)
	{
		mRestrictions.add(aRestriction);
	}
	
	/**
	 * @return a set of all restrictions of this clan.
	 */
	public Set<Restriction> getRestrictions()
	{
		return mRestrictions;
	}
	
	private void addDiscipline(final DisciplineItem aDiscipline)
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
	
	/**
	 * @return a description for this clan containing name and disciplines.
	 */
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
	
	/**
	 * @return a set of all clan disciplines.
	 */
	public Set<DisciplineItem> getDisciplines()
	{
		return mDisciplines;
	}
	
	@Override
	public String toString()
	{
		return getName() + " " + mDisciplines;
	}
	
	@Override
	public int hashCode()
	{
		return getName().hashCode();
	}
	
	/**
	 * Creates a new clan out of the given data.
	 * 
	 * @param aData
	 *            The clan data.
	 * @param aDisciplines
	 *            The discipline controller.
	 * @return a new clan.
	 */
	public static Clan create(String aData, DisciplineController aDisciplines)
	{
		final String[] clanData = aData.split(NAME_DELIM);
		Clan clan = new Clan(clanData[0]);
		if (clanData.length > 1)
		{
			final String[] clanDisciplines = clanData[1].split(GENERATION_DELIM);
			if ( !clanDisciplines[0].isEmpty())
			{
				for (final String clanDiscipline : clanDisciplines[0].split(CLAN_DISCIPLIN_DELIM))
				{
					clan.addDiscipline(aDisciplines.getDisciplines().getItem(clanDiscipline));
				}
			}
			if (clanDisciplines.length > 1)
			{
				for (final String restrictionData : clanDisciplines[1].split(Restriction.RESTRICTIONS_DELIM))
				{
					clan.addRestriction(Restriction.create(restrictionData));
				}
			}
		}
		return clan;
	}
}
