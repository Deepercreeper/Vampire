package com.deepercreeper.vampireapp.controller.descriptions;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.controller.Description;
import com.deepercreeper.vampireapp.controller.DescriptionController;
import com.deepercreeper.vampireapp.controller.implementations.ListControllerImpl;

public class DescriptionValueController extends ListControllerImpl<DescriptionValue>
{
	private final DescriptionController	mController;
	
	public DescriptionValueController(final DescriptionController aController)
	{
		mController = aController;
		final List<DescriptionValue> values = new ArrayList<DescriptionValue>();
		for (final Description description : mController.getValues())
		{
			values.add(new DescriptionValue(description));
		}
		init(values);
	}
	
	public void clear()
	{
		for (final DescriptionValue value : getValues())
		{
			value.clear();
		}
	}
}
