package com.deepercreeper.vampireapp.controller.descriptions;

import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.implementations.ListControllerImpl;

public class DescriptionController extends ListControllerImpl<Description>
{
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
