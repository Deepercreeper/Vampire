package com.deepercreeper.vampireapp.lists.controllers.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.lists.controllers.creations.InsanityControllerCreation;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.mechanics.Duration.DurationListener;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.Saveable;

public class InsanityControllerInstance implements TimeListener, Saveable
{
	private final List<String>			mInsanities			= new ArrayList<String>();
	
	private final Map<String, Duration>	mInsanityDurations	= new HashMap<String, Duration>();
	
	public InsanityControllerInstance(final Element aElement)
	{
		for (int i = 0; i < aElement.getChildNodes().getLength(); i++ )
		{
			if (aElement.getChildNodes().item(i) instanceof Element)
			{
				final Element insanity = (Element) aElement.getChildNodes().item(i);
				if (insanity.getTagName().equals("insanity"))
				{
					final Duration duration = Duration.create((Element) insanity.getElementsByTagName("duration").item(0));
					addInsanity(CodingUtil.decode(insanity.getAttribute("name")), duration);
				}
			}
		}
	}
	
	public InsanityControllerInstance(final InsanityControllerCreation aController)
	{
		for (final String insanity : aController.getInsanities())
		{
			addInsanity(insanity, Duration.FOREVER);
		}
	}
	
	public List<String> getInsanities()
	{
		return mInsanities;
	}
	
	public Duration getDurationOf(final String aInsanity)
	{
		return mInsanityDurations.get(aInsanity);
	}
	
	public void addInsanity(final String aInsanity, final Duration aDuration)
	{
		aDuration.addListener(new InsanityDurationListener(aInsanity));
		mInsanities.add(aInsanity);
		mInsanityDurations.put(aInsanity, aDuration);
	}
	
	public void removeInsanity(final String aInsanity)
	{
		mInsanities.remove(aInsanity);
		mInsanityDurations.remove(aInsanity);
	}
	
	@Override
	public void day()
	{
		for (final Duration duration : mInsanityDurations.values())
		{
			duration.day();
		}
	}
	
	@Override
	public void hour()
	{
		for (final Duration duration : mInsanityDurations.values())
		{
			duration.hour();
		}
	}
	
	@Override
	public void round()
	{
		for (final Duration duration : mInsanityDurations.values())
		{
			duration.round();
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("insanities");
		for (final String insanity : getInsanities())
		{
			final Element insanityElement = aDoc.createElement("insanity");
			insanityElement.setAttribute("name", CodingUtil.encode(insanity));
			insanityElement.appendChild(getDurationOf(insanity).asElement(aDoc));
			element.appendChild(insanityElement);
		}
		return element;
	}
	
	private class InsanityDurationListener implements DurationListener
	{
		private final String	mInsanity;
		
		public InsanityDurationListener(final String aInsanity)
		{
			mInsanity = aInsanity;
		}
		
		@Override
		public void onDue()
		{
			removeInsanity(mInsanity);
		}
	}
}
