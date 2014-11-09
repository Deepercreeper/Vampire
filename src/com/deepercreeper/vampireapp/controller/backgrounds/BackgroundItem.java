package com.deepercreeper.vampireapp.controller.backgrounds;

import com.deepercreeper.vampireapp.controller.implementations.ItemImpl;
import com.deepercreeper.vampireapp.controller.interfaces.Item;

/**
 * Background items are items that are not able to be changed after the character creation.<br>
 * It represents a background information for the character.
 * 
 * @author Vincent
 */
public class BackgroundItem extends ItemImpl
{
	private static final int	MAX_VALUE		= 5, MAX_START_VALUE = 5, START_VALUE = 0, FREE_POINTS_COST = 1;
	
	/**
	 * The number of backgrounds that can be set for one character.
	 */
	public static final int		MAX_BACKGROUNDS	= 5;
	
	private BackgroundItem(final String aName)
	{
		super(aName);
	}
	
	@Override
	public int getFreePointsCost()
	{
		return FREE_POINTS_COST;
	}
	
	@Override
	public int getMaxStartValue()
	{
		return MAX_START_VALUE;
	}
	
	@Override
	public int getMaxValue()
	{
		return MAX_VALUE;
	}
	
	@Override
	public int getStartValue()
	{
		return START_VALUE;
	}
	
	@Override
	public int compareTo(final Item aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof BackgroundItem)
		{
			final BackgroundItem item = (BackgroundItem) aO;
			return getName().equals(item.getName());
		}
		return false;
	}
	
	/**
	 * Creates a background out of the given data.
	 * 
	 * @param aData
	 *            The data out of which the background is created.
	 * @return the created background.
	 */
	public static BackgroundItem create(final String aData)
	{
		return new BackgroundItem(aData);
	}
	
	@Override
	protected String createDescription()
	{
		// TODO Implement
		return getName();
	}
}
