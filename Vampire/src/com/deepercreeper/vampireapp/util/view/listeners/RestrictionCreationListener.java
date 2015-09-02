package com.deepercreeper.vampireapp.util.view.listeners;

import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance;

/**
 * A listener for create restriction dialogs.
 * 
 * @author Vincent
 */
public interface RestrictionCreationListener
{
	/**
	 * Invoked when a new restriction was created.
	 * 
	 * @param aRestriction
	 *            The new restriction.
	 */
	public void restrictionCreated(RestrictionInstance aRestriction);
}
