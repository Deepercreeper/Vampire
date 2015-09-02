package com.deepercreeper.vampireapp.items.implementations.instances.restrictions;

import java.util.HashSet;
import java.util.Set;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance.RestrictionInstanceType;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionableInstance;

/**
 * A restrictionable implementation.
 * 
 * @author vrl
 */
public abstract class RestrictionableInstanceImpl implements RestrictionableInstance
{
	private final Set<RestrictionInstance> mRestrictions = new HashSet<RestrictionInstance>();
	
	private final CharacterInstance mCharacter;
	
	private ItemControllerInstance mControllerInstance = null;
	
	/**
	 * Creates a new restrictionable with no item controller.
	 * 
	 * @param aCharacter
	 *            The character.
	 */
	public RestrictionableInstanceImpl(final CharacterInstance aCharacter)
	{
		this(aCharacter, null);
	}
	
	/**
	 * Creates a new restrictionable.
	 * 
	 * @param aCharacter
	 *            The character.
	 * @param aController
	 *            The item controller.
	 */
	public RestrictionableInstanceImpl(final CharacterInstance aCharacter, final ItemControllerInstance aController)
	{
		mCharacter = aCharacter;
		mControllerInstance = aController;
	}
	
	@Override
	public final void addRestriction(final RestrictionInstance aRestriction)
	{
		getRestrictions().add(aRestriction);
		aRestriction.setParent(this);
		updateUI();
	}
	
	@Override
	public CharacterInstance getCharacter()
	{
		return mCharacter;
	}
	
	@Override
	public final int getMaxValue(final RestrictionInstanceType... aTypes)
	{
		int maxValue = Integer.MAX_VALUE;
		for (final RestrictionInstance restriction : getRestrictions(aTypes))
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
	public final int getMinValue(final RestrictionInstanceType... aTypes)
	{
		int minValue = Integer.MIN_VALUE;
		for (final RestrictionInstance restriction : getRestrictions(aTypes))
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
	public final Set<RestrictionInstance> getRestrictions(final RestrictionInstanceType... aTypes)
	{
		final Set<RestrictionInstance> restrictions = new HashSet<RestrictionInstance>();
		final Set<RestrictionInstanceType> types = new HashSet<RestrictionInstanceType>();
		for (final RestrictionInstanceType type : aTypes)
		{
			types.add(type);
		}
		if (types.isEmpty())
		{
			return mRestrictions;
		}
		for (final RestrictionInstance restriction : mRestrictions)
		{
			if (types.contains(restriction.getType()))
			{
				restrictions.add(restriction);
			}
		}
		return restrictions;
	}
	
	@Override
	public final boolean hasRestrictions(final RestrictionInstanceType... aTypes)
	{
		return !getRestrictions(aTypes).isEmpty();
	}
	
	@Override
	public final boolean isValueOk(final int aValue, final RestrictionInstanceType... aTypes)
	{
		return getMinValue(aTypes) <= aValue && aValue <= getMaxValue(aTypes);
	}
	
	@Override
	public final void removeRestriction(final RestrictionInstance aRestriction)
	{
		getRestrictions().remove(aRestriction);
		getCharacter().removeRestriction(aRestriction);
		updateUI();
	}
}
