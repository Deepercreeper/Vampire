package com.deepercreeper.vampireapp.items.implementations.instances.restrictions;

import java.util.HashSet;
import java.util.Set;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction.InstanceRestrictionType;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestrictionable;

public abstract class InstanceRestrictionableImpl implements InstanceRestrictionable
{
	private final Set<InstanceRestriction>	mRestrictions		= new HashSet<InstanceRestriction>();
	
	private final CharacterInstance			mCharacter;
	
	private ItemControllerInstance			mControllerInstance	= null;
	
	public InstanceRestrictionableImpl(final CharacterInstance aCharacter)
	{
		mCharacter = aCharacter;
	}
	
	@Override
	public CharacterInstance getCharacter()
	{
		return mCharacter;
	}
	
	@Override
	public final void addRestriction(final InstanceRestriction aRestriction)
	{
		getRestrictions().add(aRestriction);
		aRestriction.setParent(this);
		updateRestrictions();
	}
	
	@Override
	public final int getMaxValue(final InstanceRestrictionType... aTypes)
	{
		int maxValue = Integer.MAX_VALUE;
		for (final InstanceRestriction restriction : getRestrictions(aTypes))
		{
			if (restriction.isActive(mControllerInstance))
			{
				maxValue = Math.min(maxValue, restriction.getMaximum());
			}
			break;
		}
		return maxValue;
	}
	
	@Override
	public final int getMinValue(final InstanceRestrictionType... aTypes)
	{
		int minValue = Integer.MIN_VALUE;
		for (final InstanceRestriction restriction : getRestrictions(aTypes))
		{
			if (restriction.isActive(mControllerInstance))
			{
				minValue = Math.max(minValue, restriction.getMinimum());
			}
			break;
		}
		return minValue;
	}
	
	@Override
	public final Set<InstanceRestriction> getRestrictions(final InstanceRestrictionType... aTypes)
	{
		final Set<InstanceRestriction> restrictions = new HashSet<InstanceRestriction>();
		final Set<InstanceRestrictionType> types = new HashSet<InstanceRestrictionType>();
		for (final InstanceRestrictionType type : aTypes)
		{
			types.add(type);
		}
		if (types.isEmpty())
		{
			return mRestrictions;
		}
		for (final InstanceRestriction restriction : mRestrictions)
		{
			if (types.contains(restriction.getType()))
			{
				restrictions.add(restriction);
			}
		}
		return restrictions;
	}
	
	@Override
	public final boolean hasRestrictions(final InstanceRestrictionType... aTypes)
	{
		return !getRestrictions(aTypes).isEmpty();
	}
	
	@Override
	public final boolean isValueOk(final int aValue, final InstanceRestrictionType... aTypes)
	{
		return getMinValue(aTypes) <= aValue && aValue <= getMaxValue(aTypes);
	}
	
	@Override
	public final void removeRestriction(final InstanceRestriction aRestriction)
	{
		getRestrictions().remove(aRestriction);
		getCharacter().removeRestriction(aRestriction);
		updateRestrictions();
	}
	
	public final void setController(final ItemControllerInstance aController)
	{
		mControllerInstance = aController;
	}
}
