package com.deepercreeper.vampireapp.util;

import com.deepercreeper.vampireapp.util.view.Expander;
import com.deepercreeper.vampireapp.util.view.ResizeHeightAnimation;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;

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
	 * Hides the given view by setting the height to {@code 0}.
	 * 
	 * @param aView
	 *            The view.
	 */
	public static void hideHeight(final View aView)
	{
		setHeight(aView, 0);
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
	 * Sets the height of the given view to {@link LayoutParams#MATCH_PARENT}.
	 * 
	 * @param aView
	 *            The view to match in height.
	 */
	public static void matchHeight(View aView)
	{
		aView.getLayoutParams().height = LayoutParams.MATCH_PARENT;
	}
	
	/**
	 * Sets the width of the given view to {@link LayoutParams#MATCH_PARENT}.
	 * 
	 * @param aView
	 *            The view to match in width.
	 */
	public static void matchWidth(View aView)
	{
		aView.getLayoutParams().width = LayoutParams.MATCH_PARENT;
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
	 * Sets the width of the given view to the given value calculated into pixels.
	 * 
	 * @param aView
	 *            The view.
	 * @param aHeight
	 *            The new width.
	 */
	public static void setHeight(final View aView, final int aHeight)
	{
		aView.getLayoutParams().height = calcPx(aHeight, aView.getContext());
	}
	
	/**
	 * Sets the width of the given view to the given value.
	 * 
	 * @param aView
	 *            The view.
	 * @param aHeight
	 *            The new height.
	 */
	public static void setPxHeight(final View aView, final int aHeight)
	{
		aView.getLayoutParams().width = aHeight;
	}
	
	/**
	 * Sets the width of the given view to the given value.
	 * 
	 * @param aView
	 *            The view.
	 * @param aWidth
	 *            The new width.
	 */
	public static void setPxWidth(final View aView, final int aWidth)
	{
		aView.getLayoutParams().width = aWidth;
	}
	
	/**
	 * Sets the width of the given view to the given value calculated into pixels.
	 * 
	 * @param aView
	 *            The view.
	 * @param aWidth
	 *            The new width.
	 */
	public static void setWidth(final View aView, final int aWidth)
	{
		aView.getLayoutParams().width = calcPx(aWidth, aView.getContext());
	}
	
	/**
	 * Sets the height of the given view to {@link LayoutParams#WRAP_CONTENT}.
	 * 
	 * @param aView
	 *            The view to wrap in height.
	 */
	public static void wrapHeight(View aView)
	{
		aView.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
	}
	
	/**
	 * Sets the width of the given view to {@link LayoutParams#WRAP_CONTENT}.
	 * 
	 * @param aView
	 *            The view to wrap in width.
	 */
	public static void wrapWidth(View aView)
	{
		aView.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
	}
}
