package com.deepercreeper.vampireapp.newControllers;

import android.widget.LinearLayout;

public class BackgroundValueController implements ValueController
{
	private boolean							mCreation;
	
	private final BackgroundController		mController;
	
	private final BackgroundItemValueGroup	mBackgrounds;
	
	public BackgroundValueController(final BackgroundController aController, final boolean aCreation)
	{
		mCreation = aCreation;
		mController = aController;
		mBackgrounds = new BackgroundItemValueGroup(mController.getBackgrounds(), mCreation);
	}
	
	@Override
	public void setCreation(final boolean aCreation)
	{
		mCreation = aCreation;
		mBackgrounds.setCreation(mCreation);
	}
	
	@Override
	public boolean isCreation()
	{
		return mCreation;
	}
	
	@Override
	public BackgroundController getController()
	{
		return mController;
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{}
}
