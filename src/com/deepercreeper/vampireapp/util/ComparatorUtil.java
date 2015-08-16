package com.deepercreeper.vampireapp.util;

import java.util.Comparator;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;

/**
 * Some items and groups need a special comparator. This utility class contains all of them.
 * 
 * @author vrl
 */
public class ComparatorUtil
{
	/**
	 * A group comparator that sorts for their current value.
	 * 
	 * @author vrl
	 */
	public static class GroupComparator implements Comparator<ItemGroupCreation>
	{
		private String	mGroupName;
		
		private int		mValue;
		
		private GroupComparator()
		{}
		
		@Override
		public int compare(ItemGroupCreation aLhs, ItemGroupCreation aRhs)
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
		
		/**
		 * Sets the current group, that is changed by the given value.
		 * 
		 * @param aGroupName
		 *            The group name.
		 * @param aValue
		 *            The changed value.
		 */
		public void setGroupChangeValue(String aGroupName, int aValue)
		{
			mGroupName = aGroupName;
			mValue = aValue;
		}
	}
	
	/**
	 * Sorts item group creations for their value.
	 */
	public static final GroupComparator	ITEM_GROUP_CREATION_COMPARATOR	= new GroupComparator();
}
