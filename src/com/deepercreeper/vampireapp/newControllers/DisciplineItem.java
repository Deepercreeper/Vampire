package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DisciplineItem implements Item
{
	private final String							mName;
	
	private final String							mDescription;
	
	private final DisciplineItem					mParent;
	
	private final HashMap<String, DisciplineItem>	mSubItems		= new HashMap<String, DisciplineItem>();
	
	private final List<DisciplineItem>				mSubItemNames	= new ArrayList<DisciplineItem>();
	
	private final List<Ability>						mAbilities		= new ArrayList<Ability>();
	
	public DisciplineItem(final String aName, final DisciplineItem aParent)
	{
		mName = aName;
		mParent = aParent;
		mDescription = createDescription();
	}
	
	public void emptyMethod()
	{
		// TODO Init abilities
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
	
	public DisciplineItem(final String aName)
	{
		this(aName, null);
	}
	
	public List<Ability> getAbilities()
	{
		return mAbilities;
	}
	
	public void initSubItems(final Set<DisciplineItem> aSubItems)
	{
		for (final DisciplineItem subItem : aSubItems)
		{
			mSubItems.put(subItem.getName(), subItem);
			mSubItemNames.add(subItem);
		}
		Collections.sort(mSubItemNames);
	}
	
	public List<DisciplineItem> getSubItems()
	{
		return mSubItemNames;
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
}
