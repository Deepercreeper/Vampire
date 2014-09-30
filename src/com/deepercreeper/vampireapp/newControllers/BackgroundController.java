package com.deepercreeper.vampireapp.newControllers;

import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

public class BackgroundController implements Controller
{
	private final BackgroundItemGroup	mBackgrounds;
	
	public BackgroundController(final Resources aResources)
	{
		mBackgrounds = BackgroundItemGroup.create(aResources.getString(R.string.backgrounds), aResources.getStringArray(R.array.backgrounds));
	}
	
	public BackgroundItemGroup getBackgrounds()
	{
		return mBackgrounds;
	}
}
