package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DisciplineItem implements Item
{
	private static final String							SUB_PREFIX			= ">", PARENT_PREFIX = "#", NAME_DELIM = ":", ABILITIES_DELIM = ";",
			SUB_ITEMS_DELIM = ",";
	
	private static final int							MAX_VALUE			= 6, MAX_START_VALUE = 3, START_VALUE = 0;
	
	public static final int								MIN_FIRST_SUB_VALUE	= 2, MAX_SUB_DISCIPLINES = 2;
	
	private final String								mName;
	
	private final String								mDescription;
	
	private final HashMap<String, SubDisciplineItem>	mSubItems			= new HashMap<String, SubDisciplineItem>();
	
	private final List<SubDisciplineItem>				mSubItemNames		= new ArrayList<SubDisciplineItem>();
	
	private final List<Ability>							mAbilities			= new ArrayList<Ability>();
	
	protected DisciplineItem(final String aName)
	{
		mName = aName;
		mDescription = createDescription();
	}
	
	public void addSubItem(final SubDisciplineItem aSubItem)
	{
		mSubItems.put(aSubItem.getName(), aSubItem);
		mSubItemNames.add(aSubItem);
		Collections.sort(mSubItemNames);
	}
	
	@Override
	public int compareTo(final Item aAnother)
	{
		return getName().compareTo(mName);
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
	
	public List<Ability> getAbilities()
	{
		return mAbilities;
	}
	
	@Override
	public String getDescription()
	{
		return mDescription;
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
	public String getName()
	{
		return mName;
	}
	
	@Override
	public int getStartValue()
	{
		return START_VALUE;
	}
	
	public SubDisciplineItem getSubItem(final String aName)
	{
		return mSubItems.get(aName);
	}
	
	public Set<String> getSubItemNames()
	{
		return mSubItems.keySet();
	}
	
	public List<SubDisciplineItem> getSubItems()
	{
		return mSubItemNames;
	}
	
	@Override
	public int hashCode()
	{
		return mName.hashCode();
	}
	
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
	
	private String createDescription()
	{
		// TODO Implement
		return mName;
	}
	
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