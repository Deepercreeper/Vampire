package com.deepercreeper.vampireapp.items.interfaces.creations.restrictions;

import java.util.Set;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;

/**
 * All values and fields that can be restricted have to implements this interface.
 * 
 * @author vrl
 */
public interface RestrictionableCreation extends Viewable
{
	/**
	 * Adds the given restriction to the current set of restrictions and updates all values.
	 * 
	 * @param aRestriction
	 *            The restriction that has to be added.
	 */
	public void addRestriction(RestrictionCreation aRestriction);
	
	/**
	 * @param aTypes
	 *            Types of restrictions that restrict the value.
	 * @return the maximum value calculated out of all restrictions of this field.
	 */
	public int getMaxValue(CreationRestrictionType... aTypes);
	
	/**
	 * @param aTypes
	 *            Types of restrictions that restrict the value.
	 * @return the minimum value calculated out of all restrictions of this field.
	 */
	public int getMinValue(CreationRestrictionType... aTypes);
	
	/**
	 * @param aTypes
	 *            The restriction types.
	 * @return a set of all restrictions that have one of the given types.
	 */
	public Set<RestrictionCreation> getRestrictions(CreationRestrictionType... aTypes);
	
	/**
	 * @param aTypes
	 *            The types of restrictions that are tested.
	 * @return whether restrictions are currently added to this field.
	 */
	public boolean hasRestrictions(final CreationRestrictionType... aTypes);
	
	/**
	 * @param aValue
	 *            The value that needs to be approved.
	 * @param aTypes
	 *            Types of the restrictions that are used to restrict the value.
	 * @return whether the given value doesn't collide with any of the restrictions.
	 */
	public boolean isValueOk(int aValue, CreationRestrictionType... aTypes);
	
	/**
	 * Removes the given restriction from the current restriction set and updates all values.
	 * 
	 * @param aRestriction
	 *            The restriction that has to be removed.
	 */
	public void removeRestriction(RestrictionCreation aRestriction);
}
