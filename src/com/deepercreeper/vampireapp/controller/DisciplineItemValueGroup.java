package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import com.deepercreeper.vampireapp.ResizeAnimation;
import com.deepercreeper.vampireapp.controller.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class DisciplineItemValueGroup implements ItemValueGroup<DisciplineItem>, VariableValueGroup<DisciplineItem, DisciplineItemValue>
{
	private boolean												mCreation;
	
	private final Context										mContext;
	
	private LinearLayout										mDisciplinesPanel;
	
	private TableLayout											mDisciplinesTable;
	
	private final DisciplineValueController						mController;
	
	private final DisciplineItemGroup							mGroup;
	
	private final UpdateAction									mAction;
	
	private final List<DisciplineItemValue>						mValuesList	= new ArrayList<DisciplineItemValue>();
	
	private final HashMap<DisciplineItem, DisciplineItemValue>	mValues		= new HashMap<DisciplineItem, DisciplineItemValue>();
	
	public DisciplineItemValueGroup(final DisciplineItemGroup aGroup, final DisciplineValueController aController, final Context aContext,
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
				for (final SubDisciplineItemValue subValue : value.getSubValues())
				{
					if (subValue != null)
					{
						subValue.setIncreasable(aCanIncrease && subValue.canIncrease(mCreation));
						subValue.setDecreasable(aCanDecrease && subValue.canDecrease(mCreation));
					}
				}
			}
			else
			{
				value.setIncreasable(aCanIncrease && value.canIncrease(mCreation));
				value.setDecreasable(aCanDecrease && value.canDecrease(mCreation));
			}
		}
	}
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final DisciplineItemValue valueItem : mValuesList)
		{
			if (valueItem.getItem().isParentItem())
			{
				for (final SubDisciplineItemValue subValue : valueItem.getSubValues())
				{
					value += subValue.getValue();
				}
			}
			else
			{
				value += valueItem.getValue();
			}
		}
		return value;
	}
	
	private void addValue(final DisciplineItemValue aValue)
	{
		mValuesList.add(aValue);
		mValues.put(aValue.getItem(), aValue);
		if (mDisciplinesTable != null)
		{
			mDisciplinesTable.addView(aValue.getContainer());
		}
	}
	
	@Override
	public void addItem(final DisciplineItem aItem)
	{
		addValue(new DisciplineItemValue(aItem, mContext, mAction));
	}
	
	@Override
	public void resize()
	{
		if (mDisciplinesPanel != null)
		{
			mDisciplinesPanel.startAnimation(new ResizeAnimation(mDisciplinesPanel, mDisciplinesPanel.getWidth(), ViewUtil
					.calcHeight(mDisciplinesPanel)));
		}
	}
	
	@Override
	public void clear()
	{
		mValuesList.clear();
		mValues.clear();
		if (mDisciplinesTable != null)
		{
			mDisciplinesTable.removeAllViews();
		}
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
		mDisciplinesPanel = (LinearLayout) aLayout;
		final Context context = aLayout.getContext();
		mDisciplinesTable = new TableLayout(context);
		
		mDisciplinesTable.setLayoutParams(ViewUtil.instance().getWrapHeight());
		
		for (final DisciplineItemValue value : mValuesList)
		{
			mDisciplinesTable.addView(value.getContainer());
		}
		aLayout.addView(mDisciplinesTable);
		mController.updateValues();
	}
}
