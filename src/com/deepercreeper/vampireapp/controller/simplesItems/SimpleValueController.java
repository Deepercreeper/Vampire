package com.deepercreeper.vampireapp.controller.simplesItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.ResizeAnimation;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.implementations.ValueControllerImpl;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A controller for simple value groups.
 * 
 * @author Vincent
 */
public class SimpleValueController extends ValueControllerImpl<SimpleItem>
{
	private Button										mShowAttributesPanel;
	
	private Button										mShowAbilitiesPanel;
	
	private Button										mShowVirtuesPanel;
	
	private boolean										mAttributesOpen;
	
	private boolean										mAbilitiesOpen;
	
	private boolean										mVirtuesOpen;
	
	private boolean										mInitializedAttributes	= false;
	
	private boolean										mInitializedAbilities	= false;
	
	private boolean										mInitializedVirtues		= false;
	
	private final HashMap<String, SimpleItemValueGroup>	mAttributes				= new HashMap<String, SimpleItemValueGroup>();
	
	private final List<SimpleItemValueGroup>			mAttributesList			= new ArrayList<SimpleItemValueGroup>();
	
	private final HashMap<String, SimpleItemValueGroup>	mAbilities				= new HashMap<String, SimpleItemValueGroup>();
	
	private final List<SimpleItemValueGroup>			mAbilitiesList			= new ArrayList<SimpleItemValueGroup>();
	
	private final SimpleItemValueGroup					mVirtues;
	
	/**
	 * Creates a new simple value controller.
	 * 
	 * @param aController
	 *            The controller type.
	 * @param aContext
	 *            The context.
	 * @param aMode
	 *            Whether this controller is inside the creation mode.
	 * @param aPoints
	 *            The caller for free or experience points.
	 * @param aUpdateOthers
	 *            The update others action.
	 */
	public SimpleValueController(final SimpleController aController, final Context aContext, final CharMode aMode, final PointHandler aPoints,
			final UpdateAction aUpdateOthers)
	{
		super(aController, aContext, aMode, aPoints, aUpdateOthers);
		for (final SimpleItemGroup group : getController().getAttributes())
		{
			final SimpleItemValueGroup valueGroup = new SimpleItemValueGroup(group, this, getContext(), getCreationMode(), getPoints());
			mAttributes.put(group.getName(), valueGroup);
			mAttributesList.add(valueGroup);
		}
		for (final SimpleItemGroup group : getController().getAbilities())
		{
			final SimpleItemValueGroup valueGroup = new SimpleItemValueGroup(group, this, getContext(), getCreationMode(), getPoints());
			mAbilities.put(group.getName(), valueGroup);
			mAbilitiesList.add(valueGroup);
		}
		mVirtues = new SimpleItemValueGroup(getController().getVirtues(), this, getContext(), getCreationMode(), getPoints());
	}
	
	@Override
	public void close()
	{
		if (mAttributesOpen)
		{
			mShowAttributesPanel.callOnClick();
		}
		if (mAbilitiesOpen)
		{
			mShowAbilitiesPanel.callOnClick();
		}
		if (mVirtuesOpen)
		{
			mShowVirtuesPanel.callOnClick();
		}
	}
	
	@Override
	public SimpleController getController()
	{
		return (SimpleController) super.getController();
	}
	
