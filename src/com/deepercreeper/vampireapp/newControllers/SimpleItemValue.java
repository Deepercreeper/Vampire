package com.deepercreeper.vampireapp.newControllers;

import java.util.Comparator;
import android.widget.ImageButton;

public class SimpleItemValue implements ItemValue<SimpleItem>
{
	private final SimpleItem	mItem;
	
	private int					mValue;
	
	private ImageButton			mIncreaseButton;
	
	private ImageButton			mDecreaseButton;
	
	public SimpleItemValue(final SimpleItem aItem)
	{
		mItem = aItem;
		mValue = mItem.getStartValue();
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
	public void setIncreaseButton(final ImageButton aIncreaseButton)
	{
		mIncreaseButton = aIncreaseButton;
	}
	
	@Override
	public void setDecreaseButton(final ImageButton aDecreaseButton)
	{
		mDecreaseButton = aDecreaseButton;
	}
	
	@Override
	public int getValue()
	{
		return mValue;
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
	
	@Override
	public SimpleItem getItem()
	{
		return mItem;
	}
	
	public static Comparator<? super SimpleItemValue> getComparator()
	{
		return new Comparator<SimpleItemValue>()
		{
			@Override
			public int compare(final SimpleItemValue aLhs, final SimpleItemValue aRhs)
			{
				return aLhs.getItem().compareTo(aRhs.getItem());
			}
		};
	}
}
