package com.deepercreeper.vampireapp.host.change;

import com.deepercreeper.vampireapp.host.Message;
import com.deepercreeper.vampireapp.util.view.Toaster;

/**
 * A listener for changes that are created by the character and changes that have to be applied to the character.
 * 
 * @author vrl
 */
public interface MessageListener extends Toaster
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
	
	/**
	 * A message was sent by another device and needs to be approved.
	 * 
	 * @param aMessage
	 *            The sent message.
	 */
	public void applyMessage(Message aMessage);
	
	/**
	 * Sends the given message to the other connection side.
	 * 
	 * @param aMessage
	 *            The message to send.
	 */
	public void sendMessage(Message aMessage);
}
