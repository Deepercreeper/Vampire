package com.deepercreeper.vampireapp.character.instance;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class HealthControllerInstance implements TimeListener, Saveable
{
	private final RelativeLayout	mContainer;
	
	private final Context			mContext;
	
	private ImageButton				mHealButton;
	
	private ProgressBar				mValueBar;
	
	private TextView				mStepLabel;
	
	private boolean					mInitialized	= false;
	
	private int[]					mSteps;
	
	private boolean					mHeavyWounds	= false;
	
	private boolean					mCanHeal		= false;
	
	private int						mValue			= 0;
	
	public HealthControllerInstance(final Element aElement, final Context aContext)
	{
		mContext = aContext;
		mContainer = (RelativeLayout) View.inflate(aContext, R.layout.health, null);
		mSteps = DataUtil.parseValues(aElement.getAttribute("steps"));
		mHeavyWounds = Boolean.valueOf(aElement.getAttribute("heavy"));
		mCanHeal = Boolean.valueOf(aElement.getAttribute("canHeal"));
		mValue = Integer.parseInt(aElement.getAttribute("value"));
		init();
	}
	
	public HealthControllerInstance(final int[] aSteps, final Context aContext)
	{
		mContext = aContext;
		mContainer = (RelativeLayout) View.inflate(aContext, R.layout.health, null);
		mSteps = aSteps;
		init();
	}
	
	public void init()
	{
		if ( !mInitialized)
		{
			getContainer().setLayoutParams(ViewUtil.getWrapHeight());
			
			mHealButton = (ImageButton) getContainer().findViewById(R.id.heal_button);
			mValueBar = (ProgressBar) getContainer().findViewById(R.id.health_bar);
			mStepLabel = (TextView) getContainer().findViewById(R.id.step_value);
			
			mHealButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					heal(1);
				}
			});
			
			mValueBar.setMax(mSteps.length - 1);
			mValueBar.getLayoutParams().width = ViewUtil.calcPx(70, mContext)
					+ Math.round(mContext.getResources().getDimension(R.dimen.item_value_bar_width));
		}
		
		ViewUtil.setEnabled(mHealButton, canHeal());
		updateValue();
		
		mInitialized = true;
	}
	
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	public void updateValue()
	{
		mValueBar.setProgress(mSteps.length - mValue - 1);
		mStepLabel.setText("" + -getStep());
	}
	
	public RelativeLayout getContainer()
	{
		return mContainer;
	}
	
	public void addStep()
	{
		final int[] steps = new int[mSteps.length + 1];
		steps[0] = 0;
		for (int i = 0; i < mSteps.length; i++ )
		{
			steps[i + 1] = mSteps[i];
		}
		mSteps = steps;
	}
	
	public int getStep()
	{
		if (mSteps[mValue] == -1)
		{
			return 0;
		}
		return mSteps[mValue];
	}
	
	public void hurt(final int aValue, final boolean aHeavy)
	{
		mValue += aValue;
		if (mValue >= mSteps.length)
		{
			mValue = mSteps.length - 1;
		}
		if (aHeavy)
		{
			mHeavyWounds = true;
		}
		mCanHeal = true;
		if (mHeavyWounds)
		{
			mCanHeal = false;
		}
	}
	
	public boolean canAct()
	{
		return mSteps[mValue] != -1;
	}
	
	public boolean canHeal()
	{
		// TODO Ask the player for blood
		return mCanHeal && mValue > 0;
	}
	
	public void heal(final int aValue)
	{
		if ( !canHeal())
		{
			return;
		}
		mValue -= aValue;
		if (mValue < 0)
		{
			mValue = 0;
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("health");
		element.setAttribute("steps", DataUtil.parseValues(mSteps));
		element.setAttribute("value", "" + mValue);
		element.setAttribute("canHeal", "" + mCanHeal);
		element.setAttribute("heavy", "" + mHeavyWounds);
		return element;
	}
	
	@Override
	public void day()
	{
		mCanHeal = true;
	}
	
	@Override
	public void hour()
	{}
	
	@Override
	public void round()
	{}
}
