package com.deepercreeper.vampireapp.newControllers;

import java.util.Comparator;
import android.widget.ImageButton;

public class PropertyItemValue implements ItemValue<PropertyItem>
{
	private final PropertyItem	mItem;
	
	private ImageButton			mIncreaseButton;
	
	private ImageButton			mDecreaseButton;
	
	private int					mValueId;
	
	public PropertyItemValue(final PropertyItem aItem)
	{
		mItem = aItem;
		mValueId = aItem.getStartValue();
	}
	
	@Override
	public ImageButton getDecreaseButton()
	{
		return mDecreaseButton;
	}
	
	@Override
	public ImageButton getIncreaseButton()
	{
		return mIncreaseButton;
	}
	
	@Override
	public void setDecreaseButton(final ImageButton aDecreaseButton)
	{
		mDecreaseButton = aDecreaseButton;
	}
	
	@Override
	public void setIncreaseButton(final ImageButton aIncreaseButton)
	{
		mIncreaseButton = aIncreaseButton;
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
}
