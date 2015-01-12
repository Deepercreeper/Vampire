package com.deepercreeper.vampireapp.controllers.dynamic.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Namable;

/**
 * An implementation of items. Each item should extend this abstract class.
 * 
 * @author Vincent
 */
public class ItemImpl implements Item
{
	private static final String		TAG	= "Item";
	
	private final ItemGroup			mItemGroup;
	
	private final String			mName;
	
	private final String			mDisplayName;
	
	private final Item				mParentItem;
	
	private final boolean			mNeedsDescription;
	
	private final boolean			mParent;
	
	private final boolean			mMutableParent;
	
	private final boolean			mValueItem;
	
	private final int				mStartValue;
	
	private final int[]				mValues;
	
	private final List<Item>		mChildrenList;
	
	private final Map<String, Item>	mChildren;
	
	public ItemImpl(final String aName, final ItemGroup aGroup, final boolean aNeedsDescription, final boolean aParent, final boolean aMutableParent,
			final int[] aValues, final int aStartValue, final Item aParentItem)
	{
		mName = aName;
		mItemGroup = aGroup;
		mNeedsDescription = aNeedsDescription;
		mDisplayName = createDisplayName();
		mParent = aParent;
		mMutableParent = aMutableParent;
		mValues = aValues;
		mValueItem = mValues != null;
		mParentItem = aParentItem;
		if (isParent())
		{
			mChildrenList = new ArrayList<Item>();
			mChildren = new HashMap<String, Item>();
		}
		else
		{
			mChildrenList = null;
			mChildren = null;
		}
		if (aStartValue == -1)
		{
			mStartValue = getItemGroup().getStartValue();
		}
		else
		{
			mStartValue = aStartValue;
		}
	}
	
	@Override
	public void addChild(final Item aItem)
	{
		if ( !isParent())
		{
			Log.w(TAG, "Tried to add a child to non parent item.");
			return;
		}
		mChildren.put(aItem.getName(), aItem);
		getChildrenList().add(aItem);
		Collections.sort(getChildrenList());
	}
	
	@Override
	public int compareTo(final Namable aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof Item)
		{
			final Item item = (Item) aO;
			return getName().equals(item.getName());
		}
		return false;
	}
	
	@Override
	public Item getChild(final String aName)
	{
		if ( !isParent())
		{
			Log.w(TAG, "Tried to get a child of a non parent item.");
			return null;
		}
		return mChildren.get(aName);
	}
	
	@Override
	public List<Item> getChildrenList()
	{
		if ( !isParent())
		{
			Log.w(TAG, "Tried to get the children of a non parent item.");
			return null;
		}
		return mChildrenList;
	}
	
	@Override
	public String getDisplayName()
	{
		return mDisplayName;
	}
	
	@Override
	public int getFreePointsCost()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to get the free points cost of a non value item.");
			return 0;
		}
		return getItemGroup().getFreePointsCost();
	}
	
	@Override
	public ItemGroup getItemGroup()
	{
		return mItemGroup;
	}
	
	@Override
	public int getMaxValue()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to get the maximum value of a non value item.");
			return 0;
		}
		return Math.min(getItemGroup().getMaxValue(), getValues().length - 1);
	}
	
	@Override
	public int getMaxLowLevelValue()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to get the maximum low level value of a non value item.");
			return 0;
		}
		return Math.min(getItemGroup().getMaxLowLevelValue(), getValues().length - 1);
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	@Override
	public Item getParentItem()
	{
		return mParentItem;
	}
	
	@Override
	public int getStartValue()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to get the start value of a non value item.");
			return 0;
		}
		return mStartValue;
	}
	
	@Override
	public int[] getValues()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to get the values of a non value item.");
			return null;
		}
		return mValues;
	}
	
	@Override
	public boolean hasChild(final Item aItem)
	{
		if ( !isParent())
		{
			Log.w(TAG, "Tried to get a child of a non parent item.");
			return false;
		}
		return mChildren.containsKey(aItem.getName());
	}
	
	@Override
	public boolean hasChild(final String aName)
	{
		if ( !isParent())
		{
			Log.w(TAG, "Tried to get a child of a non parent item.");
			return false;
		}
		return mChildren.containsKey(aName);
	}
	
	@Override
	public int hashCode()
	{
		return getName().hashCode();
	}
	
	@Override
	public boolean hasParentItem()
	{
		return mParentItem != null;
	}
	
	@Override
	public boolean isMutableParent()
	{
		return mMutableParent;
	}
	
	@Override
	public boolean isParent()
	{
		return mParent;
	}
	
	@Override
	public boolean isValueItem()
	{
		return mValueItem;
	}
	
	@Override
	public boolean needsDescription()
	{
		return mNeedsDescription;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	private String createDisplayName()
	{
		return getName();
	}
}
