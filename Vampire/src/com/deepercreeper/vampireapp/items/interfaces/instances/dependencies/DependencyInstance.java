package com.deepercreeper.vampireapp.items.interfaces.instances.dependencies;

import com.deepercreeper.vampireapp.items.interfaces.Dependency.DestinationType;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;

/**
 * A dependency that finds the value on its own.
 * 
 * @author vrl
 */
public interface DependencyInstance
{
	/**
	 * @return the dependency destination type.
	 */
	public DestinationType getDestinationType();
	
	/**
	 * @return the current value of the destination.
	 */
	public int getDestinationValue();
	
	/**
	 * @return The dependency type.
	 */
	public Type getType();
	
	/**
	 * @param aDefault
	 *            The default value that is returned, when no value is set for the current value.
	 * @return the current depending value.
	 */
	public int getValue(int aDefault);
	
	/**
	 * @param aDefault
	 *            The default value that is returned, when no value is set for the current value.
	 * @return the current depending values array.
	 */
	public int[] getValues(int[] aDefault);
	
	/**
	 * @return whether this dependency has a destination.
	 */
	public boolean isActive();
}
