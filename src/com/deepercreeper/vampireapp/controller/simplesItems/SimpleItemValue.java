package com.deepercreeper.vampireapp.controller.simplesItems;

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
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.implementations.ItemValueImpl;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * The value instance of the simple item.
 * 
 * @author Vincent
 */
public class SimpleItemValue extends ItemValueImpl<SimpleItem, SimpleItemValue>
{
	private int					mValue;
	
	private int					mTempPoints;
	
	private final ImageButton	mIncreaseButton;
	
	private final ImageButton	mDecreaseButton;
	
	private final RadioButton[]	mValueDisplay;
	
	private final TableRow		mContainer;
	
	/**
	 * Creates a new simple item value.
	 * 
	 * @param aItem
	 *            The item type.
	 * @param aContext
	 *            The context.
	 * @param aAction
	 *            The update action.
	 * @param aGroup
	 *            The parent group.
	 * @param aMode
	 *            The current creation mode.
	 * @param aPoints
	 *            The points handler.
	 */
	public SimpleItemValue(final SimpleItem aItem, final Context aContext, final UpdateAction aAction, final SimpleItemValueGroup aGroup,
			final CharMode aMode, final PointHandler aPoints)
	{
		super(aItem, aContext, aAction, aGroup, aMode, aPoints);
		mValueDisplay = new RadioButton[getItem().getMaxValue()];
		mContainer = new TableRow(getContext());
		mIncreaseButton = new ImageButton(getContext());
		mDecreaseButton = new ImageButton(getContext());
		mValue = getItem().getStartValue();
		init();
	}
	
	@Override
	public boolean canDecrease(final CharMode aMode)
	{
		switch (aMode)
		{
			case MAIN :
				return canDecrease();
			case POINTS :
				return mTempPoints > 0;
			case NORMAL :
				return false;
		}
		return false;
	}
	
	@Override
	public boolean canIncrease(final CharMode aMode)
	{
		switch (aMode)
		{
			case MAIN :
				return canIncrease() && mValue < getItem().getMaxStartValue();
			case POINTS :
				return canIncrease() && getPoints().getPoints() >= getItem().getFreePointsCost();
			case NORMAL :
				return canIncrease();
		}
		return false;
	}
	
	@Override
	protected void updateRestrictions()
	{
		final int minValue = getMinValue();
		final int maxValue = getMaxValue();
		
		if (minValue > mValue)
		{
			mValue = minValue;
		}
		if (maxValue < mValue)
		{
			mValue = maxValue;
		}
		refreshValue();
		getUpdateAction().update();
	}
	
	@Override
	protected void resetRestrictions()
	{
		mValue = getItem().getStartValue();
		refreshValue();
		getUpdateAction().update();
	}
	
	@Override
	public void decrease()
	{
		if (canDecrease())
		{
			if (getCreationMode() == CharMode.POINTS)
			{
				mTempPoints-- ;
				getPoints().increase(getItem().getFreePointsCost());
			}
			else
			{
				mValue-- ;
			}
		}
	}
	
	@Override
	public TableRow getContainer()
	{
		return mContainer;
	}
	
	@Override
	public int getTempPoints()
	{
		return mTempPoints;
	}
	
	@Override
	public int getValue()
	{
		return mValue + mTempPoints;
	}
	
	@Override
	public void increase()
	{
		if (canIncrease())
		{
			if (getCreationMode() == CharMode.POINTS)
			{
				mTempPoints++ ;
				getPoints().decrease(getItem().getFreePointsCost());
			}
			else
			{
				mValue++ ;
			}
		}
	}
	
	@Override
	public void refreshValue()
	{
		ViewUtil.applyValue(getValue(), mValueDisplay);
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(mContainer, false);
	}
	
	@Override
	public void resetTempPoints()
	{
		mTempPoints = 0;
		refreshValue();
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
	
	private void init()
	{
		mContainer.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		
		final TextView valueName = new TextView(getContext());
		valueName.setLayoutParams(ViewUtil.instance().getRowNameLong());
		valueName.setText(getItem().getName());
		valueName.setGravity(Gravity.CENTER_VERTICAL);
		valueName.setSingleLine();
		valueName.setEllipsize(TruncateAt.END);
		mContainer.addView(valueName);
		
		final GridLayout spinnerGrid = new GridLayout(getContext());
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
					getUpdateAction().update();
				}
			});
			spinnerGrid.addView(mDecreaseButton);
			
			for (int i = 0; i < mValueDisplay.length; i++ )
			{
				final RadioButton valuePoint = new RadioButton(getContext());
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
					getUpdateAction().update();
				}
			});
			spinnerGrid.addView(mIncreaseButton);
			
			refreshValue();
		}
		mContainer.addView(spinnerGrid);
	}
}
