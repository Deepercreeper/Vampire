package com.deepercreeper.vampireapp.controller.backgrounds;

import com.deepercreeper.vampireapp.controller.implementations.ItemGroupImpl;


/**
 * A group of background items.
 * 
 * @author Vincent
 */
public class BackgroundItemGroup extends ItemGroupImpl<BackgroundItem>
{
	private BackgroundItemGroup(final String aName)
	{
		super(aName);
	}
	
	/**
	 * Creates a new background item group out of the given data.
	 * 
	 * @param aName
	 *            The group name.
	 * @param aData
	 *            The data out of which the group is created.
	 * @return the created group.
	 */
	public static BackgroundItemGroup create(final String aName, final String[] aData)
	{
		final BackgroundItemGroup group = new BackgroundItemGroup(aName);
		for (final String item : aData)
		{
			group.addItem(BackgroundItem.create(item));
		}
		return group;
	}
}
