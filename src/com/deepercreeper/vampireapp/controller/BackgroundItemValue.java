package com.deepercreeper.vampireapp.controller;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * This is the value of the background item.
 * 
 * @author Vincent
 */
public class BackgroundItemValue implements ItemValue<BackgroundItem>
{
	private CreationMode					mMode;
	
	private final BackgroundItem			mItem;
	
	private final Context					mContext;
	
	private final BackgroundItemValueGroup	mGroup;
	
	private final ImageButton				mIncreaseButton;
	
	private final ImageButton				mDecreaseButton;
	
	private RadioButton[]					mValueDisplay;
	
	private TableRow						mContainer;
	
	private final UpdateAction				mAction;
	
	private int								mValue;
	
	private int								mTempPoints;
	
	/**
	 * Creates a new background value.
	 * 
	 * @param aItem
	 *            The background item type.
	 * @param aContext
	 *            The context.
	 * @param aAction
	 *            The update action.
	 * @param aGroup
	 *            The parent value group.
	 * @param aMode
	 *            The current creation mode.
	 */
	public BackgroundItemValue(final BackgroundItem aItem, final Context aContext, final UpdateAction aAction, final BackgroundItemValueGroup aGroup,
			final CreationMode aMode)
	{
		mMode = aMode;
		mIncreaseButton = new ImageButton(aContext);
		mDecreaseButton = new ImageButton(aContext);
		mItem = aItem;
		mContext = aContext;
		mAction = aAction;
		mGroup = aGroup;
		mValue = mItem.getStartValue();
	}
	
	@Override
	public int getTempPoints()
	{
		return mTempPoints;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(mIncreaseButton, false);
		ViewUtil.release(mDecreaseButton, false);
		ViewUtil.release(mContainer, true);
	}
	
	@Override
	public void setCreationMode(final CreationMode aMode)
	{
		mMode = aMode;
	}
	
	@Override
	public CreationMode getCreationMode()
	{
		return mMode;
	}
	
	/**
	 * Initializes a table row so that all needed widgets are added to handle this value.
	 * 
	 * @param aRow
	 *            The row to initialize into.
	 */
	public void initRow(final TableRow aRow)
	{
		mContainer = aRow;
		
		mContainer.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		
		aRow.removeAllViews();
		
		final TextView valueName = new TextView(mContext);
		valueName.setLayoutParams(ViewUtil.instance().getRowNameShort());
		valueName.setGravity(Gravity.CENTER_VERTICAL);
		valueName.setEllipsize(TruncateAt.END);
		valueName.setSingleLine();
		valueName.setText(mItem.getName());
		mContainer.addView(valueName);
		
		if (mMode == CreationMode.CREATION)
		{
			final ImageButton edit = new ImageButton(mContext);
			edit.setLayoutParams(ViewUtil.instance().getRowButtonSize());
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
		}
		
		final GridLayout spinnerGrid = new GridLayout(mContext);
		spinnerGrid.setLayoutParams(ViewUtil.instance().getRowWrapAll());
		{
			mValueDisplay = new RadioButton[mItem.getMaxValue()];
			
			mDecreaseButton.setLayoutParams(ViewUtil.instance().getButtonSize());
			mDecreaseButton.setContentDescription("Decrease");
			mDecreaseButton.setImageResource(android.R.drawable.ic_media_previous);
			mDecreaseButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					decrease();
					refreshValue();
					mAction.update();
				}
			});
			spinnerGrid.addView(mDecreaseButton);
			
			for (int i = 0; i < mValueDisplay.length; i++ )
			{
				final RadioButton valuePoint = new RadioButton(mContext);
				valuePoint.setLayoutParams(ViewUtil.instance().getValueSize());
				valuePoint.setClickable(false);
				spinnerGrid.addView(valuePoint);
				mValueDisplay[i] = valuePoint;
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
					refreshValue();
					mAction.update();
				}
			});
			spinnerGrid.addView(mIncreaseButton);
			
			refreshValue();
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
	public boolean canIncrease(final CreationMode aMode)
	{
		switch (aMode)
		{
			case CREATION :
				return canIncrease() && mValue < getItem().getMaxStartValue();
			case FREE_POINTS :
				return canIncrease() && mValue + mTempPoints < getItem().getMaxStartValue();
			case NORMAL :
				return canIncrease();
		}
		return false;
	}
	
	@Override
	public void resetTempPoints()
	{
		mTempPoints = 0;
	}
	
	@Override
	public boolean canDecrease(final CreationMode aMode)
	{
		switch (aMode)
		{
			case CREATION :
				return canDecrease();
			case FREE_POINTS :
				return mTempPoints > 0;
			case NORMAL :
				return false;
		}
		return false;
	}
	
	@Override
	public boolean canIncrease()
	{
		return getValue() < getItem().getMaxValue();
	}
	
	@Override
	public void increase()
	{
		if (canIncrease())
		{
			if (mMode == CreationMode.FREE_POINTS)
			{
				mTempPoints++ ;
			}
			else
			{
				mValue++ ;
			}
		}
	}
	
	@Override
	public boolean canDecrease()
	{
		return getValue() > getItem().getStartValue();
	}
	
	@Override
	public void decrease()
	{
		if (canDecrease())
		{
			if (mMode == CreationMode.FREE_POINTS)
			{
				mTempPoints-- ;
			}
			else
			{
				mValue-- ;
			}
		}
	}
	
	@Override
	public int getValue()
	{
		return mValue + mTempPoints;
	}
	
	@Override
	public BackgroundItem getItem()
	{
		return mItem;
	}
	
	@Override
	public void refreshValue()
	{
		ViewUtil.applyValue(getValue(), mValueDisplay);
	}
}
