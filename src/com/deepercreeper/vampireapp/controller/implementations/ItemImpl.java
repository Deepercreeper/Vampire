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
	
	private final String	mDescription;
	
	/**
	 * Creates a new item.
	 * 
	 * @param aName
	 *            The item name.
	 */
	public ItemImpl(final String aName)
	{
		mName = aName;
		mDescription = createDescription();
	}
	
	@Override
	public int compareTo(final Item aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	@Override
	public String getDescription()
	{
		return mDescription;
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
	
	protected abstract String createDescription();
}
