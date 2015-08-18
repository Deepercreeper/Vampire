package com.deepercreeper.vampireapp.items.interfaces.instances.dependencies;

import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;

/**
 * A dependency instance depending item.
 * 
 * @author vrl
 */
public interface DependableInstance
{
	/**
	 * Adds the given dependency to this dependable.
	 * 
	 * @param aDependency
	 */
	public void addDependency(DependencyInstance aDependency);
	
	/**
	 * @param aType
	 *            The dependency type.
	 * @return the dependency instance with the given type.
	 */
	public DependencyInstance getDependency(Type aType);
	
	/**
	 * @return the maximum value.
	 */
	public int getMaxValue();
	
	/**
	 * @return the maximum values.
	 */
	public int[] getMaxValues();
	
	/**
	 * @return the start value.
	 */
	public int getStartValue();
	
	/**
	 * @return the values array.
	 */
	public int[] getValues();
	
	/**
	 * @param aType
	 *            The dependency type.
	 * @return whether this dependable is depending on a dependency with the given type.
	 */
	public boolean hasDependency(Type aType);
}
