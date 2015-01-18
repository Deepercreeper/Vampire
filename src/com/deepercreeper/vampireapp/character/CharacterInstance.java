package com.deepercreeper.vampireapp.character;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.controllers.GenerationController;
import com.deepercreeper.vampireapp.controllers.InsanityController;
import com.deepercreeper.vampireapp.controllers.descriptions.DescriptionValueController;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.ItemControllerInstance;

public class CharacterInstance
{
	private final GenerationController			mGeneration;
	
	private final List<ItemControllerInstance>	mControllers;
	
	private final DescriptionValueController	mDescriptions;
	
	private final InsanityController			mInsanities;
	
	private final EPHandler						mEP;
	
	public CharacterInstance(final CharCreator aCreator)
	{
		mGeneration = new GenerationController(aCreator.getGeneration().getGeneration());
		mControllers = new ArrayList<ItemControllerInstance>();
		mDescriptions = new DescriptionValueController(aCreator.getDescriptions());
		mInsanities = new InsanityController();
		mEP = new EPHandler();
	}
	
	public void update()
	{}
	
	public boolean isLowLevel()
	{
		return mGeneration.isLowLevel();
	}
}
