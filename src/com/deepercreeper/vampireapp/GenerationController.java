package com.deepercreeper.vampireapp;

import java.util.HashSet;
import java.util.Set;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import com.deepercreeper.vampireapp.controller.Restriction;
import com.deepercreeper.vampireapp.controller.Restrictionable;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class GenerationController implements Restrictionable
{
	private final NumberPicker		mPicker;
	
	private final Set<Restriction>	mRestrictions	= new HashSet<Restriction>();
	
	private int						mGeneration;
	
	public GenerationController(final Context aContext, final CharCreator aCreator)
	{
		mPicker = new NumberPicker(aContext);
		mPicker.setLayoutParams(ViewUtil.instance().getWrapHeight());
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
	
	public int getGeneration()
	{
		return mGeneration;
	}
	
	public void init(final LinearLayout aLayout)
	{
		aLayout.removeAllViews();
		aLayout.addView(mPicker);
	}
	
	public void release()
	{
		ViewUtil.release(mPicker, false);
	}
	
	@Override
	public void addRestriction(final Restriction aRestriction)
	{
		mRestrictions.add(aRestriction);
		aRestriction.addParent(this);
		mPicker.setMinValue(getMinValue());
		mPicker.setMaxValue(getMaxValue());
	}
	
	@Override
	public int getMinValue()
	{
		int minValue = CharCreator.MIN_GENERATION;
		for (final Restriction restriction : mRestrictions)
		{
			if (restriction.getMinValue() > minValue)
			{
				minValue = restriction.getMinValue();
			}
		}
		return minValue;
	}
	
	@Override
	public int getMaxValue()
	{
		int maxValue = CharCreator.MAX_GENERATION;
		for (final Restriction restriction : mRestrictions)
		{
			if (restriction.getMaxValue() < maxValue || restriction.getMaxValue() > CharCreator.MAX_GENERATION)
			{
				maxValue = restriction.getMaxValue();
			}
		}
		return maxValue;
	}
	
	@Override
	public Set<Restriction> getRestrictions()
	{
		return mRestrictions;
	}
	
	@Override
	public boolean hasRestrictions()
	{
		return !mRestrictions.isEmpty();
	};
	
	@Override
	public void removeRestriction(final Restriction aRestriction)
	{
		mRestrictions.remove(aRestriction);
		mPicker.setMinValue(getMinValue());
		mPicker.setMaxValue(getMaxValue());
	};
}
