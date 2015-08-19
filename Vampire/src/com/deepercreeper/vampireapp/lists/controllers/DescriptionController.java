package com.deepercreeper.vampireapp.lists.controllers;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.lists.items.Description;
import android.content.res.Resources;

/**
 * This controller manages the list of description types.
 * 
 * @author vrl
 */
public class DescriptionController
{
	private final List<Description> mDescriptions = new ArrayList<Description>();
	
	/**
	 * Creates a new description list out of the given resources.
	 * 
	 * @param aResources
	 *            The resources.
	 */
	public DescriptionController(final Resources aResources)
	{
		for (final String description : aResources.getStringArray(R.array.description_fields))
		{
			mDescriptions.add(new Description(description));
		}
	}
	
	/**
	 * @return a list of all description types.
	 */
	public List<Description> getDescriptionsList()
	{
		return mDescriptions;
	}
}
