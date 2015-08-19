package com.deepercreeper.vampireapp.character.creation;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.implementations.creations.ItemControllerCreationImpl;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation.CreationRestrictionType;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionControllerCreation;
import com.deepercreeper.vampireapp.lists.items.Clan;
import com.deepercreeper.vampireapp.lists.items.Nature;
import com.deepercreeper.vampireapp.util.Log;
import android.content.Context;

/**
 * This class is used to create characters. It handles all values that need to be created<br>
 * and initializes the UI at creation time.
 * 
 * @author vrl
 */
public class CharacterCreation
{
	/**
	 * A listener for character creation changes.
	 * 
	 * @author vrl
	 */
	public interface CharCreationListener
	{
		/**
		 * The free points of this creation have changed.
		 * 
		 * @param aValue
		 *            The new number of free points.
		 */
		public void setFreePoints(int aValue);
		
		/**
		 * Insanities have been added or removed so that the OK state of it may changed.
		 * 
		 * @param aOk
		 *            Whether the insanities are OK now.
		 */
		public void setInsanitiesOk(boolean aOk);
	}
	
	/**
	 * The default minimum generation that is set, when creating a character.
	 */
	public static final int MIN_GENERATION = 12;
	
	/**
	 * The default maximum generation that is set, when creating a character.
	 */
	public static final int MAX_GENERATION = 12;
	
	/**
	 * The default number of free bonus points, a user can spend into his new created character.
	 */
	public static final int START_FREE_POINTS = 15;
	
	private final ItemProvider mItems;
	
	private final Context mContext;
	
	private final List<ItemControllerCreation> mControllers;
	
	private final DescriptionControllerCreation mDescriptions;
	
	private final InsanityControllerCreation mInsanities;
	
	private final HealthControllerCreation mHealth;
	
	private final CharCreationListener mListener;
	
	private final GenerationControllerCreation mGeneration;
	
	private CreationMode mMode;
	
	private String mName = "";
	
	private String mConcept = "";
	
	private Nature mNature;
	
	private Nature mBehavior;
	
	private Clan mClan;
	
	private int mFreePoints = START_FREE_POINTS;
	
	/**
	 * Creates a new character creation.
	 * 
	 * @param aItems
	 *            An item provider that is used to create item creations from each item type.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            A character creation listener, that is called at specific changes.
	 * @param aMode
	 *            The initial creation mode.
	 */
	public CharacterCreation(final ItemProvider aItems, final Context aContext, final CharCreationListener aListener, final CreationMode aMode)
	{
		mItems = aItems;
		mContext = aContext;
		mListener = aListener;
		mMode = aMode;
		mControllers = new ArrayList<ItemControllerCreation>();
		mDescriptions = new DescriptionControllerCreation(mItems.getDescriptions(), mContext);
		mInsanities = new InsanityControllerCreation(mContext, this);
		mGeneration = new GenerationControllerCreation(mContext, this, aMode.isFreeMode());
		mHealth = new HealthControllerCreation(mContext, mItems);
		mBehavior = mNature = mItems.getNatures().getFirst();
		for (final ItemController controller : mItems.getControllers())
		{
			mControllers.add(new ItemControllerCreationImpl(controller, mContext, this));
		}
		setClan(mItems.getClans().getFirst());
	}
	
	/**
	 * Removes all user defined descriptions.
	 */
	public void clearDescriptions()
	{
		mDescriptions.resetValues();
		mInsanities.clear();
	}
	
	/**
	 * Decreases the current available points by {@code aValue} points.
	 * 
	 * @param aValue
	 *            The points to add.
	 */
	public void decreaseFreePoints(final int aValue)
	{
		mFreePoints -= aValue;
		freePointsChanged();
	}
	
	/**
	 * @param aName
	 *            The item name.
	 * @return The item creation with the given name.
	 */
	public ItemCreation findItem(final String aName)
	{
		for (final ItemControllerCreation controller : mControllers)
		{
			if (controller.hasItem(aName))
			{
				return controller.getItem(aName);
			}
		}
		return null;
	}
	
	/**
	 * @return the current behavior.
	 */
	public Nature getBehavior()
	{
		return mBehavior;
	}
	
