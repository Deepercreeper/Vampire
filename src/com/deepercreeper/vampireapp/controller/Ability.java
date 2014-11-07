package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Each discipline, which is no parent discipline has abilities.<br>
 * Each ability represents a possible action that can be done as a character.<br>
 * Clans also have their own clan disciplines and so their own clan abilities.
 * 
 * @author Vincent
 */
public class Ability implements Comparable<Ability>
{
	private static final String	NAME_DELIM	= ",", COST_DELIM = "#";
	
	private static final String	VOLITION_POINTS	= "VolitionPoints", BLOOD_POINTS = "BloodPoints", VOLITION = "Volition", DICES = "Dices";
	
	private final String		mName;
	
	private final int			mLevel;
	
	private boolean				mVolition		= false;
	
	private int					mVolitionPoints	= 0, mBloodPoints = 0, mDices = 0;
	
	private final Set<String>	mCosts			= new HashSet<String>();
	
	private Ability(final String aName, final int aLevel)
	{
		mName = aName;
		mLevel = aLevel;
	}
	
	@Override
	public int compareTo(final Ability aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	/**
	 * Some abilities cost blood points.<br>
	 * This returns the number of blood points to spend when using this ability.<br>
	 * {@code -1} is returned, when each added dice thrown costs a blood point.
	 * 
	 * @return the number of blood points to spend for using this ability.
	 */
	public int getBloodPoints()
	{
		return mBloodPoints;
	}
	
	/**
	 * Some abilities have several costs. Each cost is a attribute or another item.<br>
	 * The sum of all cost values is the number of dices that can be thrown for this ability.
	 * 
	 * @return a set of all costs.
	 */
	public Set<String> getCosts()
	{
		return mCosts;
	}
	
	/**
	 * Some abilities have a base number of dices that are thrown for this ability.<br>
	 * This returns the number of dices that are added to the other costs to throw.<br>
	 * {@code -1} is returned if a user defined number of dices can be added for the ability.
	 * 
	 * @return the number of dices for using this ability.
	 */
	public int getDices()
	{
		return mDices;
	}
	
	/**
	 * Each ability has a level that defines the minimum discipline level from where at it is possible to use this ability.
	 * 
	 * @return the level of this ability.
	 */
	public int getLevel()
	{
		return mLevel;
	}
	
	/**
	 * @return the name of this ability.
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * Some abilities cost volition points. This returns the number of points to spend for using this ability.<br>
	 * {@code -1} is returned when each dice that is thrown costs one volition point.
	 * 
	 * @return the number of volition points to spend for this ability.
	 */
	public int getVolitionPoints()
	{
		return mVolitionPoints;
	}
	
	/**
	 * If this ability has volition as cost additional to normal costs this returns {@code true}.<br>
	 * That means that the number of volition points the character has at this time of dices is added to the dice throw.
	 * 
	 * @return {@code true} if the volition is added to the throw and {@code false} if not.
	 */
	public boolean hasVolitionCost()
	{
		return mVolition;
	}
	
	private void addCost(final String aCost, final List<Integer> aNumberData)
	{
		if (aCost.equals(VOLITION))
		{
			mVolition = true;
		}
		else if (aCost.equals(VOLITION_POINTS))
		{
			if (aNumberData.isEmpty())
			{
				mVolitionPoints = -1;
			}
			else
			{
				mVolitionPoints = aNumberData.remove(0);
			}
		}
		else if (aCost.equals(BLOOD_POINTS))
		{
			if (aNumberData.isEmpty())
			{
				mBloodPoints = -1;
			}
			else
			{
				mBloodPoints = aNumberData.remove(0);
			}
		}
		else if (aCost.equals(DICES))
		{
			if (aNumberData.isEmpty())
			{
				mDices = -1;
			}
			else
			{
				mDices = aNumberData.remove(0);
			}
		}
		else
		{
			mCosts.add(aCost);
		}
	}
	
	/**
	 * Creates an ability out of the given data string.
	 * 
	 * @param aData
	 *            The data out of which the ability is created.
	 * @return the created ability.
	 */
	public static Ability create(final String aData)
	{
		final String[] data = aData.split(NAME_DELIM);
		final Ability ability = new Ability(data[0], Integer.parseInt(data[1]));
		if (data.length > 2)
		{
			final List<Integer> numberData = new ArrayList<Integer>();
			for (int i = 3; i < data.length; i++ )
			{
				numberData.add(Integer.parseInt(data[i]));
			}
			for (final String cost : data[2].split(COST_DELIM))
			{
				ability.addCost(cost, numberData);
			}
		}
		return ability;
	}
	
	@Override
	public String toString()
	{
		return "<" + getName() + ":" + ">";
	}
}
