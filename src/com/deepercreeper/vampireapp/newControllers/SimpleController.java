package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

public class SimpleController
{
	private final HashMap<String, SimpleItemGroup>	mAttributes			= new HashMap<String, SimpleItemGroup>();
	
	private final List<SimpleItemGroup>				mAttributeGroups	= new ArrayList<SimpleItemGroup>();
	
	private final HashMap<String, SimpleItemGroup>	mAbilities			= new HashMap<String, SimpleItemGroup>();
	
	private final List<SimpleItemGroup>				mAbilityGroups		= new ArrayList<SimpleItemGroup>();
	
	private final SimpleItemGroup					mVirtues;
	
	public SimpleController(final Resources aResources)
	{
		for (final String attributesGroup : aResources.getStringArray(R.array.attributes))
		{
			final SimpleItemGroup group = SimpleItemGroup.create(attributesGroup, 1);
			mAttributes.put(group.getName(), group);
			mAttributeGroups.add(group);
		}
		Collections.sort(mAttributeGroups, SimpleItemGroup.getComparator());
		for (final String abilitiesGroup : aResources.getStringArray(R.array.abilities))
		{
			final SimpleItemGroup group = SimpleItemGroup.create(abilitiesGroup, 0);
			mAbilities.put(group.getName(), group);
			mAbilityGroups.add(group);
		}
		Collections.sort(mAbilityGroups, SimpleItemGroup.getComparator());
		mVirtues = SimpleItemGroup.create(aResources.getString(R.string.virutes), 1);
	}
	
	public List<SimpleItemGroup> getAbilities()
	{
		return mAbilityGroups;
	}
	
	public List<SimpleItemGroup> getAttributes()
	{
		return mAttributeGroups;
	}
	
	public SimpleItemGroup getVirtues()
	{
		return mVirtues;
	}
}
