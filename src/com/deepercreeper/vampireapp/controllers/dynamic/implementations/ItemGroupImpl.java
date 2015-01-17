package com.deepercreeper.vampireapp.controllers.dynamic.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.Log;

/**
 * An implementation of item groups. Each item group should extend this class.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 */
public class ItemGroupImpl implements ItemGroup
{
	private static final String			TAG			= "ItemGroup";
	
	private final String				mName;
	
	private final List<Item>			mItemsList	= new ArrayList<Item>();
	
	private final HashMap<String, Item>	mItems		= new HashMap<String, Item>();
	
	private final int					mFreePointsCost;
	
	private final int					mMaxLowLevelValue;
	
	private final int					mMaxItems;
	
	private final int					mStartValue;
	
	private final int					mMaxValue;
	
	private final boolean				mMutable;
	
	private final boolean				mValueGroup;
	
	/**
	 * Creates a new item group.
	 * 
	 * @param aName
	 *            The group name.
	 */
	public ItemGroupImpl(final String aName, final boolean aMutable, final int aMaxLowLevelValue, final int aStartValue, final int aMaxValue,
			final int aFreePointsCost, final boolean aValueGroup, final int aMaxItems)
	{
		mName = aName;
		mMutable = aMutable;
		mValueGroup = aValueGroup;
		mFreePointsCost = aFreePointsCost;
		mMaxLowLevelValue = aMaxLowLevelValue;
		mStartValue = aStartValue;
		mMaxValue = aMaxValue;
		mMaxItems = aMaxItems;
	}
	
	@Override
	public void addItem(final Item aItem)
	{
		getItemsList().add(aItem);
		mItems.put(aItem.getName(), aItem);
		Collections.sort(getItemsList());
	}
	
	@Override
	public int compareTo(final ItemGroup aAnother)
	{
		if (aAnother == null)
		{
			return getName().compareTo(null);
		}
		return getName().compareTo(aAnother.getName());
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof ItemGroup)
		{
			final ItemGroup group = (ItemGroup) aO;
			return getName().equals(group.getName());
		}
		return false;
	}
	
	@Override
	public int[] getDefaultValues()
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to create default values of a non value group.");
			return null;
		}
		final int[] values = new int[getMaxValue() + 1];
		for (int i = 0; i <= getMaxValue(); i++ )
		{
			values[i] = i;
		}
		return values;
	}
	
	@Override
	public String getDisplayName()
	{
		return LanguageUtil.instance().getValue(getName());
	}
	
	@Override
	public int getFreePointsCost()
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to get the free points cost of a non value group.");
			return 0;
		}
		return mFreePointsCost;
	}
	
	@Override
	public int getMaxItems()
	{
		return mMaxItems;
	}
	
	@Override
	public Item getItem(final String aName)
	{
		return mItems.get(aName);
	}
	
	@Override
	public List<Item> getItemsList()
	{
		return mItemsList;
	}
	
	@Override
	public int getMaxLowLevelValue()
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to get the max low level value of a non value group.");
			return 0;
		}
		return Math.min(mMaxLowLevelValue, getMaxValue());
	}
	
	@Override
	public int getMaxValue()
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to get the max value of a non value group.");
			return 0;
		}
		return mMaxValue;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	@Override
	public int getStartValue()
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to get the start value of a non value group.");
			return 0;
		}
		return mStartValue;
	}
	
	@Override
	public boolean hasItem(final String aName)
	{
		return mItems.containsKey(aName);
	}
	
	@Override
	public boolean isMutable()
	{
		return mMutable;
	}
	
	@Override
	public boolean isValueGroup()
	{
		return mValueGroup;
	}
	
	@Override
	public String toString()
	{
		return getDisplayName() + ": " + getItemsList().toString();
	}
}
