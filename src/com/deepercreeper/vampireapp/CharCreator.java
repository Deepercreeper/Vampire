package com.deepercreeper.vampireapp;

import com.deepercreeper.vampireapp.controller.BackgroundController;
import com.deepercreeper.vampireapp.controller.BackgroundValueController;
import com.deepercreeper.vampireapp.controller.CreationMode;
import com.deepercreeper.vampireapp.controller.DisciplineController;
import com.deepercreeper.vampireapp.controller.DisciplineValueController;
import com.deepercreeper.vampireapp.controller.PropertyController;
import com.deepercreeper.vampireapp.controller.PropertyValueController;
import com.deepercreeper.vampireapp.controller.SimpleController;
import com.deepercreeper.vampireapp.controller.SimpleValueController;
import com.deepercreeper.vampireapp.controller.ValueController.PointHandler;

public class CharCreator
{
	public static final int					MIN_GENERATION	= 8, DEFAULT_GENERATION = 12, MAX_GENERATION = 12, START_FREE_POINTS = 20;
	
	private String							mName			= "";
	
	private String							mConcept		= "";
	
	private String							mNature;
	
	private String							mBehavior;
	
	private Clan							mClan;
	
	private int								mGeneration		= DEFAULT_GENERATION;
	
	private int								mFreePoints		= START_FREE_POINTS;
	
	private final DisciplineValueController	mDisciplines;
	
	private final PropertyValueController	mProperties;
	
	private final BackgroundValueController	mBackgrounds;
	
	private final SimpleValueController		mSimpleValues;
	
	public CharCreator(final MainActivity aContext, final DisciplineController aDisciplines, final PropertyController aProperties,
			final BackgroundController aBackgrounds, final SimpleController aSimpleItems, final String aNature, final String aBehavior,
			final Clan aClan)
	{
		final PointHandler points = new PointHandler()
		{
			@Override
			public int getPoints()
			{
				return mFreePoints;
			}
			
			@Override
			public void increase(final int aValue)
			{
				mFreePoints += aValue;
				aContext.setFreePoints(mFreePoints);
			}
			
			@Override
			public void decrease(final int aValue)
			{
				mFreePoints -= aValue;
				aContext.setFreePoints(mFreePoints);
			}
		};
		mDisciplines = new DisciplineValueController(aDisciplines, aContext, CreationMode.CREATION, points);
		mProperties = new PropertyValueController(aProperties, aContext, CreationMode.CREATION);
		mBackgrounds = new BackgroundValueController(aBackgrounds, aContext, CreationMode.CREATION, points);
		mSimpleValues = new SimpleValueController(aSimpleItems, aContext, CreationMode.CREATION, points);
		mNature = aNature;
		mBehavior = aBehavior;
		setClan(aClan, true);
	}
	
	public void resetFreePoints()
	{
		mDisciplines.resetTempPoints();
		mProperties.resetTempPoints();
		mBackgrounds.resetTempPoints();
		mSimpleValues.resetTempPoints();
		mFreePoints = START_FREE_POINTS;
	}
	
	public void setCreationMode(final CreationMode aMode)
	{
		mDisciplines.setCreationMode(aMode);
		mProperties.setCreationMode(aMode);
		mBackgrounds.setCreationMode(aMode);
		mSimpleValues.setCreationMode(aMode);
	}
	
	public void setFreePoints(final int aFreePoints)
	{
		mFreePoints = aFreePoints;
	}
	
	public int getFreePoints()
	{
		return mFreePoints;
	}
	
	public void release()
	{
		mDisciplines.release();
		mProperties.release();
		mBackgrounds.release();
		mSimpleValues.release();
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
	
	public void setClan(final Clan aClan, final boolean aForceSet)
	{
		if (aForceSet || !aClan.equals(mClan))
		{
			mClan = aClan;
			mDisciplines.close();
			mDisciplines.changeClan(aClan);
		}
		if (aClan.getDisciplines().isEmpty())
		{
			mDisciplines.setEnabled(false);
		}
		else
		{
			mDisciplines.setEnabled(true);
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
