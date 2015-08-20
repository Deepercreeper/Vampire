package com.deepercreeper.vampireapp.character.creation.controllers;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This controller controls the health system of a character in creation mode.<br>
 * It provides adding, removing and changing health steps.
 * 
 * @author vrl
 */
public class HealthControllerCreation implements Viewable
{
	private class Step
	{
		private final LinearLayout mStepContainer;
		
		private int mValue;
		
		public Step(final int aValue)
		{
			mValue = aValue;
			mStepContainer = (LinearLayout) View.inflate(mContext, R.layout.view_step, null);
			final TextView value = ((TextView) mStepContainer.findViewById(R.id.view_step_value_label));
			final ImageButton remove = ((ImageButton) mStepContainer.findViewById(R.id.view_remove_step_button));
			final ImageButton increase = ((ImageButton) mStepContainer.findViewById(R.id.view_increase_step_button));
			final ImageButton decrease = ((ImageButton) mStepContainer.findViewById(R.id.view_decrease_step_button));
			if (mValue == -1)
			{
				value.setText(R.string.ko);
				ViewUtil.setEnabled(remove, false);
				ViewUtil.setEnabled(increase, false);
				ViewUtil.setEnabled(decrease, false);
			}
			else
			{
				value.setText("" + mValue);
				remove.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						release();
						removeStep(Step.this);
					}
				});
				increase.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						mValue++ ;
						value.setText("" + mValue);
						ViewUtil.setEnabled(decrease, mValue > 0);
					}
				});
				decrease.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						if (mValue > 0)
						{
							mValue-- ;
							value.setText("" + mValue);
						}
						ViewUtil.setEnabled(decrease, mValue > 0);
					}
				});
			}
			ViewUtil.setEnabled(decrease, mValue > 0);
		}
		
		public LinearLayout getContainer()
		{
			return mStepContainer;
		}
		
		public int getValue()
		{
			return mValue;
		}
		
		public void release()
		{
			ViewUtil.release(mStepContainer);
		}
	}
	
	private final List<Step> mSteps = new ArrayList<Step>();
	
	private final LinearLayout mContainer;
	
	private final Context mContext;
	
	private final String mCost;
	
	/**
	 * Creates a new health controller creation.
	 * 
	 * @param aContext
	 *            The underlying context.
	 * @param aItems
	 *            The item provider.
	 */
	public HealthControllerCreation(final Context aContext, final ItemProvider aItems)
	{
		mContext = aContext;
		mCost = aItems.getHealth().getCost();
		
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_health_creation, null);
		
		for (final int health : aItems.getHealth().getSteps())
		{
			final Step step = new Step(health);
			getContainer().addView(step.getContainer());
			mSteps.add(step);
		}
		
		final Button addButton = (Button) getContainer().findViewById(R.id.view_add_health_step_button);
		addButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				addStep();
			}
		});
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the name of the item, which is decreased, when the character tries to heal a health step.
	 */
	public String getCost()
	{
		return mCost;
	}
	
	/**
	 * @return an array of health steps. each step describes, how many negative points the state contains.
	 */
	public int[] getSteps()
	{
		final int[] steps = new int[mSteps.size()];
		for (int i = 0; i < steps.length; i++ )
		{
			steps[i] = mSteps.get(i).getValue();
		}
		return steps;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void updateUI()
	{}
	
	private void addStep()
	{
		mSteps.add(0, new Step(0));
		getContainer().addView(mSteps.get(0).getContainer(), 1);
	}
	
	private void removeStep(final Step aStep)
	{
		mSteps.remove(aStep);
	}
}
