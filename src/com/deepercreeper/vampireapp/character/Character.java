package com.deepercreeper.vampireapp.character;

import com.deepercreeper.vampireapp.creation.CharCreator;

public class Character
{
	private final GenerationValueController		mGeneration;
	
	private final SimpleValueController			mSimpleValues;
	
	private final DisciplineValueController		mDisciplines;
	
	private final BackgroundValueController		mBackgrounds;
	
	private final PropertyValueController		mProperties;
	
	private final DescriptionValueController	mDescriptions;
	
	private final InsanityValueController		mInsanities;
	
	private final VolitionController			mVolition;
	
	public Character(final CharCreator aCreator)
	{
		mGeneration = new GenerationValueController();
		mSimpleValues = new SimpleValueController();
		mDisciplines = new DisciplineValueController();
		mBackgrounds = new BackgroundValueController();
		mProperties = new PropertyValueController();
		mDescriptions = new DescriptionValueController();
		mInsanities = new InsanityValueController();
		mVolition = new VolitionController();
	}
}
