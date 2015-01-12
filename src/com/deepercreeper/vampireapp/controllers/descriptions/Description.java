package com.deepercreeper.vampireapp.controllers.descriptions;

import com.deepercreeper.vampireapp.controllers.implementations.Named;

/**
 * A character has several fields that need to be filled with a description.<br>
 * Each filed has a type, called description which is represented by instances of this class.
 * 
 * @author vrl
 */
public class Description extends Named
{
	/**
	 * Creates a new description type.
	 * 
	 * @param aName
	 *            The description type name.
	 */
	public Description(final String aName)
	{
		super(aName);
	}
}
