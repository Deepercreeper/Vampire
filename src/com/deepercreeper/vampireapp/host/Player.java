package com.deepercreeper.vampireapp.host;

import com.deepercreeper.vampireapp.connection.ConnectedDevice;

public class Player
{
	private final ConnectedDevice	mDevice;
	
	private final String			mName;
	
	public Player(String aName, ConnectedDevice aDevice)
	{
		mName = aName;
		mDevice = aDevice;
	}
	
	public String getName()
	{
		return mName;
	}
	
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
