package com.deepercreeper.vampireapp.items.interfaces;

/**
 * Anything that has a name should implement this interface.
 * 
 * @author Vincent
 */
public interface Nameable extends Comparable<Nameable>, CharSequence
{
	/**
	 * @return the locale depending display name.
	 */
	public String getDisplayName();
	
	/**
	 * @return the name of this entity.
	 */
	public String getName();
}
