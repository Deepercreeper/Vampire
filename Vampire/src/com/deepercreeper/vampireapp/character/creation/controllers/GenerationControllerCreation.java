package com.deepercreeper.vampireapp.character.creation.controllers;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.RestrictionableCreationImpl;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.ViewUtil;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

/**
 * A controller for the character generation.
 * 
 * @author vrl
 */
public class GenerationControllerCreation extends RestrictionableCreationImpl
{
	private static final int MAX_LEVEL_POINTS = 7;
	
	private final LinearLayout mContainer;
	
	private final Context mContext;
	
	private final CharacterCreation mChar;
	
	private final NumberPicker mPicker;
	
	private final boolean mFreeMode;
	
	/**
	 * Creates a new generation controller.
	 * 
	 * @param aContext
	 *            The underlying context.
	 * @param aChar
	 *            The parent character.
	 * @param aFreeMode
	 *            Whether this controller is started in free mode.
	 */
	public GenerationControllerCreation(final Context aContext, final CharacterCreation aChar, final boolean aFreeMode)
	{
		mContext = aContext;
		mChar = aChar;
		mFreeMode = aFreeMode;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_generation_controller_creation, null);
		
		mPicker = (NumberPicker) getContainer().findViewById(R.id.view_generation_picker);
		mPicker.setValue(mFreeMode ? 10 : CharacterCreation.MIN_GENERATION);
		mPicker.setOnValueChangedListener(new OnValueChangeListener()
		{
			@Override
			public void onValueChange(final NumberPicker aPicker, final int aOldVal, final int aNewVal)
			{
				mChar.updateUI();
			}
		});
		updateUI();
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the current generation.
	 */
	public int getGeneration()
	{
		return mPicker.getValue();
	}
	
	/**
	 * @return whether the character is a low level character.
	 */
	public boolean isLowLevel()
	{
		return getGeneration() > MAX_LEVEL_POINTS;
	}
	
	/**
	 * Releases the picker.
	 */
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void updateUI()
	{
		if (mFreeMode)
		{
			mPicker.setMinValue(1);
			mPicker.setMaxValue(13);
		}
		else
		{
			mPicker.setMinValue(CharacterCreation.MIN_GENERATION);
			mPicker.setMaxValue(CharacterCreation.MAX_GENERATION);
		}
		if (hasRestrictions(CreationRestrictionType.GENERATION))
		{
			mPicker.setMinValue(getMinValue(CreationRestrictionType.GENERATION));
			mPicker.setMaxValue(getMaxValue(CreationRestrictionType.GENERATION));
		}
	}
}
