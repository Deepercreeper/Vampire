package com.deepercreeper.vampireapp;

public class CreationDiscipline implements Creation
{
	private static final int	MAX_CREATION_VALUE	= 3, MAX_VALUE = 6;
	
	private final Discipline	mDiscipline;
	
	private CreationDiscipline	mFirstSubDiscipline, mSecondSubDiscipline;
	
	private int					mValue				= 0;
	
	public CreationDiscipline(final Discipline aDiscipline)
	{
		mDiscipline = aDiscipline;
	}
	
	public void setSubDiscipline(final CreationDiscipline aSubDiscipline, final boolean aFirst)
	{
		if (aFirst)
		{
			mFirstSubDiscipline = aSubDiscipline;
		}
		else
		{
			mSecondSubDiscipline = aSubDiscipline;
		}
	}
	
	public void setSecondSubDiscipline(final CreationDiscipline aSecondSubDiscipline)
	{
		mSecondSubDiscipline = aSecondSubDiscipline;
	}
	
	public CreationDiscipline getSubDiscipline(final boolean aFirst)
	{
		return aFirst ? mFirstSubDiscipline : mSecondSubDiscipline;
	}
	
	public boolean hasSubDiscipline(final boolean aFirst)
	{
		if (aFirst)
		{
			return mFirstSubDiscipline != null;
		}
		return mSecondSubDiscipline != null;
	}
	
	public Discipline getDiscipline()
	{
		return mDiscipline;
	}
	
	@Override
	public void increase()
	{
		if (mValue < MAX_VALUE && mValue < MAX_CREATION_VALUE)
		{
			mValue++ ;
		}
	}
	
	@Override
	public void decrease()
	{
		if (mValue > 0)
		{
			mValue-- ;
		}
	}
	
	@Override
	public int getValue()
	{
		return mValue;
	}
}
