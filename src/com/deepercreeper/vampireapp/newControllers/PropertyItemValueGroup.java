package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.widget.LinearLayout;

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
		// TODO Implement
	}
}
