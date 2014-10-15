package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parent disciplines have sub disciplines that act like normal disciplines.<br>
 * They have specified permissions to be created and a character can only have<br>
 * a subset of all sub disciplines from any parent discipline.
 * 
 * @author Vincent
 */
public class SubDisciplineItem extends DisciplineItem
{
	private static final String	NAME_DELIM			= ":", ABILITIES_DELIM = ";";
	
	private static final int	MAX_VALUE			= 6, MAX_START_VALUE = 3, START_VALUE = 0;
	
	/**
	 * The number of points that have to be set for the first sub discipline to add other sub disciplines.
	 */
	public static final int		MIN_FIRST_SUB_VALUE	= 2;
	
	private final String		mDescription;
	
	private DisciplineItem		mParent;
	
	private final List<Ability>	mAbilities			= new ArrayList<Ability>();
	
	private SubDisciplineItem(final String aName)
	{
		super(aName);
		mDescription = createDescription();
	}
	
	/**
	 * Sets the parent discipline for this sub discipline.
	 * 
	 * @param aParent
	 *            The parent discipline.
	 */
	public void setParent(final DisciplineItem aParent)
	{
		mParent = aParent;
	}
	
	@Override
	public List<Ability> getAbilities()
	{
		return mAbilities;
	}
	
	private void addAbility(final Ability aAbility)
	{
		mAbilities.add(aAbility);
		Collections.sort(mAbilities);
	}
	
	/**
	 * @return the parent discipline if set already.
	 */
	public DisciplineItem getParent()
	{
		return mParent;
	}
	
	@Override
	public int compareTo(final Item aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	@Override
	public String getDescription()
	{
		return mDescription;
	}
	
	private String createDescription()
	{
		// TODO Implement
		return getName();
	}
	
	@Override
	public int getMaxStartValue()
	{
		return MAX_START_VALUE;
	}
	
	@Override
	public int getMaxValue()
	{
		return MAX_VALUE;
	}
	
	@Override
	public int getStartValue()
	{
		return START_VALUE;
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof SubDisciplineItem)
		{
			final SubDisciplineItem item = (SubDisciplineItem) aO;
			return getName().equals(item.getName());
		}
		return false;
	}
	
	/**
	 * Creates a sub discipline out of the given data.
	 * 
	 * @param aData
	 *            The data out of which the sub discipline is created.
	 * @return the created sub discipline item.
	 */
	public static SubDisciplineItem create(final String aData)
	{
		SubDisciplineItem discipline;
		final String[] data = aData.substring(1).split(NAME_DELIM);
		discipline = new SubDisciplineItem(data[0]);
		if (data.length > 1)
		{
			for (final String ability : data[1].split(ABILITIES_DELIM))
			{
				discipline.addAbility(Ability.create(ability));
			}
		}
		return discipline;
	}
}
