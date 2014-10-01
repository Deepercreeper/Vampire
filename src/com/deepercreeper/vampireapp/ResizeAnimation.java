package com.deepercreeper.vampireapp;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation
{
	private final View	mView;
	private final float	mToHeight;
	private final float	mFromHeight;
	
	private final float	mToWidth;
	private final float	mFromWidth;
	
	public ResizeAnimation(final View aV, final float aToWidth, final float aToHeight)
	{
		mToHeight = aToHeight;
		mToWidth = aToWidth;
		mFromHeight = aV.getHeight();
		mFromWidth = aV.getWidth();
		mView = aV;
		setDuration(100);
	}
	
	@Override
	protected void applyTransformation(final float aInterpolatedTime, final Transformation aTransformation)
	{
		final float height = mFromHeight + (mToHeight - mFromHeight) * aInterpolatedTime;
		final float width = mFromWidth + (mToWidth - mFromWidth) * aInterpolatedTime;
		final LayoutParams p = mView.getLayoutParams();
		p.height = (int) height;
		p.width = (int) width;
		mView.requestLayout();
	}
	
	@Override
	public boolean willChangeBounds()
	{
		return true;
	}
}
