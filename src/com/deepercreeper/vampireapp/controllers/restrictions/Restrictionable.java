package com.deepercreeper.vampireapp.controllers.restrictions;

import java.util.Set;
import com.deepercreeper.vampireapp.controllers.restrictions.Restriction.RestrictionType;

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
	 * @return whether restrictions are currently added to this field.
	 */
	public boolean hasRestrictions();
	
	public Set<Restriction> getRestrictions(RestrictionType... aTypes);
	
	public void updateRestrictions();
	
	public boolean isValueOk(int aValue, RestrictionType... aTypes);
	
	/**
	 * @return the minimum value calculated out of all restrictions of this field.
	 */
	public int getMinValue(RestrictionType... aTypes);
	
	/**
	 * @return the maximum value calculated out of all restrictions of this field.
	 */
	public int getMaxValue(RestrictionType... aTypes);
}
