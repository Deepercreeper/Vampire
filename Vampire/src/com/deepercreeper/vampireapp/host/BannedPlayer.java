package com.deepercreeper.vampireapp.host;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.util.ContactsUtil;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;

/**
 * Represents a host banned player.
 * 
 * @author Vincent
 */
public class BannedPlayer extends Named implements Saveable
{
	private final String	mAddress;
	
	private String			mNumber;
	
	private String			mPlayer;
	
	/**
	 * Creates a banned player out of the given XML data.
	 * 
	 * @param aXML
	 *            The data.
	 * @param aContext
	 *            The underlying context.
	 */
	public BannedPlayer(final Element aXML, final Context aContext)
	{
		super(ContactsUtil.getContactOrBluetoothName(aContext, aXML.getAttribute("number"), aXML.getAttribute("address")));
		mNumber = aXML.getAttribute("number");
		mAddress = aXML.getAttribute("address");
		mPlayer = aXML.getAttribute("player");
	}
	
	/**
	 * Creates a new banned player.
	 * 
	 * @param aPlayer
	 *            The last player name.
	 * @param aAddress
	 *            The player address.
	 * @param aNumber
	 *            The player phone number.
	 * @param aContext
	 *            The underlying context.
	 */
	public BannedPlayer(final String aPlayer, final String aAddress, final String aNumber, final Context aContext)
	{
		super(ContactsUtil.getContactOrBluetoothName(aContext, aNumber, aAddress));
		mNumber = aNumber;
		mAddress = aAddress;
		mPlayer = aPlayer;
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("banned");
		element.setAttribute("address", getAddress());
		element.setAttribute("number", getNumber());
		element.setAttribute("player", getPlayer());
		return element;
	}
	
	/**
	 * @return the players address.
	 */
	public String getAddress()
	{
		return mAddress;
	}
	
	/**
	 * @return the phone number of the player.
	 */
	public String getNumber()
	{
		return mNumber;
	}
	
	/**
	 * @return the last player name.
	 */
	public String getPlayer()
	{
		return mPlayer;
	}
	
	/**
	 * Updates the phone number of the player.
	 * 
	 * @param aNumber
	 *            The new phone number.
	 * @param aContext
	 *            The underlying context.
	 */
	public void setNumber(final String aNumber, final Context aContext)
	{
		mNumber = aNumber;
		setName(ContactsUtil.getContactOrBluetoothName(aContext, aNumber, getAddress()));
	}
	
	/**
	 * Updates the player name.
	 * 
	 * @param aPlayer
	 *            The new player name.
	 */
	public void setPlayer(final String aPlayer)
	{
		mPlayer = aPlayer;
	}
}
