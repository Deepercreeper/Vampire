package com.deepercreeper.vampireapp.items.implementations.creations.restrictions;

import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationCondition;

public class CreationConditionImpl implements CreationCondition
{
	private final CreationConditionQuery	mQuery;
	
	private final String			mItemName;
	
	private final int				mMinimum;
	
	private final int				mMaximum;
	
	private final int				mIndex;
	
	public CreationConditionImpl(final CreationConditionQuery aQuery, final String aItemName, final int aMinimum, final int aMaximum, final int aIndex)
	{
		mQuery = aQuery;
		mItemName = aItemName;
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
	public boolean complied(final ItemControllerCreation aController)
	{
		return mQuery.complied(aController, this);
	}
	
	@Override
	public String getItemName()
	{
		return mItemName;
	}
	
	@Override
	public boolean isInRange(final int aValue)
	{
		return mMinimum <= aValue && aValue <= mMaximum;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + mIndex;
		result = prime * result + ((mItemName == null) ? 0 : mItemName.hashCode());
		result = prime * result + mMaximum;
		result = prime * result + mMinimum;
		result = prime * result + ((mQuery == null) ? 0 : mQuery.hashCode());
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
		if ( !(obj instanceof CreationConditionImpl))
		{
			return false;
		}
		final CreationConditionImpl other = (CreationConditionImpl) obj;
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
		if (mMaximum != other.mMaximum)
		{
			return false;
		}
		if (mMinimum != other.mMinimum)
		{
			return false;
		}
		if (mQuery == null)
		{
			if (other.mQuery != null)
			{
				return false;
			}
		}
		else if ( !mQuery.equals(other.mQuery))
		{
			return false;
		}
		return true;
	}
	
}
