package com.deepercreeper.vampireapp.host;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.FilesUtil;

/**
 * This host represents a whole vampire game, that accepts users and handles the game flow.
 * 
 * @author vrl
 */
public class Host
{
	private final String		mName;
	
	@SuppressWarnings("unused")
	/**
	 * TODO Use this member
	 */
	private final ItemProvider	mItems;
	
	private final List<Player>	mPlayers	= new ArrayList<Player>();
	
	private final List<String>	mBanned		= new ArrayList<String>();
	
	private final String		mLocation;
	
	private LinearLayout		mPlayersList;
	
	/**
	 * Creates a host out of the given XML data.
	 * 
	 * @param aXML
	 *            The XML data.
	 * @param aItems
	 *            The item provider.
	 */
	public Host(final String aXML, final ItemProvider aItems)
	{
		mItems = aItems;
		
		final Document doc = FilesUtil.loadDocument(aXML);
		if (doc == null)
		{
			throw new IllegalArgumentException();
		}
		
		// Root element
		final Element root = (Element) doc.getElementsByTagName("host").item(0);
		
		// Meta data
		final Element meta = (Element) root.getElementsByTagName("meta").item(0);
		mName = CodingUtil.decode(meta.getAttribute("name"));
		mLocation = meta.getAttribute("location");
	}
	
	/**
	 * Creates a new host with the given name and location.
	 * 
	 * @param aName
	 *            The host name.
	 * @param aItems
	 *            The item provider.
	 * @param aLocation
	 *            The host location.
	 */
	public Host(final String aName, final ItemProvider aItems, final String aLocation)
	{
		mName = aName;
		mItems = aItems;
		mLocation = aLocation;
	}
	
	/**
	 * Adds a player to the players list.
	 * 
	 * @param aPlayer
	 *            The player to add.
	 * @return {@code true} if the player was added and {@code false} if the name was taken already.
	 */
	public boolean addPlayer(final Player aPlayer)
	{
		if (mPlayers.contains(aPlayer) || isBanned(aPlayer.getDevice()))
		{
			return false;
		}
		mPlayers.add(aPlayer);
		mPlayersList.addView(aPlayer.getContainer());
		return true;
	}
	
	/**
	 * Adds the given device to the list of banned devices.
	 * 
	 * @param aDevice
	 *            The banned device.
	 */
	public void ban(final ConnectedDevice aDevice)
	{
		mBanned.add(aDevice.getDevice().getAddress());
	}
	
	/**
	 * @return the host name.
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * @param aDevice
	 *            The players device.
	 * @return the player with the given device.
	 */
	public Player getPlayer(final ConnectedDevice aDevice)
	{
		for (final Player player : mPlayers)
		{
			if (player.getDevice().equals(aDevice))
			{
				return player;
			}
		}
		return null;
	}
	
	/**
	 * @param aDevice
	 *            The device.
	 * @return whether the given device is banned.
	 */
	public boolean isBanned(final ConnectedDevice aDevice)
	{
		return mBanned.contains(aDevice.getDevice().getAddress());
	}
	
	/**
	 * Finds the player with the given device and removes it from the players list.
	 * 
	 * @param aDevice
	 *            The players device.
	 */
	public void removePlayer(final ConnectedDevice aDevice)
	{
		final Player player = getPlayer(aDevice);
		player.release();
		mPlayers.remove(player);
		
	}
	
	/**
	 * @return a serialized version of this host, that is possible to be sent and saved.
	 */
	public String serialize()
	{
		final Document doc = FilesUtil.createDocument();
		if (doc == null)
		{
			return null;
		}
		
		// Root element
		final Element root = doc.createElement("host");
		doc.appendChild(root);
		
		// Meta data
		final Element meta = doc.createElement("meta");
		meta.setAttribute("name", CodingUtil.encode(getName()));
		meta.setAttribute("location", mLocation);
		root.appendChild(meta);
		
		return FilesUtil.readDocument(doc);
	}
	
	/**
	 * Sets the list, all players are displayed inside.
	 * 
	 * @param aPlayersList
	 *            The players list.
	 */
	public void setPlayersList(final LinearLayout aPlayersList)
	{
		mPlayersList = aPlayersList;
	}
	
	/**
	 * Removes the given device from the banned devices list.
	 * 
	 * @param aDevice
	 *            The device.
	 */
	public void unban(final ConnectedDevice aDevice)
	{
		mBanned.remove(aDevice.getDevice().getAddress());
	}
}