	/**
	 * @return whether this character is still a low level character.
	 */
	public boolean isLowLevel()
	{
		return mGeneration.isLowLevel();
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
	 * @return The underlying context.
	 */
	public Context getContext()
	{
		return mContext;
	}
	
	/**
	 * @return a list of item controller creations.
	 */
	public List<ItemControllerCreation> getControllers()
	{
		return mControllers;
	}
	
	/**
	 * @return a list of all item values that currently need a description.
	 */
	public List<ItemCreation> getDescriptionItems()
	{
		final List<ItemCreation> list = new ArrayList<ItemCreation>();
		for (final ItemControllerCreation controller : mControllers)
		{
			list.addAll(controller.getDescriptionValues());
		}
		return list;
	}
	
	/**
	 * @return the descriptions controller.
	 */
	public DescriptionControllerCreation getDescriptions()
	{
		return mDescriptions;
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
	public GenerationControllerCreation getGeneration()
	{
		return mGeneration;
	}
	
	/**
	 * @return The current generation value.
	 */
	public int getGenerationValue()
	{
		int generation = mGeneration.getGeneration();
		final ItemCreation generationItem = findItem(mItems.getGenerationItem());
		if (generationItem != null)
		{
			generation -= generationItem.getValue();
		}
		return generation;
	}
	
	/**
	 * @return the health controller.
	 */
	public HealthControllerCreation getHealth()
	{
		return mHealth;
	}
	
	/**
	 * @return the insanity controller.
	 */
	public InsanityControllerCreation getInsanities()
	{
		return mInsanities;
	}
	
	/**
	 * @return the item provider.
	 */
	public ItemProvider getItems()
	{
		return mItems;
	}
	
	/**
	 * @return the current creation mode.
	 */
	public CreationMode getMode()
	{
		// TODO Remove the creation mode from each item class. That update should be done via update groups.
		return mMode;
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
	 * @return all persistent non creation restrictions.
	 */
	public List<RestrictionInstance> getRestrictions()
	{
		final List<RestrictionInstance> restrictions = new ArrayList<RestrictionInstance>();
		for (final RestrictionCreation restriction : mClan.getRestrictions())
		{
			if (restriction.isPersistent() && !restriction.isCreationRestriction())
			{
				restrictions.add(restriction.createInstance());
			}
		}
		return restrictions;
	}
	
	/**
	 * Increases the current available points by {@code aValue} points.
	 * 
	 * @param aValue
	 *            The points to subtract.
	 */
	public void increaseFreePoints(final int aValue)
	{
		mFreePoints += aValue;
		freePointsChanged();
	}
	
	/**
	 * @return whether the insanities are currently OK.
	 */
	public boolean isInsanitiesOk()
	{
		return mInsanities.isOk();
	}
	
	/**
	 * Releases all item controller views.
	 */
	public void release()
	{
		for (final ItemControllerCreation controller : mControllers)
		{
			controller.release();
		}
		mHealth.release();
		mGeneration.release();
		mInsanities.release();
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
		mFreePoints = START_FREE_POINTS;
		freePointsChanged();
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
		mConcept = aConcept.trim();
	}
	
	/**
	 * Sets the current creation mode.
	 * 
	 * @param aMode
	 *            The new creation mode.
	 */
	public void setCreationMode(final CreationMode aMode)
	{
		mMode = aMode;
		for (final ItemControllerCreation controller : mControllers)
		{
			controller.updateUI();
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
		mListener.setInsanitiesOk(aOk);
	}
	
	/**
	 * Sets the current character name.
	 * 
	 * @param aName
	 *            The new name.
	 */
	public void setName(final String aName)
	{
		mName = aName.trim();
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
	 * Updates the user interface for all controllers.
	 */
	public void updateUI()
	{
		for (final ItemControllerCreation controller : getControllers())
		{
			controller.updateUI();
		}
	}
	
	private void addRestrictions()
	{
		if (getMode().isFreeMode())
		{
			return;
		}
		for (final RestrictionCreation restriction : mClan.getRestrictions())
		{
			final CreationRestrictionType type = restriction.getType();
			if (type.equals(CreationRestrictionType.ITEM_VALUE) || type.equals(CreationRestrictionType.ITEM_CHILDREN_COUNT)
					|| type.equals(CreationRestrictionType.ITEM_CHILD_VALUE_AT) || type.equals(CreationRestrictionType.GROUP_CHILDREN)
					|| type.equals(CreationRestrictionType.GROUP_CHILDREN_COUNT) || type.equals(CreationRestrictionType.GROUP_ITEM_VALUE_AT))
			{
				final boolean item = type.equals(CreationRestrictionType.ITEM_VALUE) || type.equals(CreationRestrictionType.ITEM_CHILDREN_COUNT)
						|| type.equals(CreationRestrictionType.ITEM_CHILD_VALUE_AT);
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
			else if (type.equals(CreationRestrictionType.INSANITY))
			{
				mInsanities.addRestriction(restriction);
			}
			else if (type.equals(CreationRestrictionType.GENERATION))
			{
				mGeneration.addRestriction(restriction);
			}
		}
	}
	
	private void freePointsChanged()
	{
		for (final ItemControllerCreation controller : mControllers)
		{
			controller.updateUI();
		}
		mListener.setFreePoints(mFreePoints);
	}
	
	private void removeRestrictions()
	{
		for (final RestrictionCreation restriction : mClan.getRestrictions())
		{
			restriction.clear();
		}
	}
}