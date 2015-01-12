package com.deepercreeper.vampireapp.controllers.restrictions;

import java.util.HashSet;
import java.util.Set;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.controllers.restrictions.Restriction.RestrictionType;

public abstract class RestrictionableImpl implements Restrictionable
{
	private final Set<Restriction>	mRestrictions	= new HashSet<Restriction>();
	
	private ItemControllerCreation	mController		= null;
	
	@Override
	public final void addRestriction(final Restriction aRestriction)
	{
		getRestrictions().add(aRestriction);
		aRestriction.setParent(this);
		updateRestrictions();
	}
	
	public final void setController(final ItemControllerCreation aController)
	{
		mController = aController;
	}
	
	@Override
	public final int getMinValue(final RestrictionType... aTypes)
	{
		int minValue = Integer.MIN_VALUE;
		for (final Restriction restriction : getRestrictions())
		{
			for (final RestrictionType type : aTypes)
			{
				if (restriction.getRestrictionType().equals(type))
				{
					if (restriction.isActive(mController))
					{
						minValue = Math.max(minValue, restriction.getMinimum());
					}
					break;
				}
			}
		}
		return minValue;
	}
	
	@Override
	public final int getMaxValue(final RestrictionType... aTypes)
	{
		int maxValue = Integer.MAX_VALUE;
		for (final Restriction restriction : getRestrictions())
		{
			for (final RestrictionType type : aTypes)
			{
				if (restriction.getRestrictionType().equals(type))
				{
					if (restriction.isActive(mController))
					{
						maxValue = Math.min(maxValue, restriction.getMaximum());
					}
					break;
				}
			}
		}
		return maxValue;
	}
	
	@Override
	public final Set<Restriction> getRestrictions(final RestrictionType... aTypes)
	{
		final Set<Restriction> restrictions = new HashSet<Restriction>();
		final Set<RestrictionType> types = new HashSet<RestrictionType>();
		for (final RestrictionType type : aTypes)
		{
			types.add(type);
		}
		if (types.isEmpty())
		{
			return mRestrictions;
		}
		for (final Restriction restriction : mRestrictions)
		{
			if (types.contains(restriction.getRestrictionType()))
			{
				restrictions.add(restriction);
			}
		}
		return restrictions;
	}
	
	@Override
	public final boolean hasRestrictions()
	{
		return !getRestrictions().isEmpty();
	}
	
	@Override
	public final void removeRestriction(final Restriction aRestriction)
	{
		getRestrictions().remove(aRestriction);
		updateRestrictions();
	}
	
	@Override
	public final boolean isValueOk(final int aValue, final RestrictionType... aTypes)
	{
		return getMinValue(aTypes) <= aValue && aValue <= getMaxValue(aTypes);
	}
}
