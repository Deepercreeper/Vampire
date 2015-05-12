package com.deepercreeper.vampireapp.items.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.util.Log;

/**
 * An implementation of item groups. Each item group should extend this class.
 * 
 * @author Vincent
 */
public class ItemGroupImpl extends Named implements ItemGroup
{
	private static final String			TAG			= "ItemGroup";
	
	private final List<Item>			mItemsList	= new ArrayList<Item>();
	
	private final HashMap<String, Item>	mItems		= new HashMap<String, Item>();
	
	private final int					mFreePointsCost;
	
	private final int					mMaxLowLevelValue;
	
	private final int					mMaxItems;
	
	private final int					mStartValue;
	
	private final int					mMaxValue;
	
	private final int					mEPCost;
	
	private final int					mEPCostNew;
	
	private final int					mEPCostMultiplicator;
	
	private final boolean				mOrder;
	
	private final boolean				mMutable;
	
	private final boolean				mFreeMutable;
	
	private final boolean				mValueGroup;
	
	/**
	 * Creates a new item group.
	 * 
	 * @param aName
	 *            The group name.
	 */
	public ItemGroupImpl(final String aName, final boolean aMutable, final boolean aOrder, final boolean aFreeMutable, final int aMaxLowLevelValue,
			final int aStartValue, final int aMaxValue, final int aFreePointsCost, final boolean aValueGroup, final int aMaxItems, final int aEPCost,
			final int aEPCostNew, final int aEPCostMultiplicator)
	{
		super(aName);
		mMutable = aMutable;
		mOrder = aOrder;
		mFreeMutable = aFreeMutable;
		mValueGroup = aValueGroup;
		mFreePointsCost = aFreePointsCost;
		mMaxLowLevelValue = aMaxLowLevelValue;
		mStartValue = aStartValue;
		mMaxValue = aMaxValue;
		mMaxItems = aMaxItems;
		mEPCost = aEPCost;
		mEPCostNew = aEPCostNew;
		mEPCostMultiplicator = aEPCostMultiplicator;
	}
	
	@Override
	public void addItem(final Item aItem)
	{
		getItemsList().add(aItem);
		mItems.put(aItem.getName(), aItem);
		Collections.sort(getItemsList());
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
	public int getEPCost()
	{
		return mEPCost;
	}
	
	@Override
	public int getEPCostMultiplicator()
	{
		return mEPCostMultiplicator;
	}
	
	@Override
	public int getEPCostNew()
	{
		return mEPCostNew;
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
	public int getMaxItems()
	{
		return mMaxItems;
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
	public boolean hasOrder()
	{
		return mOrder;
	}
	
	@Override
	public boolean isFreeMutable()
	{
		return mFreeMutable;
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
