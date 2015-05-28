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
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Named other = (Named) obj;
		if (mName == null)
		{
			if (other.mName != null) return false;
		}
		else if ( !mName.equals(other.mName)) return false;
		return true;
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
