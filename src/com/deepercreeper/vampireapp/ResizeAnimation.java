package com.deepercreeper.vampireapp;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * An animation used to resize layouts along the y-axis.
 * 
 * @author Vincent
 */
public class ResizeAnimation extends Animation
{
	private static final int	DURATION	= 300;
	
	private final View			mView;
	
	private final float			mDestinationHeight;
	
	private final float			mStartHeight;
	
	private final float			mDestinationWidth;
	
	private final float			mStartWidth;
	
	/**
	 * Creates a new animation for the given view.
	 * 
	 * @param aView
	 *            The view to resize.
	 * @param aDestinationWidth
	 *            The width that should be reached.
	 * @param aDestinationHeight
	 *            The height that should be reached.
	 */
	public ResizeAnimation(final View aView, final float aDestinationWidth, final float aDestinationHeight)
	{
		mDestinationHeight = aDestinationHeight;
		mDestinationWidth = aDestinationWidth;
		mStartHeight = aView.getHeight();
		mStartWidth = aView.getWidth();
		mView = aView;
		setDuration(DURATION);
	}
	
	@Override
	public boolean willChangeBounds()
	{
		return true;
	}
	
	@Override
	protected void applyTransformation(final float aInterpolatedTime, final Transformation aTransformation)
	{
		final LayoutParams p = mView.getLayoutParams();
		p.height = (int) (mStartHeight + (mDestinationHeight - mStartHeight) * aInterpolatedTime);
		p.width = (int) (mStartWidth + (mDestinationWidth - mStartWidth) * aInterpolatedTime);
		mView.requestLayout();
	}
}
