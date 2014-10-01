package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class SimpleItemValueGroup implements ItemValueGroup<SimpleItem>
{
	private boolean										mCreation;
	
	private final SimpleItemGroup						mGroup;
	
	private final List<SimpleItemValue>					mValuesList	= new ArrayList<SimpleItemValue>();
	
	private final HashMap<SimpleItem, SimpleItemValue>	mValues		= new HashMap<SimpleItem, SimpleItemValue>();
	
	public SimpleItemValueGroup(final SimpleItemGroup aGroup, final boolean aCreation)
	{
		mCreation = aCreation;
		mGroup = aGroup;
		for (final SimpleItem item : mGroup.getItems())
		{
			addValue(item.createValue());
		}
	}
	
	private void addValue(final SimpleItemValue aValue)
	{
		mValuesList.add(aValue);
		mValues.put(aValue.getItem(), aValue);
	}
	
	@Override
	public SimpleItemGroup getGroup()
	{
		return mGroup;
	}
	
	@Override
	public List<SimpleItemValue> getValues()
	{
		return mValuesList;
	}
	
	@Override
	public SimpleItemValue getValue(final SimpleItem aItem)
	{
		return mValues.get(aItem);
	}
	
	@Override
	public SimpleItemValue getValue(final String aName)
	{
		return getValue(getGroup().getItem(aName));
	}
	
	@Override
	public void setCreation(final boolean aCreation)
	{
		mCreation = aCreation;
	}
	
	@Override
	public boolean isCreation()
	{
		return mCreation;
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{
		// TODO Add on click actions
		// TODO Set decrease and increase buttons enabled/disabled
		// TODO Calculate whether increase and decrease is possible
		
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
			
			final TextView title = new TextView(context);
			title.setLayoutParams(wrapAll);
			title.setText(mGroup.getName());
			title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			titleRow.addView(title);
		}
		table.addView(titleRow);
		
		for (final SimpleItemValue value : mValuesList)
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
						
					}
				});
				spinnerGrid.addView(increase);
				
				ViewUtil.applyValue(value.getValue(), valueDisplay);
			}
			valueRow.addView(spinnerGrid);
		}
	}
}
