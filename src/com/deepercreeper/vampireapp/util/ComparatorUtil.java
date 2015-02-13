package com.deepercreeper.vampireapp.util;

import java.util.Comparator;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;

public class ComparatorUtil
{
	public static final GroupComparator	ITEM_GROUP_CREATION_COMPARATOR	= new GroupComparator()
																		{
																			
																			private String	mGroupName;
																			
																			private int		mValue;
																			
																			@Override
																			public int compare(final ItemGroupCreation aLhs,
																					final ItemGroupCreation aRhs)
																			{
																				int leftValue = aLhs.getValue();
																				if (aLhs.getName().equals(mGroupName))
																				{
																					leftValue += mValue;
																				}
																				int rightValue = aRhs.getValue();
																				if (aRhs.getName().equals(mGroupName))
																				{
																					rightValue += mValue;
																				}
																				return leftValue - rightValue;
																			}
																			
																			@Override
																			public void setGroupChangeValue(final String aGroupName, final int aValue)
																			{
																				mGroupName = aGroupName;
																				mValue = aValue;
																			}
																		};
	
	public static interface GroupComparator extends Comparator<ItemGroupCreation>
	{
		public void setGroupChangeValue(String aGroupName, int aValue);
	}
}
