package com.deepercreeper.vampireapp.character.creation.controllers;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The insanity creation.
 * 
 * @author vrl
 */
public class InsanityCreation extends Named implements Viewable
{
	private final LinearLayout mContainer;
	
	private final InsanityControllerCreation mController;
	
	/**
	 * Creates a new insanity creation.
	 * 
	 * @param aName
	 *            The insanity name.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The insanity controller.
	 */
	public InsanityCreation(String aName, Context aContext, InsanityControllerCreation aController)
	{
		super(aName);
		mController = aController;
		
		mContainer = (LinearLayout) View.inflate(aContext, R.layout.view_insanity, null);
		getContainer().findViewById(R.id.view_remove_insanity_button).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mController.remove(InsanityCreation.this);
			}
		});
		((TextView) getContainer().findViewById(R.id.view_insanity_label)).setText(getName());
	}
	
	@Override
	public void updateUI()
	{}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public String getDisplayName()
	{
		return getName();
	}
}
