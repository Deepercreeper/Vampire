package com.deepercreeper.vampireapp.controller.lists;

import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.implementations.ListControllerImpl;

/**
 * A list controller for all natures.
 * 
 * @author vrl
 */
public class NatureController extends ListControllerImpl<Nature>
{
	/**
	 * Creates a new nature controller.
	 * 
	 * @param aResources
	 *            The resources.
	 */
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
