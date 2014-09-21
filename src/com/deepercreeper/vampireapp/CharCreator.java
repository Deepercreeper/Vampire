package com.deepercreeper.vampireapp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class CharCreator
{
	public static final int											MIN_GENERATION	= 8, DEFAULT_GENERATION = 12, MAX_GENERATION = 12;
	
	private String													mName			= "";
	
	private String													mConcept		= "";
	
	private String													mNature;
	
	private String													mBehavior;
	
	private Clan													mClan;
	
	private int														mGeneration		= DEFAULT_GENERATION;
	
	private final HashMap<String, CreationDiscipline>				mDisciplines	= new HashMap<>();
	
	private final HashMap<String, HashMap<String, CreationItem>>	mAttributes, mAbilities;
	
	private final HashMap<Background, CreationBackground>			mBackgrounds	= new HashMap<>();
	
	private final HashMap<Property, CreationProperty>				mProperties		= new HashMap<>();
	
	public CharCreator(final HashMap<String, HashMap<String, CreationItem>> aAttributes,
			final HashMap<String, HashMap<String, CreationItem>> aAbilities, final String aNature, final String aBehavior, final Clan aClan)
	{
		mAttributes = aAttributes;
		mAbilities = aAbilities;
		mNature = aNature;
		mBehavior = aBehavior;
		setClan(aClan);
	}
	
	public void increaseItem(final Item aItem)
	{
		if (canIncreaseItem(aItem))
		{
			final CreationItem item;
			if (aItem.isAttribute())
			{
				item = mAttributes.get(aItem.getParent()).get(aItem.getName());
			}
			else
			{
				item = mAbilities.get(aItem.getParent()).get(aItem.getName());
			}
			item.increase();
		}
	}
	
	public void decreaseItem(final Item aItem)
	{
		final CreationItem item;
		if (aItem.isAttribute())
		{
			item = mAttributes.get(aItem.getParent()).get(aItem.getName());
		}
		else
		{
			item = mAbilities.get(aItem.getParent()).get(aItem.getName());
		}
		item.decrease();
	}
	
	public void increaseSubDiscipline(final Discipline aDiscipline, final boolean aFirst)
	{
		if (canIncreaseDiscipline(getDiscipline(aDiscipline).getSubDiscipline(aFirst).getDiscipline()))
		{
			getDiscipline(aDiscipline).getSubDiscipline(aFirst).increase();
		}
	}
	
	public void decreaseSubDiscipline(final Discipline aDiscipline, final boolean aFirst)
	{
		if (canDecreaseDiscipline(getDiscipline(aDiscipline).getSubDiscipline(aFirst).getDiscipline()))
		{
			getDiscipline(aDiscipline).getSubDiscipline(aFirst).decrease();
		}
	}
	
	public void increaseDiscipline(final Discipline aDiscipline)
	{
		if (canIncreaseDiscipline(aDiscipline))
		{
			final CreationDiscipline discipline = mDisciplines.get(aDiscipline.getName());
			discipline.increase();
		}
	}
	
	public void decreaseDiscipline(final Discipline aDiscipline)
	{
		final CreationDiscipline discipline = mDisciplines.get(aDiscipline.getName());
		discipline.decrease();
	}
	
	public void increaseBackground(final Background aBackground)
	{
		if (canIncreaseBackground(aBackground))
		{
			getBackground(aBackground).increase();
		}
	}
	
	public void decreaseBackground(final Background aBackground)
	{
		getBackground(aBackground).decrease();
	}
	
	public void increaseProperty(final Property aProperty)
	{
		if (canIncreaseProperty(aProperty))
		{
			getProperty(aProperty).increase();
		}
	}
	
	public void decreaseProperty(final Property aProperty)
	{
		if (canDecreaseProperty(aProperty))
		{
			getProperty(aProperty).decrease();
		}
	}
	
	public void setName(final String aName)
	{
		mName = aName;
	}
	
	public void setConcept(final String aConcept)
	{
		mConcept = aConcept;
	}
	
	public void setNature(final String aNature)
	{
		mNature = aNature;
	}
	
	public void setBehavior(final String aBehavior)
	{
		mBehavior = aBehavior;
	}
	
	public void setClan(final Clan aClan)
	{
		mClan = aClan;
		mDisciplines.clear();
		for (final Discipline discipline : mClan.getDisciplines())
		{
			mDisciplines.put(discipline.getName(), new CreationDiscipline(discipline));
		}
	}
	
	public void removeProperty(final Property aProperty)
	{
		mProperties.remove(aProperty);
	}
	
	public void removeBackground(final Background aBackground)
	{
		mBackgrounds.remove(aBackground);
	}
	
	public Set<Property> getProperties()
	{
		return mProperties.keySet();
	}
	
	public Set<Background> getBackgrounds()
	{
		return mBackgrounds.keySet();
	}
	
	public int getPropertiesCount()
	{
		return mProperties.size();
	}
	
	public int getBackgroundsCount()
	{
		return mBackgrounds.size();
	}
	
	public CreationProperty getProperty(final Property aProperty)
	{
		return mProperties.get(aProperty);
	}
	
	public CreationBackground getBackground(final Background aBackground)
	{
		return mBackgrounds.get(aBackground);
	}
	
	public void addProperty(final CreationProperty aProperty)
	{
		mProperties.put(aProperty.getProperty(), aProperty);
	}
	
	public void addBackground(final CreationBackground aBackground)
	{
		mBackgrounds.put(aBackground.getBackground(), aBackground);
	}
	
	public CreationDiscipline getDiscipline(final Discipline aDiscipline)
	{
		return mDisciplines.get(aDiscipline.getName());
	}
	
	public void setGeneration(final int aGeneration)
	{
		mGeneration = aGeneration;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public String getConcept()
	{
		return mConcept;
	}
	
	public String getNature()
	{
		return mNature;
	}
	
	public String getBehavior()
	{
		return mBehavior;
	}
	
	public Clan getClan()
	{
		return mClan;
	}
	
	public int getGeneration()
	{
		return mGeneration;
	}
	
	public CreationItem getItem(final Item aItem)
	{
		if (aItem.isAttribute())
		{
			return mAttributes.get(aItem.getParent()).get(aItem.getName());
		}
		return mAbilities.get(aItem.getParent()).get(aItem.getName());
	}
	
	private HashMap<String, Integer> getValues(final HashMap<String, HashMap<String, CreationItem>> aParents)
	{
		final HashMap<String, Integer> values = new HashMap<>();
		for (final String parent : aParents.keySet())
		{
			int value = 0;
			for (final CreationItem item : aParents.get(parent).values())
			{
				value += item.getValue();
			}
			values.put(parent, value);
		}
		return values;
	}
	
	private boolean canIncreaseProperty(final Property aProperty)
	{
		if (aProperty.isNegative())
		{
			return true;
		}
		int value = 0;
		for (final CreationProperty property : mProperties.values())
		{
			value += property.getValue() * (property.getProperty().isNegative() ? -1 : 1);
		}
		final int valueId = getProperty(aProperty).getValueId();
		final int[] values = aProperty.getValues();
		if (values.length > valueId + 1 && value - values[valueId] + values[valueId + 1] <= 0)
		{
			return true;
		}
		return false;
	}
	
	private boolean canDecreaseProperty(final Property aProperty)
	{
		if ( !aProperty.isNegative())
		{
			return true;
		}
		int value = 0;
		for (final CreationProperty property : mProperties.values())
		{
			value += property.getValue() * (property.getProperty().isNegative() ? -1 : 1);
		}
		final int valueId = getProperty(aProperty).getValueId();
		final int[] values = aProperty.getValues();
		if (valueId > 0 && value + values[valueId] - values[valueId - 1] <= 0)
		{
			return true;
		}
		return false;
	}
	
	private boolean canIncreaseBackground(final Background aBackground)
	{
		int value = 0;
		for (final CreationBackground background : mBackgrounds.values())
		{
			value += background.getValue();
		}
		return value < Background.START_POINTS;
	}
	
	private boolean canIncreaseDiscipline(final Discipline aDiscipline)
	{
		if (aDiscipline.isSubDiscipline())
		{
			final CreationDiscipline parent = getDiscipline(aDiscipline.getParentDiscipline());
			if (parent.hasSubDiscipline(false) && parent.getSubDiscipline(false).getDiscipline().equals(aDiscipline))
			{
				if ( !parent.hasSubDiscipline(true) || parent.getSubDiscipline(true).getValue() < 2)
				{
					return false;
				}
			}
		}
		int values = 0;
		for (final CreationDiscipline discipline : mDisciplines.values())
		{
			if (discipline.getDiscipline().isParentDiscipline())
			{
				if (discipline.hasSubDiscipline(true))
				{
					values += discipline.getSubDiscipline(true).getValue();
				}
				if (discipline.hasSubDiscipline(false))
				{
					values += discipline.getSubDiscipline(false).getValue();
				}
			}
			else
			{
				values += discipline.getValue();
			}
		}
		return values < Discipline.START_POINTS;
	}
	
	private boolean canDecreaseDiscipline(final Discipline aDiscipline)
	{
		if (aDiscipline.isSubDiscipline())
		{
			final CreationDiscipline parent = getDiscipline(aDiscipline.getParentDiscipline());
			if (parent.hasSubDiscipline(true) && parent.getSubDiscipline(true).getDiscipline().equals(aDiscipline))
			{
				if (parent.getSubDiscipline(true).getValue() == 2 && parent.hasSubDiscipline(false) && parent.getSubDiscipline(false).getValue() > 0)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean canIncreaseItem(final Item aItem)
	{
		final int[] maxPoints = aItem.getMaxPoints();
		final HashMap<String, Integer> values = getValues(aItem.isAttribute() ? mAttributes : mAbilities);
		final int parentValue = values.get(aItem.getParent());
		values.remove(aItem.getParent());
		if (parentValue < maxPoints[0])
		{
			return true;
		}
		boolean maxDone = false, midDone = false;
		if (Collections.max(values.values()) > maxPoints[1])
		{
			maxDone = true;
		}
		if (Collections.min(values.values()) > maxPoints[0])
		{
			midDone = true;
		}
		if (parentValue == maxPoints[0] && midDone)
		{
			return false;
		}
		if (parentValue == maxPoints[1] && maxDone)
		{
			return false;
		}
		return parentValue < maxPoints[2];
	}
}
