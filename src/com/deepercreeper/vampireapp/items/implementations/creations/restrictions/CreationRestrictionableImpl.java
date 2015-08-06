package com.deepercreeper.vampireapp.items.implementations.creations.restrictions;

import java.util.HashSet;
import java.util.Set;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestrictionable;

/**
 * A creation restrictionable implementation.
 * 
 * @author vrl
 */
public abstract class CreationRestrictionableImpl implements CreationRestrictionable
{
	private final Set<CreationRestriction> mRestrictions = new HashSet<CreationRestriction>();
	
	private ItemControllerCreation mControllerCreation = null;
	
	@Override
	public final void addRestriction(final CreationRestriction aRestriction)
	{
		getRestrictions().add(aRestriction);
		aRestriction.setParent(this);
		updateRestrictions();
	}
	
	/**
	 * Creates a new creation restrictionable.
	 * 
	 * @param aController
	 *            The item controller.
	 */
	public CreationRestrictionableImpl(final ItemControllerCreation aController)
	{
		mControllerCreation = aController;
	}
	
	/**
	 * Creates a new creation restrictionable without controller.
	 */
	public CreationRestrictionableImpl()
	{
		this(null);
	}
	
	@Override
	public final int getMinValue(final CreationRestrictionType... aTypes)
	{
		int minValue = Integer.MIN_VALUE;
		for (final CreationRestriction restriction : getRestrictions(aTypes))
		{
			if (restriction.isActive(mControllerCreation))
			{
				minValue = Math.max(minValue, restriction.getMinimum());
			}
			break;
		}
		return minValue;
	}
	
	@Override
	public final int getMaxValue(final CreationRestrictionType... aTypes)
	{
		int maxValue = Integer.MAX_VALUE;
		for (final CreationRestriction restriction : getRestrictions(aTypes))
		{
			if (restriction.isActive(mControllerCreation))
			{
				maxValue = Math.min(maxValue, restriction.getMaximum());
			}
			break;
		}
		return maxValue;
	}
	
	@Override
	public final Set<CreationRestriction> getRestrictions(final CreationRestrictionType... aTypes)
	{
		final Set<CreationRestriction> restrictions = new HashSet<CreationRestriction>();
		final Set<CreationRestrictionType> types = new HashSet<CreationRestrictionType>();
		for (final CreationRestrictionType type : aTypes)
		{
			types.add(type);
		}
		if (types.isEmpty())
		{
			return mRestrictions;
		}
		for (final CreationRestriction restriction : mRestrictions)
		{
			if (types.contains(restriction.getType()))
			{
				restrictions.add(restriction);
			}
		}
		return restrictions;
	}
	
	@Override
	public final boolean hasRestrictions(final CreationRestrictionType... aTypes)
	{
		return !getRestrictions(aTypes).isEmpty();
	}
	
	@Override
	public final void removeRestriction(final CreationRestriction aRestriction)
	{
		getRestrictions().remove(aRestriction);
		updateRestrictions();
	}
	
	@Override
	public final boolean isValueOk(final int aValue, final CreationRestrictionType... aTypes)
	{
		return getMinValue(aTypes) <= aValue && aValue <= getMaxValue(aTypes);
	}
}