	@Override
	public List<ItemValue<SimpleItem>> getDescriptionValues()
	{
		final List<ItemValue<SimpleItem>> list = new ArrayList<ItemValue<SimpleItem>>();
		for (final SimpleItemValueGroup group : mAttributesList)
		{
			list.addAll(group.getDescriptionValues());
		}
		for (final SimpleItemValueGroup group : mAbilitiesList)
		{
			list.addAll(group.getDescriptionValues());
		}
		list.addAll(mVirtues.getDescriptionValues());
		return list;
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{
		final Context context = aLayout.getContext();
		aLayout.removeAllViews();
		
		mAttributesOpen = mInitializedAttributes = false;
		mAbilitiesOpen = mInitializedAbilities = false;
		mVirtuesOpen = mInitializedVirtues = false;
		
		// Attributes
		mShowAttributesPanel = new Button(context);
		final TableLayout attributes = new TableLayout(context);
		attributes.setLayoutParams(ViewUtil.instance().getZeroHeight());
		
		mShowAttributesPanel.setLayoutParams(ViewUtil.instance().getWrapHeight());
		mShowAttributesPanel.setText(R.string.attributes);
		mShowAttributesPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		mShowAttributesPanel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aArg0)
			{
				mAttributesOpen = !mAttributesOpen;
				if (mAttributesOpen)
				{
					if ( !mInitializedAttributes)
					{
						for (final SimpleItemValueGroup valueGroup : mAttributesList)
						{
							valueGroup.initLayout(attributes);
						}
						mInitializedAttributes = true;
						updateValues(false);
					}
					attributes.startAnimation(new ResizeAnimation(attributes, attributes.getWidth(), ViewUtil.calcHeight(attributes)));
					mShowAttributesPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					attributes.startAnimation(new ResizeAnimation(attributes, attributes.getWidth(), 0));
					mShowAttributesPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
				}
			}
		});
		
		aLayout.addView(mShowAttributesPanel);
		aLayout.addView(attributes);
		
		// Abilities
		mShowAbilitiesPanel = new Button(context);
		final TableLayout abilities = new TableLayout(context);
		abilities.setLayoutParams(ViewUtil.instance().getZeroHeight());
		
		mShowAbilitiesPanel.setLayoutParams(ViewUtil.instance().getWrapHeight());
		mShowAbilitiesPanel.setText(R.string.abilities);
		mShowAbilitiesPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		mShowAbilitiesPanel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aArg0)
			{
				mAbilitiesOpen = !mAbilitiesOpen;
				if (mAbilitiesOpen)
				{
					if ( !mInitializedAbilities)
					{
						for (final SimpleItemValueGroup valueGroup : mAbilitiesList)
						{
							valueGroup.initLayout(abilities);
						}
						mInitializedAbilities = true;
						updateValues(false);
					}
					abilities.startAnimation(new ResizeAnimation(abilities, abilities.getWidth(), ViewUtil.calcHeight(abilities)));
					mShowAbilitiesPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					abilities.startAnimation(new ResizeAnimation(abilities, abilities.getWidth(), 0));
					mShowAbilitiesPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
				}
			}
		});
		
		aLayout.addView(mShowAbilitiesPanel);
		aLayout.addView(abilities);
		
		// Virtues
		mShowVirtuesPanel = new Button(context);
		final TableLayout virtues = new TableLayout(context);
		virtues.setLayoutParams(ViewUtil.instance().getZeroHeight());
		
		mShowVirtuesPanel.setLayoutParams(ViewUtil.instance().getWrapHeight());
		mShowVirtuesPanel.setText(R.string.virtues);
		mShowVirtuesPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		mShowVirtuesPanel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aArg0)
			{
				mVirtuesOpen = !mVirtuesOpen;
				if (mVirtuesOpen)
				{
					if ( !mInitializedVirtues)
					{
						mVirtues.initLayout(virtues);
						mInitializedVirtues = true;
						updateValues(false);
					}
					virtues.startAnimation(new ResizeAnimation(virtues, virtues.getWidth(), ViewUtil.calcHeight(virtues)));
					mShowVirtuesPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					virtues.startAnimation(new ResizeAnimation(virtues, virtues.getWidth(), 0));
					mShowVirtuesPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
				}
			}
		});
		
		aLayout.addView(mShowVirtuesPanel);
		aLayout.addView(virtues);
	}
	
	@Override
	public void release()
	{
		for (final SimpleItemValueGroup group : mAttributesList)
		{
			group.release();
		}
		for (final SimpleItemValueGroup group : mAbilitiesList)
		{
			group.release();
		}
		mVirtues.release();
	}
	
	@Override
	public void resetTempPoints()
	{
		for (final SimpleItemValueGroup group : mAttributesList)
		{
			group.resetTempPoints();
		}
		for (final SimpleItemValueGroup group : mAbilitiesList)
		{
			group.resetTempPoints();
		}
		mVirtues.resetTempPoints();
	}
	
	@Override
	public void setCreationMode(final CharMode aMode)
	{
		super.setCreationMode(aMode);
		for (final SimpleItemValueGroup valueGroup : mAttributesList)
		{
			valueGroup.setCreationMode(aMode);
		}
		for (final SimpleItemValueGroup valueGroup : mAbilitiesList)
		{
			valueGroup.setCreationMode(aMode);
		}
		mVirtues.setCreationMode(aMode);
	}
	
	@Override
	public void setEnabled(final boolean aEnabled)
	{
		if (mShowAttributesPanel != null)
		{
			mShowAttributesPanel.setEnabled(aEnabled);
		}
		if (mShowAbilitiesPanel != null)
		{
			mShowAbilitiesPanel.setEnabled(aEnabled);
		}
		if (mShowVirtuesPanel != null)
		{
			mShowVirtuesPanel.setEnabled(aEnabled);
		}
	}
	
	@Override
	public void setPoints(final PointHandler aPoints)
	{
		super.setPoints(aPoints);
		for (final SimpleItemValueGroup group : mAttributesList)
		{
			group.setPoints(getPoints());
		}
		for (final SimpleItemValueGroup group : mAbilitiesList)
		{
			group.setPoints(getPoints());
		}
		mVirtues.setPoints(getPoints());
	}
	
	@Override
	protected void updateValues()
	{
		switch (getCreationMode())
		{
			case MAIN :
				updateCreation();
				break;
			case POINTS :
				updateFreePoints();
				break;
			case NORMAL :
				updateNormal();
				break;
		}
	}
	
	private boolean canIncreaseCreation(final SimpleItemValueGroup aGroup, final List<SimpleItemValueGroup> aGroups, final int[] aMaxValues)
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
	
	private void updateCreation()
	{
		int[] maxValues;
		
		if (mInitializedAttributes)
		{
			maxValues = getController().getAttributeCreationValues();
			for (final SimpleItemValueGroup group : mAttributesList)
			{
				group.updateValues(canIncreaseCreation(group, mAttributesList, maxValues), true);
			}
		}
		
		if (mInitializedAbilities)
		{
			maxValues = getController().getAbilityCreationValues();
			for (final SimpleItemValueGroup group : mAbilitiesList)
			{
				group.updateValues(canIncreaseCreation(group, mAbilitiesList, maxValues), true);
			}
		}
		
		if (mInitializedVirtues)
		{
			mVirtues.updateValues(mVirtues.getValue() < getController().getVirtueCreationValue(), true);
		}
	}
	
	private void updateFreePoints()
	{
		if (mInitializedAttributes)
		{
			for (final SimpleItemValueGroup group : mAttributesList)
			{
				group.updateValues(true, true);
			}
		}
		
		if (mInitializedAbilities)
		{
			for (final SimpleItemValueGroup group : mAbilitiesList)
			{
				group.updateValues(true, true);
			}
		}
		
		if (mInitializedVirtues)
		{
			mVirtues.updateValues(true, true);
		}
	}
	
	private void updateNormal()
	{
		if (mInitializedAttributes)
		{
			for (final SimpleItemValueGroup group : mAttributesList)
			{
				group.updateValues(true, false);
			}
		}
		
		if (mInitializedAbilities)
		{
			for (final SimpleItemValueGroup group : mAbilitiesList)
			{
				group.updateValues(true, false);
			}
		}
		
		if (mInitializedVirtues)
		{
			mVirtues.updateValues(true, false);
		}
	}
}
