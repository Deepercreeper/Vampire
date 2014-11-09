package com.deepercreeper.vampireapp.controller.implementations;

import android.content.Context;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.interfaces.Controller;
import com.deepercreeper.vampireapp.controller.interfaces.Item;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.interfaces.VariableValueGroup;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue.UpdateAction;

public abstract class VariableValueControllerImpl <T extends Item, S extends ItemValue<T>> extends ValueControllerImpl<T> implements
		VariableValueGroup<T, S>
{
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
