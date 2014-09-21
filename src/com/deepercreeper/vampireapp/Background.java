package com.deepercreeper.vampireapp;

public class Background
{
	public static final int	START_POINTS	= 5, NUMBER_BACKGROUNDS = 5;
	
	private final String	mName;
	
	public Background(final String aName)
	{
		mName = aName;
	}
	
	public String getName()
	{
		return mName;
	}
}
