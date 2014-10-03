package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DisciplineItem implements Item
{
	private static final String						PARENT_PREFIX		= "#", NAME_DELIM = ":", ABILITIES_DELIM = ";", SUB_ITEMS_DELIM = ",";
	
	private static final int						MAX_VALUE			= 6, MAX_START_VALUE = 3, START_VALUE = 0;
	
	public static final int							MIN_FIRST_SUB_VALUE	= 2, MAX_SUB_DISCIPLINES = 2;
	
	private final String							mName;
	
	private final String							mDescription;
	
	private DisciplineItem							mParent;
	
	private final HashMap<String, DisciplineItem>	mSubItems			= new HashMap<String, DisciplineItem>();
	
	private final List<DisciplineItem>				mSubItemNames		= new ArrayList<DisciplineItem>();
	
	private final List<Ability>						mAbilities			= new ArrayList<Ability>();
	
	private DisciplineItem(final String aName)
	{
		mName = aName;
		mDescription = createDescription();
	}
	
	public void setParent(final DisciplineItem aParent)
	{
		mParent = aParent;
	}
	
	private void addSubItemName(final String aName)
	{
		mSubItems.put(aName, null);
	}
	
	private void addAbility(final Ability aAbility)
	{
		mAbilities.add(aAbility);
		Collections.sort(mAbilities);
	}
	
	public boolean isSubItem()
	{
		return mParent != null;
	}
	
	public boolean isParentItem()
	{
		return mSubItems.isEmpty();
	}
	
	public DisciplineItem getSubItem(final String aName)
	{
		return mSubItems.get(aName);
	}
	
	@Override
	public DisciplineItemValue createValue()
	{
		return new DisciplineItemValue(this);
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
	
	public List<Ability> getAbilities()
	{
		return mAbilities;
	}
	
	@Override
	public int getStartValue()
	{
		return START_VALUE;
	}
	
	public void addSubItem(final DisciplineItem aSubItem)
	{
		mSubItems.put(aSubItem.getName(), aSubItem);
		mSubItemNames.add(aSubItem);
		Collections.sort(mSubItemNames);
	}
	
	public List<DisciplineItem> getSubItems()
	{
		return mSubItemNames;
	}
	
	public Set<String> getSubItemNames()
	{
		return mSubItems.keySet();
	}
	
	public DisciplineItem getParent()
	{
		return mParent;
	}
	
	private String createDescription()
	{
		// TODO Implement
		return mName;
	}
	
	@Override
	public String getDescription()
	{
		return mDescription;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	@Override
	public int compareTo(final Item aAnother)
	{
		return getName().compareTo(mName);
	}
	
	@Override
	public int hashCode()
	{
		return mName.hashCode();
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
	
	public static DisciplineItem create(final String aData)
	{
		DisciplineItem discipline;
		if (aData.startsWith(PARENT_PREFIX))
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