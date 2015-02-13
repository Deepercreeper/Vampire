package com.deepercreeper.vampireapp.lists.controllers.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.lists.controllers.creations.InsanityControllerCreation;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.mechanics.Duration.DurationListener;

public class InsanityControllerInstance implements TimeListener
{
	private final List<String>			mInsanities			= new ArrayList<String>();
	
	private final Map<String, Duration>	mInsanityDurations	= new HashMap<String, Duration>();
	
	public InsanityControllerInstance()
	{}
	
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
