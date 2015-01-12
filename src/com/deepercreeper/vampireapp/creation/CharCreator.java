package com.deepercreeper.vampireapp.creation;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.widget.TableLayout;
import com.deepercreeper.vampireapp.Vampire;
import com.deepercreeper.vampireapp.controllers.GenerationCreationValueController;
import com.deepercreeper.vampireapp.controllers.InsanityCreationValueController;
import com.deepercreeper.vampireapp.controllers.descriptions.DescriptionController;
import com.deepercreeper.vampireapp.controllers.descriptions.DescriptionCreationValueController;
import com.deepercreeper.vampireapp.controllers.dynamic.implementations.creations.ItemControllerCreationImpl;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemController;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemControllerCreation.PointHandler;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.controllers.lists.Clan;
import com.deepercreeper.vampireapp.controllers.lists.Nature;
import com.deepercreeper.vampireapp.controllers.lists.Path;
import com.deepercreeper.vampireapp.controllers.restrictions.Restriction;
import com.deepercreeper.vampireapp.controllers.restrictions.Restriction.RestrictionType;

/**
 * This class is used to create characters. It handles all values that need to be created<br>
 * and initializes the UI at creation time.
 * 
 * @author vrl
 */
public class CharCreator
{
	/**
	 * The default minimum generation that is set, when creating a character.
	 */
	public static final int								MIN_GENERATION		= 8;
	
	/**
	 * The default maximum generation that is set, when creating a character.
	 */
	public static final int								MAX_GENERATION		= 12;
	
	/**
	 * The default number of free bonus points, a user can spend into his new created character.
	 */
	public static final int								START_FREE_POINTS	= 15;
	
	private static final int							MAX_VOLITION_POINTS	= 20, MAX_PATH_POINTS = 10, START_VOLITION_POINTS = 5;
	
	private static final int							START_PATH_POINTS	= 5, VOLITION_POINTS_COST = 2, PATH_POINTS_COST = 1;
	
	private final Vampire								mVampire;
	
	private String										mName				= "";
	
	private String										mConcept			= "";
	
	private Nature										mNature;
	
	private Nature										mBehavior;
	
	private Path										mPath				= null;
	
	private Clan										mClan;
	
	private int											mPathPoints			= START_PATH_POINTS;
	
	private final GenerationCreationValueController		mGeneration;
	
	private int											mFreePoints			= START_FREE_POINTS;
	
	private int											mVolitionPoints		= START_VOLITION_POINTS;
	
	private final List<ItemControllerCreation>			mControllers;
	
	private final DescriptionCreationValueController	mDescriptions;
	
	private final InsanityCreationValueController		mInsanities;
	
	/**
	 * Creates a new character creator and initializes all values for the first time.
	 * 
	 * @param aVampire
	 *            The vampire application.
	 * @param aNature
	 *            The start nature.
	 * @param aBehavior
	 *            The start behavior.
	 * @param aClan
	 *            The start clan.
	 * @param aDescriptions
	 *            The description fields.
	 */
	public CharCreator(final Vampire aVampire, final List<ItemController> aControllers, final Nature aNature, final Nature aBehavior,
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
		mControllers = new ArrayList<ItemControllerCreation>();
		for (final ItemController controller : aControllers)
		{
			mControllers.add(new ItemControllerCreationImpl(controller, aVampire.getContext(), CreationMode.MAIN, points));
		}
		mDescriptions = new DescriptionCreationValueController(aDescriptions);
		mInsanities = new InsanityCreationValueController(mVampire.getContext(), this);
		mGeneration = new GenerationCreationValueController(mVampire.getContext(), this);
		mNature = aNature;
		mBehavior = aBehavior;
		setClan(aClan);
	}
	
	/**
	 * Adds an insanity to the current list.
	 * 
	 * @param aInsanity
	 *            The new insanity.
	 */
	public void addInsanity(final String aInsanity)
	{
		mInsanities.addInsanity(aInsanity);
	}
	
	/**
	 * Removes all user defined descriptions.
	 */
	public void clearDescriptions()
	{
		mDescriptions.clear();
		mInsanities.clear();
	}
	
	/**
	 * Decreases the number of points currently spent for the path.
	 */
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
	
