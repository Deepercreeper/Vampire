package com.deepercreeper.vampireapp.newControllers;

import java.util.Comparator;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class PropertyItemValue implements ItemValue<PropertyItem>
{
	private final PropertyItem	mItem;
	
	private final Context		mContext;
	
	private final ImageButton	mIncreaseButton;
	
	private final ImageButton	mDecreaseButton;
	
	private final TableRow		mContainer;
	
	private final UpdateAction	mAction;
	
	private int					mValueId;
	
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
	
	private void init()
	{
		final LayoutParams nameSize = new TableRow.LayoutParams(ViewUtil.calcPx(120, mContext), LayoutParams.WRAP_CONTENT);
		final LayoutParams buttonSize = new LayoutParams(ViewUtil.calcPx(30, mContext), ViewUtil.calcPx(30, mContext));
		final LayoutParams valueSize = new LayoutParams(ViewUtil.calcPx(25, mContext), LayoutParams.WRAP_CONTENT);
		final LayoutParams wrapAll = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams wrapRowAll = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		mContainer.setLayoutParams(wrapAll);
		
		final TextView valueName = new TextView(mContext);
		valueName.setLayoutParams(nameSize);
		valueName.setText(getItem().getName());
		mContainer.addView(valueName);
		
		final GridLayout spinnerGrid = new GridLayout(mContext);
		spinnerGrid.setLayoutParams(wrapRowAll);
		{
			final RadioButton[] valueDisplay = new RadioButton[getItem().getValue(getItem().getMaxValue())];
			
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
			mAction.update();
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
	
	public int getFinalValue()
	{
		return getItem().getFinalValue(mValueId);
	}
	
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
	
	public static Comparator<? super PropertyItemValue> getComparator()
	{
		return new Comparator<PropertyItemValue>()
		{
			@Override
			public int compare(final PropertyItemValue aLhs, final PropertyItemValue aRhs)
			{
				return aLhs.getItem().compareTo(aRhs.getItem());
			}
		};
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
