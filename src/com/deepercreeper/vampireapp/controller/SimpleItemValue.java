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
import com.deepercreeper.vampireapp.controller.ValueController.PointHandler;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * The value instance of the simple item.
 * 
 * @author Vincent
 */
public class SimpleItemValue implements ItemValue<SimpleItem>
{
	private CreationMode		mMode;
	
	private PointHandler		mPoints;
	
	private final SimpleItem	mItem;
	
	private int					mValue;
	
	private int					mTempPoints;
	
	private final Context		mContext;
	
	private final ImageButton	mIncreaseButton;
	
	private final ImageButton	mDecreaseButton;
	
	private final RadioButton[]	mValueDisplay;
	
	private final TableRow		mContainer;
	
	private final UpdateAction	mAction;
	
	/**
	 * Creates a new simple item value.
	 * 
	 * @param aItem
	 *            The item type.
	 * @param aContext
	 *            The context.
	 * @param aAction
	 *            The update action.
	 * @param aMode
	 *            The current creation mode.
	 * @param aPoints
	 *            The points handler.
	 */
	public SimpleItemValue(final SimpleItem aItem, final Context aContext, final UpdateAction aAction, final CreationMode aMode,
			final PointHandler aPoints)
	{
		mMode = aMode;
		mPoints = aPoints;
		mItem = aItem;
		mContext = aContext;
		mAction = aAction;
		mValueDisplay = new RadioButton[mItem.getMaxValue()];
		mContainer = new TableRow(mContext);
		mIncreaseButton = new ImageButton(mContext);
		mDecreaseButton = new ImageButton(mContext);
		mValue = mItem.getStartValue();
		init();
	}
	
	@Override
	public void setPoints(final PointHandler aPoints)
	{
		mPoints = aPoints;
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
	
	@Override
	public int getTempPoints()
	{
		return mTempPoints;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(mContainer, false);
	}
	
	private void init()
	{
		mContainer.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		
		final TextView valueName = new TextView(mContext);
		valueName.setLayoutParams(ViewUtil.instance().getRowNameLong());
		valueName.setText(getItem().getName());
		valueName.setGravity(Gravity.CENTER_VERTICAL);
		valueName.setSingleLine();
		valueName.setEllipsize(TruncateAt.END);
		mContainer.addView(valueName);
		
		final GridLayout spinnerGrid = new GridLayout(mContext);
		spinnerGrid.setLayoutParams(ViewUtil.instance().getRowWrapAll());
		{
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
	public void setIncreasable(final boolean aEnabled)
	{
		mIncreaseButton.setEnabled(aEnabled);
	}
	
	@Override
	public void setDecreasable(final boolean aEnabled)
	{
		mDecreaseButton.setEnabled(aEnabled);
	}
	
	@Override
	public TableRow getContainer()
	{
		return mContainer;
	}
	
	@Override
	public void refreshValue()
	{
		ViewUtil.applyValue(getValue(), mValueDisplay);
	}
	
	@Override
	public int getValue()
	{
		return mValue + mTempPoints;
	}
	
	@Override
	public boolean canIncrease()
	{
		return getValue() < getItem().getMaxValue();
	}
	
	@Override
	public boolean canDecrease()
	{
		return getValue() > getItem().getStartValue();
	}
	
	@Override
	public void resetTempPoints()
	{
		mTempPoints = 0;
		refreshValue();
	}
	
	@Override
	public boolean canIncrease(final CreationMode aMode)
	{
		switch (aMode)
		{
			case CREATION :
				return canIncrease() && mValue < getItem().getMaxStartValue();
			case FREE_POINTS :
				return canIncrease() && mValue + mTempPoints < getItem().getMaxStartValue() && mPoints.getPoints() >= getItem().getFreePointsCost();
			case NORMAL :
				return canIncrease();
		}
		return false;
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
	public void increase()
	{
		if (canIncrease())
		{
			if (mMode == CreationMode.FREE_POINTS)
			{
				mTempPoints++ ;
				mPoints.decrease(getItem().getFreePointsCost());
			}
			else
			{
				mValue++ ;
			}
		}
	}
	
	@Override
	public void decrease()
	{
		if (canDecrease())
		{
			if (mMode == CreationMode.FREE_POINTS)
			{
				mTempPoints-- ;
				mPoints.increase(getItem().getFreePointsCost());
			}
			else
			{
				mValue-- ;
			}
		}
	}
	
	@Override
	public SimpleItem getItem()
	{
		return mItem;
	}
}
