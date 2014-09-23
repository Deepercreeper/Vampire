package com.deepercreeper.vampireapp.newControllers;

import java.util.HashMap;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

public class SimpleController
{
	private final HashMap<String, SimpleItemGroup>	mAttributes	= new HashMap<String, SimpleItemGroup>();
	
	private final HashMap<String, SimpleItemGroup>	mAbilities	= new HashMap<String, SimpleItemGroup>();
	
	private final SimpleItemGroup					mVirtues;
	
	public SimpleController(final Resources aResources)
	{
		for (final String attributesGroup : aResources.getStringArray(R.array.attributes))
		{
			final SimpleItemGroup group = SimpleItemGroup.create(attributesGroup, 1);
			mAttributes.put(group.getName(), group);
		}
		for (final String abilitiesGroup : aResources.getStringArray(R.array.abilities))
		{
			final SimpleItemGroup group = SimpleItemGroup.create(abilitiesGroup, 0);
			mAbilities.put(group.getName(), group);
		}
		mVirtues = SimpleItemGroup.create(aResources.getString(R.string.virutes), 1);
	}
}
