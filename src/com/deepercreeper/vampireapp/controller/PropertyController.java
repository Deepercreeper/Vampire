package com.deepercreeper.vampireapp.controller;

import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

/**
 * A controller for property item value groups.
 * 
 * @author Vincent
 */
public class PropertyController implements Controller<PropertyItem>
{
	private final PropertyItemGroup	mProperties;
	
	/**
	 * Creates a new property controller out of the given resources.
	 * 
	 * @param aResources
	 *            The context resources.
	 */
	public PropertyController(final Resources aResources)
	{
		mProperties = PropertyItemGroup.create(aResources.getString(R.string.properties), aResources.getStringArray(R.array.properties));
	}
	
	/**
	 * @return the property item group.
	 */
	public PropertyItemGroup getProperties()
	{
		return mProperties;
	}
}
