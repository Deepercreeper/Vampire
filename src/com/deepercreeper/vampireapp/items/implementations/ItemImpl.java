package com.deepercreeper.vampireapp.items.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.mechanics.Action;
import com.deepercreeper.vampireapp.util.Log;

/**
 * An implementation of items. Each item should extend this abstract class.
 * 
 * @author Vincent
 */
public class ItemImpl extends Named implements Item
{
	private static final String TAG = "Item";
	
	private final Map<String, Action> mActions = new HashMap<String, Action>();
	
	private final List<Action> mActionsList = new ArrayList<Action>();
	
	private final ItemGroup mItemGroup;
	
	private final Item mParentItem;
	
	private final boolean mNeedsDescription;
	
	private final boolean mParent;
	
	private final boolean mMutableParent;
	
	private final boolean mValueItem;
	
	private final boolean mOrder;
	
	private final int mStartValue;
	
	private final int mEPCost;
	
	private final int mEPCostNew;
	
	private final int mEPCostMultiplicator;
	
	private final int[] mValues;
	
	private final List<Item> mChildrenList;
	
	private final Map<String, Item> mChildren;
	
	/**
	 * Creates a new item.
	 * 
	 * @param aName
	 *            The item name.
	 * @param aGroup
	 *            The item group.
	 * @param aNeedsDescription
	 *            Whether this item needs a description.
	 * @param aParent
	 *            Whether this is a parent item.
	 * @param aMutableParent
	 *            Whether this is a mutable parent item.
	 * @param aOrder
	 *            whether the child items of this item have a specific order.
	 * @param aValues
	 *            The values this item can get.
	 * @param aStartValue
	 *            The start value for this item.
	 * @param aEPCost
	 *            The experience cost for increasing this item.
	 * @param aEPCostNew
	 *            The experience cost for adding the first point to this item.
	 * @param aEPCostMultiplicator
	 *            The experience cost that is multiplied with the current item value.
	 * @param aParentItem
	 *            The parent item or {@code null} if this is no child item.
	 */
	public ItemImpl(final String aName, final ItemGroup aGroup, final boolean aNeedsDescription, final boolean aParent, final boolean aMutableParent,
			final boolean aOrder, final int[] aValues, final int aStartValue, final int aEPCost, final int aEPCostNew, final int aEPCostMultiplicator,
			final Item aParentItem)
	{
		super(aName);
		mItemGroup = aGroup;
		mNeedsDescription = aNeedsDescription;
		mParent = aParent;
		mMutableParent = aMutableParent;
		mOrder = aOrder;
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
		if (aEPCost == -1)
		{
			mEPCost = getItemGroup().getEPCost();
		}
		else
		{
			mEPCost = aEPCost;
		}
		if (aEPCostNew == -1)
		{
			mEPCostNew = getItemGroup().getEPCostNew();
		}
		else
		{
			mEPCostNew = aEPCostNew;
		}
		if (aEPCostMultiplicator == -1)
		{
			mEPCostMultiplicator = getItemGroup().getEPCostMultiplicator();
		}
		else
		{
			mEPCostMultiplicator = aEPCostMultiplicator;
		}
	}
	
	@Override
	public void addAction(final Action aAction)
	{
		mActionsList.add(aAction);
		mActions.put(aAction.getName(), aAction);
		Collections.sort(getActionsList());
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
	public Action getAction(final String aName)
	{
		return mActions.get(aName);
	}
	
	@Override
	public List<Action> getActionsList()
	{
		return mActionsList;
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
	public String getDescription()
	{
		String displayName = getDisplayName();
		
		if (hasActions())
		{
			displayName += ":";
			boolean first = true;
			for (final Action action : getActionsList())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					displayName += ",";
				}
				displayName += " " + action.getDisplayName();
			}
		}
		return displayName;
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
	public boolean hasActions()
	{
		return !getActionsList().isEmpty();
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
	public boolean hasOrder()
	{
		return mOrder;
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
}
