package com.deepercreeper.vampireapp.items.interfaces;

import java.util.Collection;

/**
 * Any item that is able to contain dependencies is a dependable.
 * 
 * @author Vincent
 */
public interface Dependable
{
	/**
	 * Adds the given dependency to this group.
	 * 
	 * @param aDependency
	 *            A dependency.
	 */
	public void addDependency(Dependency aDependency);
	
	/**
	 * @return whether this group has any dependencies.
	 */
	public boolean hasDependencies();
	
	/**
	 * @return a collection of all dependencies of this dependable.
	 */
	public Collection<Dependency> getDependencies();
}
