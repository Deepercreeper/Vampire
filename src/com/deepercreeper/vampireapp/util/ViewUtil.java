package com.deepercreeper.vampireapp.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * A helper class for view operations. It also contains all layout parameters.
 * 
 * @author Vincent
 */
public class ViewUtil
{
	private static Context	sContext;
	
	private static int		sId	= 1;
	
	public static void setContext(final Context aContext)
	{
		sContext = aContext;
	}
	
	public static void generateId(final View aView)
	{
		aView.setId(sId++ );
	}
	
	/**
	 * @return layout parameters for buttons.
	 */
	public static LinearLayout.LayoutParams getButtonSize()
	{
		return new LinearLayout.LayoutParams(calcPx(30), calcPx(30));
	}
	
	/**
	 * @return layout parameters for relative buttons.
	 */
	public static RelativeLayout.LayoutParams getRelativeButtonSize()
	{
		return new RelativeLayout.LayoutParams(calcPx(30), calcPx(30));
	}
	
	/**
	 * @return layout parameters for short name text views.
	 */
	public static LinearLayout.LayoutParams getNameSizeShort()
	{
		return new LinearLayout.LayoutParams(calcPx(82), calcPx(30));
	}
	
	/**
	 * @return layout parameters for number text views.
	 */
	public static LinearLayout.LayoutParams getNumberSize()
	{
		return new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, calcPx(30));
	}
	
	/**
	 * @return layout parameters for table row buttons.
	 */
	public static TableRow.LayoutParams getRowButtonSize()
	{
		return new TableRow.LayoutParams(calcPx(30), calcPx(30));
	}
	
	public static RelativeLayout.LayoutParams getRelativeValueBarSize(final int aWidth)
	{
		return new RelativeLayout.LayoutParams(calcPx(aWidth), calcPx(30));
	}
	
	public static RelativeLayout.LayoutParams getRelativeValueTextSize()
	{
		return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, calcPx(30));
	}
	
	/**
	 * @return layout parameters for long table row name text views.
	 */
	public static RelativeLayout.LayoutParams getRelativeNameLong()
	{
		return new RelativeLayout.LayoutParams(calcPx(80), calcPx(30));
	}
	
	/**
	 * @return layout parameters for long table row name text views.
	 */
	public static TableRow.LayoutParams getRowNameVeryLong()
	{
		return new TableRow.LayoutParams(calcPx(300), LayoutParams.MATCH_PARENT);
	}
	
	/**
	 * @return layout parameters for short table row name text views.
	 */
	public static TableRow.LayoutParams getRowNameShort()
	{
		return new TableRow.LayoutParams(calcPx(82), LayoutParams.MATCH_PARENT);
	}
	
	/**
	 * @return layout parameters for table row text views.
	 */
	public static TableRow.LayoutParams getRowTextSize()
	{
		return new TableRow.LayoutParams(calcPx(247), LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * @return layout parameters for table row wrap all views.
	 */
	public static TableRow.LayoutParams getRowWrapAll()
	{
		return new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * @return layout parameters for table row match all views.
	 */
	public static TableRow.LayoutParams getRowMatchAll()
	{
		return new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	
	/**
	 * @return layout parameters for table wrap all views.
	 */
	public static TableLayout.LayoutParams getTableWrapAll()
	{
		return new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * @return layout parameters for table wrap height views.
	 */
	public static TableLayout.LayoutParams getTableWrapHeight()
	{
		return new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * @return layout parameters for value views.
	 */
	public static LinearLayout.LayoutParams getValueSize()
	{
		return new LinearLayout.LayoutParams(calcPx(25), LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * @return layout parameters for wrap all views.
	 */
	public static LinearLayout.LayoutParams getWrapAll()
	{
		return new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
	 * Applies the given value to the array of radio buttons to display it.
	 * 
	 * @param aValue
	 *            The value do display.
	 * @param aValueDisplay
	 *            The radio button array.
	 */
	public static void applyValue(final int aValue, final RadioButton[] aValueDisplay)
	{
		for (int i = 0; i < aValueDisplay.length; i++ )
		{
			aValueDisplay[i].setChecked(i < aValue);
		}
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
	 * @return the number of pixels that corresponds to the given value.
	 */
	public static int calcPx(final int aDp)
	{
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, aDp, sContext.getResources().getDisplayMetrics()));
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
