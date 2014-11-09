package com.deepercreeper.vampireapp.controller.implementations;

import android.content.Context;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.interfaces.Item;
import com.deepercreeper.vampireapp.controller.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;
import com.deepercreeper.vampireapp.controller.interfaces.VariableValueGroup;

/**
 * An implementation for variable value groups. Each variable value group should extend this class.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 * @param <S>
 *            The value type.
 */
public abstract class VariableValueGroupImpl <T extends Item, S extends ItemValue<T>> extends ItemValueGroupImpl<T, S> implements
		VariableValueGroup<T, S>
{
	/**
	 * Creates a new variable value group.
	 * 
	 * @param aGroup
	 *            The group type.
	 * @param aController
	 *            The parent controller.
	 * @param aContext
	 *            The context.
	 * @param aMode
	 *            The creation mode.
	 * @param aPoints
	 *            The point handler.
	 */
	public VariableValueGroupImpl(final ItemGroup<T> aGroup, final ValueController<T> aController, final Context aContext, final CharMode aMode,
			final PointHandler aPoints)
	{
		super(aGroup, aController, aContext, aMode, aPoints);
	}
	
	@Override
	public void clear()
	{
		getValues().clear();
		getValuesList().clear();
	}
}
