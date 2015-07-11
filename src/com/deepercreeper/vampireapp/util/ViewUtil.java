package com.deepercreeper.vampireapp.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.util.view.Expander;
import com.deepercreeper.vampireapp.util.view.ResizeHeightAnimation;

/**
 * A helper class for view operations. It also contains all layout parameters.
 * 
 * @author Vincent
 */
public class ViewUtil
{
	private ViewUtil()
	{}
	
	/**
	 * Calculates the fetched height of the given layout in pixels.
	 * 
	 * @param aView
	 *            The layout to measure.
	 * @return the height of the given layout.
	 */
	public static int calcHeight(final View aView)
	{
		// TODO Remove all unnecessary gravity attributes
		final LayoutParams params = aView.getLayoutParams();
		final int height = params.height;
		params.height = LayoutParams.WRAP_CONTENT;
		aView.measure(0, 0);
		final int measuredHeight = aView.getMeasuredHeight();
		params.height = height;
		return measuredHeight;
	}
	
	/**
	 * Calculates the number of pixels corresponding to the given device independent pixels for the current device.
	 * 
	 * @param aDp
	 *            The number of device independent pixels to transform.
	 * @param aContext
	 *            The underlying context.
	 * @return the number of pixels that corresponds to the given value.
	 */
	public static int calcPx(final int aDp, final Context aContext)
	{
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, aDp, aContext.getResources().getDisplayMetrics()));
	}
	
	/**
	 * Calculates the fetched width of the given layout in pixels.
	 * 
	 * @param aLayout
	 *            The layout to measure.
	 * @return the height of the given layout.
	 */
	public static int calcWidth(final ViewGroup aLayout)
	{
		final LayoutParams params = aLayout.getLayoutParams();
		final int width = params.width;
		params.width = LayoutParams.WRAP_CONTENT;
		aLayout.measure(0, 0);
		final int measuredWidth = aLayout.getMeasuredWidth();
		params.width = width;
		return measuredWidth;
	}
	
	/**
	 * @return layout parameters for wrap height views.
	 */
	public static LinearLayout.LayoutParams getWrapHeight()
	{
		// TODO Get away from programmatically created views
		return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * Hides the given view by setting the width to {@code 0}.
	 * 
	 * @param aView
	 *            The view.
	 */
	public static void hideWidth(final View aView)
	{
		setWidth(aView, 0);
	}
	
	/**
	 * Removes a view from its parent view. Optionally removes all child views.
	 * 
	 * @param aView
	 *            The view to release.
	 */
	public static void release(final View aView)
	{
		if (aView == null)
		{
			return;
		}
		final ViewParent parent = aView.getParent();
		if (parent instanceof ViewGroup)
		{
			((ViewGroup) parent).removeView(aView);
		}
	}
	
	/**
	 * Resizes the given expander by resizing all expander parents at once.
	 * 
	 * @param aExpander
	 *            The resized expander.
	 */
	public static void resizeRecursive(final Expander aExpander)
	{
		int height = 0;
		if (aExpander.isOpen())
		{
			height = calcHeight(aExpander.getContainer());
		}
		aExpander.getContainer().startAnimation(new ResizeHeightAnimation(aExpander.getContainer(), height));
		Expander expander = aExpander.getParent();
		while (expander != null)
		{
			if ( !expander.isInitialized())
			{
				return;
			}
			if (expander.isOpen())
			{
				expander.getContainer().getLayoutParams().height = LayoutParams.WRAP_CONTENT;
			}
			expander = expander.getParent();
		}
	}
	
	/**
	 * Enables or disables the given image button and sets its opacity to a corresponding value.
	 * 
	 * @param aView
	 *            The image button.
	 * @param aEnabled
	 *            Whether the button should be enabled.
	 */
	public static void setEnabled(final View aView, final boolean aEnabled)
	{
		if (aView == null)
		{
			return;
		}
		aView.setEnabled(aEnabled);
		aView.setAlpha(aEnabled ? 1f : 0.4f);
	}
	
	/**
	 * Sets the width of the given view to the given value.
	 * 
	 * @param aView
	 *            The view.
	 * @param aWidth
	 *            The new width.
	 */
	public static void setWidth(final View aView, final int aWidth)
	{
		final LayoutParams params = aView.getLayoutParams();
		params.width = calcPx(aWidth, aView.getContext());
	}
}
