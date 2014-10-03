package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class BackgroundItemValueGroup implements ItemValueGroup<BackgroundItem>, VariableItemValueGroup<BackgroundItem, BackgroundItemValue>
{
	private boolean												mCreation;
	
	private final BackgroundValueController						mController;
	
	private final BackgroundItemGroup							mGroup;
	
	private final List<BackgroundItemValue>						mValuesList	= new ArrayList<BackgroundItemValue>();
	
	private final HashMap<BackgroundItem, BackgroundItemValue>	mValues		= new HashMap<BackgroundItem, BackgroundItemValue>();
	
	public BackgroundItemValueGroup(final BackgroundItemGroup aGroup, final BackgroundValueController aController, final boolean aCreation)
	{
		mController = aController;
		mGroup = aGroup;
		mCreation = aCreation;
	}
	
	@Override
	public BackgroundValueController getController()
	{
		return mController;
	}
	
	@Override
	public void addValue(final BackgroundItemValue aValue)
	{
		mValuesList.add(aValue);
		mValues.put(aValue.getItem(), aValue);
		Collections.sort(mValuesList, BackgroundItemValue.getComparator());
	}
	
	@Override
	public void addValue(final BackgroundItem aItem)
	{
		addValue(aItem.createValue());
	}
	
	@Override
	public void addValue(final String aName)
	{
		addValue(getGroup().getItem(aName));
	}
	
	@Override
	public void removeValue(final BackgroundItemValue aValue)
	{
		mValuesList.remove(aValue);
		mValues.remove(aValue.getItem());
	}
	
	@Override
	public void removeValue(final BackgroundItem aItem)
	{
		removeValue(aItem.createValue());
	}
	
	@Override
	public void removeValue(final String aName)
	{
		removeValue(getGroup().getItem(aName));
	}
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final BackgroundItemValue valueItem : mValuesList)
		{
			value += valueItem.getValue();
		}
		return value;
	}
	
	@Override
	public void updateValues(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		for (final BackgroundItemValue value : mValuesList)
		{
			value.getIncreaseButton().setEnabled(aCanIncrease && value.canIncrease(mCreation));
			value.getDecreaseButton().setEnabled(aCanDecrease && value.canDecrease(mCreation));
		}
	}
	
	@Override
	public ItemGroup<BackgroundItem> getGroup()
	{
		return mGroup;
	}
	
	@Override
	public List<BackgroundItemValue> getValuesList()
	{
		return mValuesList;
	}
	
	@Override
	public BackgroundItemValue getValue(final String aName)
	{
		return getValue(getGroup().getItem(aName));
	}
	
	@Override
	public BackgroundItemValue getValue(final BackgroundItem aItem)
	{
		return mValues.get(aItem);
	}
	
	@Override
	public boolean isCreation()
	{
		return mCreation;
	}
	
	@Override
	public void setCreation(final boolean aCreation)
	{
		mCreation = aCreation;
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{
		final Context context = aLayout.getContext();
		final TableLayout table = new TableLayout(context);
		
		final LayoutParams wrapHeight = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams wrapAll = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams buttonSize = new LayoutParams(ViewUtil.calcPx(30, context), ViewUtil.calcPx(30, context));
		final LayoutParams valueSize = new LayoutParams(ViewUtil.calcPx(25, context), LayoutParams.WRAP_CONTENT);
		
		table.setLayoutParams(wrapHeight);
		
		final TableRow titleRow = new TableRow(context);
		titleRow.setLayoutParams(wrapAll);
		{
			titleRow.addView(new Space(context));
			
			final Button title = new Button(context);
			title.setLayoutParams(wrapAll);
			title.setText(context.getResources().getString(R.string.add_background));
			title.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					// TODO Implement
				}
			});
			titleRow.addView(title);
		}
		table.addView(titleRow);
		
		for (final BackgroundItemValue value : mValuesList)
		{
			final TableRow valueRow = new TableRow(context);
			valueRow.setLayoutParams(wrapAll);
			
			final TextView valueName = new TextView(context);
			valueName.setLayoutParams(wrapAll);
			valueName.setText(value.getItem().getName());
			valueRow.addView(valueName);
			
			final GridLayout spinnerGrid = new GridLayout(context);
			spinnerGrid.setLayoutParams(wrapAll);
			{
				final ImageButton decrease = new ImageButton(context);
				final ImageButton increase = new ImageButton(context);
				final RadioButton[] valueDisplay = new RadioButton[value.getItem().getMaxValue()];
				
				decrease.setLayoutParams(buttonSize);
				decrease.setContentDescription("Decrease");
				decrease.setImageResource(android.R.drawable.ic_media_previous);
				decrease.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						value.decrease();
						ViewUtil.applyValue(value.getValue(), valueDisplay);
						mController.updateValues();
					}
				});
				spinnerGrid.addView(decrease);
				
				for (int i = 0; i < valueDisplay.length; i++ )
				{
					final RadioButton valuePoint = new RadioButton(context);
					valuePoint.setLayoutParams(valueSize);
					valuePoint.setClickable(false);
					spinnerGrid.addView(valuePoint);
					valueDisplay[i] = valuePoint;
				}
				
				increase.setLayoutParams(buttonSize);
				increase.setContentDescription("Increase");
				increase.setImageResource(android.R.drawable.ic_media_next);
				increase.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						value.increase();
						ViewUtil.applyValue(value.getValue(), valueDisplay);
						mController.updateValues();
					}
				});
				spinnerGrid.addView(increase);
				
				value.setIncreaseButton(increase);
				value.setDecreaseButton(decrease);
				
				ViewUtil.applyValue(value.getValue(), valueDisplay);
				mController.updateValues();
			}
			valueRow.addView(spinnerGrid);
			table.addView(valueRow);
		}
	}
}
