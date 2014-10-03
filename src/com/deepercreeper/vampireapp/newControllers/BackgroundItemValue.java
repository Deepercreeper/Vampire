package com.deepercreeper.vampireapp.newControllers;

import java.util.Comparator;
import android.widget.ImageButton;

public class BackgroundItemValue implements ItemValue<BackgroundItem>
{
	private final BackgroundItem	mItem;
	
	private ImageButton				mIncreaseButton;
	
	private ImageButton				mDecreaseButton;
	
	private int						mValue;
	
	public BackgroundItemValue(final BackgroundItem aItem)
	{
		mItem = aItem;
		mValue = mItem.getStartValue();
	}
	
	@Override
	public void setDecreaseButton(final ImageButton aButton)
	{
		mDecreaseButton = aButton;
	}
	
	@Override
	public void setIncreaseButton(final ImageButton aButton)
	{
		mIncreaseButton = aButton;
	}
	
	@Override
	public ImageButton getIncreaseButton()
	{
		return mIncreaseButton;
	}
	
	@Override
	public ImageButton getDecreaseButton()
	{
		return mDecreaseButton;
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
