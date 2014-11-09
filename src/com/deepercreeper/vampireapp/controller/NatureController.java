package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.implementations.ListControllerImpl;

public class NatureController extends ListControllerImpl<Nature>
{
	public NatureController(final Resources aResources)
	{
		final List<Nature> natures = new ArrayList<Nature>();
		for (final String natureAndBehavior : aResources.getStringArray(R.array.nature_and_behavior))
		{
			natures.add(new Nature(natureAndBehavior));
		}
		init(natures);
	}
}