	/**
	 * Decreases the number of points currently spent for the volition.
	 */
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
	
	public List<ItemControllerCreation> getControllers()
	{
		return mControllers;
	}
	
	/**
	 * @return the current behavior.
	 */
	public Nature getBehavior()
	{
		return mBehavior;
	}
	
	/**
	 * @return the current clan.
	 */
	public Clan getClan()
	{
		return mClan;
	}
	
	/**
	 * @return the current concept.
	 */
	public String getConcept()
	{
		return mConcept;
	}
	
	/**
	 * @return the descriptions controller.
	 */
	public DescriptionCreationValueController getDescriptions()
	{
		return mDescriptions;
	}
	
	/**
	 * @return a list of all item values that currently need a description.
	 */
	public List<ItemCreation> getDescriptionValues()
	{
		final List<ItemCreation> list = new ArrayList<ItemCreation>();
		for (final ItemControllerCreation controller : mControllers)
		{
			list.addAll(controller.getDescriptionValues());
		}
		return list;
	}
	
	/**
	 * @return the number of free bonus points, available at this time.
	 */
	public int getFreePoints()
	{
		return mFreePoints;
	}
	
	/**
	 * @return the generation controller.
	 */
	public GenerationCreationValueController getGeneration()
	{
		return mGeneration;
	}
	
	/**
	 * @return a list of all insanities.
	 */
	public List<String> getInsanities()
	{
		return mInsanities.getInsanities();
	}
	
	/**
	 * @return the character name.
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * @return the current nature.
	 */
	public Nature getNature()
	{
		return mNature;
	}
	
	/**
	 * @return the current path.
	 */
	public Path getPath()
	{
		return mPath;
	}
	
	/**
	 * @return the number of points currently spent for the path.
	 */
	public int getPathPoints()
	{
		return mPathPoints;
	}
	
	/**
	 * @return the number of points currently spent for the volition.
	 */
	public int getVolitionPoints()
	{
		return mVolitionPoints;
	}
	
	/**
	 * @return whether a path is selected at this time.
	 */
	public boolean hasPath()
	{
		return mPath != null;
	}
	
	/**
	 * Increases the number of points spent for the path.
	 */
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
	
	/**
	 * Increases the number of points spent for the volition.
	 */
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
	
	/**
	 * Initializes the insanities table into the given one.
	 * 
	 * @param aTable
	 *            The table.
	 */
	public void initInsanities(final TableLayout aTable)
	{
		mInsanities.init(aTable);
	}
	
	/**
	 * @return whether the insanities are currently OK.
	 */
	public boolean insanitiesOk()
	{
		return mInsanities.isOk();
	}
	
	/**
	 * Releases the insanities table.
	 */
	public void releaseInsanities()
	{
		mInsanities.release();
	}
	
	/**
	 * Releases all item controller views.
	 */
	public void releaseViews()
	{
		for (final ItemControllerCreation controller : mControllers)
		{
			controller.release();
		}
	}
	
	/**
	 * Removes an insanity from the current list.
	 * 
	 * @param aInsanity
	 */
	public void removeInsanity(final String aInsanity)
	{
		mInsanities.remove(aInsanity);
	}
	
	/**
	 * Resets the free bonus points spent.
	 */
	public void resetFreePoints()
	{
		for (final ItemControllerCreation controller : mControllers)
		{
			controller.resetTempPoints();
		}
		
		resetPath();
		
		mFreePoints = START_FREE_POINTS;
		mVolitionPoints = START_VOLITION_POINTS;
		mPathPoints = START_PATH_POINTS;
		
		freePointsChanged();
		mVampire.setVolitionPoints(mVolitionPoints);
		mVampire.setPathPoints(mPathPoints);
	}
	
	/**
	 * Resets the path.
	 */
	public void resetPath()
	{
		mFreePoints += (mPathPoints - START_PATH_POINTS) * PATH_POINTS_COST;
		freePointsChanged();
		
		mPathPoints = START_PATH_POINTS;
		updatePathEnabled();
		mVampire.setPathPoints(mPathPoints);
	}
	
	/**
	 * Sets the current behavior.
	 * 
	 * @param aBehavior
	 *            The new behavior.
	 */
	public void setBehavior(final Nature aBehavior)
	{
		mBehavior = aBehavior;
	}
	
