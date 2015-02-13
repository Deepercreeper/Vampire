package com.deepercreeper.vampireapp.mechanics;

import java.util.ArrayList;
import java.util.List;

public class Duration implements TimeListener
{
	public interface DurationListener
	{
		public void onDue();
	}
	
	public enum Type
	{
		ROUND, HOUR, DAY
	}
	
	public static final Duration			FOREVER		= new Duration();
	
	private final Type						mType;
	
	private final List<DurationListener>	mListeners	= new ArrayList<DurationListener>();
	
	private int								mValue;
	
	private Duration()
	{
		mType = null;
		mValue = -1;
	}
	
	public Duration(final Type aType, final int aValue)
	{
		if (aValue <= 0)
		{
			throw new IllegalArgumentException("Value has to be positive.");
		}
		if (aType == null)
		{
			throw new IllegalArgumentException("The type must not be null.");
		}
		mType = aType;
		mValue = aValue;
	}
	
	public Type getType()
	{
		return mType;
	}
	
	public int getValue()
	{
		return mValue;
	}
	
	public void addListener(final DurationListener aListener)
	{
		mListeners.add(aListener);
	}
	
	@Override
	public void day()
	{
		switch (mType)
		{
			case DAY :
				mValue-- ;
				break;
			case ROUND :
				mValue = 0;
				break;
			case HOUR :
				if (mValue >= 10)
				{
					mValue -= 10;
				}
				else
				{
					mValue = 0;
				}
				break;
			default :
				return;
		}
		if (mValue == 0)
		{
			onDue();
		}
	}
	
	private void onDue()
	{
		for (final DurationListener listener : mListeners)
		{
			listener.onDue();
		}
	}
	
	@Override
	public void hour()
	{
		switch (mType)
		{
			case HOUR :
				mValue-- ;
				break;
			case ROUND :
				mValue = 0;
				break;
			default :
				return;
		}
		if (mValue == 0)
		{
			onDue();
		}
	}
	
	@Override
	public void round()
	{
		if (mType != Type.ROUND)
		{
			return;
		}
		mValue-- ;
		if (mValue == 0)
		{
			onDue();
		}
	}
}
