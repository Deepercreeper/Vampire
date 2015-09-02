package com.deepercreeper.vampireapp.items.implementations.creations.restrictions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.deepercreeper.vampireapp.items.implementations.instances.restrictions.RestrictionInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.ConditionCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionableCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance;
import com.deepercreeper.vampireapp.mechanics.Duration;

/**
 * Some clans have restrictions, that define whether values or attributes have to have a specific value.<br>
 * Each restriction is represented by an instance of this class.
 * 
 * @author vrl
 */
public class RestrictionCreationImpl implements RestrictionCreation
{
	private final String mItemName;
	
	private final List<String> mItems;
	
	private final RestrictionCreationType mType;
	
	private final int mMinimum;
	
	private final int mMaximum;
	
	private final int mIndex;
	
	private final int mValue;
	
	private final boolean mCreationRestriction;
	
	private final Set<ConditionCreation> mConditions = new HashSet<ConditionCreation>();
	
	private RestrictionableCreation mParent;
	
	/**
	 * Creates a new creation restriction.
	 * 
	 * @param aType
	 *            The restriction type.
	 * @param aItemName
	 *            The item name.
	 * @param aMinimum
	 *            The minimum value.
	 * @param aMaximum
	 *            The maximum value.
	 * @param aItems
	 *            aList of item names.
	 * @param aIndex
	 *            The index.
	 * @param aValue
	 *            The value.
	 * @param aCreationRestriction
	 *            Whether this is a creation only restriction.
	 */
	public RestrictionCreationImpl(final RestrictionCreationType aType, final String aItemName, final int aMinimum, final int aMaximum,
			final List<String> aItems, final int aIndex, final int aValue, final boolean aCreationRestriction)
	{
		mType = aType;
		mItemName = aItemName;
		mItems = aItems;
		mMinimum = aMinimum;
		mMaximum = aMaximum;
		mIndex = aIndex;
		mValue = aValue;
		mCreationRestriction = aCreationRestriction;
	}
	
	@Override
	public void addCondition(final ConditionCreation aCondition)
	{
		mConditions.add(aCondition);
	}
	
	/**
	 * Removes this restriction from each restricted parent.
	 */
	@Override
	public void clear()
	{
		if (mParent != null)
		{
			mParent.removeRestriction(this);
			mParent = null;
		}
	}
	
	@Override
	public RestrictionInstance createInstance()
	{
		if ( !isPersistent())
		{
			return null;
		}
		final RestrictionInstance restriction = new RestrictionInstanceImpl(getType().getInstanceType(), getItemName(), getMinimum(), getMaximum(),
				getIndex(), getValue(), Duration.FOREVER);
		for (final ConditionCreation condition : getConditions())
		{
			if (condition.isPersistent())
			{
				restriction.addCondition(condition.createInstance());
			}
		}
		return restriction;
	}
	
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if ( !(obj instanceof RestrictionCreationImpl))
		{
			return false;
		}
		final RestrictionCreationImpl other = (RestrictionCreationImpl) obj;
		if (mConditions == null)
		{
			if (other.mConditions != null)
			{
				return false;
			}
		}
		else if ( !mConditions.equals(other.mConditions))
		{
			return false;
		}
		if (mIndex != other.mIndex)
		{
			return false;
		}
		if (mItemName == null)
		{
			if (other.mItemName != null)
			{
				return false;
			}
		}
		else if ( !mItemName.equals(other.mItemName))
		{
			return false;
		}
		if (mItems == null)
		{
			if (other.mItems != null)
			{
				return false;
			}
		}
		else if ( !mItems.equals(other.mItems))
		{
			return false;
		}
		if (mMaximum != other.mMaximum)
		{
			return false;
		}
		if (mMinimum != other.mMinimum)
		{
			return false;
		}
		if (mType == null)
		{
			if (other.mType != null)
			{
				return false;
			}
		}
		else if ( !mType.equals(other.mType))
		{
			return false;
		}
		return true;
	}
	
	@Override
	public Set<ConditionCreation> getConditions()
	{
		return mConditions;
	}
	
	@Override
	public int getIndex()
	{
		return mIndex;
	}
	
	@Override
	public String getItemName()
	{
		return mItemName;
	}
	
	@Override
	public List<String> getItems()
	{
		return mItems;
	}
	
	@Override
	public int getMaximum()
	{
		return mMaximum;
	}
	
	@Override
	public int getMinimum()
	{
		return mMinimum;
	}
	
	@Override
	public RestrictionableCreation getParent()
	{
		return mParent;
	}
	
	@Override
	public RestrictionCreationType getType()
	{
		return mType;
	}
	
	@Override
	public int getValue()
	{
		return mValue;
	}
	
	@Override
	public boolean hasConditions()
	{
		return !mConditions.isEmpty();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mConditions == null) ? 0 : mConditions.hashCode());
		result = prime * result + mIndex;
		result = prime * result + ((mItemName == null) ? 0 : mItemName.hashCode());
		result = prime * result + ((mItems == null) ? 0 : mItems.hashCode());
		result = prime * result + mMaximum;
		result = prime * result + mMinimum;
		result = prime * result + ((mType == null) ? 0 : mType.hashCode());
		return result;
	}
	
	@Override
	public boolean isActive(final ItemControllerCreation aController)
	{
		for (final ConditionCreation condition : mConditions)
		{
			if ( !condition.complied(aController))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean isCreationRestriction()
	{
		return mCreationRestriction;
	}
	
	@Override
	public boolean isInRange(final int aValue)
	{
		return mMinimum <= aValue && aValue <= mMaximum;
	}
	
	@Override
	public boolean isPersistent()
	{
		return getType().isPersistent();
	}
	
	/**
	 * Adds a restricted parent to this restriction. That makes sure,<br>
	 * that the removal of restrictions is done for each restricted value of this restriction.
	 * 
	 * @param aParent
	 *            The parent.
	 */
	@Override
	public void setParent(final RestrictionableCreation aParent)
	{
		mParent = aParent;
	}
	
	@Override
	public void updateUI()
	{
		getParent().updateUI();
	}
}
