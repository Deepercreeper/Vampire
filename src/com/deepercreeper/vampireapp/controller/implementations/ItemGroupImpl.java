package com.deepercreeper.vampireapp.controller.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.deepercreeper.vampireapp.controller.interfaces.Item;
import com.deepercreeper.vampireapp.controller.interfaces.ItemGroup;

/**
 * An implementation of item groups. Each item group should extend this class.
 * 
 * @author Vincent
 * @param <T>
 *            The item type.
 */
public abstract class ItemGroupImpl <T extends Item> implements ItemGroup<T>
{
	private final String				mName;
	
	private final List<T>				mItems		= new ArrayList<T>();
	
	private final HashMap<String, T>	mItemNames	= new HashMap<String, T>();
	
	/**
	 * Creates a new item group.
	 * 
	 * @param aName
	 *            The group name.
	 */
	public ItemGroupImpl(final String aName)
	{
		mName = aName;
	}
	
	@Override
	public T getItem(final String aName)
	{
		return mItemNames.get(aName);
	}
	
	@Override
	public List<T> getItems()
	{
		return mItems;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	protected void addItem(final T aItem)
	{
		mItems.add(aItem);
		mItemNames.put(aItem.getName(), aItem);
		Collections.sort(mItems);
	}
}
