package com.deepercreeper.vampireapp.items.implementations;

import com.deepercreeper.vampireapp.items.interfaces.Nameable;
import com.deepercreeper.vampireapp.util.LanguageUtil;

/**
 * Anything that has a name should extends this class.
 * 
 * @author Vincent
 */
public abstract class Named implements Nameable
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
	public int compareTo(final Nameable aAnother)
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
	
	@Override
	public char charAt(final int aIndex)
	{
		return mName.charAt(aIndex);
	}
	
	@Override
	public int length()
	{
		return mName.length();
	}
	
	@Override
	public CharSequence subSequence(final int aStart, final int aEnd)
	{
		return mName.subSequence(aStart, aEnd);
	}
	
	@Override
	public String toString()
	{
		return mName;
	}
}
