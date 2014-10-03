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

public class PropertyItemValueGroup implements ItemValueGroup<PropertyItem>, VariableItemValueGroup<PropertyItem, PropertyItemValue>
{
	private boolean											mCreation;
	
	private final PropertyValueController					mController;
	
	private final PropertyItemGroup							mGroup;
	
	private final List<PropertyItemValue>					mValuesList	= new ArrayList<PropertyItemValue>();
	
	private final HashMap<PropertyItem, PropertyItemValue>	mValues		= new HashMap<PropertyItem, PropertyItemValue>();
	
	public PropertyItemValueGroup(final PropertyItemGroup aGroup, final PropertyValueController aController, final boolean aCreation)
	{
		mController = aController;
		mGroup = aGroup;
		mCreation = aCreation;
	}
	
	@Override
	public PropertyValueController getController()
	{
		return mController;
	}
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final PropertyItemValue valueItem : mValuesList)
		{
			value += valueItem.getFinalValue();
		}
		return value;
	}
	
	@Override
	public void updateValues(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		final int value = getValue();
		for (final PropertyItemValue valueItem : mValuesList)
		{
			boolean canIncrease = aCanIncrease && valueItem.canIncrease(mCreation);
			boolean canDecrease = aCanDecrease && valueItem.canDecrease(mCreation);
			if (canIncrease)
			{
				final int increasedValue = value - valueItem.getFinalValue() + valueItem.getItem().getFinalValue(valueItem.getValueId() + 1);
				canIncrease = increasedValue <= 0;
			}
			if (canDecrease)
			{
				final int decreasedValue = value - valueItem.getFinalValue() + valueItem.getItem().getFinalValue(valueItem.getValueId() - 1);
				canDecrease = decreasedValue <= 0;
			}
			valueItem.getIncreaseButton().setEnabled(canIncrease);
			valueItem.getDecreaseButton().setEnabled(canDecrease);
		}
	}
	
	@Override
	public void addValue(final PropertyItemValue aValue)
	{
		mValuesList.add(aValue);
		mValues.put(aValue.getItem(), aValue);
		Collections.sort(mValuesList, PropertyItemValue.getComparator());
	}
	
	@Override
	public void addValue(final PropertyItem aItem)
	{
		addValue(aItem.createValue());
	}
	
	@Override
	public void addValue(final String aName)
	{
		addValue(getGroup().getItem(aName));
	}
	
	@Override
	public void removeValue(final PropertyItemValue aValue)
	{
		mValuesList.remove(aValue);
		mValues.remove(aValue.getItem());
	}
	
	@Override
	public void removeValue(final PropertyItem aItem)
	{
		removeValue(aItem.createValue());
	}
	
	@Override
	public void removeValue(final String aName)
	{
		removeValue(getGroup().getItem(aName));
	}
	
	@Override
	public PropertyItemValue getValue(final PropertyItem aItem)
	{
		return mValues.get(aItem);
	}
	
	@Override
	public PropertyItemValue getValue(final String aName)
	{
		return getValue(getGroup().getItem(aName));
	}
	
	@Override
	public List<PropertyItemValue> getValuesList()
	{
		return mValuesList;
	}
	
	@Override
	public PropertyItemGroup getGroup()
	{
		return mGroup;
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
			title.setText(context.getResources().getString(R.string.add_property));
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
		
		for (final PropertyItemValue value : mValuesList)
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
