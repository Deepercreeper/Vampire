package com.deepercreeper.vampireapp.items.implementations.creations.restrictions;

import com.deepercreeper.vampireapp.items.implementations.instances.restrictions.ConditionInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.ConditionCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.ConditionInstance;

/**
 * A creation condition implementation.
 * 
 * @author vrl
 */
public class ConditionCreationImpl implements ConditionCreation
{
	private final ConditionQueryCreation mQuery;
	
	private final String mItemName;
	
	private final int mMinimum;
	
	private final int mMaximum;
	
	private final int mIndex;
	
	private final boolean mPersistent;
	
	/**
	 * Creates a new creation condition.
	 * 
	 * @param aQuery
	 *            The condition query.
	 * @param aItemName
	 *            The item name.
	 * @param aMinimum
	 *            The minimum value.
	 * @param aMaximum
	 *            The maximum value.
	 * @param aIndex
	 *            The index.
	 * @param aPersistent
	 *            Whether this condition will be ported into the character instance.
	 */
	public ConditionCreationImpl(final ConditionQueryCreation aQuery, final String aItemName, final int aMinimum, final int aMaximum,
			final int aIndex, boolean aPersistent)
	{
		mQuery = aQuery;
		mItemName = aItemName;
		mMinimum = aMinimum;
		mMaximum = aMaximum;
		mIndex = aIndex;
		mPersistent = aPersistent;
	}
	
	@Override
	public boolean isPersistent()
	{
		return mPersistent;
	}
	
	@Override
	public boolean complied(final ItemControllerCreation aController)
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
		if ( !(obj instanceof ConditionCreationImpl))
		{
			return false;
		}
		final ConditionCreationImpl other = (ConditionCreationImpl) obj;
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
	
	public ConditionInstance createInstance()
	{
		if ( !isPersistent())
		{
			return null;
		}
		return new ConditionInstanceImpl(mQuery.getInstanceQuery(), getItemName(), mMinimum, mMaximum, mIndex);
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
