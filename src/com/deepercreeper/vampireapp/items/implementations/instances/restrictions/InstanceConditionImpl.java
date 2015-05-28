package com.deepercreeper.vampireapp.items.implementations.instances.restrictions;

import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceCondition;

public class InstanceConditionImpl implements InstanceCondition
{
	private final InstanceConditionQuery	mQuery;
	
	private final String			mItemName;
	
	private final int				mMinimum;
	
	private final int				mMaximum;
	
	private final int				mIndex;
	
	public InstanceConditionImpl(final InstanceConditionQuery aQuery, final String aItemName, final int aMinimum, final int aMaximum, final int aIndex)
	{
		mQuery = aQuery;
		mItemName = aItemName;
		mMinimum = aMinimum;
		mMaximum = aMaximum;
		mIndex = aIndex;
	}
	
	@Override
	public boolean complied(final ItemControllerInstance aController)
	{
		return mQuery.complied(aController, this);
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
		if ( !(obj instanceof InstanceConditionImpl))
		{
			return false;
		}
		final InstanceConditionImpl other = (InstanceConditionImpl) obj;
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
	public boolean isInRange(final int aValue)
	{
		return mMinimum <= aValue && aValue <= mMaximum;
	}
	
}
