package com.deepercreeper.vampireapp.character.controllers;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class EPController
{
	private int						mEP;
	
	private final RelativeLayout	mContainer;
	
	private final TextView			mEPLabel;
	
	public EPController(final int aEP, final Context aContext)
	{
		mEP = aEP;
		mContainer = (RelativeLayout) View.inflate(aContext, R.layout.ep, null);
		mEPLabel = (TextView) getContainer().findViewById(R.id.ep);
		init();
	}
	
	public EPController(final Context aContext)
	{
		mEP = 0;
		mContainer = (RelativeLayout) View.inflate(aContext, R.layout.ep, null);
		mEPLabel = (TextView) getContainer().findViewById(R.id.ep);
		init();
	}
	
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	public void init()
	{
		mEPLabel.setText("" + mEP);
	}
	
	public RelativeLayout getContainer()
	{
		return mContainer;
	}
	
	public int getExperience()
	{
		return mEP;
	}
	
	public void increaseBy(final int aValue)
	{
		mEP += aValue;
		init();
	}
	
	public void decreaseBy(final int aValue)
	{
		mEP -= aValue;
		if (mEP < 0)
		{
			mEP = 0;
		}
		init();
	}
}
