package com.deepercreeper.vampireapp.controllers;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import com.deepercreeper.vampireapp.character.CharacterCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.implementations.creations.restrictions.CreationRestrictionableImpl;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A controller for the character generation.
 * 
 * @author vrl
 */
public class GenerationControllerCreation extends CreationRestrictionableImpl
{
	private final NumberPicker	mPicker;
	
	/**
	 * Creates a new generation controller.
	 * 
	 * @param aContext
	 *            The context.
	 * @param aCreator
	 *            The character creator.
	 */
	public GenerationControllerCreation(final Context aContext, final CharacterCreation aCreator)
	{
		mPicker = new NumberPicker(aContext);
		mPicker.setLayoutParams(ViewUtil.getWrapHeight());
	}
	
	/**
	 * @return the current generation.
	 */
	public int getGeneration()
	{
		return mPicker.getValue();
	}
	
	/**
	 * Adds the generation picker to the given layout.
	 * 
	 * @param aLayout
	 *            The layout.
	 */
	public void init(final LinearLayout aLayout, final boolean aFreeMode)
	{
		aLayout.removeAllViews();
		if (aFreeMode)
		{
			mPicker.setMinValue(1);
			mPicker.setMaxValue(13);
			mPicker.setValue(10);
		}
		else
		{
			mPicker.setMinValue(CharacterCreation.MIN_GENERATION);
			mPicker.setMaxValue(CharacterCreation.MAX_GENERATION);
			mPicker.setValue(CharacterCreation.MIN_GENERATION);
		}
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
		mPicker.setMinValue(CharacterCreation.MIN_GENERATION);
		mPicker.setMaxValue(CharacterCreation.MAX_GENERATION);
		if ( !getRestrictions(CreationRestrictionType.GENERATION).isEmpty())
		{
			mPicker.setMinValue(getMinValue(CreationRestrictionType.GENERATION));
			mPicker.setMaxValue(getMaxValue(CreationRestrictionType.GENERATION));
		}
	}
}
