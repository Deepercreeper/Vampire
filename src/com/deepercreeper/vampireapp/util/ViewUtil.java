package com.deepercreeper.vampireapp.util;

import android.content.Context;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class ViewUtil
{
	private ViewUtil()
	{
		// Should not be instanced
	}
	
	public static int calcHeight(final LinearLayout aLayout)
	{
		int height = 0;
		for (int i = 0; i < aLayout.getChildCount(); i++ )
		{
			height += aLayout.getChildAt(i).getHeight();
		}
		return height;
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
