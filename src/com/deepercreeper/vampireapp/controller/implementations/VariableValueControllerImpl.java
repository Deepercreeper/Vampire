package com.deepercreeper.vampireapp.controller.implementations;

import android.content.Context;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.interfaces.Controller;
import com.deepercreeper.vampireapp.controller.interfaces.Item;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.interfaces.VariableValueGroup;

/**
 * An implementation for variable value controllers. Each variable value controller should extend this class.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 * @param <S>
 *            The value type.
 */
public abstract class VariableValueControllerImpl <T extends Item, S extends ItemValue<T>> extends ValueControllerImpl<T> implements
		VariableValueGroup<T, S>
{
	/**
	 * Creates a new variable value controller.
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
	public VariableValueControllerImpl(final Controller<T> aController, final Context aContext, final CharMode aMode, final PointHandler aPoints,
			final UpdateAction aAction)
	{
		super(aController, aContext, aMode, aPoints, aAction);
	}
	
	@Override
	public void clear()
	{
		close();
	}
}
