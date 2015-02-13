package com.deepercreeper.vampireapp.util.view;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * An animation used to resize layouts along the y-axis.
 * 
 * @author Vincent
 */
public class ResizeHeightAnimation extends Animation
{
	private static final int	DURATION	= 300;
	
	private final View			mView;
	
	private final float			mDestinationHeight;
	
	private final float			mStartHeight;
	
	/**
	 * Creates a new animation for the given view.
	 * 
	 * @param aView
	 *            The view to resize.
	 * @param aDestinationHeight
	 *            The height that should be reached.
	 */
	public ResizeHeightAnimation(final View aView, final float aDestinationHeight)
	{
		mDestinationHeight = aDestinationHeight;
		mStartHeight = aView.getHeight();
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
		mView.requestLayout();
	}
}
