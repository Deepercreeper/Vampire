package com.deepercreeper.vampireapp.controller.properties;

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
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.SelectItemDialog;
import com.deepercreeper.vampireapp.controller.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.controller.implementations.VariableValueGroupImpl;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A group of property values.
 * 
 * @author Vincent
 */
public class PropertyItemValueGroup extends VariableValueGroupImpl<PropertyItem, PropertyItemValue>
{
	private LinearLayout	mPropertiesPanel;
	
	private TableLayout		mPropertiesTable;
	
	/**
	 * Creates a new property value group.
	 * 
	 * @param aGroup
	 *            The item group type.
	 * @param aController
	 *            The parent controller.
	 * @param aContext
	 *            The context.
	 * @param aMode
	 *            Whether this group is inside creation mode.
	 */
	public PropertyItemValueGroup(final PropertyItemGroup aGroup, final PropertyValueController aController, final Context aContext,
			final CharMode aMode)
	{
		super(aGroup, aController, aContext, aMode, null);
	}
	
	@Override
	public void setPoints(final PointHandler aPoints)
	{
		return;
	}
	
	@Override
	public void resetTempPoints()
	{
		return;
	}
	
	@Override
	public List<PropertyItemValue> getValuesList()
	{
		return super.getValuesList();
	}
	
	@Override
	public HashMap<PropertyItem, PropertyItemValue> getValues()
	{
		return super.getValues();
	}
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final PropertyItemValue valueItem : getValuesList())
		{
			value += valueItem.getFinalValue();
		}
		return value;
	}
	
	@Override
	public int getTempPoints()
	{
		return 0;
	}
	
	@Override
	public void updateValues(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		final int value = getValue();
		for (final PropertyItemValue valueItem : getValuesList())
		{
			boolean canIncrease = aCanIncrease && valueItem.canIncrease(getCreationMode());
			boolean canDecrease = aCanDecrease && valueItem.canDecrease(getCreationMode());
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
		getValuesList().add(aValue);
		getValues().put(aValue.getItem(), aValue);
		if (mPropertiesTable != null)
		{
			mPropertiesTable.addView(aValue.getContainer());
		}
		getUpdateAction().update();
	}
	
	@Override
	public void addItem(final PropertyItem aItem)
	{
		addValue(new PropertyItemValue(aItem, getContext(), getUpdateAction(), this, getCreationMode()));
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
	public void initLayout(final ViewGroup aLayout)
	{
		mPropertiesPanel = (LinearLayout) aLayout;
		mPropertiesTable = new TableLayout(getContext());
		
		mPropertiesTable.setLayoutParams(ViewUtil.instance().getWrapHeight());
		
		final LinearLayout titleRow = new LinearLayout(getContext());
		titleRow.setLayoutParams(ViewUtil.instance().getWrapHeight());
		{
			final Button addProperty = new Button(getContext());
			addProperty.setLayoutParams(ViewUtil.instance().getWrapHeight());
			addProperty.setTextSize(14);
			addProperty.setText(getContext().getResources().getString(R.string.add_property));
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
					items.addAll(getGroup().getItems());
					for (final PropertyItemValue value : getValuesList())
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
					
					SelectItemDialog.<PropertyItem> showSelectionDialog(items, getContext().getResources().getString(R.string.add_property),
							getContext(), action);
				}
			});
			titleRow.addView(addProperty);
		}
		mPropertiesTable.addView(titleRow);
		
		for (final PropertyItemValue value : getValuesList())
		{
			mPropertiesTable.addView(value.getContainer());
			value.refreshValue();
		}
		aLayout.addView(mPropertiesTable);
		getController().updateValues(false);
	}
}
