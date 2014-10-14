package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class DisciplineItemValue implements ItemValue<DisciplineItem>
{
	private final DisciplineItem				mItem;
	
	private int									mValue;
	
	private final Context						mContext;
	
	private final LinearLayout					mContainer;
	
	private ImageButton							mIncreaseButton;
	
	private ImageButton							mDecreaseButton;
	
	private final UpdateAction					mAction;
	
	private final List<SubDisciplineItemValue>	mSubValues	= new ArrayList<SubDisciplineItemValue>();
	
	public DisciplineItemValue(final DisciplineItem aItem, final Context aContext, final UpdateAction aAction)
	{
		mItem = aItem;
		mContext = aContext;
		mAction = aAction;
		mContainer = new LinearLayout(mContext);
		mIncreaseButton = new ImageButton(mContext);
		mDecreaseButton = new ImageButton(mContext);
		mValue = mItem.getStartValue();
		init();
	}
	
	protected void init()
	{
		if (mItem.isParentItem())
		{
			initParentDiscipline();
		}
		else
		{
			initDiscipline();
		}
	}
	
	private void initParentDiscipline()
	{
		mContainer.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		
		final TableLayout table = new TableLayout(mContext);
		table.setLayoutParams(ViewUtil.instance().getWrapHeight());
		
		final LinearLayout parentRow = new LinearLayout(mContext);
		parentRow.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		{
			final TextView parentName = new TextView(mContext);
			parentName.setLayoutParams(ViewUtil.instance().getWrapAll());
			parentName.setText(getItem().getName() + ":");
			parentRow.addView(parentName);
		}
		table.addView(parentRow);
		
		for (int i = 0; i < DisciplineItem.MAX_SUB_DISCIPLINES; i++ )
		{
			table.addView(createSubDisciplineRow(i));
		}
		
		mContainer.addView(table);
	}
	
	protected void setIncreaseButton(final ImageButton aIncreaseButton)
	{
		mIncreaseButton = aIncreaseButton;
	}
	
	protected void setDecreaseButton(final ImageButton aDecreaseButton)
	{
		mDecreaseButton = aDecreaseButton;
	}
	
	private void initDiscipline()
	{
		mContainer.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		
		final TextView valueName = new TextView(mContext);
		valueName.setLayoutParams(ViewUtil.instance().getRowNameLong());
		valueName.setGravity(Gravity.CENTER_VERTICAL);
		valueName.setSingleLine();
		valueName.setEllipsize(TruncateAt.END);
		valueName.setText(getItem().getName());
		mContainer.addView(valueName);
		
		final GridLayout spinnerGrid = new GridLayout(mContext);
		spinnerGrid.setLayoutParams(ViewUtil.instance().getRowWrapAll());
		{
			final RadioButton[] valueDisplay = new RadioButton[getItem().getMaxValue()];
			
			mDecreaseButton.setLayoutParams(ViewUtil.instance().getButtonSize());
			mDecreaseButton.setContentDescription("Decrease");
			mDecreaseButton.setImageResource(android.R.drawable.ic_media_previous);
			mDecreaseButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					decrease();
					ViewUtil.applyValue(getValue(), valueDisplay);
					mAction.update();
				}
			});
			spinnerGrid.addView(mDecreaseButton);
			
			for (int i = 0; i < valueDisplay.length; i++ )
			{
				final RadioButton valuePoint = new RadioButton(mContext);
				valuePoint.setLayoutParams(ViewUtil.instance().getValueSize());
				valuePoint.setClickable(false);
				spinnerGrid.addView(valuePoint);
				valueDisplay[i] = valuePoint;
			}
			
			mIncreaseButton.setLayoutParams(ViewUtil.instance().getButtonSize());
			mIncreaseButton.setContentDescription("Increase");
			mIncreaseButton.setImageResource(android.R.drawable.ic_media_next);
			mIncreaseButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					increase();
					ViewUtil.applyValue(getValue(), valueDisplay);
					mAction.update();
				}
			});
			spinnerGrid.addView(mIncreaseButton);
			
			ViewUtil.applyValue(getValue(), valueDisplay);
		}
		mContainer.addView(spinnerGrid);
	}
	
	private TableRow createSubDisciplineRow(final int aValueIx)
	{
		final TableRow subRow = new TableRow(mContext);
		subRow.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		
		final GridLayout numberAndName = new GridLayout(mContext);
		numberAndName.setLayoutParams(ViewUtil.instance().getRowWrapAll());
		{
			final TextView number = new TextView(mContext);
			number.setLayoutParams(ViewUtil.instance().getNumberSize());
			number.setText((aValueIx + 1) + ".");
			number.setEllipsize(TruncateAt.END);
			number.setGravity(Gravity.CENTER_VERTICAL);
			number.setSingleLine();
			numberAndName.addView(number);
			
			final ImageButton edit = new ImageButton(mContext);
			edit.setLayoutParams(ViewUtil.instance().getButtonSize());
			edit.setContentDescription("Edit");
			edit.setImageResource(android.R.drawable.ic_menu_add);
			edit.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					if (SelectItemDialog.isDialogOpen())
					{
						return;
					}
					final List<SubDisciplineItem> items = new ArrayList<SubDisciplineItem>();
					items.addAll(getItem().getSubItems());
					for (final SubDisciplineItemValue value : getSubValues())
					{
						items.remove(value.getItem());
					}
					
					final SelectionListener<SubDisciplineItem> action = new SelectionListener<SubDisciplineItem>()
					{
						@Override
						public void select(final SubDisciplineItem aItem)
						{
							final SubDisciplineItemValue value = new SubDisciplineItemValue(aItem, mContext, mAction);
							setSubValue(aValueIx, value);
							value.initRow(subRow, aValueIx);
						}
					};
					
					new SelectItemDialog<SubDisciplineItem>(items, mContext.getResources().getString(R.string.edit_discipline), mContext, action);
				}
			});
			numberAndName.addView(edit);
		}
		subRow.addView(numberAndName);
		return subRow;
	}
	
	protected UpdateAction getAction()
	{
		return mAction;
	}
	
	public int getSubValueIndex(final SubDisciplineItemValue aSubValue)
	{
		for (int i = 0; i < mSubValues.size(); i++ )
		{
			if (aSubValue.equals(mSubValues.get(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	protected Context getContext()
	{
		return mContext;
	}
	
	@Override
	public void setDecreasable(final boolean aEnabled)
	{
		mDecreaseButton.setEnabled(aEnabled);
	}
	
	@Override
	public void setIncreasable(final boolean aEnabled)
	{
		mIncreaseButton.setEnabled(aEnabled);
	}
	
	@Override
	public boolean canIncrease(final boolean aCreation)
	{
		return canIncrease() && ( !aCreation || mValue < getItem().getMaxStartValue());
	}
	
	@Override
	public boolean canDecrease(final boolean aCreation)
	{
		return canDecrease();
	}
	
	@Override
	public boolean canIncrease()
	{
		return mValue < getItem().getMaxValue();
	}
	
	@Override
	public boolean canDecrease()
	{
		return mValue > getItem().getStartValue();
	}
	
	public SubDisciplineItemValue getSubValue(final int aPos)
	{
		if (aPos >= mSubValues.size())
		{
			return null;
		}
		return mSubValues.get(aPos);
	}
	
	public void setSubValue(final int aPos, final SubDisciplineItemValue aSubValue)
	{
		if (mSubValues.size() <= aPos)
		{
			mSubValues.add(aPos, aSubValue);
		}
		else
		{
			mSubValues.set(aPos, aSubValue);
		}
		aSubValue.setParent(this);
	}
	
	public boolean hasSubDiscipline(final int aPos)
	{
		return mSubValues.size() > aPos && mSubValues.get(aPos) != null;
	}
	
	@Override
	public DisciplineItem getItem()
	{
		return mItem;
	}
	
	public List<SubDisciplineItemValue> getSubValues()
	{
		return mSubValues;
	}
	
	@Override
	public int getValue()
	{
		return mValue;
	}
	
	@Override
	public void increase()
	{
		if (canIncrease())
		{
			mValue++ ;
		}
	}
	
	@Override
	public void decrease()
	{
		if (canDecrease())
		{
			mValue-- ;
		}
	}
	
	public static Comparator<? super DisciplineItemValue> getComparator()
	{
		return new Comparator<DisciplineItemValue>()
		{
			@Override
			public int compare(final DisciplineItemValue aLhs, final DisciplineItemValue aRhs)
			{
				return aLhs.getItem().compareTo(aRhs.getItem());
			}
		};
	}
}
