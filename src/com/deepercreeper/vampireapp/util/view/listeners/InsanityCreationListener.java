package com.deepercreeper.vampireapp.util.view.listeners;

import com.deepercreeper.vampireapp.mechanics.Duration;

/**
 * A listener for insanity creation dialogs.
 * 
 * @author vrl
 */
public interface InsanityCreationListener
{
	/**
	 * Invoked when an insanity was created.
	 * 
	 * @param aName
	 *            The insanity name.
	 * @param aDuration
	 *            The duration of the new insanity.
	 */
	public void insanityCreated(String aName, Duration aDuration);
}
