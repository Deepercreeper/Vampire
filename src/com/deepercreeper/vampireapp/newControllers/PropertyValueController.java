package com.deepercreeper.vampireapp.newControllers;

import android.widget.LinearLayout;

public class PropertyValueController implements ValueController
{
	private boolean							mCreation;
	
	private final PropertyController		mController;
	
	private final PropertyItemValueGroup	mProperties;
	
	public PropertyValueController(final PropertyController aController, final boolean aCreation)
	{
		mCreation = aCreation;
		mController = aController;
		mProperties = new PropertyItemValueGroup(mController.getProperties(), mCreation);
	}
	
	@Override
	public void setCreation(final boolean aCreation)
	{
		mCreation = aCreation;
		mProperties.setCreation(mCreation);
	}
	
	@Override
	public boolean isCreation()
	{
		return mCreation;
	}
	
	@Override
	public PropertyController getController()
	{
		return mController;
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{}
}
