package com.deepercreeper.vampireapp.character.controllers;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * This controller is used to control the experience a character collects.<br>
 * It also displays the current amount of experience and allows modifying and reading it.
 * 
 * @author vrl
 */
public class EPController implements Viewable
{
	private int						mEP;
	
	private final RelativeLayout	mContainer;
	
	private final TextView			mEPLabel;
	
	/**
	 * Creates a new experience controller with no experience from start.
	 * 
	 * @param aContext
	 *            The underlying context.
	 */
	public EPController(final Context aContext)
	{
		mEP = 0;
		mContainer = (RelativeLayout) View.inflate(aContext, R.layout.ep, null);
		mEPLabel = (TextView) getContainer().findViewById(R.id.ep);
		init();
	}
	
	/**
	 * Creates a new experience controller with the given experience level.
	 * 
	 * @param aEP
	 *            The experience level.
	 * @param aContext
	 *            The underlying context.
	 */
	public EPController(final int aEP, final Context aContext)
	{
		mEP = aEP;
		mContainer = (RelativeLayout) View.inflate(aContext, R.layout.ep, null);
		mEPLabel = (TextView) getContainer().findViewById(R.id.ep);
		init();
	}
	
	/**
	 * Decreases the current amount of experience by the given number.
	 * 
	 * @param aValue
	 *            The value to subtract.
	 */
	public void decreaseBy(final int aValue)
	{
		mEP -= aValue;
		if (mEP < 0)
		{
			mEP = 0;
		}
		init();
	}
	
	/**
	 * @return the view container for the experience display.
	 */
	public RelativeLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the current amount of experience.
	 */
	public int getExperience()
	{
		return mEP;
	}
	
	/**
	 * Increases the current amount of experience by the given number.
	 * 
	 * @param aValue
	 *            The value to add.
	 */
	public void increaseBy(final int aValue)
	{
		mEP += aValue;
		init();
	}
	
	@Override
	public void init()
	{
		mEPLabel.setText("" + mEP);
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
}
