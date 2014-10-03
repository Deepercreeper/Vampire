package com.deepercreeper.vampireapp.newControllers;

import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

public class DisciplineController implements Controller<DisciplineItem>
{
	private final int					mMaxCreationValue;
	
	private final DisciplineItemGroup	mDisciplines;
	
	public DisciplineController(final Resources aResources)
	{
		mMaxCreationValue = aResources.getInteger(R.integer.discipline_max_creation_value);
		mDisciplines = DisciplineItemGroup.create(aResources.getString(R.string.disciplines), aResources.getStringArray(R.array.disciplines));
	}
	
	public int getMaxCreationValue()
	{
		return mMaxCreationValue;
	}
	
	public DisciplineItemGroup getDisciplines()
	{
		return mDisciplines;
	}
}
