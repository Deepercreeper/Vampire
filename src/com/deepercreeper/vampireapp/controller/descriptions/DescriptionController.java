package com.deepercreeper.vampireapp.controller.descriptions;

import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.implementations.ListControllerImpl;

/**
 * This controller manages the list of description types.
 * 
 * @author vrl
 */
public class DescriptionController extends ListControllerImpl<Description>
{
	/**
	 * Creates a new description list out of the given resources.
	 * 
	 * @param aResources
	 *            The resources.
	 */
	public DescriptionController(final Resources aResources)
	{
		final List<Description> descriptions = new ArrayList<Description>();
		for (final String description : aResources.getStringArray(R.array.description_fields))
		{
			descriptions.add(new Description(description));
		}
		init(descriptions);
	}
}
