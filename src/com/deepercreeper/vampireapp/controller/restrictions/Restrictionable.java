package com.deepercreeper.vampireapp.controller.restrictions;

import java.util.Set;

/**
 * All values and fields that can be restricted have to implements this interface.
 * 
 * @author vrl
 */
public interface Restrictionable
{
	/**
	 * Removes the given restriction from the current restriction set and updates all values.
	 * 
	 * @param aRestriction
	 *            The restriction that has to be removed.
	 */
	public void removeRestriction(Restriction aRestriction);
	
	/**
	 * Adds the given restriction to the current set of restrictions and updates all values.
	 * 
	 * @param aRestriction
	 *            The restriction that has to be added.
	 */
	public void addRestriction(Restriction aRestriction);
	
	/**
	 * @return a set of all current restrictions for this field or value.
	 */
	public Set<Restriction> getRestrictions();
	
	/**
	 * @return whether restrictions are currently added to this field.
	 */
	public boolean hasRestrictions();
	
	/**
	 * @return the minimum value calculated out of all restrictions of this field.
	 */
	public int getMinValue();
	
	/**
	 * @return the maximum value calculated out of all restrictions of this field.
	 */
	public int getMaxValue();
}
