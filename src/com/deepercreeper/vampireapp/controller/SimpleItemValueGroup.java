package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.controller.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A group of simple values.
 * 
 * @author Vincent
 */
public class SimpleItemValueGroup implements ItemValueGroup<SimpleItem>
{
	private boolean										mCreation;
	
	private final Context								mContext;
	
	private final UpdateAction							mAction;
	
	private final SimpleValueController					mController;
	
	private final SimpleItemGroup						mGroup;
	
	private final List<SimpleItemValue>					mValuesList	= new ArrayList<SimpleItemValue>();
	
	private final HashMap<SimpleItem, SimpleItemValue>	mValues		= new HashMap<SimpleItem, SimpleItemValue>();
	
	/**
	 * Creates a new item value group.
	 * 
	 * @param aGroup
	 *            The item group type.
	 * @param aController
	 *            The parent controller.
	 * @param aContext
	 *            The context.
	 * @param aCreation
	 *            Whether this value group is inside the creation mode.
	 */
	public SimpleItemValueGroup(final SimpleItemGroup aGroup, final SimpleValueController aController, final Context aContext, final boolean aCreation)
	{
		mController = aController;
		mCreation = aCreation;
		mGroup = aGroup;
		mContext = aContext;
		mAction = new UpdateAction()
		{
			@Override
			public void update()
			{
				mController.updateValues();
			}
		};
		for (final SimpleItem item : mGroup.getItems())
		{
			addValue(new SimpleItemValue(item, mContext, mAction));
		}
	}
	
	@Override
	public void release()
	{
		for (final SimpleItemValue value : mValuesList)
		{
			value.release();
		}
	}
	
	@Override
	public SimpleValueController getController()
	{
		return mController;
	}
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final SimpleItemValue itemValue : mValuesList)
		{
			value += itemValue.getValue();
		}
		return value;
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
	public List<SimpleItemValue> getValuesList()
	{
		return mValuesList;
	}
	
	@Override
	public SimpleItemValue getValue(final String aName)
	{
		return mValues.get(getGroup().getItem(aName));
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
	public void updateValues(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		for (final SimpleItemValue value : mValuesList)
		{
			value.setIncreasable(aCanIncrease && value.canIncrease(mCreation));
			value.setDecreasable(aCanDecrease && value.canDecrease(mCreation));
		}
	}
	
	@Override
	public void initLayout(final ViewGroup aLayout)
	{
		final Context context = aLayout.getContext();
		
		final TableRow titleRow = new TableRow(context);
		titleRow.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		{
			titleRow.addView(new Space(context));
			
			final TextView title = new TextView(context);
			title.setLayoutParams(ViewUtil.instance().getRowWrapAll());
			title.setText(mGroup.getName());
			title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			titleRow.addView(title);
		}
		aLayout.addView(titleRow);
		
		for (final SimpleItemValue value : mValuesList)
		{
			aLayout.addView(value.getContainer());
		}
	}
}
