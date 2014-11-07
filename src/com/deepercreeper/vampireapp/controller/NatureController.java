package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

public class NatureController
{
	private final List<String>	mNatures	= new ArrayList<String>();
	
	public NatureController(final Resources aResources)
	{
		for (final String natureAndBehavior : aResources.getStringArray(R.array.nature_and_behavior))
		{
			mNatures.add(natureAndBehavior);
		}
		Collections.sort(mNatures);
	}
	
	public String getFirst()
	{
		return mNatures.get(0);
	}
	
	public String get(final int aPos)
	{
		return mNatures.get(aPos);
	}
	
	public int indexOf(final String aNature)
	{
		return mNatures.indexOf(aNature);
	}
	
	public List<String> getNatures()
	{
		return mNatures;
	}
}
