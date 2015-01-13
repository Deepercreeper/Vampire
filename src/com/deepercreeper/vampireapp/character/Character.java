package com.deepercreeper.vampireapp.character;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.controllers.GenerationValueController;
import com.deepercreeper.vampireapp.controllers.InsanityValueController;
import com.deepercreeper.vampireapp.controllers.descriptions.DescriptionValueController;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.creation.CharCreator;

public class Character
{
	private final GenerationValueController		mGeneration;
	
	private final List<ItemControllerInstance>	mControllers;
	
	private final DescriptionValueController	mDescriptions;
	
	private final InsanityValueController		mInsanities;
	
	private final ExperienceController			mEP;
	
	public Character(final CharCreator aCreator)
	{
		mGeneration = new GenerationValueController(aCreator.getGeneration().getGeneration());
		mControllers = new ArrayList<ItemControllerInstance>();
		mDescriptions = new DescriptionValueController(aCreator.getDescriptions());
		mInsanities = new InsanityValueController();
		mEP = new ExperienceController();
	}
}
