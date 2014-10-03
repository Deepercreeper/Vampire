package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.ResizeAnimation;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class SimpleValueController implements ValueController<SimpleItem>
{
	private boolean										mCreation;
	
	private final SimpleController						mController;
	
	private final HashMap<String, SimpleItemValueGroup>	mAttributes		= new HashMap<String, SimpleItemValueGroup>();
	
	private final List<SimpleItemValueGroup>			mAttributesList	= new ArrayList<SimpleItemValueGroup>();
	
	private final HashMap<String, SimpleItemValueGroup>	mAbilities		= new HashMap<String, SimpleItemValueGroup>();
	
	private final List<SimpleItemValueGroup>			mAbilitiesList	= new ArrayList<SimpleItemValueGroup>();
	
	private final SimpleItemValueGroup					mVirtues;
	
	public SimpleValueController(final SimpleController aController, final boolean aCreation)
	{
		mCreation = aCreation;
		mController = aController;
		for (final SimpleItemGroup group : mController.getAttributes())
		{
			final SimpleItemValueGroup valueGroup = new SimpleItemValueGroup(group, this, mCreation);
			mAttributes.put(group.getName(), valueGroup);
			mAttributesList.add(valueGroup);
		}
		for (final SimpleItemGroup group : mController.getAbilities())
		{
			final SimpleItemValueGroup valueGroup = new SimpleItemValueGroup(group, this, mCreation);
			mAbilities.put(group.getName(), valueGroup);
			mAbilitiesList.add(valueGroup);
		}
		mVirtues = new SimpleItemValueGroup(mController.getVirtues(), this, mCreation);
	}
	
	@Override
	public void setCreation(final boolean aCreation)
	{
		mCreation = aCreation;
		for (final SimpleItemValueGroup valueGroup : mAttributesList)
		{
			valueGroup.setCreation(mCreation);
		}
		for (final SimpleItemValueGroup valueGroup : mAbilitiesList)
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
		
		final LayoutParams wrapHeight = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams zeroHeight = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		
		aLayout.setLayoutParams(wrapHeight);
		
		// Attributes
		final Button showAttributes = new Button(context);
		final LinearLayout attributes = new LinearLayout(context);
		attributes.setLayoutParams(zeroHeight);
		
		showAttributes.setLayoutParams(wrapHeight);
		showAttributes.setText(R.string.attributes);
		showAttributes.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		showAttributes.setOnClickListener(new OnClickListener()
		{
			private boolean			mOpen			= false;
			
			private final boolean	mInitialized	= false;
			
			@Override
			public void onClick(final View aArg0)
			{
				mOpen = !mOpen;
				if (mOpen)
				{
					if ( !mInitialized)
					{
						for (final SimpleItemValueGroup valueGroup : mAttributesList)
						{
							valueGroup.initLayout(attributes);
						}
					}
					attributes.startAnimation(new ResizeAnimation(attributes, attributes.getWidth(), ViewUtil.calcHeight(attributes)));
					showAttributes.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					attributes.startAnimation(new ResizeAnimation(attributes, attributes.getWidth(), 0));
					showAttributes.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
			}
		});
		
		aLayout.addView(showAttributes);
		aLayout.addView(attributes);
		
		// Abilities
		final Button showAbilities = new Button(context);
		final LinearLayout abilities = new LinearLayout(context);
		abilities.setLayoutParams(zeroHeight);
		
		showAbilities.setLayoutParams(wrapHeight);
		showAbilities.setText(R.string.abilities);
		showAbilities.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		showAbilities.setOnClickListener(new OnClickListener()
		{
			private boolean			mOpen			= false;
			
			private final boolean	mInitialized	= false;
			
			@Override
			public void onClick(final View aArg0)
			{
				mOpen = !mOpen;
				if (mOpen)
				{
					if ( !mInitialized)
					{
						for (final SimpleItemValueGroup valueGroup : mAbilitiesList)
						{
							valueGroup.initLayout(abilities);
						}
					}
					abilities.startAnimation(new ResizeAnimation(abilities, abilities.getWidth(), ViewUtil.calcHeight(abilities)));
					showAbilities.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					abilities.startAnimation(new ResizeAnimation(abilities, abilities.getWidth(), 0));
					showAbilities.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
			}
		});
		
		aLayout.addView(showAbilities);
		aLayout.addView(abilities);
		
		// Virtues
		final Button showVirtues = new Button(context);
		final LinearLayout virtues = new LinearLayout(context);
		virtues.setLayoutParams(zeroHeight);
		
		showVirtues.setLayoutParams(wrapHeight);
		showVirtues.setText(R.string.virtues);
		showVirtues.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		showVirtues.setOnClickListener(new OnClickListener()
		{
			private boolean			mOpen			= false;
			
			private final boolean	mInitialized	= false;
			
			@Override
			public void onClick(final View aArg0)
			{
				mOpen = !mOpen;
				if (mOpen)
				{
					if ( !mInitialized)
					{
						mVirtues.initLayout(virtues);
					}
					virtues.startAnimation(new ResizeAnimation(virtues, virtues.getWidth(), ViewUtil.calcHeight(virtues)));
					showVirtues.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					virtues.startAnimation(new ResizeAnimation(virtues, virtues.getWidth(), 0));
					showVirtues.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
			}
		});
		
		aLayout.addView(showVirtues);
		aLayout.addView(virtues);
	}
	
	@Override
	public void updateValues()
	{
		int[] maxValues;
		
		maxValues = mController.getAttributeCreationValues();
		for (final SimpleItemValueGroup group : mAttributesList)
		{
			group.updateValues(canIncrease(group, mAttributesList, maxValues), true);
		}
		
		maxValues = mController.getAbilityCreationValues();
		for (final SimpleItemValueGroup group : mAbilitiesList)
		{
			group.updateValues(canIncrease(group, mAbilitiesList, maxValues), true);
		}
		
		mVirtues.updateValues(mVirtues.getValue() < mController.getVirtueCreationValue(), true);
	}
	
	private boolean canIncrease(final SimpleItemValueGroup aGroup, final List<SimpleItemValueGroup> aGroups, final int[] aMaxValues)
	{
		final int value = aGroup.getValue();
		final HashSet<Integer> values = new HashSet<Integer>();
		for (final SimpleItemValueGroup group : aGroups)
		{
			if (group != aGroup)
			{
				values.add(group.getValue());
			}
		}
		
		boolean maxDone = false, midDone = false;
		if (Collections.max(values) > aMaxValues[1])
		{
			maxDone = true;
		}
		if (Collections.min(values) > aMaxValues[0])
		{
			midDone = true;
		}
		if (value == aMaxValues[0] && midDone)
		{
			return false;
		}
		if (value == aMaxValues[1] && maxDone)
		{
			return false;
		}
		return value < aMaxValues[2];
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
