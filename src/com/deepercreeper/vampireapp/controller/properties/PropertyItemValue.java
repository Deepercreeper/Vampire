package com.deepercreeper.vampireapp.controller.properties;

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
 * The value instance of the property item.
 * 
 * @author Vincent
 */
public class PropertyItemValue extends ItemValueImpl<PropertyItem, PropertyItemValue>
{
	private final ImageButton	mIncreaseButton;
	
	private final ImageButton	mDecreaseButton;
	
	private final RadioButton[]	mValueDisplay;
	
	private final TableRow		mContainer;
	
	private int					mValueId;
	
	/**
	 * Creates a new property item value.
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
	 */
	public PropertyItemValue(final PropertyItem aItem, final Context aContext, final UpdateAction aAction, final PropertyItemValueGroup aGroup,
			final CharMode aMode)
	{
		super(aItem, aContext, aAction, aGroup, aMode, null);
		mIncreaseButton = new ImageButton(aContext);
		mDecreaseButton = new ImageButton(aContext);
		mContainer = new TableRow(aContext);
		mValueDisplay = new RadioButton[getItem().getValue(getItem().getMaxValue())];
		mValueId = getItem().getStartValue();
		init();
	}
	
	@Override
	protected void updateRestrictions()
	{
		final int minValue = getMinValue();
		final int maxValue = getMaxValue();
		
		if (minValue > mValueId)
		{
			mValueId = minValue;
		}
		if (maxValue < mValueId)
		{
			mValueId = maxValue;
		}
		refreshValue();
		getUpdateAction().update();
	}
	
	@Override
	protected void resetRestrictions()
	{
		mValueId = getItem().getStartValue();
		refreshValue();
		getUpdateAction().update();
	}
	
	@Override
	public boolean canDecrease()
	{
		return mValueId > getItem().getStartValue() && mValueId > getMinValue();
	}
	
	@Override
	public boolean canDecrease(final CharMode aMode)
	{
		switch (aMode)
		{
			case MAIN :
				return canDecrease();
			case POINTS :
				return false;
			case NORMAL :
				return false;
		}
		return false;
	}
	
	@Override
	public boolean canIncrease()
	{
		return mValueId < getItem().getMaxValue() && mValueId < getMaxValue();
	}
	
	@Override
	public boolean canIncrease(final CharMode aMode)
	{
		switch (aMode)
		{
			case MAIN :
				return canIncrease() && mValueId < getItem().getMaxStartValue();
			case POINTS :
				return false;
			case NORMAL :
				return false;
		}
		return false;
	}
	
	@Override
	public void decrease()
	{
		if (canDecrease())
		{
			mValueId-- ;
		}
	}
	
	@Override
	public TableRow getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the current value specified in {@link PropertyItem#getFinalValue(int)}.
	 */
	public int getFinalValue()
	{
		return getItem().getFinalValue(mValueId);
	}
	
	@Override
	public int getTempPoints()
	{
		return 0;
	}
	
	@Override
	public int getValue()
	{
		return getItem().getValue(mValueId);
	}
	
	/**
	 * @return the current value id.
	 */
	public int getValueId()
	{
		return mValueId;
	}
	
	@Override
	public void increase()
	{
		if (canIncrease())
		{
			mValueId++ ;
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
		return;
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
	public void setPoints(final PointHandler aPoints)
	{
		return;
	}
	
	private void init()
	{
		mContainer.setLayoutParams(ViewUtil.instance().getWrapAll());
		
		final TextView valueName = new TextView(getContext());
		valueName.setLayoutParams(ViewUtil.instance().getRowNameShort());
		valueName.setGravity(Gravity.CENTER_VERTICAL);
		valueName.setEllipsize(TruncateAt.END);
		valueName.setSingleLine();
		valueName.setText(getItem().getName());
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
