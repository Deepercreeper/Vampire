package com.deepercreeper.vampireapp.items.interfaces.instances.restrictions;

import java.util.Set;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction.InstanceRestrictionType;

/**
 * All values and fields that can be restricted have to implements this interface.
 * 
 * @author vrl
 */
public interface InstanceRestrictionable
{
	/**
	 * Removes the given restriction from the current restriction set and updates all values.
	 * 
	 * @param aRestriction
	 *            The restriction that has to be removed.
	 */
	public void removeRestriction(InstanceRestriction aRestriction);
	
	/**
	 * Adds the given restriction to the current set of restrictions and updates all values.
	 * 
	 * @param aRestriction
	 *            The restriction that has to be added.
	 */
	public void addRestriction(InstanceRestriction aRestriction);
	
	/**
	 * @return whether restrictions are currently added to this field.
	 */
	public boolean hasRestrictions();
	
	public Set<InstanceRestriction> getRestrictions(InstanceRestrictionType... aTypes);
	
	public void updateRestrictions();
	
	public boolean isValueOk(int aValue, InstanceRestrictionType... aTypes);
	
	/**
	 * @return the minimum value calculated out of all restrictions of this field.
	 */
	public int getMinValue(InstanceRestrictionType... aTypes);
	
	/**
	 * @return the maximum value calculated out of all restrictions of this field.
	 */
	public int getMaxValue(InstanceRestrictionType... aTypes);
}
