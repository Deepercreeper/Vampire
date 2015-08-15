package com.deepercreeper.vampireapp.items.interfaces.instances.restrictions;

import java.util.Set;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance.InstanceRestrictionType;

/**
 * All values and fields that can be restricted have to implements this interface.
 * 
 * @author vrl
 */
public interface RestrictionableInstance
{
	/**
	 * Removes the given restriction from the current restriction set and updates all values.
	 * 
	 * @param aRestriction
	 *            The restriction that has to be removed.
	 */
	public void removeRestriction(RestrictionInstance aRestriction);
	
	/**
	 * Adds the given restriction to the current set of restrictions and updates all values.
	 * 
	 * @param aRestriction
	 *            The restriction that has to be added.
	 */
	public void addRestriction(RestrictionInstance aRestriction);
	
	/**
	 * @param aTypes
	 *            The restriction types.
	 * @return whether restrictions with one of the given types are currently added to this field.
	 */
	public boolean hasRestrictions(InstanceRestrictionType... aTypes);
	
	/**
	 * @return the character.
	 */
	public CharacterInstance getCharacter();
	
	/**
	 * @param aTypes
	 *            The restriction types.
	 * @return a set of restrictions that have one of the given types.
	 */
	public Set<RestrictionInstance> getRestrictions(InstanceRestrictionType... aTypes);
	
	/**
	 * Updates all restrictions.
	 */
	public void updateRestrictions();
	
	/**
	 * @param aValue
	 *            The value to approve.
	 * @param aTypes
	 *            The restriction types.
	 * @return whether the given value can be approved by all restriction with one of the given restriction types.
	 */
	public boolean isValueOk(int aValue, InstanceRestrictionType... aTypes);
	
	/**
	 * @param aTypes
	 *            The restriction types.
	 * @return the minimum value calculated out of all restrictions with one of the given restriction types of this field.
	 */
	public int getMinValue(InstanceRestrictionType... aTypes);
	
	/**
	 * @param aTypes
	 *            The restriction types.
	 * @return the maximum value calculated out of all restrictions with one of the given restriction types of this field.
	 */
	public int getMaxValue(InstanceRestrictionType... aTypes);
}
