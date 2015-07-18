package com.deepercreeper.vampireapp.host.change;

import com.deepercreeper.vampireapp.host.Message;
import com.deepercreeper.vampireapp.host.Message.ButtonAction;
import com.deepercreeper.vampireapp.util.interfaces.Toaster;

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
	 * The given message has been approved by the message receiver.
	 * 
	 * @param aMessage
	 *            The sent message.
	 * @param aAction
	 *            The message button action.
	 * @return whether the message should be released.
	 */
	public boolean applyMessage(Message aMessage, ButtonAction aAction);
	
	/**
	 * Sends the given message to the other connection side.
	 * 
	 * @param aMessage
	 *            The message to send.
	 */
	public void sendMessage(Message aMessage);
	
	/**
	 * Shows this message directly to the user.
	 * 
	 * @param aMessage
	 *            The message to show.
	 */
	public void showMessage(Message aMessage);
}
