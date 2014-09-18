package com.deepercreeper.vampireapp;

import java.util.HashMap;
import android.widget.ImageButton;
import android.widget.RadioButton;

public class AttributeListener
{
	public static class Attribute
	{
		private final String		mName;
		
		private int					mValue	= 0;
		
		private final RadioButton[]	mRadios	= new RadioButton[5];
		
		private ImageButton			mSub, mAdd;
		
		public Attribute(String aName)
		{
			mName = aName;
		}
		
		public void setRadio(int aId, RadioButton aRadio)
		{
			mRadios[aId] = aRadio;
		}
		
		public void setAdd(ImageButton aAdd)
		{
			mAdd = aAdd;
		}
		
		public void setSub(ImageButton aSub)
		{
			mSub = aSub;
		}
		
		public String getName()
		{
			return mName;
		}
		
		public void addValue()
		{
			mValue++ ;
		}
		
		public void subValue()
		{
			mValue-- ;
		}
		
		public int getValue()
		{
			return mValue;
		}
	}
	
	private final HashMap<String, HashMap<String, Attribute>>	mHeaders	= new HashMap<>();
	
	public AttributeListener()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void addAttribute(String aHeader, Attribute aAttr)
	{
		HashMap<String, Attribute> attributes = mHeaders.get(aHeader);
		if (attributes == null)
		{
			attributes = new HashMap<>();
			mHeaders.put(aHeader, attributes);
		}
		attributes.put(aAttr.getName(), aAttr);
	}
}
