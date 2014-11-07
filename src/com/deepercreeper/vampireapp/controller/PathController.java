package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

public class PathController
{
	private final List<String>	mPaths	= new ArrayList<String>();
	
	public PathController(final Resources aResources)
	{
		for (final String path : aResources.getStringArray(R.array.paths))
		{
			mPaths.add(path);
		}
		Collections.sort(mPaths);
	}
	
	public String get(final int aIndex)
	{
		return mPaths.get(aIndex);
	}
	
	public int indexOf(final String aPath)
	{
		return mPaths.indexOf(aPath);
	}
}
