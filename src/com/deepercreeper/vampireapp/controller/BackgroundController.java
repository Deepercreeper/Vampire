package com.deepercreeper.vampireapp.controller;

import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

public class BackgroundController implements Controller<BackgroundItem>
{
	private final int					mMaxCreationValue;
	
	private final BackgroundItemGroup	mBackgrounds;
	
	public BackgroundController(final Resources aResources)
	{
		mMaxCreationValue = aResources.getInteger(R.integer.background_max_creation_value);
		mBackgrounds = BackgroundItemGroup.create(aResources.getString(R.string.backgrounds), aResources.getStringArray(R.array.backgrounds));
	}
	
	public int getMaxCreationValue()
	{
		return mMaxCreationValue;
	}
	
	public BackgroundItemGroup getBackgrounds()
	{
		return mBackgrounds;
	}
}
