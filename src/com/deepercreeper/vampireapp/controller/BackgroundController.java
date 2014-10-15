package com.deepercreeper.vampireapp.controller;

import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

/**
 * A controller for background item groups.
 * 
 * @author Vincent
 */
public class BackgroundController implements Controller<BackgroundItem>
{
	private final int					mMaxCreationValue;
	
	private final BackgroundItemGroup	mBackgrounds;
	
	/**
	 * Creates a new background controller out of the given resources.
	 * 
	 * @param aResources
	 *            The context resources.
	 */
	public BackgroundController(final Resources aResources)
	{
		mMaxCreationValue = aResources.getInteger(R.integer.background_max_creation_value);
		mBackgrounds = BackgroundItemGroup.create(aResources.getString(R.string.backgrounds), aResources.getStringArray(R.array.backgrounds));
	}
	
	/**
	 * @return the maximum creation value for all item values.
	 */
	public int getMaxCreationValue()
	{
		return mMaxCreationValue;
	}
	
	/**
	 * @return the background item group.
	 */
	public BackgroundItemGroup getBackgrounds()
	{
		return mBackgrounds;
	}
}
