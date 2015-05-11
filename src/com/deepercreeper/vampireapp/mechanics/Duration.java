package com.deepercreeper.vampireapp.mechanics;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.util.Saveable;

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
	 * There are different duration types.
	 * 
	 * @author vrl
	 */
	public enum Type
	{
		/**
		 * A round is equal to a very short time, that is used to make the game flow.<br>
		 * Typically a round is around a second, but does not count up to hours.
		 */
		ROUND,
		
		/**
		 * Represents an hour.
		 */
		HOUR,
		
		/**
		 * Represents a day. By default a day is around 10 hours long.
		 */
		DAY
	}
	
	/**
	 * This duration won't end.
	 */
	public static final Duration			FOREVER		= new Duration();
	
	private final Type						mType;
	
	private final List<DurationListener>	mListeners	= new ArrayList<DurationListener>();
	
	private int								mValue;
	
	private Duration()
	{
		mType = null;
		mValue = -1;
	}
	
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
	
	private Duration(final Element aElement)
	{
		mType = Type.valueOf(aElement.getAttribute("durationType"));
		mValue = Integer.parseInt(aElement.getAttribute("durationValue"));
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
