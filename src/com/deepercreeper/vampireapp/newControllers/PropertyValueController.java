package com.deepercreeper.vampireapp.newControllers;

public class PropertyValueController
{
	private final PropertyController		mController;
	
	private final PropertyItemValueGroup	mProperties;
	
	public PropertyValueController(final PropertyController aController)
	{
		mController = aController;
		mProperties = new PropertyItemValueGroup(mController.getProperties());
	}
	
	public PropertyController getController()
	{
		return mController;
	}
}
