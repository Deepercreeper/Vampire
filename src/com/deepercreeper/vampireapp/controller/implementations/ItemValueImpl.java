package com.deepercreeper.vampireapp.controller.implementations;

import android.content.Context;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.interfaces.Item;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValueGroup;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;

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
	public PointHandler getPoints()
	{
		return mPoints;
	}
	
	@Override
	public UpdateAction getUpdateAction()
	{
		return mUpdateAction;
	}
	
	protected ItemValueGroup<T, S> getGroup()
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
	
	@Override
	public Context getContext()
	{
		return mContext;
	}
	
	@Override
	public T getItem()
	{
		return mItem;
	}
}
