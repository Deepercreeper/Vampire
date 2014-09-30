package com.deepercreeper.vampireapp.newControllers;

import android.widget.LinearLayout;

public class DisciplineValueController implements ValueController
{
	private boolean							mCreation;
	
	private final DisciplineController		mController;
	
	private final DisciplineItemValueGroup	mDisciplines;
	
	public DisciplineValueController(final DisciplineController aController, final boolean aCreation)
	{
		mCreation = aCreation;
		mController = aController;
		mDisciplines = new DisciplineItemValueGroup(mController.getDisciplines(), mCreation);
	}
	
	@Override
	public void setCreation(final boolean aCreation)
	{
		mCreation = aCreation;
		mDisciplines.setCreation(mCreation);
	}
	
	@Override
	public boolean isCreation()
	{
		return mCreation;
	}
	
	@Override
	public DisciplineController getController()
	{
		return mController;
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{}
}
