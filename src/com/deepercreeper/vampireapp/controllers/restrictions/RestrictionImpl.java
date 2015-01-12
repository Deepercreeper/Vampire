package com.deepercreeper.vampireapp.controllers.restrictions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemControllerCreation;

/**
 * Some clans have restrictions, that define whether values or attributes have to have a specific value.<br>
 * Each restriction is represented by an instance of this class.
 * 
 * @author vrl
 */
public class RestrictionImpl implements Restriction
{
	private final String			mItemName;
	
	private final List<String>		mItems;
	
	private final RestrictionType	mType;
	
	private final int				mMinimum;
	
	private final int				mMaximum;
	
	private final int				mIndex;
	
	private final Set<Condition>	mConditions	= new HashSet<Condition>();
	
	private Restrictionable			mParent;
	
	public RestrictionImpl(final RestrictionType aType, final String aItemName, final int aMinimum, final int aMaximum, final List<String> aItems,
			final int aIndex)
	{
		mType = aType;
		mItemName = aItemName;
		mItems = aItems;
		mMinimum = aMinimum;
		mMaximum = aMaximum;
		mIndex = aIndex;
	}
	
	@Override
	public int getIndex()
	{
		return mIndex;
	}
	
	@Override
	public void addCondition(final Condition aCondition)
	{
		mConditions.add(aCondition);
	}
	
	@Override
	public boolean hasConditions()
	{
		return !mConditions.isEmpty();
	}
	
	@Override
	public boolean isActive(final ItemControllerCreation aController)
	{
		for (final Condition condition : mConditions)
		{
			if ( !condition.complied(aController))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void update()
	{
		getParent().updateRestrictions();
	}
	
	/**
	 * Adds a restricted parent to this restriction. That makes sure,<br>
	 * that the removal of restrictions is done for each restricted value of this restriction.
	 * 
	 * @param aParent
	 *            The parent.
	 */
	@Override
	public void setParent(final Restrictionable aParent)
	{
		mParent = aParent;
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
	public int getMinimum()
	{
		return mMinimum;
	}
	
	@Override
	public Restrictionable getParent()
	{
		return mParent;
	}
	
	@Override
	public int getMaximum()
	{
		return mMaximum;
	}
	
	@Override
	public boolean isInRange(final int aValue)
	{
		return mMinimum <= aValue && aValue <= mMaximum;
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
	public RestrictionType getRestrictionType()
	{
		return mType;
	}
	
	@Override
	public Set<Condition> getConditions()
	{
		return mConditions;
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
		if ( !(obj instanceof RestrictionImpl))
		{
			return false;
		}
		final RestrictionImpl other = (RestrictionImpl) obj;
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
}
