package com.deepercreeper.vampireapp.controller;

/**
 * A group of discipline items.
 * 
 * @author Vincent
 */
public class DisciplineItemGroup extends ItemGroupImpl<DisciplineItem>
{
	private DisciplineItemGroup(final String aName)
	{
		super(aName);
	}
	
	private void initParents()
	{
		for (final DisciplineItem parent : getItems())
		{
			if (parent.isParentItem())
			{
				for (final String subItemName : parent.getSubItemNames())
				{
					final SubDisciplineItem subItem = (SubDisciplineItem) getItem(subItemName);
					parent.addSubItem(subItem);
					subItem.setParent(parent);
				}
			}
		}
	}
	
	/**
	 * Creates a new discipline group out of the given data.
	 * 
	 * @param aName
	 *            The group name.
	 * @param aData
	 *            The data out of which the group is created.
	 * @return the created discipline item group.
	 */
	public static DisciplineItemGroup create(final String aName, final String[] aData)
	{
		final DisciplineItemGroup group = new DisciplineItemGroup(aName);
		for (final String discipline : aData)
		{
			group.addItem(DisciplineItem.create(discipline));
		}
		group.initParents();
		return group;
	}
}
