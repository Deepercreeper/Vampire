package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleValueController
{
	private final SimpleController						mController;
	
	private final HashMap<String, SimpleItemValueGroup>	mAttributes			= new HashMap<String, SimpleItemValueGroup>();
	
	private final List<SimpleItemValueGroup>			mAttributeGroups	= new ArrayList<SimpleItemValueGroup>();
	
	private final HashMap<String, SimpleItemValueGroup>	mAbilities			= new HashMap<String, SimpleItemValueGroup>();
	
	private final List<SimpleItemValueGroup>			mAbilityGroups		= new ArrayList<SimpleItemValueGroup>();
	
	private final SimpleItemValueGroup					mVirtues;
	
	public SimpleValueController(SimpleController aController)
	{
		mController = aController;
		for (SimpleItemGroup group : mController.getAttributes())
		{
			SimpleItemValueGroup valueGroup = new SimpleItemValueGroup(group);
			mAttributes.put(group.getName(), valueGroup);
			mAttributeGroups.add(valueGroup);
		}
		for (SimpleItemGroup group : mController.getAbilities())
		{
			SimpleItemValueGroup valueGroup = new SimpleItemValueGroup(group);
			mAbilities.put(group.getName(), valueGroup);
			mAbilityGroups.add(valueGroup);
		}
		mVirtues = new SimpleItemValueGroup(mController.getVirtues());
	}
	
	public SimpleController getController()
	{
		return mController;
	}
}
