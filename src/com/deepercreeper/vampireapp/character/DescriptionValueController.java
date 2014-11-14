package com.deepercreeper.vampireapp.character;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.controller.descriptions.DescriptionCreationValue;
import com.deepercreeper.vampireapp.controller.descriptions.DescriptionCreationValueController;

public class DescriptionValueController
{
	private final Map<String, DescriptionValue>	mValues	= new HashMap<String, DescriptionValue>();
	
	public DescriptionValueController(final DescriptionCreationValueController aController)
	{
		for (final DescriptionCreationValue value : aController.getValues())
		{
			mValues.put(value.getName(), new DescriptionValue(value.getItem(), value.getValue()));
		}
	}
	
	public DescriptionValue getValue(final String aName)
	{
		return mValues.get(aName);
	}
}
