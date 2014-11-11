package com.deepercreeper.vampireapp.controller.descriptions;

import com.deepercreeper.vampireapp.controller.implementations.Named;

public class DescriptionValue extends Named
{
	private final Description	mItem;
	
	private String				mValue;
	
	public DescriptionValue(final Description aItem)
	{
		super(aItem.getName());
		mItem = aItem;
	}
	
	public void setValue(final String aValue)
	{
		mValue = aValue;
	}
	
	public String getValue()
	{
		return mValue;
	}
	
	public void clear()
	{
		mValue = "";
	}
	
	public Description getItem()
	{
		return mItem;
	}
}
