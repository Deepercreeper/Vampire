package com.deepercreeper.vampireapp.lists.controllers.creations;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.lists.ListControllerImpl;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionController;
import com.deepercreeper.vampireapp.lists.items.Description;
import com.deepercreeper.vampireapp.lists.items.DescriptionCreation;

/**
 * This controller manages the description values, defined by a user for each character.
 * 
 * @author vrl
 */
public class DescriptionControllerCreation extends ListControllerImpl<DescriptionCreation>
{
	private final DescriptionController	mController;
	
	/**
	 * Creates a new description value controller.
	 * 
	 * @param aController
	 *            The description controller.
	 */
	public DescriptionControllerCreation(final DescriptionController aController)
	{
		mController = aController;
		final List<DescriptionCreation> values = new ArrayList<DescriptionCreation>();
		for (final Description description : mController.getValuesList())
		{
			values.add(new DescriptionCreation(description));
		}
		init(values);
	}
	
	/**
	 * Resets all user defined descriptions.
	 */
	public void clear()
	{
		for (final DescriptionCreation value : getValuesList())
		{
			value.clear();
		}
	}
}
