package com.deepercreeper.vampireapp.items.implementations.creations.restrictions;

import java.util.HashSet;
import java.util.Set;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation.RestrictionCreationType;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionableCreation;

/**
 * A creation restrictionable implementation.
 * 
 * @author vrl
 */
public abstract class RestrictionableCreationImpl implements RestrictionableCreation
{
	private final Set<RestrictionCreation> mRestrictions = new HashSet<RestrictionCreation>();
	
	private ItemControllerCreation mControllerCreation = null;
	
	/**
	 * Creates a new creation restrictionable without controller.
	 */
	public RestrictionableCreationImpl()
	{
		this(null);
	}
	
	/**
	 * Creates a new creation restrictionable.
	 * 
	 * @param aController
	 *            The item controller.
	 */
	public RestrictionableCreationImpl(final ItemControllerCreation aController)
	{
		mControllerCreation = aController;
	}
	
	@Override
	public final void addRestriction(final RestrictionCreation aRestriction)
	{
		getRestrictions().add(aRestriction);
		aRestriction.setParent(this);
		updateUI();
	}
	
	@Override
	public final int getMaxValue(final RestrictionCreationType... aTypes)
	{
		int maxValue = Integer.MAX_VALUE;
		for (final RestrictionCreation restriction : getRestrictions(aTypes))
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
	public final int getMinValue(final RestrictionCreationType... aTypes)
	{
		int minValue = Integer.MIN_VALUE;
		for (final RestrictionCreation restriction : getRestrictions(aTypes))
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
	public final Set<RestrictionCreation> getRestrictions(final RestrictionCreationType... aTypes)
	{
		final Set<RestrictionCreation> restrictions = new HashSet<RestrictionCreation>();
		final Set<RestrictionCreationType> types = new HashSet<RestrictionCreationType>();
		for (final RestrictionCreationType type : aTypes)
		{
			types.add(type);
		}
		if (types.isEmpty())
		{
			return mRestrictions;
		}
		for (final RestrictionCreation restriction : mRestrictions)
		{
			if (types.contains(restriction.getType()))
			{
				restrictions.add(restriction);
			}
		}
		return restrictions;
	}
	
	@Override
	public final boolean hasRestrictions(final RestrictionCreationType... aTypes)
	{
		return !getRestrictions(aTypes).isEmpty();
	}
	
	@Override
	public final boolean isValueOk(final int aValue, final RestrictionCreationType... aTypes)
	{
		return getMinValue(aTypes) <= aValue && aValue <= getMaxValue(aTypes);
	}
	
	@Override
	public final void removeRestriction(final RestrictionCreation aRestriction)
	{
		getRestrictions().remove(aRestriction);
		updateUI();
	}
}
