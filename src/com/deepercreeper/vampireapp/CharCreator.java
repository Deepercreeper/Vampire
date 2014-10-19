package com.deepercreeper.vampireapp;

import com.deepercreeper.vampireapp.controller.BackgroundController;
import com.deepercreeper.vampireapp.controller.BackgroundValueController;
import com.deepercreeper.vampireapp.controller.DisciplineController;
import com.deepercreeper.vampireapp.controller.DisciplineValueController;
import com.deepercreeper.vampireapp.controller.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.Mode;
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
	
	private final MainActivity				mActivity;
	
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
	
	public CharCreator(final MainActivity aActivity, final DisciplineController aDisciplines, final PropertyController aProperties,
			final BackgroundController aBackgrounds, final SimpleController aSimpleItems, final String aNature, final String aBehavior,
			final Clan aClan)
	{
		mActivity = aActivity;
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
				mActivity.setFreePoints(mFreePoints);
			}
			
			@Override
			public void decrease(final int aValue)
			{
				mFreePoints -= aValue;
				mActivity.setFreePoints(mFreePoints);
			}
		};
		final UpdateAction updateDisciplineOthers = new UpdateAction()
		{
			@Override
			public void update()
			{
				mSimpleValues.updateValues(false);
				mBackgrounds.updateValues(false);
				updateVolition();
				updatePath();
			}
		};
		final UpdateAction updateSimpleOthers = new UpdateAction()
		{
			@Override
			public void update()
			{
				mDisciplines.updateValues(false);
				mBackgrounds.updateValues(false);
				updateVolition();
				updatePath();
			}
		};
		final UpdateAction updateBackgroundOthers = new UpdateAction()
		{
			@Override
			public void update()
			{
				mDisciplines.updateValues(false);
				mSimpleValues.updateValues(false);
				updateVolition();
				updatePath();
			}
		};
		mDisciplines = new DisciplineValueController(aDisciplines, mActivity, Mode.CREATION, points, updateDisciplineOthers);
		mProperties = new PropertyValueController(aProperties, mActivity, Mode.CREATION);
		mBackgrounds = new BackgroundValueController(aBackgrounds, mActivity, Mode.CREATION, points, updateBackgroundOthers);
		mSimpleValues = new SimpleValueController(aSimpleItems, mActivity, Mode.CREATION, points, updateSimpleOthers);
		mNature = aNature;
		mBehavior = aBehavior;
		setClan(aClan, true);
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
			resetPath(true);
		}
		updatePath();
	}
	
	public void resetPath(final boolean aAddFreePoints)
	{
		if (aAddFreePoints)
		{
			mFreePoints += (mPathPoints - START_PATH_POINTS) * PATH_POINTS_COST;
			updateFreePointsValues();
		}
		mPathPoints = START_PATH_POINTS;
		mActivity.setPathPoints(mPathPoints);
		updatePath();
		mActivity.setFreePoints(mFreePoints);
	}
	
	public String getPath()
	{
		return mPath;
	}
	
	public void updateFreePointsValues()
	{
		mDisciplines.updateValues(false);
		mSimpleValues.updateValues(false);
		mBackgrounds.updateValues(false);
		updateVolition();
		updatePath();
	}
	
	private void updateVolition()
	{
		mActivity.setVolitionChangeEnabled(mVolitionPoints < MAX_VOLITION_POINTS && mFreePoints >= VOLITION_POINTS_COST,
				mVolitionPoints > START_VOLITION_POINTS);
	}
	
	public int getVolitionPoints()
	{
		return mVolitionPoints;
	}
	
	private void updatePath()
	{
		mActivity.setPathChangeEnabled(mPathPoints < MAX_PATH_POINTS && mFreePoints >= PATH_POINTS_COST, mPathPoints > START_PATH_POINTS,
				mPath != null);
	}
	
	public void increasePathPoints()
	{
		if (mPathPoints < MAX_PATH_POINTS && mFreePoints >= PATH_POINTS_COST)
		{
			mPathPoints++ ;
			mFreePoints -= PATH_POINTS_COST;
			updateFreePointsValues();
			mActivity.setPathPoints(mPathPoints);
			mActivity.setFreePoints(mFreePoints);
		}
	}
	
	public void decreasePathPoints()
	{
		if (mPathPoints > START_PATH_POINTS)
		{
			mPathPoints-- ;
			mFreePoints += PATH_POINTS_COST;
			updateFreePointsValues();
			mActivity.setPathPoints(mPathPoints);
			mActivity.setFreePoints(mFreePoints);
		}
	}
	
	public void increaseVolitionPoints()
	{
		if (mVolitionPoints < MAX_VOLITION_POINTS && mFreePoints >= VOLITION_POINTS_COST)
		{
			mVolitionPoints++ ;
			mFreePoints -= VOLITION_POINTS_COST;
			updateFreePointsValues();
			mActivity.setVolitionPoints(mVolitionPoints);
			mActivity.setFreePoints(mFreePoints);
		}
	}
	
	public void decreaseVolitionPoints()
	{
		if (mVolitionPoints > START_VOLITION_POINTS)
		{
			mVolitionPoints-- ;
			mFreePoints += VOLITION_POINTS_COST;
			updateFreePointsValues();
			mActivity.setVolitionPoints(mVolitionPoints);
			mActivity.setFreePoints(mFreePoints);
		}
	}
	
	public void resetFreePoints()
	{
		mDisciplines.resetTempPoints();
		mBackgrounds.resetTempPoints();
		mSimpleValues.resetTempPoints();
		mFreePoints = START_FREE_POINTS;
		mVolitionPoints = START_VOLITION_POINTS;
		resetPath(false);
		mDisciplines.updateValues(false);
		mBackgrounds.updateValues(false);
		mSimpleValues.updateValues(false);
		mActivity.setVolitionPoints(mVolitionPoints);
		updateVolition();
	}
	
	public void setCreationMode(final Mode aMode)
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
