package com.deepercreeper.vampireapp;

import android.content.Context;
import com.deepercreeper.vampireapp.newControllers.BackgroundController;
import com.deepercreeper.vampireapp.newControllers.BackgroundValueController;
import com.deepercreeper.vampireapp.newControllers.DisciplineController;
import com.deepercreeper.vampireapp.newControllers.DisciplineItem;
import com.deepercreeper.vampireapp.newControllers.DisciplineValueController;
import com.deepercreeper.vampireapp.newControllers.PropertyController;
import com.deepercreeper.vampireapp.newControllers.PropertyValueController;
import com.deepercreeper.vampireapp.newControllers.SimpleController;
import com.deepercreeper.vampireapp.newControllers.SimpleValueController;

public class CharCreator
{
	public static final int					MIN_GENERATION	= 8, DEFAULT_GENERATION = 12, MAX_GENERATION = 12;
	
	private String							mName			= "";
	
	private String							mConcept		= "";
	
	private String							mNature;
	
	private String							mBehavior;
	
	private Clan							mClan;
	
	private int								mGeneration		= DEFAULT_GENERATION;
	
	private final DisciplineValueController	mDisciplines;
	
	private final PropertyValueController	mProperties;
	
	private final BackgroundValueController	mBackgrounds;
	
	private final SimpleValueController		mSimpleValues;
	
	public CharCreator(final Context aContext, final DisciplineController aDisciplines, final PropertyController aProperties,
			final BackgroundController aBackgrounds, final SimpleController aSimpleItems, final String aNature, final String aBehavior,
			final Clan aClan)
	{
		mDisciplines = new DisciplineValueController(aDisciplines, aContext, true);
		mProperties = new PropertyValueController(aProperties, aContext, true);
		mBackgrounds = new BackgroundValueController(aBackgrounds, aContext, true);
		mSimpleValues = new SimpleValueController(aSimpleItems, aContext, true);
		mNature = aNature;
		mBehavior = aBehavior;
		setClan(aClan);
	}
	
	public SimpleValueController getSimpleValues()
	{
		return mSimpleValues;
	}
	
	public PropertyValueController getProperties()
	{
		return mProperties;
	}
	
	public DisciplineValueController getDisciplines()
	{
		return mDisciplines;
	}
	
	public BackgroundValueController getBackgrounds()
	{
		return mBackgrounds;
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
		for (final DisciplineItem discipline : mClan.getDisciplines())
		{
			mDisciplines.addItem(discipline);
		}
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
}
