package com.deepercreeper.vampireapp.controller.implementations;

import android.content.Context;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.interfaces.Item;
import com.deepercreeper.vampireapp.controller.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController;
import com.deepercreeper.vampireapp.controller.interfaces.VariableValueGroup;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;

public abstract class VariableValueGroupImpl <T extends Item, S extends ItemValue<T>> extends ItemValueGroupImpl<T> implements
		VariableValueGroup<T, S>
{
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
