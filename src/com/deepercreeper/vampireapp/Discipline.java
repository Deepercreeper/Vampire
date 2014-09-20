package com.deepercreeper.vampireapp;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Discipline
{
	public static final String					PARENT_DISCIPLINE	= "#";
	
	private static final String					ABILITIES_DELIM		= ";", SUB_DISCIPLINES_DELIM = ",";
	
	private final String						mName;
	
	private final boolean						mParentDiscipline;
	
	private final HashMap<String, Discipline>	mSubDisciplines		= new HashMap<>();
	
	private final HashSet<Ability>				mAbilities			= new HashSet<>();
	
	public Discipline(final String aName, final String aData, final boolean aParentDiscipline)
	{
		mName = aName;
		mParentDiscipline = aParentDiscipline;
		if (mParentDiscipline)
		{
			final String[] subDisciplines = aData.split(SUB_DISCIPLINES_DELIM);
			for (final String subDiscipline : subDisciplines)
			{
				mSubDisciplines.put(subDiscipline, null);
			}
		}
		else
		{
			final String[] abilities = aData.split(ABILITIES_DELIM);
			for (final String ability : abilities)
			{
				mAbilities.add(new Ability(ability));
			}
		}
	}
	
	public Discipline getSubDiscipline(final String aSubDiscipline)
	{
		return mSubDisciplines.get(aSubDiscipline);
	}
	
	public void addSubDiscipline(final Discipline aSubDiscipline)
	{
		mSubDisciplines.put(aSubDiscipline.getName(), aSubDiscipline);
	}
	
	public boolean isParentDiscipline()
	{
		return mParentDiscipline;
	}
	
	public Collection<Discipline> getSubDisciplines()
	{
		return mSubDisciplines.values();
	}
	
	public Set<String> getSubDisciplineNames()
	{
		return mSubDisciplines.keySet();
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
