package com.deepercreeper.vampireapp.controllers.actions;

import com.deepercreeper.vampireapp.controllers.implementations.Named;

public class ActionImpl extends Named implements Action
{
	private final ActionType	mType;
	
	private final int			mMinLevel;
	
	public ActionImpl(final String aName, final ActionType aType, final int aMinLevel)
	{
		super(aName);
		mType = aType;
		mMinLevel = aMinLevel;
	}
	
	@Override
	public int getMinLevel()
	{
		return mMinLevel;
	}
	
	@Override
	public ActionType getType()
	{
		return mType;
	}
}
