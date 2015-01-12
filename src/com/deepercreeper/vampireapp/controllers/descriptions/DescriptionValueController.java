package com.deepercreeper.vampireapp.controllers.descriptions;

import java.util.HashMap;
import java.util.Map;

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
