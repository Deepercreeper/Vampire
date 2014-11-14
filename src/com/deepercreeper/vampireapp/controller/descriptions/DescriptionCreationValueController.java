package com.deepercreeper.vampireapp.controller.descriptions;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.controller.implementations.ListControllerImpl;

/**
 * This controller manages the description values, defined by a user for each character.
 * 
 * @author vrl
 */
public class DescriptionCreationValueController extends ListControllerImpl<DescriptionCreationValue>
{
	private final DescriptionController	mController;
	
	/**
	 * Creates a new description value controller.
	 * 
	 * @param aController
	 *            The description controller.
	 */
	public DescriptionCreationValueController(final DescriptionController aController)
	{
		mController = aController;
		final List<DescriptionCreationValue> values = new ArrayList<DescriptionCreationValue>();
		for (final Description description : mController.getValues())
		{
			values.add(new DescriptionCreationValue(description));
		}
		init(values);
	}
	
	/**
	 * Resets all user defined descriptions.
	 */
	public void clear()
	{
		for (final DescriptionCreationValue value : getValues())
		{
			value.clear();
		}
	}
}
