package com.deepercreeper.vampireapp.host;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
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
	private final String				mName;
	
	@SuppressWarnings("unused")
	/**
	 * TODO Use this member
	 */
	private final ItemProvider			mItems;
	
	private final List<Player>			mPlayers	= new ArrayList<Player>();
	
	private final List<BannedPlayer>	mBanned		= new ArrayList<BannedPlayer>();
	
	private final String				mLocation;
	
	private final Context				mContext;
	
	private LinearLayout				mPlayersList;
	
	/**
	 * Creates a host out of the given XML data.
	 * 
	 * @param aXML
	 *            The XML data.
	 * @param aItems
	 *            The item provider.
	 * @param aContext
	 *            The underlying context.
	 */
	public Host(final String aXML, final ItemProvider aItems, final Context aContext)
	{
		mItems = aItems;
		mContext = aContext;
		
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
		
		// Banned players
		final Element bans = (Element) root.getElementsByTagName("bans").item(0);
		for (int i = 0; i < bans.getChildNodes().getLength(); i++ )
		{
			if (bans.getChildNodes().item(i) instanceof Element)
			{
				final Element banned = (Element) bans.getChildNodes().item(i);
				if (banned.getTagName().equals("banned"))
				{
					mBanned.add(new BannedPlayer(banned, mContext));
				}
			}
		}
	}
	
	/**
	 * Creates a new host with the given name and location.
	 * 
	 * @param aName
	 *            The host name.
	 * @param aItems
	 *            The item provider.
	 * @param aContext
	 *            The underlying context.
	 * @param aLocation
	 *            The host location.
	 */
	public Host(final String aName, final ItemProvider aItems, final String aLocation, final Context aContext)
	{
		mName = aName;
		mContext = aContext;
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
		if (mPlayers.contains(aPlayer) || isBanned(aPlayer))
		{
			return false;
		}
		mPlayers.add(aPlayer);
		mPlayersList.addView(aPlayer.getContainer());
		return true;
	}
	
	/**
	 * Adds the given player to the list of banned players.
	 * 
	 * @param aPlayer
	 *            The player to ban.
	 */
	public void ban(final Player aPlayer)
	{
		getBannedPlayers().add(new BannedPlayer(aPlayer.getName(), aPlayer.getDevice().getDevice().getAddress(), aPlayer.getNumber(), mContext));
		Collections.sort(getBannedPlayers());
	}
	
	/**
	 * @return a list of all currently banned players.
	 */
	public List<BannedPlayer> getBannedPlayers()
	{
		return mBanned;
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
	 * @param aPlayer
	 *            The player.
	 * @return whether the given player is banned.
	 */
	public boolean isBanned(final Player aPlayer)
	{
		for (final BannedPlayer player : getBannedPlayers())
		{
			if (player.getAddress().equals(aPlayer.getDevice().getDevice().getAddress()))
			{
				player.setPlayer(aPlayer.getName());
				return true;
			}
		}
		return false;
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
		
		// Banned players
		final Element bans = doc.createElement("bans");
		for (final BannedPlayer player : getBannedPlayers())
		{
			bans.appendChild(player.asElement(doc));
		}
		root.appendChild(bans);
		
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
	 * Removes the given player from the banned players list.
	 * 
	 * @param aPlayer
	 *            The player to remove from the banned players list.
	 */
	public void unban(final BannedPlayer aPlayer)
	{
		getBannedPlayers().remove(aPlayer);
	}
}
