package com.deepercreeper.vampireapp.controller.implementations;

import android.content.Context;
import com.deepercreeper.vampireapp.controller.interfaces.Item;
import com.deepercreeper.vampireapp.controller.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controller.interfaces.ItemCreationValue;
import com.deepercreeper.vampireapp.controller.interfaces.CreationValueController;
import com.deepercreeper.vampireapp.controller.interfaces.CreationValueController.PointHandler;
import com.deepercreeper.vampireapp.controller.interfaces.VariableCreationValueGroup;
import com.deepercreeper.vampireapp.creation.CharMode;

/**
 * An implementation for variable value groups. Each variable value group should extend this class.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 * @param <S>
 *            The value type.
 */
public abstract class VariableCreationValueGroupImpl <T extends Item, S extends ItemCreationValue<T>> extends ItemCreationValueGroupImpl<T, S> implements
		VariableCreationValueGroup<T, S>
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
	public VariableCreationValueGroupImpl(final ItemGroup<T> aGroup, final CreationValueController<T> aController, final Context aContext, final CharMode aMode,
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
