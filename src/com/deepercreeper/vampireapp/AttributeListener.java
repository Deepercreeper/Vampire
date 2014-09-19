package com.deepercreeper.vampireapp;

import java.util.HashMap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;

public class AttributeListener
{
	public static class Attribute
	{
		private final String		mName;
		
		private final String		mHeader;
		
		private final boolean		mAttribute;
		
		private AttributeListener	mListener;
		
		private int					mValue;
		
		private final int			mMinValue;
		
		private final RadioButton[]	mRadios	= new RadioButton[6];
		
		private ImageButton			mSub, mAdd;
		
		public Attribute(final String aName, final String aHeader, final boolean aAttribute)
		{
			mName = aName;
			mHeader = aHeader;
			mAttribute = aAttribute;
			if (mAttribute)
			{
				mValue = mMinValue = 1;
			}
			else
			{
				mValue = mMinValue = 0;
			}
		}
		
		public void setRadio(final int aId, final RadioButton aRadio)
		{
			mRadios[aId] = aRadio;
		}
		
		public void setAdd(final ImageButton aAdd)
		{
			mAdd = aAdd;
		}
		
		public void setSub(final ImageButton aSub)
		{
			mSub = aSub;
		}
		
		public String getHeader()
		{
			return mHeader;
		}
		
		public String getName()
		{
			return mName;
		}
		
		public int getValue()
		{
			return mValue;
		}
		
		private void applyValue()
		{
			for (int i = 0; i < 6; i++ )
			{
				mRadios[i].setChecked(i < mValue);
			}
		}
		
		public void init(final AttributeListener aListener)
		{
			mListener = aListener;
			mAdd.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					if (mValue < 3 + mMinValue && mListener.canAddValue(Attribute.this, mAttribute))
					{
						mValue++ ;
					}
					applyValue();
				}
			});
			mSub.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					if (mValue > mMinValue)
					{
						mValue-- ;
					}
					applyValue();
				}
			});
			applyValue();
		}
	}
	
	private final int[]											mAttributePoints;
	
	private final int[]											mAbilityPoints;
	
	private final HashMap<String, HashMap<String, Attribute>>	mHeaders	= new HashMap<>();
	
	public AttributeListener(final int[] aAttributePoints, final int[] aAbilityPoints)
	{
		mAttributePoints = aAttributePoints;
		mAbilityPoints = aAbilityPoints;
	}
	
	private boolean canAddValue(final Attribute aAttr, final boolean aAttributes)
	{
		int[] points;
		if (aAttributes)
		{
			points = mAttributePoints;
		}
		else
		{
			points = mAbilityPoints;
		}
		final HashMap<String, Integer> values = new HashMap<>();
		for (final String header : mHeaders.keySet())
		{
			int value = 0;
			boolean discard = false;
			for (final Attribute attribute : mHeaders.get(header).values())
			{
				if (attribute.mAttribute != aAttributes)
				{
					discard = true;
				}
				value += attribute.getValue();
			}
			if (discard)
			{
				continue;
			}
			values.put(header, value);
		}
		
		int big = 0, middle = 0;
		for (final String header : values.keySet())
		{
			if (header == aAttr.getHeader())
			{
				continue;
			}
			final int value = values.get(header);
			if (value > points[1])
			{
				big = 1;
			}
			else if (value > points[0])
			{
				middle++ ;
			}
		}
		final int value = values.get(aAttr.getHeader());
		if (value == points[2])
		{
			return false;
		}
		if (big > 0 && value == points[1])
		{
			return false;
		}
		if (big + middle > 1 && value == points[0])
		{
			return false;
		}
		return true;
	}
	
	public void addAttribute(final String aHeader, final Attribute aAttr)
	{
		HashMap<String, Attribute> attributes = mHeaders.get(aHeader);
		if (attributes == null)
		{
			attributes = new HashMap<>();
			mHeaders.put(aHeader, attributes);
		}
		attributes.put(aAttr.getName(), aAttr);
		aAttr.init(this);
	}
}
