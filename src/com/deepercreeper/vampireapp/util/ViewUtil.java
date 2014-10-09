package com.deepercreeper.vampireapp.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;

public class ViewUtil
{
	private ViewUtil()
	{
		// Should not be instanced
	}
	
	public static int calcHeight(final ViewGroup aLayout)
	{
		int height = 0;
		for (int i = 0; i < aLayout.getChildCount(); i++ )
		{
			final View child = aLayout.getChildAt(i);
			final LayoutParams params = child.getLayoutParams();
			
			if (child instanceof ViewGroup)
			{
				if (params.height == LayoutParams.WRAP_CONTENT)
				{
					height += calcHeight((ViewGroup) child);
				}
				else if (params.height >= 0)
				{
					height += params.height;
				}
			}
			else if (params.height >= 0)
			{
				height += params.height;
			}
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
