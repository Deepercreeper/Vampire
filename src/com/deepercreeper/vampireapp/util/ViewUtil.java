package com.deepercreeper.vampireapp.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * A helper class for view operations. It also contains all layout parameters.
 * 
 * @author Vincent
 */
public class ViewUtil
{
	private static ViewUtil					INSTANCE;
	
	private final TableLayout.LayoutParams	mTableWrapAll		= new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	private final TableLayout.LayoutParams	mTableWrapHeight	= new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	
	private final TableRow.LayoutParams		mRowWrapAll			= new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	private final TableRow.LayoutParams		mRowTextSize;
	
	private final TableRow.LayoutParams		mRowNameShort;
	
	private final TableRow.LayoutParams		mRowNameLong;
	
	private final TableRow.LayoutParams		mRowButtonSize;
	
	private final LayoutParams				mWrapHeight			= new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	
	private final LayoutParams				mZeroHeight			= new LayoutParams(LayoutParams.MATCH_PARENT, 0);
	
	private final LayoutParams				mButtonSize;
	
	private final LayoutParams				mValueSize;
	
	private final LayoutParams				mNameSizeShort;
	
	private final LayoutParams				mNumberSize;
	
	private final LayoutParams				mWrapAll			= new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	private ViewUtil(final Context aContext)
	{
		mRowNameShort = new TableRow.LayoutParams(calcPx(82, aContext), LayoutParams.MATCH_PARENT);
		mRowNameLong = new TableRow.LayoutParams(calcPx(120, aContext), LayoutParams.MATCH_PARENT);
		mButtonSize = new LayoutParams(calcPx(30, aContext), calcPx(30, aContext));
		mRowButtonSize = new TableRow.LayoutParams(calcPx(30, aContext), calcPx(30, aContext));
		mValueSize = new LayoutParams(calcPx(25, aContext), LayoutParams.WRAP_CONTENT);
		mNumberSize = new LayoutParams(LayoutParams.WRAP_CONTENT, calcPx(30, aContext));
		mNameSizeShort = new LayoutParams(calcPx(82, aContext), calcPx(30, aContext));
		mRowTextSize = new TableRow.LayoutParams(calcPx(247, aContext), LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * @return layout parameters for buttons.
	 */
	public LayoutParams getButtonSize()
	{
		return mButtonSize;
	}
	
	/**
	 * @return layout parameters for short name text views.
	 */
	public LayoutParams getNameSizeShort()
	{
		return mNameSizeShort;
	}
	
	/**
	 * @return layout parameters for number text views.
	 */
	public LayoutParams getNumberSize()
	{
		return mNumberSize;
	}
	
	/**
	 * @return layout parameters for table row buttons.
	 */
	public TableRow.LayoutParams getRowButtonSize()
	{
		return mRowButtonSize;
	}
	
	/**
	 * @return layout parameters for long table row name text views.
	 */
	public TableRow.LayoutParams getRowNameLong()
	{
		return mRowNameLong;
	}
	
	/**
	 * @return layout parameters for short table row name text views.
	 */
	public TableRow.LayoutParams getRowNameShort()
	{
		return mRowNameShort;
	}
	
	/**
	 * @return layout parameters for table row wrap all views.
	 */
	public TableRow.LayoutParams getRowWrapAll()
	{
		return mRowWrapAll;
	}
	
	/**
	 * @return layout parameters for table row text views.
	 */
	public TableRow.LayoutParams getRowTextSize()
	{
		return mRowTextSize;
	}
	
	/**
	 * @return layout parameters for table wrap all views.
	 */
	public TableLayout.LayoutParams getTableWrapAll()
	{
		return mTableWrapAll;
	}
	
	/**
	 * @return layout parameters for table wrap height views.
	 */
	public TableLayout.LayoutParams getTableWrapHeight()
	{
		return mTableWrapHeight;
	}
	
	/**
	 * @return layout parameters for value views.
	 */
	public LayoutParams getValueSize()
	{
		return mValueSize;
	}
	
	/**
	 * @return layout parameters for wrap all views.
	 */
	public LayoutParams getWrapAll()
	{
		return mWrapAll;
	}
	
	/**
	 * @return layout parameters for wrap height views.
	 */
	public LayoutParams getWrapHeight()
	{
		return mWrapHeight;
	}
	
	/**
	 * @return layout parameters for zero height views.
	 */
	public LayoutParams getZeroHeight()
	{
		return mZeroHeight;
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
	 * Calculates the fetched height of the given layout in pixels.
	 * 
	 * @param aLayout
	 *            The layout to measure.
	 * @return the height of the given layout.
	 */
	public static int calcHeight(final ViewGroup aLayout)
	{
		aLayout.measure(0, 0);
		return aLayout.getMeasuredHeight();
	}
	
	/**
	 * Calculates the number of pixels corresponding to the given device independent pixels for the current device.
	 * 
	 * @param aDp
	 *            The number of device independent pixels to transform.
	 * @param aContext
	 *            The context.
	 * @return the number of pixels that corresponds to the given value.
	 */
	public static int calcPx(final int aDp, final Context aContext)
	{
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, aDp, aContext.getResources().getDisplayMetrics()));
	}
	
	/**
	 * Initializes the layout parameters by instantiating the utility.
	 * 
	 * @param aContext
	 *            The context.
	 */
	public static void init(final Context aContext)
	{
		INSTANCE = new ViewUtil(aContext);
	}
	
	/**
	 * @return the current view utility instance.
	 */
	public static ViewUtil instance()
	{
		return INSTANCE;
	}
	
	/**
	 * Removes a view from its parent view. Optionally removes all child views.
	 * 
	 * @param aView
	 *            The view to release.
	 * @param aChildren
	 *            Whether the children of the given view should be removed too.
	 */
	public static void release(final View aView, final boolean aChildren)
	{
		if (aView == null)
		{
			return;
		}
		if (aChildren && aView instanceof ViewGroup)
		{
			((ViewGroup) aView).removeAllViews();
		}
		final ViewParent parent = aView.getParent();
		if (parent instanceof ViewGroup)
		{
			((ViewGroup) parent).removeAllViews();
		}
	}
}
