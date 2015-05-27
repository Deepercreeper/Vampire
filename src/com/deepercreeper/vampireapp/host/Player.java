package com.deepercreeper.vampireapp.host;

import com.deepercreeper.vampireapp.connection.ConnectedDevice;

/**
 * A host side represented character player.
 * 
 * @author vrl
 */
public class Player
{
	private final ConnectedDevice	mDevice;
	
	private final String			mName;
	
	/**
	 * Creates a new player that caches all needed data of the remote character.
	 * 
	 * @param aName
	 *            The player name.
	 * @param aDevice
	 *            The connected player device.
	 */
	public Player(String aName, ConnectedDevice aDevice)
	{
		mName = aName;
		mDevice = aDevice;
	}
	
	/**
	 * @return the players name.
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * @return the players device.
	 */
	public ConnectedDevice getDevice()
	{
		return mDevice;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Player other = (Player) obj;
		if (mName == null)
		{
			if (other.mName != null) return false;
		}
		else if ( !mName.equals(other.mName)) return false;
		return true;
	}
	
}
