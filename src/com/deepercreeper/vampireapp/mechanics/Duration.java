package com.deepercreeper.vampireapp.mechanics;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.util.Saveable;

public class Duration implements TimeListener, Saveable
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
	
	private Duration(final Element aElement)
	{
		mType = Type.valueOf(aElement.getAttribute("durationType"));
		mValue = Integer.parseInt(aElement.getAttribute("durationValue"));
	}
	
	public static Duration create(final Element aElement)
	{
		if (aElement.getAttribute("type").equals("forever"))
		{
			return FOREVER;
		}
		return new Duration(aElement);
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("duration");
		if (this == FOREVER)
		{
			element.setAttribute("type", "forever");
		}
		else
		{
			element.setAttribute("type", getType().name());
			element.setAttribute("value", "" + getValue());
		}
		return element;
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
	
	@Override
	public String toString()
	{
		if (this == FOREVER)
		{
			return "Forever";
		}
		return getType().name() + ": " + getValue();
	}
}
