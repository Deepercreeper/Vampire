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

public class BackgroundItemValueGroup implements ItemValueGroup<BackgroundItem>, VariableValueGroup<BackgroundItem, BackgroundItemValue>
{
	private boolean												mCreation;
	
	private final Context										mContext;
	
	private LinearLayout										mBackgroundsPanel;
	
	private TableLayout											mBackgroundsTable;
	
	private final BackgroundValueController						mController;
	
	private final BackgroundItemGroup							mGroup;
	
	private final List<BackgroundItemValue>						mValuesList	= new ArrayList<BackgroundItemValue>();
	
	private final HashMap<BackgroundItem, BackgroundItemValue>	mValues		= new HashMap<BackgroundItem, BackgroundItemValue>();
	
	private final UpdateAction									mAction;
	
	public BackgroundItemValueGroup(final BackgroundItemGroup aGroup, final BackgroundValueController aController, final Context aContext,
			final boolean aCreation)
	{
		mController = aController;
		mGroup = aGroup;
		mContext = aContext;
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
	public BackgroundValueController getController()
	{
		return mController;
	}
	
	private void addValue(final BackgroundItemValue aValue)
	{
		mValuesList.add(aValue);
		mValues.put(aValue.getItem(), aValue);
		if (mBackgroundsTable != null)
		{
			mBackgroundsTable.addView(aValue.getContainer());
		}
	}
	
	@Override
	public void addItem(final BackgroundItem aItem)
	{
		addValue(new BackgroundItemValue(aItem, mContext, mAction));
	}
	
	@Override
	public void resize()
	{
		mBackgroundsPanel
				.startAnimation(new ResizeAnimation(mBackgroundsPanel, mBackgroundsPanel.getWidth(), ViewUtil.calcHeight(mBackgroundsPanel)));
	}
	
	@Override
	public void clear()
	{
		mValuesList.clear();
		mValues.clear();
	}
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final BackgroundItemValue valueItem : mValuesList)
		{
			value += valueItem.getValue();
		}
		return value;
	}
	
	@Override
	public void updateValues(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		for (final BackgroundItemValue value : mValuesList)
		{
			value.setIncreasable(aCanIncrease && value.canIncrease(mCreation));
			value.setDecreasable(aCanDecrease && value.canDecrease(mCreation));
		}
	}
	
	@Override
	public ItemGroup<BackgroundItem> getGroup()
	{
		return mGroup;
	}
	
	@Override
	public List<BackgroundItemValue> getValuesList()
	{
		return mValuesList;
	}
	
	@Override
	public BackgroundItemValue getValue(final String aName)
	{
		return getValue(getGroup().getItem(aName));
	}
	
	@Override
	public BackgroundItemValue getValue(final BackgroundItem aItem)
	{
		return mValues.get(aItem);
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
		mBackgroundsPanel = (LinearLayout) aLayout;
		mBackgroundsTable = new TableLayout(mContext);
		
		final LayoutParams wrapHeight = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams wrapAll = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		mBackgroundsTable.setLayoutParams(wrapHeight);
		
		final TableRow titleRow = new TableRow(mContext);
		titleRow.setLayoutParams(wrapAll);
		{
			titleRow.addView(new Space(mContext));
			
			final Button addBackground = new Button(mContext);
			addBackground.setLayoutParams(wrapAll);
			addBackground.setText(mContext.getResources().getString(R.string.add_background));
			addBackground.setEnabled(mValuesList.size() < BackgroundItem.MAX_BACKGROUNDS);
			addBackground.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					final List<BackgroundItem> items = new ArrayList<BackgroundItem>();
					items.addAll(mGroup.getItems());
					for (final BackgroundItemValue value : mValuesList)
					{
						items.remove(value.getItem());
					}
					
					final SelectionListener<BackgroundItem> action = new SelectionListener<BackgroundItem>()
					{
						@Override
						public void select(final BackgroundItem aItem)
						{
							final BackgroundItemValue value = new BackgroundItemValue(aItem, mContext, mAction);
							addValue(value);
							addBackground.setEnabled(mValuesList.size() < BackgroundItem.MAX_BACKGROUNDS);
							mBackgroundsTable.addView(value.getContainer());
						}
					};
					
					new SelectItemDialog<BackgroundItem>(items, mContext.getResources().getString(R.string.add_background), mContext, action);
				}
			});
			titleRow.addView(addBackground);
		}
		mBackgroundsTable.addView(titleRow);
		
		for (final BackgroundItemValue value : mValuesList)
		{
			mBackgroundsTable.addView(value.getContainer());
		}
		aLayout.addView(mBackgroundsTable);
		mController.updateValues();
	}
}
