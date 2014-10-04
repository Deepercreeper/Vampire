package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
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
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.newControllers.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class DisciplineItemValueGroup implements ItemValueGroup<DisciplineItem>, VariableItemValueGroup<DisciplineItem, DisciplineItemValue>
{
	private boolean												mCreation;
	
	private final DisciplineValueController						mController;
	
	private final DisciplineItemGroup							mGroup;
	
	private final List<DisciplineItemValue>						mValuesList	= new ArrayList<DisciplineItemValue>();
	
	private final HashMap<DisciplineItem, DisciplineItemValue>	mValues		= new HashMap<DisciplineItem, DisciplineItemValue>();
	
	public DisciplineItemValueGroup(final DisciplineItemGroup aGroup, final DisciplineValueController aController, final boolean aCreation)
	{
		mController = aController;
		mGroup = aGroup;
		mCreation = aCreation;
	}
	
	@Override
	public DisciplineValueController getController()
	{
		return mController;
	}
	
	@Override
	public DisciplineItemGroup getGroup()
	{
		return mGroup;
	}
	
	@Override
	public List<DisciplineItemValue> getValuesList()
	{
		return mValuesList;
	}
	
	@Override
	public DisciplineItemValue getValue(final String aName)
	{
		return getValue(getGroup().getItem(aName));
	}
	
	@Override
	public DisciplineItemValue getValue(final DisciplineItem aItem)
	{
		return mValues.get(aItem);
	}
	
	@Override
	public void updateValues(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		for (final DisciplineItemValue value : mValuesList)
		{
			if (value.getItem().isParentItem())
			{
				for (final DisciplineItemValue subValue : value.getSubValues())
				{
					if (subValue != null)
					{
						value.getIncreaseButton().setEnabled(aCanIncrease && value.canIncrease(mCreation));
						value.getDecreaseButton().setEnabled(aCanDecrease && value.canDecrease(mCreation));
					}
				}
			}
			else
			{
				value.getIncreaseButton().setEnabled(aCanIncrease && value.canIncrease(mCreation));
				value.getDecreaseButton().setEnabled(aCanDecrease && value.canDecrease(mCreation));
			}
		}
	}
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final DisciplineItemValue valueItem : mValuesList)
		{
			value += valueItem.getValue();
		}
		return value;
	}
	
	@Override
	public void addValue(final DisciplineItemValue aValue)
	{
		mValuesList.add(aValue);
		mValues.put(aValue.getItem(), aValue);
		Collections.sort(mValuesList, DisciplineItemValue.getComparator());
	}
	
	@Override
	public void addValue(final DisciplineItem aItem)
	{
		addValue(aItem.createValue());
	}
	
	@Override
	public void addValue(final String aName)
	{
		addValue(getGroup().getItem(aName));
	}
	
	@Override
	public void removeValue(final DisciplineItemValue aValue)
	{
		mValuesList.remove(aValue);
		mValues.remove(aValue.getItem());
	}
	
	@Override
	public void removeValue(final DisciplineItem aItem)
	{
		removeValue(aItem.createValue());
	}
	
	@Override
	public void removeValue(final String aName)
	{
		removeValue(getGroup().getItem(aName));
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
		
		for (final DisciplineItemValue value : mValuesList)
		{
			if (value.getItem().isParentItem())
			{
				addParentDisciplineRows(value, table, context);
			}
			
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
	
	private void addParentDisciplineRows(final DisciplineItemValue aParent, final TableLayout aTable, final Context aContext)
	{
		final LayoutParams wrapAll = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		final TableRow parentRow = new TableRow(aContext);
		parentRow.setLayoutParams(wrapAll);
		{
			parentRow.addView(new Space(aContext));
			
			final TextView parentName = new TextView(aContext);
			parentName.setLayoutParams(wrapAll);
			parentName.setText(aParent.getItem().getName() + ":");
			parentRow.addView(parentName);
		}
		aTable.addView(parentRow);
		
		for (int i = 0; i < DisciplineItem.MAX_SUB_DISCIPLINES; i++ )
		{
			final TableRow subRow = new TableRow(aContext);
			subRow.setLayoutParams(wrapAll);
			
			createSubRow(subRow, aContext, aParent.getSubValue(i), aParent, i);
			
			aTable.addView(subRow);
		}
	}
	
	private void createSubRow(final TableRow aRow, final Context aContext, final DisciplineItemValue aValue, final DisciplineItemValue aParent,
			final int aValueIx)
	{
		aRow.removeAllViews();
		
		final LayoutParams wrapAll = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams buttonSize = new LayoutParams(ViewUtil.calcPx(30, aContext), ViewUtil.calcPx(30, aContext));
		final LayoutParams valueSize = new LayoutParams(ViewUtil.calcPx(25, aContext), LayoutParams.WRAP_CONTENT);
		final LayoutParams nameSize = new LayoutParams(ViewUtil.calcPx(80, aContext), LayoutParams.WRAP_CONTENT);
		
		final TextView number = new TextView(aContext);
		number.setLayoutParams(wrapAll);
		number.setText(aValueIx + ".");
		aRow.addView(number);
		
		final ImageButton edit = new ImageButton(aContext);
		edit.setLayoutParams(buttonSize);
		edit.setContentDescription("Edit");
		edit.setImageResource(android.R.drawable.ic_menu_add);
		edit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				final List<DisciplineItem> items = new ArrayList<DisciplineItem>();
				items.addAll(aParent.getItem().getSubItems());
				for (final DisciplineItemValue value : aParent.getSubValues())
				{
					items.remove(value.getItem());
				}
				
				final SelectionListener<DisciplineItem> action = new SelectionListener<DisciplineItem>()
				{
					@Override
					public void select(final DisciplineItem aItem)
					{
						final DisciplineItemValue value = aItem.createValue();
						addValue(value);
						createSubRow(aRow, aContext, value, aParent, aValueIx);
					}
				};
				
				new SelectItemDialog<DisciplineItem>(items, aContext.getResources().getString(R.string.add_background), aContext, action);
			}
		});
		aRow.addView(edit);
		
		if (aValue != null)
		{
			edit.setImageResource(android.R.drawable.ic_menu_edit);
			
			final TextView name = new TextView(aContext);
			name.setLayoutParams(nameSize);
			name.setText(aValue.getItem().getName());
			aRow.addView(name);
			
			final GridLayout spinnerGrid = new GridLayout(aContext);
			spinnerGrid.setLayoutParams(wrapAll);
			{
				final ImageButton decrease = new ImageButton(aContext);
				final ImageButton increase = new ImageButton(aContext);
				final RadioButton[] valueDisplay = new RadioButton[aValue.getItem().getMaxValue()];
				
				decrease.setLayoutParams(buttonSize);
				decrease.setContentDescription("Decrease");
				decrease.setImageResource(android.R.drawable.ic_media_previous);
				decrease.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						aValue.decrease();
						ViewUtil.applyValue(aValue.getValue(), valueDisplay);
						mController.updateValues();
					}
				});
				spinnerGrid.addView(decrease);
				
				for (int j = 0; j < valueDisplay.length; j++ )
				{
					final RadioButton valuePoint = new RadioButton(aContext);
					valuePoint.setLayoutParams(valueSize);
					valuePoint.setClickable(false);
					spinnerGrid.addView(valuePoint);
					valueDisplay[j] = valuePoint;
				}
				
				increase.setLayoutParams(buttonSize);
				increase.setContentDescription("Increase");
				increase.setImageResource(android.R.drawable.ic_media_next);
				increase.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						aValue.increase();
						ViewUtil.applyValue(aValue.getValue(), valueDisplay);
						mController.updateValues();
					}
				});
				spinnerGrid.addView(increase);
				
				aValue.setIncreaseButton(increase);
				aValue.setDecreaseButton(decrease);
				
				ViewUtil.applyValue(aValue.getValue(), valueDisplay);
				mController.updateValues();
			}
			aRow.addView(spinnerGrid);
		}
	}
}
