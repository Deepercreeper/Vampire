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

public class ViewUtil
{
	private static ViewUtil					INSTANCE;
	
	private final TableLayout.LayoutParams	mTableWrapAll	= new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	private final TableRow.LayoutParams		mRowWrapAll		= new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	private final TableRow.LayoutParams		mRowNameShort;
	
	private final TableRow.LayoutParams		mRowNameLong;
	
	private final TableRow.LayoutParams		mRowButtonSize;
	
	private final LayoutParams				mWrapHeight		= new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	
	private final LayoutParams				mZeroHeight		= new LayoutParams(LayoutParams.MATCH_PARENT, 0);
	
	private final LayoutParams				mButtonSize;
	
	private final LayoutParams				mValueSize;
	
	private final LayoutParams				mNameSizeShort;
	
	private final LayoutParams				mNumberSize;
	
	private final LayoutParams				mWrapAll		= new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	public static void init(final Context aContext)
	{
		INSTANCE = new ViewUtil(aContext);
	}
	
	public static ViewUtil instance()
	{
		return INSTANCE;
	}
	
	private ViewUtil(final Context aContext)
	{
		mRowNameShort = new TableRow.LayoutParams(calcPx(82, aContext), LayoutParams.MATCH_PARENT);
		mRowNameLong = new TableRow.LayoutParams(calcPx(120, aContext), LayoutParams.MATCH_PARENT);
		mButtonSize = new LayoutParams(calcPx(30, aContext), calcPx(30, aContext));
		mRowButtonSize = new TableRow.LayoutParams(calcPx(30, aContext), calcPx(30, aContext));
		mValueSize = new LayoutParams(calcPx(25, aContext), LayoutParams.WRAP_CONTENT);
		mNumberSize = new LayoutParams(LayoutParams.WRAP_CONTENT, calcPx(30, aContext));
		mNameSizeShort = new LayoutParams(calcPx(82, aContext), calcPx(30, aContext));
	}
	
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
	
	public LayoutParams getNameSizeShort()
	{
		return mNameSizeShort;
	}
	
	public TableRow.LayoutParams getRowButtonSize()
	{
		return mRowButtonSize;
	}
	
	public LayoutParams getButtonSize()
	{
		return mButtonSize;
	}
	
	public LayoutParams getNumberSize()
	{
		return mNumberSize;
	}
	
	public TableRow.LayoutParams getRowNameLong()
	{
		return mRowNameLong;
	}
	
	public LayoutParams getWrapAll()
	{
		return mWrapAll;
	}
	
	public TableRow.LayoutParams getRowNameShort()
	{
		return mRowNameShort;
	}
	
	public LayoutParams getValueSize()
	{
		return mValueSize;
	}
	
	public LayoutParams getWrapHeight()
	{
		return mWrapHeight;
	}
	
	public LayoutParams getZeroHeight()
	{
		return mZeroHeight;
	}
	
	public TableRow.LayoutParams getRowWrapAll()
	{
		return mRowWrapAll;
	}
	
	public TableLayout.LayoutParams getTableWrapAll()
	{
		return mTableWrapAll;
	}
	
	public static int calcHeight(final ViewGroup aLayout)
	{
		aLayout.measure(0, 0);
		return aLayout.getMeasuredHeight();
	}
	
	public static int calcPx(final int aDp, final Context aContext)
	{
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, aDp, aContext.getResources().getDisplayMetrics()));
	}
	
	public static void applyValue(final int aValue, final RadioButton[] aValueDisplay)
	{
		for (int i = 0; i < aValueDisplay.length; i++ )
		{
			aValueDisplay[i].setChecked(i < aValue);
		}
	}
}
