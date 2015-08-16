package com.deepercreeper.vampireapp.host;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import android.content.Context;
import android.widget.LinearLayout;

/**
 * This host represents a whole vampire game, that accepts users and handles the game flow.
 * 
 * @author vrl
 */
public class Host implements TimeListener, Saveable
{
	private final String mName;
	
	private final List<Player> mPlayers = new ArrayList<Player>();
	
	private final List<BannedPlayer> mBanned = new ArrayList<BannedPlayer>();
	
	private final List<Message> mMessages = new ArrayList<Message>();
	
	private final Context mContext;
	
	private LinearLayout mMessageList;
	
	private LinearLayout mPlayersList;
	
	private LinearLayout mPlayersTimeList;
	
	/**
	 * Creates a host out of the given XML data.
	 * 
	 * @param aData
	 *            The XML data or host name.
	 * @param aContext
	 *            The underlying context.
	 * @param aFromXML
	 *            whether this document should be created out of XML data or the first argument is the host name.
	 */
	public Host(final String aData, final Context aContext, final boolean aFromXML)
	{
		mContext = aContext;
		if ( !aFromXML)
		{
			mName = aData;
			return;
		}
		
		final Document doc = DataUtil.loadDocument(aData);
		if (doc == null)
		{
			throw new IllegalArgumentException();
		}
		
		// Root element
		final Element root = DataUtil.getElement(doc, "host");
		
		// Meta data
		final Element meta = DataUtil.getElement(root, "meta");
		mName = CodingUtil.decode(meta.getAttribute("name"));
		
		// Banned players
		for (Element banned : DataUtil.getChildren(DataUtil.getElement(root, "bans"), "banned"))
		{
			mBanned.add(new BannedPlayer(banned, mContext));
		}
	}
	
	/**
	 * A player message was sent.
	 * 
	 * @param aMessage
	 *            The message.
	 */
	public void addMessage(final Message aMessage)
	{
		if ( !mMessages.contains(aMessage))
		{
			mMessages.add(aMessage);
			mMessageList.addView(aMessage.getContainer());
		}
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
		mPlayersTimeList.addView(aPlayer.getPlayerCheckBox());
		return true;
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element root = aDoc.createElement("host");
		
		// Meta data
		final Element meta = aDoc.createElement("meta");
		meta.setAttribute("name", CodingUtil.encode(getName()));
		root.appendChild(meta);
		
		// Banned players
		final Element bans = aDoc.createElement("bans");
		for (final BannedPlayer player : getBannedPlayers())
		{
			bans.appendChild(player.asElement(aDoc));
		}
		root.appendChild(bans);
		return root;
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
	 * Removes the given message from the messages list.
	 * 
	 * @param aMessage
	 *            The message.
	 */
	public void releaseMessage(final Message aMessage)
	{
		mMessages.remove(aMessage);
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
		if (player != null)
		{
			player.release();
			mPlayers.remove(player);
		}
	}
	
	/**
	 * Sets the list, all messages from players are displayed in.
	 * 
	 * @param aMessageList
	 *            The message list.
	 */
	public void setMessageList(final LinearLayout aMessageList)
	{
		mMessageList = aMessageList;
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
	 * Sets the list of player checkboxes for time management.
	 * 
	 * @param aList
	 *            The list.
	 */
	public void setPlayersTimeList(final LinearLayout aList)
	{
		mPlayersTimeList = aList;
	}
	
	@Override
	public void time(final Type aType, final int aAmount)
	{
		for (final Player player : mPlayers)
		{
			player.time(aType, aAmount);
		}
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
