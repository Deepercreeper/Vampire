package com.deepercreeper.vampireapp.controller.implementations;

/**
 * Anything that has a name should implement this interface.
 * 
 * @author Vincent
 */
public abstract class Named implements Comparable<Named>
{
	private final String	mName;
	
	/**
	 * Creates a new named entity.
	 * 
	 * @param aName
	 *            The entity name.
	 */
	public Named(final String aName)
	{
		mName = aName;
	}
	
	@Override
	public int compareTo(final Named aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	/**
	 * @return the name of this entity.
	 */
	public String getName()
	{
		return mName;
	}
}
