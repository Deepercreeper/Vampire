package com.deepercreeper.vampireapp.controllers.descriptions;

import com.deepercreeper.vampireapp.controllers.implementations.Named;

/**
 * Instances of this class represent user given descriptions for one description field each.<br>
 * They are used to define characters more precisely.
 * 
 * @author vrl
 */
public class DescriptionCreationValue extends Named
{
	private final Description	mItem;
	
	private String				mValue;
	
	/**
	 * Creates a new description value out of the given description type.
	 * 
	 * @param aItem
	 *            The description type.
	 */
	public DescriptionCreationValue(final Description aItem)
	{
		super(aItem.getName());
		mItem = aItem;
	}
	
	/**
	 * Sets the user defined description value.
	 * 
	 * @param aValue
	 *            The description.
	 */
	public void setValue(final String aValue)
	{
		mValue = aValue;
	}
	
	/**
	 * @return the current user defined description.
	 */
	public String getValue()
	{
		return mValue;
	}
	
	/**
	 * Removes the user defined description.
	 */
	public void clear()
	{
		mValue = "";
	}
	
	/**
	 * @return the description type.
	 */
	public Description getItem()
	{
		return mItem;
	}
}
