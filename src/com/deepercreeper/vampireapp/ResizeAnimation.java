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
	
	public ResizeAnimation(View aV, float aFromWidth, float aFromHeight, float aToWidth, float aToHeight)
	{
		mToHeight = aToHeight;
		mToWidth = aToWidth;
		mFromHeight = aFromHeight;
		mFromWidth = aFromWidth;
		mView = aV;
		setDuration(100);
	}
	
	@Override
	protected void applyTransformation(float aInterpolatedTime, Transformation aTransformation)
	{
		float height = (mToHeight - mFromHeight) * aInterpolatedTime + mFromHeight;
		float width = (mToWidth - mFromWidth) * aInterpolatedTime + mFromWidth;
		LayoutParams p = mView.getLayoutParams();
		p.height = (int) height;
		p.width = (int) width;
		mView.requestLayout();
	}
	
	@Override
	public void initialize(int aWidth, int aHeight, int aParentWidth, int aParentHeight)
	{
		super.initialize(aWidth, aHeight, aParentWidth, aParentHeight);
	}
	
	@Override
	public boolean willChangeBounds()
	{
		return true;
	}
}
