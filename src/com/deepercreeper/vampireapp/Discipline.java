package com.deepercreeper.vampireapp;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Each character has specific disciplines that can have a number of points.<br>
 * This class represents the type of any discipline.<br>
 * Each discipline has several abilities that can be used by characters.<br>
 * They can also contain sub disciplines.
 * 
 * @author Vincent
 */
public class Discipline
{
	/**
	 * The prefix for a parent discipline. This should be added in front of all parent discipline definitions.
	 */
	public static final String					PARENT_DISCIPLINE	= "#";
	
	private static final String					ABILITIES_DELIM		= ";", SUB_DISCIPLINES_DELIM = ",";
	
	private final String						mName;
	
	private final boolean						mParentDiscipline;
	
	private final HashMap<String, Discipline>	mSubDisciplines		= new HashMap<>();
	
	private final HashSet<Ability>				mAbilities			= new HashSet<>();
	
	/**
	 * Creates a discipline out of the name and the given data.<br>
	 * If this is a parent discipline the data is split into the sub discipline names.<br>
	 * Otherwise the abilities are read and created.
	 * 
	 * @param aName
	 *            The discipline name.
	 * @param aData
	 *            The creation data.
	 * @param aParentDiscipline
	 *            Whether this is a parent discipline.
	 */
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
	
	/**
	 * If this is a parent discipline the sub discipline with the given name is returned.
	 * 
	 * @param aSubDiscipline
	 *            The sub discipline name.
	 * @return the sub discipline with the given name.
	 */
	public Discipline getSubDiscipline(final String aSubDiscipline)
	{
		return mSubDisciplines.get(aSubDiscipline);
	}
	
	/**
	 * If this discipline is a parent discipline a sub discipline is added.
	 * 
	 * @param aSubDiscipline
	 *            The sub discipline to add.
	 */
	public void addSubDiscipline(final Discipline aSubDiscipline)
	{
		mSubDisciplines.put(aSubDiscipline.getName(), aSubDiscipline);
	}
	
	/**
	 * @return whether this is a parent discipline.
	 */
	public boolean isParentDiscipline()
	{
		return mParentDiscipline;
	}
	
	/**
	 * @return a collection of all sub disciplines.
	 */
	public Collection<Discipline> getSubDisciplines()
	{
		return mSubDisciplines.values();
	}
	
	/**
	 * @return a set of the names of all sub disciplines.
	 */
	public Set<String> getSubDisciplineNames()
	{
		return mSubDisciplines.keySet();
	}
	
	/**
	 * @return the name of this discipline.
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * @return a set of all abilities of this discipline.
	 */
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
