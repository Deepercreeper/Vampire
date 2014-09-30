package com.deepercreeper.vampireapp.newControllers;

import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;

public class DisciplineController implements Controller
{
	private final DisciplineItemGroup	mDisciplines;
	
	public DisciplineController(final Resources aResources)
	{
		mDisciplines = DisciplineItemGroup.create(aResources.getString(R.string.disciplines), aResources.getStringArray(R.array.disciplines));
	}
	
	public DisciplineItemGroup getDisciplines()
	{
		return mDisciplines;
	}
}
