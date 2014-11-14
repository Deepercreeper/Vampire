package com.deepercreeper.vampireapp.controller.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.controller.interfaces.Item;
import com.deepercreeper.vampireapp.controller.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controller.interfaces.ItemCreationValue;
import com.deepercreeper.vampireapp.controller.interfaces.ItemCreationValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.interfaces.ItemCreationValueGroup;
import com.deepercreeper.vampireapp.controller.interfaces.CreationValueController;
import com.deepercreeper.vampireapp.controller.interfaces.CreationValueController.PointHandler;
import com.deepercreeper.vampireapp.creation.CharMode;

/**
 * A implementation of value groups. Each value group should extend this class.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 * @param <S>
 *            The value type.
 */
public abstract class ItemCreationValueGroupImpl <T extends Item, S extends ItemCreationValue<T>> implements ItemCreationValueGroup<T, S>
{
	private CharMode					mMode;
	
	private PointHandler				mPoints;
	
	private final Context				mContext;
	
	private final CreationValueController<T>	mController;
	
	private final ItemGroup<T>			mGroup;
	
	private final List<S>				mValuesList	= new ArrayList<S>();
	
	private final HashMap<T, S>			mValues		= new HashMap<T, S>();
	
	private final UpdateAction			mUpdateAction;
	
	/**
	 * Creates a new value group.
	 * 
	 * @param aGroup
	 *            The item group type.
	 * @param aController
	 *            The parent controller.
	 * @param aContext
	 *            The context.
	 * @param aMode
	 *            The creation mode.
	 * @param aPoints
	 *            The point handler.
	 */
	public ItemCreationValueGroupImpl(final ItemGroup<T> aGroup, final CreationValueController<T> aController, final Context aContext, final CharMode aMode,
			final PointHandler aPoints)
	{
		mGroup = aGroup;
		mController = aController;
		mContext = aContext;
		mMode = aMode;
		mPoints = aPoints;
		mUpdateAction = new UpdateAction()
		{
			@Override
			public void update()
			{
				mController.updateValues(mMode == CharMode.POINTS);
			}
		};
	}
	
	@Override
	public Context getContext()
	{
		return mContext;
	}
	
	@Override
	public CreationValueController<T> getController()
	{
		return mController;
	}
	
	@Override
	public CharMode getCreationMode()
	{
		return mMode;
	}
	
	@Override
	public List<S> getDescriptionValues()
	{
		final List<S> list = new ArrayList<S>();
		for (final S value : mValuesList)
		{
			if (value.getItem().needsDescription() && value.getValue() > 0)
			{
				list.add(value);
			}
		}
		return list;
	}
	
	@Override
	public ItemGroup<T> getGroup()
	{
		return mGroup;
	}
	
	@Override
	public PointHandler getPoints()
	{
		return mPoints;
	}
	
	@Override
	public int getTempPoints()
	{
		int value = 0;
		for (final ItemCreationValue<T> valueItem : mValuesList)
		{
			value += valueItem.getTempPoints();
		}
		return value;
	}
	
	@Override
	public UpdateAction getUpdateAction()
	{
		return mUpdateAction;
	}
	
	@Override
	public boolean hasValue(final String aValue)
	{
		for (final S value : mValuesList)
		{
			if (value.getItem().getName().equals(aValue))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final ItemCreationValue<T> valueItem : mValuesList)
		{
			value += valueItem.getValue();
		}
		return value;
	}
	
	@Override
	public S getValue(final String aName)
	{
		return mValues.get(getGroup().getItem(aName));
	}
	
	@Override
	public HashMap<T, S> getValues()
	{
		return mValues;
	}
	
	@Override
	public List<S> getValuesList()
	{
		return mValuesList;
	}
	
	@Override
	public void release()
	{
		for (final ItemCreationValue<T> value : mValuesList)
		{
			value.release();
		}
	}
	
	@Override
	public void resetTempPoints()
	{
		for (final ItemCreationValue<T> value : mValuesList)
		{
			value.resetTempPoints();
		}
	}
	
	@Override
	public void setCreationMode(final CharMode aMode)
	{
		final boolean resetTempPoints = mMode == CharMode.POINTS && aMode == CharMode.MAIN;
		mMode = aMode;
		for (final ItemCreationValue<T> value : mValuesList)
		{
			value.setCreationMode(mMode);
			if (resetTempPoints)
			{
				value.resetTempPoints();
			}
		}
	}
	
	@Override
	public void setPoints(final PointHandler aPoints)
	{
		mPoints = aPoints;
		for (final ItemCreationValue<T> value : mValuesList)
		{
			value.setPoints(mPoints);
		}
	}
	
	@Override
	public String toString()
	{
		return getGroup().getName() + ": " + getValuesList().toString();
	}
	
	@Override
	public void updateValues(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		for (final ItemCreationValue<T> value : mValuesList)
		{
			value.setIncreasable(aCanIncrease && value.canIncrease(mMode));
			value.setDecreasable(aCanDecrease && value.canDecrease(mMode));
		}
	}
}
