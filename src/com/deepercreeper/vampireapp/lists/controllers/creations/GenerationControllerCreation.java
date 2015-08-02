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
	
	public GenerationControllerCreation(Context aContext, boolean aFreeMode)
	{
		mContext = aContext;
		mFreeMode = aFreeMode;
		mContainer = View.inflate(mContext, R.layout.view_generation_controller_creation, null);
		
		init();
	}
	
	@Override
	public void init()
	{
		mPicker = getContainer().findViewById(R.id.view_generation_picker);
		update();
	}
	
	/**
	 * Sets whether this generation controller is inside free mode.
	 * 
	 * @param aFreeMode
	 *            Whether free mode is active.
	 */
	public void setFreeMode(boolean aFreeMode)
	{
		mFreeMode = aFreeMode;
		update();
	}
	
	/**
	 * Updates this controller.
	 */
	public void update()
	{
		if (mFreeMode)
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
		mPicker.setMinValue(CharacterCreation.MIN_GENERATION);
		mPicker.setMaxValue(CharacterCreation.MAX_GENERATION);
		if ( !getRestrictions(CreationRestrictionType.GENERATION).isEmpty())
		{
			mPicker.setMinValue(getMinValue(CreationRestrictionType.GENERATION));
			mPicker.setMaxValue(getMaxValue(CreationRestrictionType.GENERATION));
		}
	}
}
