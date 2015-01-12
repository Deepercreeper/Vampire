package com.deepercreeper.vampireapp.controllers.lists;

import com.deepercreeper.vampireapp.controllers.implementations.Named;

/**
 * Each character has a specific nature and behavior which is an instance of this class.
 * 
 * @author vrl
 */
public class Nature extends Named
{
	/**
	 * Creates a new nature.
	 * 
	 * @param aName
	 *            The nature name.
	 */
	public Nature(final String aName)
	{
		super(aName);
	}
}
