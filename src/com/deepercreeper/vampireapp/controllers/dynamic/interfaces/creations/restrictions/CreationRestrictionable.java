package com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.restrictions;

import java.util.Set;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;

/**
 * All values and fields that can be restricted have to implements this interface.
 * 
 * @author vrl
 */
public interface CreationRestrictionable
{
	/**
	 * Removes the given restriction from the current restriction set and updates all values.
	 * 
	 * @param aRestriction
	 *            The restriction that has to be removed.
	 */
	public void removeRestriction(CreationRestriction aRestriction);
	
	/**
	 * Adds the given restriction to the current set of restrictions and updates all values.
	 * 
	 * @param aRestriction
	 *            The restriction that has to be added.
	 */
	public void addRestriction(CreationRestriction aRestriction);
	
	/**
	 * @return whether restrictions are currently added to this field.
	 */
	public boolean hasRestrictions();
	
	public Set<CreationRestriction> getRestrictions(CreationRestrictionType... aTypes);
	
	public void updateRestrictions();
	
	public boolean isValueOk(int aValue, CreationRestrictionType... aTypes);
	
	/**
	 * @return the minimum value calculated out of all restrictions of this field.
	 */
	public int getMinValue(CreationRestrictionType... aTypes);
	
	/**
	 * @return the maximum value calculated out of all restrictions of this field.
	 */
	public int getMaxValue(CreationRestrictionType... aTypes);
}
