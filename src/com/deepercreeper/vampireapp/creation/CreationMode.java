package com.deepercreeper.vampireapp.creation;

import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.controllers.restrictions.Restriction;
import com.deepercreeper.vampireapp.controllers.restrictions.Restriction.RestrictionType;

/**
 * The creation of a character has several states.<br>
 * Each is declared by one CreationMode.
 * 
 * @author Vincent
 */
public enum CreationMode
{
	/**
	 * This is the first creation mode. Here are the start points and other main options registered.
	 */
	MAIN(true, false),
	
	/**
	 * Here are the bonus points set, that can be given to each character.
	 */
	POINTS(false, true),
	
	/**
	 * Here are descriptions created. For items and character own descriptions.
	 */
	DESCRIPTIONS(false, false);
	
	private final boolean	mValueMode, mTempPointsMode;
	
	CreationMode(final boolean aValueMode, final boolean aTempPointsMode)
	{
		mValueMode = aValueMode;
		mTempPointsMode = aTempPointsMode;
		if (mValueMode && mTempPointsMode)
		{
			throw new IllegalArgumentException("Can't change value and temporary points in one mode!");
		}
	}
	
	public boolean canIncreaseItem(final ItemCreation aItem, final boolean aCanIncrease)
	{
		if ( !aCanIncrease)
		{
			return false;
		}
		if (mValueMode)
		{
			return aItem.getItemGroup().canChangeBy(aItem.getIncreasedValue() - aItem.getValue());
		}
		if (mTempPointsMode)
		{
			return aItem.hasEnoughPoints() && aItem.getItem().getFreePointsCost() != 0;
		}
		return false;
	}
	
	public boolean canRemoveItem(final ItemCreation aItem)
	{
		final ItemGroupCreation group = aItem.getItemGroup();
		if ( !group.canChangeBy( -aItem.getValue()))
		{
			return false;
		}
		for (final Restriction restriction : group.getRestrictions(RestrictionType.GROUP_CHILDREN_COUNT))
		{
			if (restriction.isActive(group.getItemController()) && group.getItemsList().size() <= restriction.getMinimum())
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean canRemoveChild(final ItemCreation aItem)
	{
		if ( !aItem.getItemGroup().canChangeBy( -aItem.getValue()))
		{
			return false;
		}
		final ItemCreation parent = aItem.getParentItem();
		for (final Restriction restriction : parent.getRestrictions(RestrictionType.ITEM_CHILDREN_COUNT))
		{
			if (restriction.isActive(aItem.getItemGroup().getItemController()) && parent.getChildrenList().size() <= restriction.getMinimum())
			{
				return false;
			}
		}
		for (final Restriction restriction : parent.getRestrictions(RestrictionType.ITEM_CHILD_VALUE_AT))
		{
			if (restriction.isActive(aItem.getItemGroup().getItemController()) && restriction.getIndex() == parent.indexOfChild(aItem)
					&& aItem.getItem().getStartValue() < restriction.getMinimum())
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean canDecreaseItem(final ItemCreation aItem, final boolean aCanDecreaseValue, final boolean aCanDecreaseTempPoints)
	{
		if (mValueMode)
		{
			return aCanDecreaseValue && aItem.getItemGroup().canChangeBy(aItem.getDecreasedValue() - aItem.getValue());
		}
		if (mTempPointsMode)
		{
			return aCanDecreaseTempPoints && aItem.getItem().getFreePointsCost() != 0;
		}
		return false;
	}
	
	public void increaseItem(final ItemCreation aItem)
	{
		if (mValueMode)
		{
			aItem.getChangeValue().increase();
		}
		else if (mTempPointsMode)
		{
			aItem.getChangeTempPoints().increase();
			aItem.getPoints().decrease(aItem.getFreePointsCost());
		}
	}
	
	public void decreaseItem(final ItemCreation aItem)
	{
		if (mValueMode)
		{
			aItem.getChangeValue().decrease();
		}
		else if (mTempPointsMode)
		{
			aItem.getChangeTempPoints().decrease();
			aItem.getPoints().increase(aItem.getFreePointsCost());
		}
	}
	
	public boolean canChangeGroup(final ItemGroupCreation aGroup)
	{
		if ( !aGroup.isMutable())
		{
			return false;
		}
		if (mValueMode)
		{
			return true;
		}
		if (mTempPointsMode)
		{
			return false;
		}
		return false;
	}
	
	public boolean canAddChild(final ItemCreation aItem, final boolean aRestrictions)
	{
		if ( !aItem.isParent())
		{
			return false;
		}
		if ( !aItem.isMutableParent())
		{
			return false;
		}
		if (aRestrictions)
		{
			for (final Restriction restriction : aItem.getRestrictions(RestrictionType.ITEM_CHILDREN_COUNT))
			{
				if (restriction.isActive(aItem.getItemGroup().getItemController()) && aItem.getChildrenList().size() >= restriction.getMaximum())
				{
					return false;
				}
			}
		}
		if (mValueMode)
		{
			return true;
		}
		if (mTempPointsMode)
		{
			return false;
		}
		return false;
	}
	
	public boolean canEditItem(final ItemCreation aItem)
	{
		if ( !aItem.hasParentItem() && !aItem.getItemGroup().isMutable())
		{
			return false;
		}
		if (aItem.hasParentItem() && !aItem.getParentItem().isMutableParent())
		{
			return false;
		}
		if (mValueMode)
		{
			return true;
		}
		if (mTempPointsMode)
		{
			return false;
		}
		return false;
	}
}
