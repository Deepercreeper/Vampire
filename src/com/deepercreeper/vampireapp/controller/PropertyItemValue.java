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
 * The value instance of the property item.
 * 
 * @author Vincent
 */
public class PropertyItemValue implements ItemValue<PropertyItem>
{
	private final PropertyItem	mItem;
	
	private final Context		mContext;
	
	private final ImageButton	mIncreaseButton;
	
	private final ImageButton	mDecreaseButton;
	
	private final TableRow		mContainer;
	
	private final UpdateAction	mAction;
	
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
	 */
	public PropertyItemValue(final PropertyItem aItem, final Context aContext, final UpdateAction aAction)
	{
		mIncreaseButton = new ImageButton(aContext);
		mDecreaseButton = new ImageButton(aContext);
		mContainer = new TableRow(aContext);
		mItem = aItem;
		mContext = aContext;
		mAction = aAction;
		mValueId = aItem.getStartValue();
		init();
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(mContainer);
		ViewUtil.release(mIncreaseButton);
		ViewUtil.release(mDecreaseButton);
	}
	
	private void init()
	{
		mContainer.setLayoutParams(ViewUtil.instance().getWrapAll());
		
		final TextView valueName = new TextView(mContext);
		valueName.setLayoutParams(ViewUtil.instance().getRowNameShort());
		valueName.setGravity(Gravity.CENTER_VERTICAL);
		valueName.setEllipsize(TruncateAt.END);
		valueName.setSingleLine();
		valueName.setText(getItem().getName());
		mContainer.addView(valueName);
		
		final GridLayout spinnerGrid = new GridLayout(mContext);
		spinnerGrid.setLayoutParams(ViewUtil.instance().getRowWrapAll());
		{
			final RadioButton[] valueDisplay = new RadioButton[getItem().getValue(getItem().getMaxValue())];
			
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
	
	@Override
	public TableRow getContainer()
	{
		return mContainer;
	}
	
	@Override
	public PropertyItem getItem()
	{
		return mItem;
	}
	
	@Override
	public int getValue()
	{
		return getItem().getValue(mValueId);
	}
	
	/**
	 * @return the current value specified in {@link PropertyItem#getFinalValue(int)}.
	 */
	public int getFinalValue()
	{
		return getItem().getFinalValue(mValueId);
	}
	
	/**
	 * @return the current value id.
	 */
	public int getValueId()
	{
		return mValueId;
	}
	
	@Override
	public boolean canIncrease()
	{
		return mValueId < getItem().getMaxValue();
	}
	
	@Override
	public boolean canDecrease()
	{
		return mValueId > getItem().getStartValue();
	}
	
	@Override
	public boolean canIncrease(final boolean aCreation)
	{
		return canIncrease() && ( !aCreation || mValueId < getItem().getMaxStartValue());
	}
	
	@Override
	public boolean canDecrease(final boolean aCreation)
	{
		return canDecrease();
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
	public void decrease()
	{
		if (canDecrease())
		{
			mValueId-- ;
		}
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
}
