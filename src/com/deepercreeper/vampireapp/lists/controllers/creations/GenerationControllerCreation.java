package com.deepercreeper.vampireapp.lists.controllers.creations;

import android.widget.NumberPicker;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.CreationRestrictionableImpl;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A controller for the character generation.
 * 
 * @author vrl
 */
public class GenerationControllerCreation extends CreationRestrictionableImpl
{
	private NumberPicker	mPicker;
	
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
	 * @param aPicker
	 *            The picker.
	 * @param aFreeMode
	 *            Whether the generation can be chosen in a free interval.
	 */
	public void init(final NumberPicker aPicker, final boolean aFreeMode)
	{
		mPicker = aPicker;
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
