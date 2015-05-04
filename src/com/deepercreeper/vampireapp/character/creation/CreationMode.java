package com.deepercreeper.vampireapp.character.creation;

import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;

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
	DESCRIPTIONS(false, false),
	
	/**
	 * This is the free version of the main mode. Used for creating free characters.
	 */
	FREE_MAIN();
	
	private final boolean	mValueMode, mTempPointsMode, mFreeMode;
	
	private CreationMode()
	{
		mValueMode = true;
		mTempPointsMode = false;
		mFreeMode = true;
	}
	
	private CreationMode(final boolean aValueMode, final boolean aTempPointsMode)
	{
		mValueMode = aValueMode;
		mTempPointsMode = aTempPointsMode;
		mFreeMode = false;
		if (mValueMode && mTempPointsMode)
		{
			throw new IllegalArgumentException("Can't change value and temporary points in one mode!");
		}
	}
	
	/**
	 * @param aItem
	 *            The parent item.
	 * @param aRestrictions
	 *            The current existing restrictions of the parent item.
	 * @return whether a child can be added to the given item, depending on the current restrictions.
	 */
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
		if (mFreeMode)
		{
			return true;
		}
		if (aRestrictions)
		{
			for (final CreationRestriction restriction : aItem.getRestrictions(CreationRestrictionType.ITEM_CHILDREN_COUNT))
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
	
	/**
	 * @param aGroup
	 *            The item group.
	 * @return whether an item can be added to the given group.
	 */
	public boolean canAddItem(final ItemGroupCreation aGroup)
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
	
	/**
	 * @param aItem
	 *            The item.
	 * @param aCanDecreaseValue
	 *            Whether the item can be decreased at all.
	 * @param aCanDecreaseTempPoints
	 *            Whether the temporary points of the given item can be decreased.
	 * @return whether the given item can be decreased.
	 */
	public boolean canDecreaseItem(final ItemCreation aItem, final boolean aCanDecreaseValue, final boolean aCanDecreaseTempPoints)
	{
		if (mFreeMode)
		{
			return aCanDecreaseValue;
		}
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
	
	/**
	 * @param aItem
	 *            The item.
	 * @return whether the given item can be replaced by another item.
	 */
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
	
	/**
	 * @param aItem
	 *            The item.
	 * @param aCanIncrease
	 *            Whether the given item can be increased at all.
	 * @return whether the given item can be increased.
	 */
	public boolean canIncreaseItem(final ItemCreation aItem, final boolean aCanIncrease)
	{
		if ( !aCanIncrease)
		{
			return false;
		}
		if (mFreeMode)
		{
			return true;
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
	
	/**
	 * @param aItem
	 *            The child item.
	 * @return Whether the given item can be removed from its parent item.
	 */
	public boolean canRemoveChild(final ItemCreation aItem)
	{
		if (mFreeMode)
		{
			return true;
		}
		if ( !aItem.getItemGroup().canChangeBy( -aItem.getValue()))
		{
			return false;
		}
		final ItemCreation parent = aItem.getParentItem();
		for (final CreationRestriction restriction : parent.getRestrictions(CreationRestrictionType.ITEM_CHILDREN_COUNT))
		{
			if (restriction.isActive(aItem.getItemGroup().getItemController()) && parent.getChildrenList().size() <= restriction.getMinimum())
			{
				return false;
			}
		}
		for (final CreationRestriction restriction : parent.getRestrictions(CreationRestrictionType.ITEM_CHILD_VALUE_AT))
		{
			if (restriction.isActive(aItem.getItemGroup().getItemController()) && restriction.getIndex() == parent.indexOfChild(aItem)
					&& aItem.getItem().getStartValue() < restriction.getMinimum())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param aItem
	 *            the child item.
	 * @return whether the given item can be removed from its parent group.
	 */
	public boolean canRemoveItem(final ItemCreation aItem)
	{
		if (mFreeMode)
		{
			return true;
		}
		final ItemGroupCreation group = aItem.getItemGroup();
		if ( !group.canChangeBy( -aItem.getValue()))
		{
			return false;
		}
		for (final CreationRestriction restriction : group.getRestrictions(CreationRestrictionType.GROUP_CHILDREN_COUNT))
		{
			if (restriction.isActive(group.getItemController()) && group.getItemsList().size() <= restriction.getMinimum())
			{
				return false;
			}
		}
		for (final CreationRestriction restriction : group.getRestrictions(CreationRestrictionType.GROUP_ITEM_VALUE_AT))
		{
			if (restriction.isActive(group.getItemController()) && restriction.getIndex() == group.indexOfItem(aItem)
					&& aItem.getItem().getStartValue() < restriction.getMinimum())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Decreases the given item depending on the current mode.
	 * 
	 * @param aItem
	 *            The item to decrease.
	 */
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
	
	/**
	 * Increases the given item depending on the current mode.
	 * 
	 * @param aItem
	 *            The item to increase.
	 */
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
	
	/**
	 * @return whether this is a free creation mode.
	 */
	public boolean isFreeMode()
	{
		return mFreeMode;
	}
}
