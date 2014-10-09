package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.ResizeAnimation;
import com.deepercreeper.vampireapp.newControllers.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.newControllers.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class PropertyItemValueGroup implements ItemValueGroup<PropertyItem>, VariableValueGroup<PropertyItem, PropertyItemValue>
{
	private boolean											mCreation;
	
	private final Context									mContext;
	
	private LinearLayout									mPropertiesPanel;
	
	private TableLayout										mPropertiesTable;
	
	private final UpdateAction								mAction;
	
	private final PropertyValueController					mController;
	
	private final PropertyItemGroup							mGroup;
	
	private final List<PropertyItemValue>					mValuesList	= new ArrayList<PropertyItemValue>();
	
	private final HashMap<PropertyItem, PropertyItemValue>	mValues		= new HashMap<PropertyItem, PropertyItemValue>();
	
	public PropertyItemValueGroup(final PropertyItemGroup aGroup, final PropertyValueController aController, final Context aContext,
			final boolean aCreation)
	{
		mController = aController;
		mContext = aContext;
		mGroup = aGroup;
		mCreation = aCreation;
		mAction = new UpdateAction()
		{
			@Override
			public void update()
			{
				mController.updateValues();
			}
		};
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
			valueItem.setIncreasable(canIncrease);
			valueItem.setDecreasable(canDecrease);
		}
	}
	
	private void addValue(final PropertyItemValue aValue)
	{
		mValuesList.add(aValue);
		mValues.put(aValue.getItem(), aValue);
		if (mPropertiesTable != null)
		{
			mPropertiesTable.addView(aValue.getContainer());
		}
	}
	
	@Override
	public void addItem(final PropertyItem aItem)
	{
		addValue(new PropertyItemValue(aItem, mContext, mAction));
	}
	
	@Override
	public void resize()
	{
		mPropertiesPanel.startAnimation(new ResizeAnimation(mPropertiesPanel, mPropertiesPanel.getWidth(), ViewUtil.calcHeight(mPropertiesPanel)));
	}
	
	@Override
	public void clear()
	{
		mValuesList.clear();
		mValues.clear();
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
	public void initLayout(final ViewGroup aLayout)
	{
		mPropertiesPanel = (LinearLayout) aLayout;
		mPropertiesTable = new TableLayout(mContext);
		
		final LayoutParams wrapHeight = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams wrapAll = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		mPropertiesTable.setLayoutParams(wrapHeight);
		
		final TableRow titleRow = new TableRow(mContext);
		titleRow.setLayoutParams(wrapAll);
		{
			titleRow.addView(new Space(mContext));
			
			final Button addProperty = new Button(mContext);
			addProperty.setLayoutParams(wrapAll);
			addProperty.setText(mContext.getResources().getString(R.string.add_property));
			addProperty.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					final List<PropertyItem> items = new ArrayList<PropertyItem>();
					items.addAll(mGroup.getItems());
					for (final PropertyItemValue value : mValuesList)
					{
						items.remove(value.getItem());
					}
					
					final SelectionListener<PropertyItem> action = new SelectionListener<PropertyItem>()
					{
						@Override
						public void select(final PropertyItem aItem)
						{
							final PropertyItemValue value = new PropertyItemValue(aItem, mContext, mAction);
							addValue(value);
							mPropertiesTable.addView(value.getContainer());
						}
					};
					
					new SelectItemDialog<PropertyItem>(items, mContext.getResources().getString(R.string.add_property), mContext, action);
				}
			});
			titleRow.addView(addProperty);
		}
		mPropertiesTable.addView(titleRow);
		
		for (final PropertyItemValue value : mValuesList)
		{
			mPropertiesTable.addView(value.getContainer());
		}
		aLayout.addView(mPropertiesTable);
		mController.updateValues();
	}
}
