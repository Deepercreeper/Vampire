package com.deepercreeper.vampireapp.newControllers;

import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

public class PropertyController implements Controller<PropertyItem>
{
	private final PropertyItemGroup	mProperties;
	
	public PropertyController(final Resources aResources)
	{
		mProperties = PropertyItemGroup.create(aResources.getString(R.string.properties), aResources.getStringArray(R.array.properties));
	}
	
	public PropertyItemGroup getProperties()
	{
		return mProperties;
	}
}
