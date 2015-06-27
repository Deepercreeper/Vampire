package com.deepercreeper.vampireapp.character.creation;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.widget.TableLayout;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.implementations.creations.ItemControllerCreationImpl;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation.PointHandler;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction;
import com.deepercreeper.vampireapp.lists.controllers.creations.DescriptionControllerCreation;
import com.deepercreeper.vampireapp.lists.controllers.creations.GenerationControllerCreation;
import com.deepercreeper.vampireapp.lists.controllers.creations.InsanityControllerCreation;
import com.deepercreeper.vampireapp.lists.items.Clan;
import com.deepercreeper.vampireapp.lists.items.Nature;
import com.deepercreeper.vampireapp.util.Log;

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
	public static final int						MIN_GENERATION		= 8;
	
	/**
	 * The default maximum generation that is set, when creating a character.
	 */
	public static final int						MAX_GENERATION		= 12;
	
	/**
	 * The default number of free bonus points, a user can spend into his new created character.
	 */
	public static final int						START_FREE_POINTS	= 15;
	
	private final ItemProvider					mItems;
	
	private final Context						mContext;
	
	private CreationMode						mMode;
	
	private String								mName				= "";
	
	private String								mConcept			= "";
	
	private Nature								mNature;
	
	private Nature								mBehavior;
	
	private Clan								mClan;
	
	private final GenerationControllerCreation	mGeneration;
	
	private int									mFreePoints			= START_FREE_POINTS;
	
	private final List<ItemControllerCreation>	mControllers;
	
	private final DescriptionControllerCreation	mDescriptions;
	
	private final InsanityControllerCreation	mInsanities;
	
	private final HealthControllerCreation		mHealth;
	
	private final CharCreationListener			mListener;
	
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
		final PointHandler points = new PointHandler()
		{
			@Override
			public void decrease(final int aValue)
			{
				mFreePoints -= aValue;
				freePointsChanged();
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
				freePointsChanged();
			}
		};
		mControllers = new ArrayList<ItemControllerCreation>();
		for (final ItemController controller : mItems.getControllers())
		{
			mControllers.add(new ItemControllerCreationImpl(controller, mContext, getCreationMode(), points));
		}
		mDescriptions = new DescriptionControllerCreation(mItems.getDescriptions());
		mInsanities = new InsanityControllerCreation(mContext, this);
		mGeneration = new GenerationControllerCreation();
		mHealth = new HealthControllerCreation(mContext, mItems);
		mBehavior = mNature = mItems.getNatures().getFirst();
		setClan(mItems.getClans().getFirst());
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
	 * @return the current creation mode.
	 */
	public CreationMode getCreationMode()
	{
		return mMode;
	}
	
	/**
	 * @return the descriptions controller.
	 */
	public DescriptionControllerCreation getDescriptions()
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
	public GenerationControllerCreation getGeneration()
	{
		return mGeneration;
	}
	
	/**
	 * @return The current generation value.
	 */
	public int getGenerationValue()
	{
		// TODO Use this method to change the maximum number of blood points.
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
	 * @return the health steps of this character.
	 */
	public int[] getHealthSteps()
	{
		return mHealth.getSteps();
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
	public List<InstanceRestriction> getRestrictions()
	{
		final List<InstanceRestriction> restrictions = new ArrayList<InstanceRestriction>();
		for (final CreationRestriction restriction : mClan.getRestrictions())
		{
			if (restriction.isPersistent() && !restriction.isCreationRestriction())
			{
				restrictions.add(restriction.createInstance());
			}
		}
		return restrictions;
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
		mHealth.release();
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
			controller.setCreationMode(getCreationMode());
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
	
	private void addRestrictions()
	{
		if (getCreationMode().isFreeMode())
		{
			return;
		}
		for (final CreationRestriction restriction : mClan.getRestrictions())
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
			controller.updateGroups();
		}
		mListener.setFreePoints(mFreePoints);
	}
	
	private void removeRestrictions()
	{
		for (final CreationRestriction restriction : mClan.getRestrictions())
		{
			restriction.clear();
		}
	}
}
