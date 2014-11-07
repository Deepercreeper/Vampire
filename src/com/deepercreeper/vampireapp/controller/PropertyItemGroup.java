package com.deepercreeper.vampireapp.controller;


/**
 * A group for property items.
 * 
 * @author Vincent
 */
public class PropertyItemGroup extends ItemGroupImpl<PropertyItem>
{
	private PropertyItemGroup(final String aName)
	{
		super(aName);
	}
	
	/**
	 * Creates a new property item group out of the given data.
	 * 
	 * @param aName
	 *            The group name.
	 * @param aData
	 *            The data out of which is the group created.
	 * @return the created property group.
	 */
	public static PropertyItemGroup create(final String aName, final String[] aData)
	{
		final PropertyItemGroup group = new PropertyItemGroup(aName);
		for (final String property : aData)
		{
			group.addItem(PropertyItem.create(property));
		}
		return group;
	}
}
