package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.widget.LinearLayout;

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
		// TODO Implement
	}
}
