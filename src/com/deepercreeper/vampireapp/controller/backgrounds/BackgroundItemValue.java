package com.deepercreeper.vampireapp.controller.backgrounds;

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
 * This is the value of the background item.
 * 
 * @author Vincent
 */
public class BackgroundItemValue extends ItemValueImpl<BackgroundItem, BackgroundItemValue>
{
	private final ImageButton	mIncreaseButton;
	
	private final ImageButton	mDecreaseButton;
	
	private RadioButton[]		mValueDisplay;
	
	private TableRow			mContainer;
	
	private int					mValue;
	
	private int					mTempPoints;
	
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
	 * @param aPoints
	 *            The points handler.
	 */
	public BackgroundItemValue(final BackgroundItem aItem, final Context aContext, final UpdateAction aAction, final BackgroundItemValueGroup aGroup,
			final CharMode aMode, final PointHandler aPoints)
	{
		super(aItem, aContext, aAction, aGroup, aMode, aPoints);
		mIncreaseButton = new ImageButton(aContext);
		mDecreaseButton = new ImageButton(aContext);
		mValue = getItem().getStartValue();
	}
	
	@Override
	protected BackgroundItemValueGroup getGroup()
	{
		return (BackgroundItemValueGroup) super.getGroup();
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
		
		final TextView valueName = new TextView(getContext());
		valueName.setLayoutParams(ViewUtil.instance().getRowNameShort());
		valueName.setGravity(Gravity.CENTER_VERTICAL);
		valueName.setEllipsize(TruncateAt.END);
		valueName.setSingleLine();
		valueName.setText(getItem().getName());
		mContainer.addView(valueName);
		
		if (getCreationMode() == CharMode.MAIN)
		{
			final ImageButton edit = new ImageButton(getContext());
			edit.setLayoutParams(ViewUtil.instance().getRowButtonSize());
			edit.setContentDescription("Edit");
			edit.setImageResource(android.R.drawable.ic_menu_edit);
			edit.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					getGroup().editValue(BackgroundItemValue.this);
				}
			});
			mContainer.addView(edit);
		}
		
		final GridLayout spinnerGrid = new GridLayout(getContext());
		spinnerGrid.setLayoutParams(ViewUtil.instance().getRowWrapAll());
		{
			mValueDisplay = new RadioButton[getItem().getMaxValue()];
			
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
	public boolean canIncrease(final CharMode aMode)
	{
		switch (aMode)
		{
			case MAIN :
				return canIncrease() && mValue < getItem().getMaxStartValue();
			case POINTS :
				return canIncrease() && mValue + mTempPoints < getItem().getMaxStartValue()
						&& getPoints().getPoints() >= getItem().getFreePointsCost();
			case NORMAL :
				return canIncrease();
		}
		return false;
	}
	
	@Override
	public void resetTempPoints()
	{
		mTempPoints = 0;
		refreshValue();
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
	public boolean canIncrease()
	{
		return getValue() < getItem().getMaxValue();
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
	public boolean canDecrease()
	{
		return getValue() > getItem().getStartValue();
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
	public int getValue()
	{
		return mValue + mTempPoints;
	}
	
	@Override
	public void refreshValue()
	{
		ViewUtil.applyValue(getValue(), mValueDisplay);
	}
}
