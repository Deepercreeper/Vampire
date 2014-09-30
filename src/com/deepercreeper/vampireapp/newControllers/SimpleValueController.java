package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.widget.LinearLayout;

public class SimpleValueController implements ValueController
{
	private boolean										mCreation;
	
	private final SimpleController						mController;
	
	private final HashMap<String, SimpleItemValueGroup>	mAttributes			= new HashMap<String, SimpleItemValueGroup>();
	
	private final List<SimpleItemValueGroup>			mAttributeGroups	= new ArrayList<SimpleItemValueGroup>();
	
	private final HashMap<String, SimpleItemValueGroup>	mAbilities			= new HashMap<String, SimpleItemValueGroup>();
	
	private final List<SimpleItemValueGroup>			mAbilityGroups		= new ArrayList<SimpleItemValueGroup>();
	
	private final SimpleItemValueGroup					mVirtues;
	
	public SimpleValueController(final SimpleController aController, final boolean aCreation)
	{
		mCreation = aCreation;
		mController = aController;
		for (final SimpleItemGroup group : mController.getAttributes())
		{
			final SimpleItemValueGroup valueGroup = new SimpleItemValueGroup(group, mCreation);
			mAttributes.put(group.getName(), valueGroup);
			mAttributeGroups.add(valueGroup);
		}
		for (final SimpleItemGroup group : mController.getAbilities())
		{
			final SimpleItemValueGroup valueGroup = new SimpleItemValueGroup(group, mCreation);
			mAbilities.put(group.getName(), valueGroup);
			mAbilityGroups.add(valueGroup);
		}
		mVirtues = new SimpleItemValueGroup(mController.getVirtues(), mCreation);
	}
	
	@Override
	public void setCreation(final boolean aCreation)
	{
		mCreation = aCreation;
		for (final SimpleItemValueGroup valueGroup : mAttributeGroups)
		{
			valueGroup.setCreation(mCreation);
		}
		for (final SimpleItemValueGroup valueGroup : mAbilityGroups)
		{
			valueGroup.setCreation(mCreation);
		}
		mVirtues.setCreation(mCreation);
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{
		final Context context = aLayout.getContext();
		aLayout.removeAllViews();
		
		/*
		 * TODO Initialize layout
		 * - Create 3 buttons and add linear layouts below them.
		 * - Each SimpleItemValueGroup should be initialized into one of the layouts.
		 * - This has to handle the opening and closing the layouts.
		 */
	}
	
	@Override
	public boolean isCreation()
	{
		return mCreation;
	}
	
	@Override
	public SimpleController getController()
	{
		return mController;
	}
}
