package com.deepercreeper.vampireapp.controller.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.interfaces.Item;
import com.deepercreeper.vampireapp.controller.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValueGroup;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;

public abstract class ItemValueGroupImpl <T extends Item> implements ItemValueGroup<T>
{
	private CharMode						mMode;
	
	private PointHandler					mPoints;
	
	private final Context					mContext;
	
	private final ValueController<T>		mController;
	
	private final ItemGroup<T>				mGroup;
	
	private final List<ItemValue<T>>		mValuesList	= new ArrayList<ItemValue<T>>();
	
	private final HashMap<T, ItemValue<T>>	mValues		= new HashMap<T, ItemValue<T>>();
	
	private final UpdateAction				mUpdateAction;
	
	public ItemValueGroupImpl(final ItemGroup<T> aGroup, final ValueController<T> aController, final Context aContext, final CharMode aMode,
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
	public ValueController<T> getController()
	{
		return mController;
	}
	
	@Override
	public void resetTempPoints()
	{
		for (final ItemValue<T> value : mValuesList)
		{
			value.resetTempPoints();
		}
	}
	
	@Override
	public ItemGroup<T> getGroup()
	{
		return mGroup;
	}
	
	@Override
	public HashMap<T, ? extends ItemValue<T>> getValues()
	{
		return mValues;
	}
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final ItemValue<T> valueItem : mValuesList)
		{
			value += valueItem.getValue();
		}
		return value;
	}
	
	@Override
	public int getTempPoints()
	{
		int value = 0;
		for (final ItemValue<T> valueItem : mValuesList)
		{
			value += valueItem.getTempPoints();
		}
		return value;
	}
	
	@Override
	public ItemValue<T> getValue(final String aName)
	{
		return mValues.get(getGroup().getItem(aName));
	}
	
	@Override
	public void release()
	{
		for (final ItemValue<T> value : mValuesList)
		{
			value.release();
		}
	}
	
	@Override
	public List<? extends ItemValue<T>> getValuesList()
	{
		return mValuesList;
	}
	
	@Override
	public CharMode getCreationMode()
	{
		return mMode;
	}
	
	@Override
	public void setCreationMode(final CharMode aMode)
	{
		final boolean resetTempPoints = mMode == CharMode.POINTS && aMode == CharMode.MAIN;
		mMode = aMode;
		for (final ItemValue<T> value : mValuesList)
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
		for (final ItemValue<T> value : mValuesList)
		{
			value.setPoints(mPoints);
		}
	}
	
	@Override
	public void updateValues(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		for (final ItemValue<T> value : mValuesList)
		{
			value.setIncreasable(aCanIncrease && value.canIncrease(mMode));
			value.setDecreasable(aCanDecrease && value.canDecrease(mMode));
		}
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
	public Context getContext()
	{
		return mContext;
	}
}
