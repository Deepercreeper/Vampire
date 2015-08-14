package com.deepercreeper.vampireapp.items.interfaces.instances;

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
	 * @return the current depending value.
	 */
	public int getValue();
	
	/**
	 * @return the current depending values array.
	 */
	public int[] getValues();
	
	/**
	 * @return the current value of the destination.
	 */
	public int getDestinationvalue();
	
	/**
	 * @return The dependency type.
	 */
	public Type getType();
	
	/**
	 * @return whether this dependency has a destination.
	 */
	public boolean isActive();
	
	/**
	 * @return the dependency destination type.
	 */
	public DestinationType getDestinationType();
}
