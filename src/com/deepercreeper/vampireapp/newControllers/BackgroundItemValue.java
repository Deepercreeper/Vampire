package com.deepercreeper.vampireapp.newControllers;

import java.util.Comparator;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class BackgroundItemValue implements ItemValue<BackgroundItem>
{
	private final BackgroundItem			mItem;
	
	private final Context					mContext;
	
	private final BackgroundItemValueGroup	mGroup;
	
	private final ImageButton				mIncreaseButton;
	
	private final ImageButton				mDecreaseButton;
	
	private TableRow						mContainer;
	
	private final UpdateAction				mAction;
	
	private int								mValue;
	
	public BackgroundItemValue(final BackgroundItem aItem, final Context aContext, final UpdateAction aAction, final BackgroundItemValueGroup aGroup)
	{
		mIncreaseButton = new ImageButton(aContext);
		mDecreaseButton = new ImageButton(aContext);
		mItem = aItem;
		mContext = aContext;
		mAction = aAction;
		mGroup = aGroup;
		mValue = mItem.getStartValue();
	}
	
	public void initRow(final TableRow aRow)
	{
		mContainer = aRow;
		
		final LayoutParams wrapAll = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams wrapAllTable = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams wrapRowAll = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams nameSize = new TableRow.LayoutParams(ViewUtil.calcPx(82, mContext), LayoutParams.WRAP_CONTENT);
		final LayoutParams buttonSize = new LayoutParams(ViewUtil.calcPx(30, mContext), ViewUtil.calcPx(30, mContext));
		final LayoutParams rowButtonSize = new TableRow.LayoutParams(ViewUtil.calcPx(30, mContext), ViewUtil.calcPx(30, mContext));
		final LayoutParams valueSize = new LayoutParams(ViewUtil.calcPx(25, mContext), LayoutParams.WRAP_CONTENT);
		
		mContainer.setLayoutParams(wrapAllTable);
		
		aRow.removeAllViews();
		
		final TextView valueName = new TextView(mContext);
		valueName.setLayoutParams(nameSize);
		valueName.setText(mItem.getName());
		mContainer.addView(valueName);
		
		final ImageButton edit = new ImageButton(mContext);
		edit.setLayoutParams(rowButtonSize);
		edit.setContentDescription("Edit");
		edit.setImageResource(android.R.drawable.ic_menu_edit);
		edit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mGroup.editValue(BackgroundItemValue.this);
			}
		});
		mContainer.addView(edit);
		
		final GridLayout spinnerGrid = new GridLayout(mContext);
		spinnerGrid.setLayoutParams(wrapRowAll);
		{
			final RadioButton[] valueDisplay = new RadioButton[mItem.getMaxValue()];
			
			mDecreaseButton.setLayoutParams(buttonSize);
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
				valuePoint.setLayoutParams(valueSize);
				valuePoint.setClickable(false);
				spinnerGrid.addView(valuePoint);
				valueDisplay[i] = valuePoint;
			}
			
			mIncreaseButton.setLayoutParams(buttonSize);
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
	
	@Override
	public TableRow getContainer()
	{
		return mContainer;
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
	public void increase()
	{
		if (canIncrease())
		{
			mValue++ ;
		}
	}
	
	@Override
	public boolean canDecrease()
	{
		return mValue > getItem().getStartValue();
	}
	
	@Override
	public void decrease()
	{
		if (canDecrease())
		{
			mValue-- ;
		}
	}
	
	@Override
	public int getValue()
	{
		return mValue;
	}
	
	@Override
	public BackgroundItem getItem()
	{
		return mItem;
	}
	
	public static Comparator<? super BackgroundItemValue> getComparator()
	{
		return new Comparator<BackgroundItemValue>()
		{
			@Override
			public int compare(final BackgroundItemValue aLhs, final BackgroundItemValue aRhs)
			{
				return aLhs.getItem().compareTo(aRhs.getItem());
			}
		};
	}
}
