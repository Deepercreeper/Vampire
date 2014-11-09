package com.deepercreeper.vampireapp.controller.implementations;

import android.content.Context;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.interfaces.Controller;
import com.deepercreeper.vampireapp.controller.interfaces.Item;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController;

/**
 * An implementation for value controllers. Each value controller should implement this class.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 */
public abstract class ValueControllerImpl <T extends Item> implements ValueController<T>
{
	private CharMode			mMode;
	
	private PointHandler		mPoints;
	
	private final UpdateAction	mUpdateAction;
	
	private final Context		mContext;
	
	private final Controller<T>	mController;
	
	/**
	 * Creates a new value controller.
	 * 
	 * @param aController
	 *            The controller type.
	 * @param aContext
	 *            The context.
	 * @param aMode
	 *            The creation mode.
	 * @param aPoints
	 *            The point handler.
	 * @param aAction
	 *            The update action.
	 */
	public ValueControllerImpl(final Controller<T> aController, final Context aContext, final CharMode aMode, final PointHandler aPoints,
			final UpdateAction aAction)
	{
		mController = aController;
		mContext = aContext;
		mMode = aMode;
		mPoints = aPoints;
		mUpdateAction = aAction;
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
	public PointHandler getPoints()
	{
		return mPoints;
	}
	
	@Override
	public Controller<T> getController()
	{
		return mController;
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
	public void updateValues(final boolean aUpdateOthers)
	{
		if (aUpdateOthers)
		{
			mUpdateAction.update();
		}
		else
		{
			updateValues();
		}
	}
	
	protected abstract void updateValues();
}
