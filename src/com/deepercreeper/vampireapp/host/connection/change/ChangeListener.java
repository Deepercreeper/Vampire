package com.deepercreeper.vampireapp.host.connection.change;

/**
 * A listener for changes that are created by the character and changes that have to be applied to the character.
 * 
 * @author vrl
 */
public interface ChangeListener
{
	/**
	 * The given change has to be sent to the corresponding device.
	 * 
	 * @param aChange
	 *            The change done.
	 */
	public void sendChange(CharacterChange aChange);
	
	/**
	 * The given change has to be done to the character.
	 * 
	 * @param aChange
	 *            The change.
	 * @param aType
	 *            The change type.
	 */
	public void applyChange(String aChange, String aType);
}
