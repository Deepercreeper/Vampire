package com.deepercreeper.vampireapp.controller.implementations;

import com.deepercreeper.vampireapp.controller.interfaces.Item;

/**
 * An implementation of items. Each item should extend this abstract class.
 * 
 * @author Vincent
 */
public abstract class ItemImpl implements Item
{
	private final String	mName;
	
	private final String	mDisplayName;
	
	private final boolean	mNeedsDescription;
	
	/**
	 * Creates a new item.
	 * 
	 * @param aName
	 *            The item name.
	 * @param aNeedsDescription
	 *            Whether this item needs a description given after creating a character.
	 */
	public ItemImpl(final String aName, final boolean aNeedsDescription)
	{
		mName = aName;
		mNeedsDescription = aNeedsDescription;
		mDisplayName = createDisplayName();
	}
	
	@Override
	public int compareTo(final Item aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	@Override
	public String getDisplayName()
	{
		return mDisplayName;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	@Override
	public int hashCode()
	{
		return getName().hashCode();
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
	
	protected abstract String createDisplayName();
}
