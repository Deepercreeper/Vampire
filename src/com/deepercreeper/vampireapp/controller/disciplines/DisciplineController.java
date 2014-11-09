package com.deepercreeper.vampireapp.controller.disciplines;

import android.content.res.Resources;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.interfaces.Controller;

/**
 * A controller for discipline item groups.
 * 
 * @author Vincent
 */
public class DisciplineController implements Controller<DisciplineItem>
{
	private final int					mMaxCreationValue;
	
	private final DisciplineItemGroup	mDisciplines;
	
	/**
	 * Creates a new discipline controller out of the given resources.
	 * 
	 * @param aResources
	 */
	public DisciplineController(final Resources aResources)
	{
		mMaxCreationValue = aResources.getInteger(R.integer.discipline_max_creation_value);
		mDisciplines = DisciplineItemGroup.create(aResources.getString(R.string.disciplines), aResources.getStringArray(R.array.disciplines));
	}
	
	/**
	 * @return the maximum creation value for all item values.
	 */
	public int getMaxCreationValue()
	{
		return mMaxCreationValue;
	}
	
	/**
	 * @return the discipline item group.
	 */
	public DisciplineItemGroup getDisciplines()
	{
		return mDisciplines;
	}
}
