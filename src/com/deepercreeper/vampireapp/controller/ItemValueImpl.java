package com.deepercreeper.vampireapp.controller;

import android.content.Context;
import com.deepercreeper.vampireapp.controller.ValueController.PointHandler;

public abstract class ItemValueImpl <T extends Item> implements ItemValue<T>
{
	private CharMode				mMode;
	
	private final T					mItem;
	
	private final Context			mContext;
	
	private final UpdateAction		mUpdateAction;
	
	private final ItemValueGroup<T>	mGroup;
	
	private PointHandler			mPoints;
	
	public ItemValueImpl(final T aItem, final Context aContext, final UpdateAction aUpdateAction, final ItemValueGroup<T> aGroup,
			final CharMode aMode, final PointHandler aPoints)
	{
		mItem = aItem;
		mContext = aContext;
		mUpdateAction = aUpdateAction;
		mGroup = aGroup;
		mMode = aMode;
		mPoints = aPoints;
	}
	
	protected PointHandler getPoints()
	{
		return mPoints;
	}
	
	protected UpdateAction getUpdateAction()
	{
		return mUpdateAction;
	}
	
	protected ItemValueGroup<T> getGroup()
	{
		return mGroup;
	}
	
	@Override
	public CharMode getCreationMode()
	{
		return mMode;
	}
	
	@Override
	public void setCreationMode(final CharMode aMode)
	{
		mMode = aMode;
	}
	
	@Override
	public void setPoints(final PointHandler aPoints)
	{
		mPoints = aPoints;
	}
	
	protected Context getContext()
	{
		return mContext;
	}
	
	@Override
	public T getItem()
	{
		return mItem;
	}
}
