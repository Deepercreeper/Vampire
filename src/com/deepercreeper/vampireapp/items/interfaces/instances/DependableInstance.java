package com.deepercreeper.vampireapp.items.interfaces.instances;

import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;

/**
 * A dependency instance depending item.
 * 
 * @author vrl
 */
public interface DependableInstance
{
	/**
	 * @param aType
	 *            The dependency type.
	 * @return whether this dependable is depending on a dependency with the given type.
	 */
	public boolean hasDependency(Type aType);
	
	/**
	 * @param aType
	 *            The dependency type.
	 * @return the dependency instance with the given type.
	 */
	public DependencyInstance getDependency(Type aType);
	
	/**
	 * @return the values array.
	 */
	public int[] getValues();
	
	/**
	 * @return the maximum value.
	 */
	public int getMaxValue();
}
