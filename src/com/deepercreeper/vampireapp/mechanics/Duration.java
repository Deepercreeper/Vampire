package com.deepercreeper.vampireapp.mechanics;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;

/**
 * This class represents a duration of time, that counts down to zero.
 * 
 * @author vrl
 */
public class Duration implements TimeListener, Saveable
{
	/**
	 * A listener that is used to wait for durations to get done.
	 * 
	 * @author vrl
	 */
	public interface DurationListener
	{
		/**
		 * Called when the given duration is due.
		 */
		public void onDue();
	}
	
	/**
	 * This duration won't end.
	 */
	public static final Duration			FOREVER		= new Duration();
	
	private final Type						mType;
	
	private final List<DurationListener>	mListeners	= new ArrayList<DurationListener>();
	
	private int								mValue;
	
	/**
	 * Creates a new duration.
	 * 
	 * @param aType
	 *            The duration type.
	 * @param aValue
	 *            The value.
	 */
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
	
	private Duration()
	{
		mType = null;
		mValue = -1;
	}
	
	private Duration(final Element aElement)
	{
		mType = Type.valueOf(aElement.getAttribute("durationType"));
		mValue = Integer.parseInt(aElement.getAttribute("durationValue"));
	}
	
	/**
	 * Adds a duration listener to this duration.
	 * 
	 * @param aListener
	 *            The listener that should be called, when this duration ends.
	 */
	public void addListener(final DurationListener aListener)
	{
		mListeners.add(aListener);
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
	
	@Override
	public void time(final Type aType, final int aAmount)
	{
		switch (aType)
		{
			case DAY :
				day(aAmount);
				break;
			case HOUR :
				hour(aAmount);
				break;
			case ROUND :
				round(aAmount);
			default :
				break;
		}
	}
	
	private void day(final int aAmount)
	{
		switch (mType)
		{
			case DAY :
				if (mValue >= aAmount)
				{
					mValue -= aAmount;
				}
				else
				{
					mValue = 0;
				}
				break;
			case ROUND :
				mValue = 0;
				break;
			case HOUR :
				if (mValue >= TimeListener.HOURS_PER_DAY)
				{
					mValue -= TimeListener.HOURS_PER_DAY;
				}
				else
				{
					mValue = 0;
				}
				if (aAmount > 1)
				{
					if (mValue >= 24 * (aAmount - 1))
					{
						mValue -= 24 * (aAmount - 1);
					}
					else
					{
						mValue = 0;
					}
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
	
	/**
	 * @return the duration type.
	 */
	public Type getType()
	{
		return mType;
	}
	
	/**
	 * @return the number of times, the duration method needs to be called to make this duration end.
	 */
	public int getValue()
	{
		return mValue;
	}
	
	private void hour(final int aAmount)
	{
		switch (mType)
		{
			case HOUR :
				if (mValue >= aAmount)
				{
					mValue -= aAmount;
				}
				else
				{
					mValue = 0;
				}
				break;
			case ROUND :
				if (aAmount > 0)
				{
					mValue = 0;
				}
			default :
				return;
		}
		if (mValue == 0)
		{
			onDue();
		}
	}
	
	private void round(final int aAmount)
	{
		if (mType != Type.ROUND)
		{
			return;
		}
		if (mValue >= aAmount)
		{
			mValue -= aAmount;
		}
		else
		{
			mValue = 0;
		}
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
	
	private void onDue()
	{
		for (final DurationListener listener : mListeners)
		{
			listener.onDue();
		}
	}
	
	/**
	 * Creates a duration out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @return a new duration that matches the data.
	 */
	public static Duration create(final Element aElement)
	{
		if (aElement.getAttribute("type").equals("forever"))
		{
			return FOREVER;
		}
		return new Duration(aElement);
	}
}
