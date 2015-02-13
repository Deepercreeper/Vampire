package com.deepercreeper.vampireapp.lists.controllers.instances;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionController;
import com.deepercreeper.vampireapp.lists.controllers.creations.DescriptionCreationController;
import com.deepercreeper.vampireapp.lists.items.Description;
import com.deepercreeper.vampireapp.lists.items.DescriptionCreation;
import com.deepercreeper.vampireapp.lists.items.DescriptionInstance;

public class DescriptionInstanceController
{
	private final Map<String, DescriptionInstance>	mValues	= new HashMap<String, DescriptionInstance>();
	
	public DescriptionInstanceController(final Map<String, String> aValues, final DescriptionController aController)
	{
		for (final Description description : aController.getValuesList())
		{
			String value = aValues.get(description.getName());
			if (value == null)
			{
				value = "";
			}
			mValues.put(description.getName(), new DescriptionInstance(description, value));
		}
	}
	
	public DescriptionInstanceController(final DescriptionCreationController aController)
	{
		for (final DescriptionCreation value : aController.getValuesList())
		{
			mValues.put(value.getName(), new DescriptionInstance(value.getItem(), value.getValue()));
		}
	}
	
	public Set<String> getKeys()
	{
		return mValues.keySet();
	}
	
	public DescriptionInstance getValue(final String aName)
	{
		return mValues.get(aName);
	}
}
