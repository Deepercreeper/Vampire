package com.deepercreeper.vampireapp.lists.controllers.creations;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.CreationRestrictionableImpl;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

/**
 * A controller for the character generation.
 * 
 * @author vrl
 */
public class GenerationControllerCreation extends CreationRestrictionableImpl implements Viewable
{
	private final LinearLayout mContainer;
	
	private final Context mContext;
	
	private NumberPicker mPicker;
	
	private boolean mFreeMode;
	
	/**
	 * Creates a new generation controller.
	 * 
	 * @param aContext
	 *            The underlying context.
	 * @param aFreeMode
	 *            Whether this controller is started in free mode.
	 */
	public GenerationControllerCreation(final Context aContext, final boolean aFreeMode)
	{
		mContext = aContext;
		mFreeMode = aFreeMode;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_generation_controller_creation, null);
		
		init();
	}
	
	@Override
	public void init()
	{
		mPicker = (NumberPicker) getContainer().findViewById(R.id.view_generation_picker);
		mPicker.setValue(mFreeMode ? 10 : CharacterCreation.MIN_GENERATION);
		updateRestrictions();
	}
	
	/**
	 * Sets whether this generation controller is inside free mode.
	 * 
	 * @param aFreeMode
	 *            Whether free mode is active.
	 */
	public void setFreeMode(final boolean aFreeMode)
	{
		mFreeMode = aFreeMode;
		updateRestrictions();
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
	 * Releases the picker.
	 */
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void updateRestrictions()
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
