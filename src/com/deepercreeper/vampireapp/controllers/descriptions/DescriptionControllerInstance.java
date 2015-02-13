package com.deepercreeper.vampireapp.controllers.descriptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DescriptionControllerInstance
{
	private final Map<String, DescriptionValue>	mValues	= new HashMap<String, DescriptionValue>();
	
	public DescriptionControllerInstance(final Map<String, String> aValues, final DescriptionController aController)
	{
		for (final Description description : aController.getValuesList())
		{
			String value = aValues.get(description.getName());
			if (value == null)
			{
				value = "";
			}
			mValues.put(description.getName(), new DescriptionValue(description, value));
		}
	}
	
	public DescriptionControllerInstance(final DescriptionControllerCreation aController)
	{
		for (final DescriptionCreationValue value : aController.getValuesList())
		{
			mValues.put(value.getName(), new DescriptionValue(value.getItem(), value.getValue()));
		}
	}
	
	public Set<String> getKeys()
	{
		return mValues.keySet();
	}
	
	public DescriptionValue getValue(final String aName)
	{
		return mValues.get(aName);
	}
}
