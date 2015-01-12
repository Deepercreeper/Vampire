package com.deepercreeper.vampireapp.controllers;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import com.deepercreeper.vampireapp.controllers.restrictions.Restriction.RestrictionType;
import com.deepercreeper.vampireapp.controllers.restrictions.RestrictionableImpl;
import com.deepercreeper.vampireapp.creation.CharCreator;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A controller for the character generation.
 * 
 * @author vrl
 */
public class GenerationCreationValueController extends RestrictionableImpl
{
	private final NumberPicker	mPicker;
	
	private int					mGeneration;
	
	/**
	 * Creates a new generation controller.
	 * 
	 * @param aContext
	 *            The context.
	 * @param aCreator
	 *            The character creator.
	 */
	public GenerationCreationValueController(final Context aContext, final CharCreator aCreator)
	{
		mPicker = new NumberPicker(aContext);
		mPicker.setLayoutParams(ViewUtil.getWrapHeight());
		mPicker.setMinValue(CharCreator.MIN_GENERATION);
		mPicker.setMaxValue(CharCreator.MAX_GENERATION);
		mPicker.setOnValueChangedListener(new OnValueChangeListener()
		{
			@Override
			public void onValueChange(final NumberPicker aPicker, final int aOldVal, final int aNewVal)
			{
				mGeneration = aNewVal;
			}
		});
	}
	
	/**
	 * @return the current generation.
	 */
	public int getGeneration()
	{
		return mGeneration;
	}
	
	/**
	 * Adds the generation picker to the given layout.
	 * 
	 * @param aLayout
	 *            The layout.
	 */
	public void init(final LinearLayout aLayout)
	{
		aLayout.removeAllViews();
		aLayout.addView(mPicker);
	}
	
	/**
	 * Releases the picker.
	 */
	public void release()
	{
		ViewUtil.release(mPicker);
	}
	
	@Override
	public void updateRestrictions()
	{
		mPicker.setMinValue(CharCreator.MIN_GENERATION);
		mPicker.setMaxValue(CharCreator.MAX_GENERATION);
		if ( !getRestrictions(RestrictionType.GENERATION).isEmpty())
		{
			mPicker.setMinValue(getMinValue(RestrictionType.GENERATION));
			mPicker.setMaxValue(getMaxValue(RestrictionType.GENERATION));
		}
	}
}
