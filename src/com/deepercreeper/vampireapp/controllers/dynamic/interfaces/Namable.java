package com.deepercreeper.vampireapp.controllers.dynamic.interfaces;

/**
 * Anything that has a name should implement this interface.
 * 
 * @author Vincent
 */
public interface Namable extends Comparable<Namable>
{
	/**
	 * @return the name of this entity.
	 */
	public String getName();
	
	public String getDisplayName();
}
