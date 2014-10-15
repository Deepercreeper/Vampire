package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

public class SimpleController implements Controller<SimpleItem>
{
	private final int[]								mAttributeCreationValues;
	
	private final int[]								mAbilityCreationValues;
	
	private final int								mVirtueCreationValue;
	
	private final HashMap<String, SimpleItemGroup>	mAttributes			= new HashMap<String, SimpleItemGroup>();
	
	private final List<SimpleItemGroup>				mAttributeGroups	= new ArrayList<SimpleItemGroup>();
	
	private final HashMap<String, SimpleItemGroup>	mAbilities			= new HashMap<String, SimpleItemGroup>();
	
	private final List<SimpleItemGroup>				mAbilityGroups		= new ArrayList<SimpleItemGroup>();
	
	private final SimpleItemGroup					mVirtues;
	
	public SimpleController(final Resources aResources)
	{
		
		mAttributeCreationValues = aResources.getIntArray(R.array.attributes_max_creation_values);
		mAbilityCreationValues = aResources.getIntArray(R.array.abilities_max_creation_values);
		mVirtueCreationValue = aResources.getInteger(R.integer.virtue_max_creation_value);
		for (final String attributesGroup : aResources.getStringArray(R.array.attribute_values))
		{
			final SimpleItemGroup group = SimpleItemGroup.create(attributesGroup, 1, 4);
			mAttributes.put(group.getName(), group);
			mAttributeGroups.add(group);
		}
		for (final String abilitiesGroup : aResources.getStringArray(R.array.ability_values))
		{
			final SimpleItemGroup group = SimpleItemGroup.create(abilitiesGroup, 0, 3);
			mAbilities.put(group.getName(), group);
			mAbilityGroups.add(group);
		}
		mVirtues = SimpleItemGroup.create(aResources.getString(R.string.virtue_values), 1, 4);
	}
	
	public int[] getAttributeCreationValues()
	{
		return mAttributeCreationValues;
	}
	
	public int[] getAbilityCreationValues()
	{
		return mAbilityCreationValues;
	}
	
	public int getVirtueCreationValue()
	{
		return mVirtueCreationValue;
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
