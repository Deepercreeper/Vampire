package com.deepercreeper.vampireapp.controller.interfaces;

import java.util.List;
import com.deepercreeper.vampireapp.controller.implementations.Named;

/**
 * A controller for list elements. Each element should have at least a name.
 * 
 * @author Vincent
 * @param <T>
 *            The element type.
 */
public interface ListController <T extends Named>
{
	/**
	 * @param aPos
	 *            The position of the value that should be returned.
	 * @return the value at the given position.
	 */
	public T get(int aPos);
	
	/**
	 * @param aName
	 *            The value name.
	 * @return The value with the given name.
	 */
	public T get(String aName);
	
	/**
	 * @return the first value.
	 */
	public T getFirst();
	
	/**
	 * @return a list of all value names.
	 */
	public List<String> getNames();
	
	/**
	 * @return a list of all values.
	 */
	public List<T> getValues();
	
	/**
	 * @param aValue
	 *            The value whose index should be returned.
	 * @return the index of the given value.
	 */
	public int indexOf(T aValue);
}
