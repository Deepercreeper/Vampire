package com.deepercreeper.vampireapp.items.implementations;

import com.deepercreeper.vampireapp.items.interfaces.Namable;
import com.deepercreeper.vampireapp.util.LanguageUtil;

/**
 * Anything that has a name should extends this class.
 * 
 * @author Vincent
 */
public abstract class Named implements Namable
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
	public int compareTo(final Namable aAnother)
	{
		return getName().compareTo(aAnother.getName());
	}
	
	@Override
	public String getDisplayName()
	{
		return LanguageUtil.instance().getValue(getName());
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
}
