package com.deepercreeper.vampireapp;

import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.DescriptionController;
import com.deepercreeper.vampireapp.controller.Nature;
import com.deepercreeper.vampireapp.controller.Path;
import com.deepercreeper.vampireapp.controller.backgrounds.BackgroundController;
import com.deepercreeper.vampireapp.controller.backgrounds.BackgroundValueController;
import com.deepercreeper.vampireapp.controller.descriptions.DescriptionValueController;
import com.deepercreeper.vampireapp.controller.disciplines.DisciplineController;
import com.deepercreeper.vampireapp.controller.disciplines.DisciplineValueController;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;
import com.deepercreeper.vampireapp.controller.properties.PropertyController;
import com.deepercreeper.vampireapp.controller.properties.PropertyValueController;
import com.deepercreeper.vampireapp.controller.simplesItems.SimpleController;
import com.deepercreeper.vampireapp.controller.simplesItems.SimpleValueController;

public class CharCreator
{
	public static final int						MIN_GENERATION	= 8, MAX_GENERATION = 12, START_FREE_POINTS = 15;
	
	private static final int					MAX_VOLITION_POINTS	= 20, MAX_PATH_POINTS = 10, DEFAULT_GENERATION = 12, START_VOLITION_POINTS = 5;
	
	private static final int					START_PATH_POINTS	= 5, VOLITION_POINTS_COST = 2, PATH_POINTS_COST = 1;
	
	private final Vampire						mVampire;
	
	private String								mName				= "";
	
	private String								mConcept			= "";
	
	private Nature								mNature;
	
	private Nature								mBehavior;
	
	private Path								mPath				= null;
	
	private Clan								mClan;
	
	private int									mPathPoints			= START_PATH_POINTS;
	
	private int									mGeneration			= DEFAULT_GENERATION;
	
	private int									mFreePoints			= START_FREE_POINTS;
	
	private int									mVolitionPoints		= START_VOLITION_POINTS;
	
	private final DisciplineValueController		mDisciplines;
	
	private final PropertyValueController		mProperties;
	
	private final BackgroundValueController		mBackgrounds;
	
	private final SimpleValueController			mSimpleValues;
	
	private final DescriptionValueController	mDescriptions;
	
	public CharCreator(final Vampire aVampire, final DisciplineController aDisciplines, final PropertyController aProperties,
			final BackgroundController aBackgrounds, final SimpleController aSimpleItems, final Nature aNature, final Nature aBehavior,
			final Clan aClan, final DescriptionController aDescriptions)
	{
		mVampire = aVampire;
		final PointHandler points = new PointHandler()
		{
			@Override
			public void decrease(final int aValue)
			{
				mFreePoints -= aValue;
				mVampire.setFreePoints(mFreePoints);
			}
			
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
		mDescriptions = new DescriptionValueController(aDescriptions);
		mNature = aNature;
		mBehavior = aBehavior;
		setClan(aClan);
	}
	
	public void clearDescriptions()
	{
		mDescriptions.clear();
	}
	
	public DescriptionValueController getDescriptions()
	{
		return mDescriptions;
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
	
	public BackgroundValueController getBackgrounds()
	{
		return mBackgrounds;
	}
	
	public List<ItemValue<?>> getDescriptionValues()
	{
		final List<ItemValue<?>> list = new ArrayList<ItemValue<?>>();
		list.addAll(mProperties.getDescriptionValues());
		list.addAll(mBackgrounds.getDescriptionValues());
		list.addAll(mSimpleValues.getDescriptionValues());
		return list;
	}
	
	public Nature getBehavior()
	{
		return mBehavior;
	}
	
	public Clan getClan()
	{
		return mClan;
	}
	
	public String getConcept()
	{
		return mConcept;
	}
	
	public DisciplineValueController getDisciplines()
	{
		return mDisciplines;
	}
	
	public int getFreePoints()
	{
		return mFreePoints;
	}
	
	public int getGeneration()
	{
		return mGeneration;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public Nature getNature()
	{
		return mNature;
	}
	
	public Path getPath()
	{
		return mPath;
	}
	
	public int getPathPoints()
	{
		return mPathPoints;
	}
	
	public PropertyValueController getProperties()
	{
		return mProperties;
	}
	
	public SimpleValueController getSimpleValues()
	{
		return mSimpleValues;
	}
	
	public int getVolitionPoints()
	{
		return mVolitionPoints;
	}
	
	public boolean hasPath()
	{
		return mPath != null;
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
	
	public void releaseViews()
	{
		mDisciplines.release();
		mProperties.release();
		mBackgrounds.release();
		mSimpleValues.release();
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
	
	public void resetPath()
	{
		mFreePoints += (mPathPoints - START_PATH_POINTS) * PATH_POINTS_COST;
		freePointsChanged();
		
		mPathPoints = START_PATH_POINTS;
		updatePathEnabled();
		mVampire.setPathPoints(mPathPoints);
	}
	
	public void setBehavior(final Nature aBehavior)
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
	
	public void setConcept(final String aConcept)
	{
		mConcept = aConcept;
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
	
	public void setGeneration(final int aGeneration)
	{
		mGeneration = aGeneration;
	}
	
	public void setName(final String aName)
	{
		mName = aName;
	}
	
	public void setNature(final Nature aNature)
	{
		mNature = aNature;
	}
	
	public void setPath(final Path aPath)
	{
		mPath = aPath;
		if (mPath == null)
		{
			resetPath();
		}
		updatePathEnabled();
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
	
	private void updatePathEnabled()
	{
		mVampire.setPathEnabled(mPath != null, mPathPoints < MAX_PATH_POINTS && mFreePoints >= PATH_POINTS_COST, mPathPoints > START_PATH_POINTS);
	}
	
	private void updateVolitionEnabled()
	{
		mVampire.setVolitionEnabled(mVolitionPoints < MAX_VOLITION_POINTS && mFreePoints >= VOLITION_POINTS_COST,
				mVolitionPoints > START_VOLITION_POINTS);
	}
}
