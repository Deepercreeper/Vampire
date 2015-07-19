package com.deepercreeper.vampireapp.items.implementations;

import com.deepercreeper.vampireapp.items.interfaces.Nameable;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.Log;

/**
 * Anything that has a name should extends this class.
 * 
 * @author Vincent
 */
public class Named implements Nameable
{
	private String mName;
	
	/**
	 * Creates a new named entity.
	 * 
	 * @param aName
	 *            The entity name.
	 */
	public Named(final String aName)
	{
		if (aName == null)
		{
			Log.e("Named", "String must not be null!");
		}
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
	
	/**
	 * Sets the name of this.
	 * 
	 * @param aName
	 *            The new name.
	 */
	public void setName(final String aName)
	{
		mName = aName;
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
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Named other = (Named) obj;
		if (getName() == null)
		{
			if (other.getName() != null) return false;
		}
		else if ( !getName().equals(other.getName())) return false;
		return true;
	}
	
	@Override
	public char charAt(final int aIndex)
	{
		return getName().charAt(aIndex);
	}
	
	@Override
	public int length()
	{
		return getName().length();
	}
	
	@Override
	public CharSequence subSequence(final int aStart, final int aEnd)
	{
		return getName().subSequence(aStart, aEnd);
	}
	
	@Override
	public String toString()
	{
		return getDisplayName();
	}
}