	/**
	 * Sets the current clan.
	 * 
	 * @param aClan
	 *            The new clan.
	 */
	public void setClan(final Clan aClan)
	{
		if ( !aClan.equals(mClan))
		{
			if (mClan != null)
			{
				removeRestrictions();
			}
			mClan = aClan;
			addRestrictions();
			// Toast.makeText(mVampire.getContext(), mClan.getDescription(), Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Sets the current concept.
	 * 
	 * @param aConcept
	 *            The new concept.
	 */
	public void setConcept(final String aConcept)
	{
		mConcept = aConcept;
	}
	
	/**
	 * Sets the current creation mode.
	 * 
	 * @param aMode
	 *            The new creation mode.
	 */
	public void setCreationMode(final CreationMode aMode)
	{
		for (final ItemControllerCreation controller : mControllers)
		{
			controller.setCreationMode(aMode);
		}
	}
	
	/**
	 * Sets the number of free points.
	 * 
	 * @param aFreePoints
	 *            The new number of free points.
	 */
	public void setFreePoints(final int aFreePoints)
	{
		mFreePoints = aFreePoints;
	}
	
	/**
	 * Sets whether the insanities are currently OK.
	 * 
	 * @param aOk
	 *            Whether the are OK.
	 */
	public void setInsanitiesOk(final boolean aOk)
	{
		mVampire.setInsanitiesOk(aOk);
	}
	
	/**
	 * Sets the current character name.
	 * 
	 * @param aName
	 *            The new name.
	 */
	public void setName(final String aName)
	{
		mName = aName;
	}
	
	/**
	 * Sets the current nature.
	 * 
	 * @param aNature
	 *            The new nature.
	 */
	public void setNature(final Nature aNature)
	{
		mNature = aNature;
	}
	
	/**
	 * Sets the current path.
	 * 
	 * @param aPath
	 *            The new path.
	 */
	public void setPath(final Path aPath)
	{
		mPath = aPath;
		if (mPath == null)
		{
			resetPath();
		}
		updatePathEnabled();
	}
	
	private void addRestrictions()
	{
		for (final Restriction restriction : mClan.getRestrictions())
		{
			final RestrictionType type = restriction.getRestrictionType();
			if (type.equals(RestrictionType.ITEM_VALUE) || type.equals(RestrictionType.ITEM_CHILDREN_COUNT)
					|| type.equals(RestrictionType.ITEM_CHILD_VALUE_AT) || type.equals(RestrictionType.GROUP_CHILDREN)
					|| type.equals(RestrictionType.GROUP_CHILDREN_COUNT))
			{
				final boolean item = type.equals(RestrictionType.ITEM_VALUE) || type.equals(RestrictionType.ITEM_CHILDREN_COUNT)
						|| type.equals(RestrictionType.ITEM_CHILD_VALUE_AT);
				final String itemName = restriction.getItemName();
				boolean foundItem = false;
				for (final ItemControllerCreation controller : mControllers)
				{
					if (item && controller.hasItem(itemName) || !item && controller.hasGroup(itemName))
					{
						controller.addRestriction(restriction);
						foundItem = true;
					}
				}
				if ( !foundItem)
				{
					Log.w("CharCreator", "Couldn't find the item of a restriction.");
				}
			}
			else if (type.equals(RestrictionType.INSANITY))
			{
				mInsanities.addRestriction(restriction);
			}
			else if (type.equals(RestrictionType.VOLITION))
			{
				// TODO Restrict the number of volition points
			}
			else if (type.equals(RestrictionType.PATH))
			{
				// TODO Restrict the number of path points
			}
			else if (type.equals(RestrictionType.GENERATION))
			{
				mGeneration.addRestriction(restriction);
			}
		}
	}
	
	private void freePointsChanged()
	{
		for (final ItemControllerCreation controller : mControllers)
		{
			controller.updateGroups();
		}
		updateVolitionEnabled();
		updatePathEnabled();
		mVampire.setFreePoints(mFreePoints);
	}
	
	private void removeRestrictions()
	{
		for (final Restriction restriction : mClan.getRestrictions())
		{
			restriction.clear();
		}
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
