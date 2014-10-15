package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.ResizeAnimation;
import com.deepercreeper.vampireapp.controller.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A group of property values.
 * 
 * @author Vincent
 */
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
	
	/**
	 * Creates a new property value group.
	 * 
	 * @param aGroup
	 *            The item group type.
	 * @param aController
	 *            The parent controller.
	 * @param aContext
	 *            The context.
	 * @param aCreation
	 *            Whether this group is inside creation mode.
	 */
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
		mAction.update();
	}
	
	@Override
	public void addItem(final PropertyItem aItem)
	{
		addValue(new PropertyItemValue(aItem, mContext, mAction));
		resize();
	}
	
	@Override
	public void resize()
	{
		if (mPropertiesPanel != null)
		{
			mPropertiesPanel
					.startAnimation(new ResizeAnimation(mPropertiesPanel, mPropertiesPanel.getWidth(), ViewUtil.calcHeight(mPropertiesPanel)));
		}
	}
	
	@Override
	public void clear()
	{
		mValuesList.clear();
		mValues.clear();
	}
	
	@Override
	public PropertyItemValue getValue(final String aName)
	{
		return mValues.get(getGroup().getItem(aName));
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
		
		mPropertiesTable.setLayoutParams(ViewUtil.instance().getWrapHeight());
		
		final LinearLayout titleRow = new LinearLayout(mContext);
		titleRow.setLayoutParams(ViewUtil.instance().getWrapHeight());
		{
			final Button addProperty = new Button(mContext);
			addProperty.setLayoutParams(ViewUtil.instance().getWrapHeight());
			addProperty.setTextSize(14);
			addProperty.setText(mContext.getResources().getString(R.string.add_property));
			addProperty.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					if (SelectItemDialog.isDialogOpen())
					{
						return;
					}
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
							addItem(aItem);
						}
					};
					
					SelectItemDialog.<PropertyItem> showSelectionDialog(items, mContext.getResources().getString(R.string.add_property), mContext,
							action);
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
