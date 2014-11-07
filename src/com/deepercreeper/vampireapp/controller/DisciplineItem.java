package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Each clan has several disciplines. The consist of some abilities or sub disciplines.<br>
 * There are other disciplines that don't belong to any clan.<br>
 * They can be learned by being taught or sucking heart blood from other characters that are able to use it.
 * 
 * @author Vincent
 */
public class DisciplineItem extends ItemImpl
{
	private static final String							SUB_PREFIX			= ">", PARENT_PREFIX = "#", NAME_DELIM = ":", ABILITIES_DELIM = ";",
			SUB_ITEMS_DELIM = ",";
	
	private static final int							MAX_VALUE			= 6, MAX_START_VALUE = 3, START_VALUE = 0, FREE_POINTS_COST = 7;
	
	/**
	 * The number of sub disciplines a parent discipline can contain.
	 */
	public static final int								MAX_SUB_DISCIPLINES	= 2;
	
	private final HashMap<String, SubDisciplineItem>	mSubItems			= new HashMap<String, SubDisciplineItem>();
	
	private final List<SubDisciplineItem>				mSubItemNames		= new ArrayList<SubDisciplineItem>();
	
	private final List<Ability>							mAbilities			= new ArrayList<Ability>();
	
	protected DisciplineItem(final String aName)
	{
		super(aName);
	}
	
	/**
	 * Adds a sub discipline to this discipline.
	 * 
	 * @param aSubItem
	 *            The sub discipline.
	 */
	public void addSubItem(final SubDisciplineItem aSubItem)
	{
		mSubItems.put(aSubItem.getName(), aSubItem);
		mSubItemNames.add(aSubItem);
		Collections.sort(mSubItemNames);
	}
	
	@Override
	public int getFreePointsCost()
	{
		return FREE_POINTS_COST;
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof DisciplineItem)
		{
			final DisciplineItem item = (DisciplineItem) aO;
			return getName().equals(item.getName());
		}
		return false;
	}
	
	/**
	 * @return a list of all abilities of this discipline. May be empty.
	 */
	public List<Ability> getAbilities()
	{
		return mAbilities;
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
	
	/**
	 * If this is a parent discipline this returns the sub discipline with the given name.
	 * 
	 * @param aName
	 *            The sub discipline name.
	 * @return the sub discipline with the given name.
	 */
	public SubDisciplineItem getSubItem(final String aName)
	{
		return mSubItems.get(aName);
	}
	
	/**
	 * When a parent discipline is created, at first the names of all sub disciplines are added.<br>
	 * After that it has to be initialized for adding the corresponding sub disciplines.<br>
	 * This returns all names of the sub disciplines to add to this parent discipline.
	 * 
	 * @return a set of sub discipline names.
	 */
	public Set<String> getSubItemNames()
	{
		return mSubItems.keySet();
	}
	
	/**
	 * After this parent item was initialized this returns all sub discipline items of this discipline.
	 * 
	 * @return a list of sub discipline items.
	 */
	public List<SubDisciplineItem> getSubItems()
	{
		return mSubItemNames;
	}
	
	/**
	 * @return whether this is a parent discipline and contains sub disciplines or not.
	 */
	public boolean isParentItem()
	{
		return !mSubItems.isEmpty();
	}
	
	private void addAbility(final Ability aAbility)
	{
		mAbilities.add(aAbility);
		Collections.sort(mAbilities);
	}
	
	private void addSubItemName(final String aName)
	{
		mSubItems.put(aName, null);
	}
	
	@Override
	protected String createDescription()
	{
		// TODO Implement
		return getName();
	}
	
	/**
	 * Creates a discipline item out of the given data.
	 * 
	 * @param aData
	 *            The data out of the discipline item is created.
	 * @return the created discipline item.
	 */
	public static DisciplineItem create(final String aData)
	{
		DisciplineItem discipline;
		if (aData.startsWith(SUB_PREFIX))
		{
			discipline = SubDisciplineItem.create(aData);
		}
		else if (aData.startsWith(PARENT_PREFIX))
		{
			final String[] data = aData.substring(1).split(NAME_DELIM);
			discipline = new DisciplineItem(data[0]);
			if (data.length > 1)
			{
				for (final String subItem : data[1].split(SUB_ITEMS_DELIM))
				{
					discipline.addSubItemName(subItem);
				}
			}
		}
		else
		{
			final String[] data = aData.split(NAME_DELIM);
			discipline = new DisciplineItem(data[0]);
			if (data.length > 1)
			{
				for (final String ability : data[1].split(ABILITIES_DELIM))
				{
					discipline.addAbility(Ability.create(ability));
				}
			}
		}
		return discipline;
	}
}