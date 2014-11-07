package com.deepercreeper.vampireapp;

import android.widget.Toast;
import com.deepercreeper.vampireapp.controller.BackgroundController;
import com.deepercreeper.vampireapp.controller.BackgroundValueController;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.DisciplineController;
import com.deepercreeper.vampireapp.controller.DisciplineValueController;
import com.deepercreeper.vampireapp.controller.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.PropertyController;
import com.deepercreeper.vampireapp.controller.PropertyValueController;
import com.deepercreeper.vampireapp.controller.SimpleController;
import com.deepercreeper.vampireapp.controller.SimpleValueController;
import com.deepercreeper.vampireapp.controller.ValueController.PointHandler;

public class CharCreator
{
	public static final int					MIN_GENERATION		= 8, MAX_GENERATION = 12, START_FREE_POINTS = 15;
	
	private static final int				MAX_VOLITION_POINTS	= 20, MAX_PATH_POINTS = 10, DEFAULT_GENERATION = 12, START_VOLITION_POINTS = 5,
			START_PATH_POINTS = 5, VOLITION_POINTS_COST = 2, PATH_POINTS_COST = 1;
	
	private final Vampire					mVampire;
	
	private String							mName				= "";
	
	private String							mConcept			= "";
	
	private String							mNature;
	
	private String							mBehavior;
	
	private String							mPath				= null;
	
	private Clan							mClan;
	
	private int								mPathPoints			= START_PATH_POINTS;
	
	private int								mGeneration			= DEFAULT_GENERATION;
	
	private int								mFreePoints			= START_FREE_POINTS;
	
	private int								mVolitionPoints		= START_VOLITION_POINTS;
	
	private final DisciplineValueController	mDisciplines;
	
	private final PropertyValueController	mProperties;
	
	private final BackgroundValueController	mBackgrounds;
	
	private final SimpleValueController		mSimpleValues;
	
	public CharCreator(final Vampire aVampire, final DisciplineController aDisciplines, final PropertyController aProperties,
			final BackgroundController aBackgrounds, final SimpleController aSimpleItems, final String aNature, final String aBehavior,
			final Clan aClan)
	{
		mVampire = aVampire;
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
				mVampire.setFreePoints(mFreePoints);
			}
			
			@Override
			public void decrease(final int aValue)
			{
				mFreePoints -= aValue;
				mVampire.setFreePoints(mFreePoints);
			}
		};
		final UpdateAction updateOthers = new UpdateAction()
		{
			@Override
			public void update()
			{
				freePointsChanged();
			}
		};
		mDisciplines = new DisciplineValueController(aDisciplines, mVampire.getContext(), CharMode.MAIN, points, updateOthers);
		mProperties = new PropertyValueController(aProperties, mVampire.getContext(), CharMode.MAIN);
		mBackgrounds = new BackgroundValueController(aBackgrounds, mVampire.getContext(), CharMode.MAIN, points, updateOthers);
		mSimpleValues = new SimpleValueController(aSimpleItems, mVampire.getContext(), CharMode.MAIN, points, updateOthers);
		mNature = aNature;
		mBehavior = aBehavior;
		setClan(aClan);
	}
	
	public int getPathPoints()
	{
		return mPathPoints;
	}
	
	public void setPath(final String aPath)
	{
		mPath = aPath;
		if (mPath == null)
		{
			resetPath();
		}
		updatePathEnabled();
	}
	
	public void resetPath()
	{
		mFreePoints += (mPathPoints - START_PATH_POINTS) * PATH_POINTS_COST;
		freePointsChanged();
		
		mPathPoints = START_PATH_POINTS;
		updatePathEnabled();
		mVampire.setPathPoints(mPathPoints);
	}
	
	public String getPath()
	{
		return mPath;
	}
	
	private void freePointsChanged()
	{
		mDisciplines.updateValues(false);
		mSimpleValues.updateValues(false);
		mBackgrounds.updateValues(false);
		updateVolitionEnabled();
		updatePathEnabled();
		mVampire.setFreePoints(mFreePoints);
	}
	
	private void updateVolitionEnabled()
	{
		mVampire.setVolitionEnabled(mVolitionPoints < MAX_VOLITION_POINTS && mFreePoints >= VOLITION_POINTS_COST,
				mVolitionPoints > START_VOLITION_POINTS);
	}
	
	public int getVolitionPoints()
	{
		return mVolitionPoints;
	}
	
	private void updatePathEnabled()
	{
		mVampire.setPathEnabled(mPath != null, mPathPoints < MAX_PATH_POINTS && mFreePoints >= PATH_POINTS_COST, mPathPoints > START_PATH_POINTS);
	}
	
	public void increasePathPoints()
	{
		if (mPathPoints < MAX_PATH_POINTS && mFreePoints >= PATH_POINTS_COST)
		{
			mPathPoints++ ;
			mFreePoints -= PATH_POINTS_COST;
			freePointsChanged();
			mVampire.setPathPoints(mPathPoints);
		}
	}
	
	public void decreasePathPoints()
	{
		if (mPathPoints > START_PATH_POINTS)
		{
			mPathPoints-- ;
			mFreePoints += PATH_POINTS_COST;
			freePointsChanged();
			mVampire.setPathPoints(mPathPoints);
		}
	}
	
	public void increaseVolitionPoints()
	{
		if (mVolitionPoints < MAX_VOLITION_POINTS && mFreePoints >= VOLITION_POINTS_COST)
		{
			mVolitionPoints++ ;
			mFreePoints -= VOLITION_POINTS_COST;
			freePointsChanged();
			mVampire.setVolitionPoints(mVolitionPoints);
		}
	}
	
	public void decreaseVolitionPoints()
	{
		if (mVolitionPoints > START_VOLITION_POINTS)
		{
			mVolitionPoints-- ;
			mFreePoints += VOLITION_POINTS_COST;
			freePointsChanged();
			mVampire.setVolitionPoints(mVolitionPoints);
		}
	}
	
	public void resetFreePoints()
	{
		mDisciplines.resetTempPoints();
		mBackgrounds.resetTempPoints();
		mSimpleValues.resetTempPoints();
		
		resetPath();
		
		mFreePoints = START_FREE_POINTS;
		mVolitionPoints = START_VOLITION_POINTS;
		mPathPoints = START_PATH_POINTS;
		
		freePointsChanged();
		mVampire.setVolitionPoints(mVolitionPoints);
		mVampire.setPathPoints(mPathPoints);
	}
	
	public void setCreationMode(final CharMode aMode)
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
	
	public void releaseViews()
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
	
	public void setClan(final Clan aClan)
	{
		if ( !aClan.equals(mClan))
		{
			mClan = aClan;
			mDisciplines.close();
			mDisciplines.changeClan(aClan);
			Toast.makeText(mVampire.getContext(), mClan.getDescription(), Toast.LENGTH_LONG).show();
		}
		mDisciplines.setEnabled( !mClan.getDisciplines().isEmpty());
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
