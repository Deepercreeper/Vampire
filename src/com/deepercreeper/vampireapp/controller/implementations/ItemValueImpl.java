package com.deepercreeper.vampireapp.controller.implementations;

import java.util.HashSet;
import java.util.Set;
import android.content.Context;
import com.deepercreeper.vampireapp.controller.interfaces.Item;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValueGroup;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;
import com.deepercreeper.vampireapp.controller.restrictions.Restriction;
import com.deepercreeper.vampireapp.creation.CharMode;

/**
 * An implementation for item values. Each item value should extend this class.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 * @param <S>
 *            The value type.
 */
public abstract class ItemValueImpl <T extends Item, S extends ItemValue<T>> implements ItemValue<T>
{
	private CharMode					mMode;
	
	private final T						mItem;
	
	private final Context				mContext;
	
	private final UpdateAction			mUpdateAction;
	
	private final ItemValueGroup<T, S>	mGroup;
	
	private PointHandler				mPoints;
	
	private String						mDescription;
	
	private final Set<Restriction>		mRestrictions	= new HashSet<Restriction>();
	
	/**
	 * Creates a new item value.
	 * 
	 * @param aItem
	 *            The item type.
	 * @param aContext
	 *            The context.
	 * @param aUpdateAction
	 *            The update action.
	 * @param aGroup
	 *            The parent group.
	 * @param aMode
	 *            The creation mode.
	 * @param aPoints
	 *            The point handler.
	 */
	public ItemValueImpl(final T aItem, final Context aContext, final UpdateAction aUpdateAction, final ItemValueGroup<T, S> aGroup,
			final CharMode aMode, final PointHandler aPoints)
	{
		mItem = aItem;
		mContext = aContext;
		mUpdateAction = aUpdateAction;
		mGroup = aGroup;
		mMode = aMode;
		mPoints = aPoints;
	}
	
	@Override
	public boolean hasRestrictions()
	{
		return !mRestrictions.isEmpty();
	}
	
	@Override
	public boolean canDecrease()
	{
		return getValue() > getItem().getStartValue() && getValue() > getMinValue();
	}
	
	@Override
	public boolean canIncrease()
	{
		return getValue() < getItem().getMaxValue() && getValue() < getMaxValue();
	}
	
	@Override
	public int getMinValue()
	{
		int minValue = -1;
		for (final Restriction restriction : mRestrictions)
		{
			if (restriction.getMinValue() > minValue)
			{
				minValue = restriction.getMinValue();
			}
		}
		return minValue;
	}
	
	@Override
	public int getMaxValue()
	{
		int maxValue = Integer.MAX_VALUE;
		for (final Restriction restriction : mRestrictions)
		{
			if (restriction.getMaxValue() < maxValue)
			{
				maxValue = restriction.getMaxValue();
			}
		}
		return maxValue;
	}
	
	@Override
	public void addRestriction(final Restriction aRestriction)
	{
		mRestrictions.add(aRestriction);
		aRestriction.addParent(this);
		updateRestrictions();
		getUpdateAction().update();
	}
	
	protected abstract void updateRestrictions();
	
	protected abstract void resetRestrictions();
	
	@Override
	public Set<Restriction> getRestrictions()
	{
		return mRestrictions;
	}
	
	@Override
	public void removeRestriction(final Restriction aRestriction)
	{
		mRestrictions.remove(aRestriction);
		resetRestrictions();
		getUpdateAction().update();
	}
	
	@Override
	public Context getContext()
	{
		return mContext;
	}
	
	@Override
	public CharMode getCreationMode()
	{
		return mMode;
	}
	
	@Override
	public String getDescription()
	{
		return mDescription;
	}
	
	@Override
	public T getItem()
	{
		return mItem;
	}
	
	@Override
	public PointHandler getPoints()
	{
		return mPoints;
	}
	
	@Override
	public UpdateAction getUpdateAction()
	{
		return mUpdateAction;
	}
	
	@Override
	public void setCreationMode(final CharMode aMode)
	{
		mMode = aMode;
	}
	
	@Override
	public void setDescription(final String aDescription)
	{
		mDescription = aDescription;
	}
	
	@Override
	public void setPoints(final PointHandler aPoints)
	{
		mPoints = aPoints;
	}
	
	@Override
	public String toString()
	{
		return "<" + getItem().getName() + ", " + getValue() + ">";
	}
	
	protected ItemValueGroup<T, S> getGroup()
	{
		return mGroup;
	}
}
