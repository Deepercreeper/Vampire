package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import android.widget.ImageButton;

public class DisciplineItemValue implements ItemValue<DisciplineItem>
{
	private final DisciplineItem			mItem;
	
	private int								mValue;
	
	private ImageButton						mIncreaseButton;
	
	private ImageButton						mDecreaseButton;
	
	private DisciplineItemValue				mParentValue;
	
	private final List<DisciplineItemValue>	mSubValues	= new ArrayList<DisciplineItemValue>();
	
	public DisciplineItemValue(final DisciplineItem aItem)
	{
		mItem = aItem;
		mValue = mItem.getStartValue();
	}
	
	public void setParentValue(final DisciplineItemValue aParentValue)
	{
		mParentValue = aParentValue;
	}
	
	public DisciplineItemValue getParentValue()
	{
		return mParentValue;
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
	public boolean canIncrease(final boolean aCreation)
	{
		// TODO Move into DisciplineItemValueGroup.updateValues()
		
		if (mItem.isSubItem())
		{
			final DisciplineItemValue parentValue = getParentValue();
			final boolean firstSubItem = parentValue.hasSubDiscipline(0) && mItem.equals(parentValue.getSubValue(0).getItem());
			if (firstSubItem || parentValue.getSubValue(0).getValue() >= DisciplineItem.MIN_FIRST_SUB_VALUE)
			{
				return true;
			}
			return false;
		}
		return canIncrease() && ( !aCreation || mValue < getItem().getMaxStartValue());
	}
	
	@Override
	public boolean canDecrease(final boolean aCreation)
	{
		// TODO Move into DisciplineItemValueGroup.updateValues()
		
		if (mItem.isSubItem())
		{
			final DisciplineItemValue parentValue = getParentValue();
			final boolean firstSubItem = parentValue.hasSubDiscipline(0) && mItem.equals(parentValue.getSubValue(0).getItem());
			if (firstSubItem)
			{
				if (mValue == DisciplineItem.MIN_FIRST_SUB_VALUE)
				{
					for (int i = 1; i < DisciplineItem.MAX_SUB_DISCIPLINES; i++ )
					{
						if (parentValue.hasSubDiscipline(i))
						{
							if (parentValue.getSubValue(i).getValue() > 0)
							{
								return false;
							}
						}
					}
				}
			}
			return true;
		}
		return canDecrease();
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
	
	public DisciplineItemValue getSubValue(final int aPos)
	{
		if (aPos >= mSubValues.size())
		{
			return null;
		}
		return mSubValues.get(aPos);
	}
	
	public void setSubValue(final int aPos, final DisciplineItemValue aSubValue)
	{
		mSubValues.add(aPos, aSubValue);
		aSubValue.setParentValue(this);
	}
	
	public boolean hasSubDiscipline(final int aPos)
	{
		return mSubValues.size() > aPos && mSubValues.get(aPos) != null;
	}
	
	@Override
	public DisciplineItem getItem()
	{
		return mItem;
	}
	
	public List<DisciplineItemValue> getSubValues()
	{
		return mSubValues;
	}
	
	@Override
	public int getValue()
	{
		return mValue;
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
	
	public static Comparator<? super DisciplineItemValue> getComparator()
	{
		return new Comparator<DisciplineItemValue>()
		{
			@Override
			public int compare(final DisciplineItemValue aLhs, final DisciplineItemValue aRhs)
			{
				return aLhs.getItem().compareTo(aRhs.getItem());
			}
		};
	}
}
