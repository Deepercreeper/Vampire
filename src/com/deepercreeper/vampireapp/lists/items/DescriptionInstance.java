package com.deepercreeper.vampireapp.lists.items;

/**
 * When a player has created a character that contains descriptions,<br>
 * these descriptions are created as DescriptionCreation and then saved as DescriptionInstance.
 * 
 * @author vrl
 */
public class DescriptionInstance
{
	private final Description	mDescription;
	
	private final String		mValue;
	
	/**
	 * Creates a new description instance.
	 * 
	 * @param aDescription
	 *            The description item.
	 * @param aValue
	 *            The value.
	 */
	public DescriptionInstance(final Description aDescription, final String aValue)
	{
		mDescription = aDescription;
		mValue = aValue;
	}
	
	/**
	 * @return the description type.
	 */
	public Description getDescription()
	{
		return mDescription;
	}
	
	/**
	 * @return the description value..
	 */
	public String getValue()
	{
		return mValue;
	}
}
