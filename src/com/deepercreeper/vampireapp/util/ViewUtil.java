package com.deepercreeper.vampireapp.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

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
	 * @param aContext
	 *            The underlying context.
	 * @return layout parameters for a small line.
	 */
	public static LinearLayout.LayoutParams getLine(final Context aContext)
	{
		return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, calcPx(1, aContext));
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return layout parameters for table row buttons.
	 */
	public static TableRow.LayoutParams getRowButtonSize(final Context aContext)
	{
		return new TableRow.LayoutParams(calcPx(30, aContext), calcPx(30, aContext));
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return layout parameters for long table row name text views.
	 */
	public static TableRow.LayoutParams getRowNameVeryLong(final Context aContext)
	{
		return new TableRow.LayoutParams(calcPx(300, aContext), LayoutParams.MATCH_PARENT);
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return layout parameters for short table row name text views.
	 */
	public static TableRow.LayoutParams getRowNameShort(final Context aContext)
	{
		return new TableRow.LayoutParams(calcPx(82, aContext), LayoutParams.MATCH_PARENT);
	}
	
	/**
	 * @param aContext
	 *            The underlying context.
	 * @return layout parameters for table row text views.
	 */
	public static TableRow.LayoutParams getRowTextSize(final Context aContext)
	{
		return new TableRow.LayoutParams(calcPx(247, aContext), LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * @return layout parameters for table wrap height views.
	 */
	public static TableLayout.LayoutParams getTableWrapHeight()
	{
		return new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * @return layout parameters for wrap height views.
	 */
	public static LinearLayout.LayoutParams getWrapHeight()
	{
		return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * @return layout parameters for zero height views.
	 */
	public static LinearLayout.LayoutParams getZeroHeight()
	{
		return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
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
	 * Calculates the fetched height of the given layout in pixels.
	 * 
	 * @param aLayout
	 *            The layout to measure.
	 * @return the height of the given layout.
	 */
	public static int calcHeight(final ViewGroup aLayout)
	{
		final LayoutParams params = aLayout.getLayoutParams();
		final int height = params.height;
		params.height = LayoutParams.WRAP_CONTENT;
		aLayout.measure(0, 0);
		final int measuredHeight = aLayout.getMeasuredHeight();
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
	 * Enables or disables the given image button and sets its opacity to a corresponding value.
	 * 
	 * @param aButton
	 *            The image button.
	 * @param aEnabled
	 *            Whether the button should be enabled.
	 */
	public static void setEnabled(final ImageButton aButton, final boolean aEnabled)
	{
		if (aButton == null)
		{
			return;
		}
		aButton.setEnabled(aEnabled);
		aButton.setAlpha(aEnabled ? 1f : 0.4f);
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
}
