package com.deepercreeper.vampireapp.controller.descriptions;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.controller.implementations.ListControllerImpl;

/**
 * This controller manages the description values, defined by a user for each character.
 * 
 * @author vrl
 */
public class DescriptionValueController extends ListControllerImpl<DescriptionValue>
{
	private final DescriptionController	mController;
	
	/**
	 * Creates a new description value controller.
	 * 
	 * @param aController
	 *            The description controller.
	 */
	public DescriptionValueController(final DescriptionController aController)
	{
		mController = aController;
		final List<DescriptionValue> values = new ArrayList<DescriptionValue>();
		for (final Description description : mController.getValues())
		{
			values.add(new DescriptionValue(description));
		}
		init(values);
	}
	
	/**
	 * Resets all user defined descriptions.
	 */
	public void clear()
	{
		for (final DescriptionValue value : getValues())
		{
			value.clear();
		}
	}
}
