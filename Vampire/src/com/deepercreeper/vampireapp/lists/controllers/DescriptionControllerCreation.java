package com.deepercreeper.vampireapp.lists.controllers;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.lists.items.Description;
import com.deepercreeper.vampireapp.lists.items.DescriptionCreation;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * This controller manages the description values, defined by a user for each character.
 * 
 * @author vrl
 */
public class DescriptionControllerCreation implements Viewable
{
	private final LinearLayout mContainer;
	
	private final Context mContext;
	
	private final List<DescriptionCreation> mDescriptions = new ArrayList<DescriptionCreation>();
	
	private final DescriptionController mController;
	
	/**
	 * Creates a new description value controller.
	 * 
	 * @param aController
	 *            The description controller.
	 * @param aContext
	 *            The underlying context.
	 */
	public DescriptionControllerCreation(final DescriptionController aController, final Context aContext)
	{
		mController = aController;
		mContext = aContext;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_description_controller, null);
		
		for (final Description description : mController.getDescriptionsList())
		{
			addDescription(new DescriptionCreation(description, mContext));
		}
	}
	
	/**
	 * Adds the given description to this controller.
	 * 
	 * @param aDescription
	 *            The description.
	 */
	public void addDescription(final DescriptionCreation aDescription)
	{
		mDescriptions.add(aDescription);
		mContainer.addView(aDescription.getContainer());
	}
	
	@Override
	public View getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return a list of all description creations.
	 */
	public List<DescriptionCreation> getDescriptionsList()
	{
		return mDescriptions;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void updateUI()
	{}
	
	/**
	 * Removes all descriptions from this controller.
	 */
	public void clear()
	{
		for (final DescriptionCreation description : mDescriptions)
		{
			description.release();
		}
		mDescriptions.clear();
	}
	
	/**
	 * Resets all user defined descriptions.
	 */
	public void resetValues()
	{
		for (final DescriptionCreation value : mDescriptions)
		{
			value.clear();
		}
	}
}
