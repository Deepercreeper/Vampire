package com.deepercreeper.vampireapp.character.instance;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.HealthControllerCreation;
import com.deepercreeper.vampireapp.host.change.HealthChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * This controller is used to control the health of a character.<br>
 * It handles the health display, hurting and healing the health.
 * 
 * @author vrl
 */
public class HealthControllerInstance implements TimeListener, Saveable, Viewable, AnimatorUpdateListener
{
	private static final int VALUE_MULTIPLICATOR = 20;
	
	private final LinearLayout mContainer;
	
	private final ItemInstance mCost;
	
	private final MessageListener mChangeListener;
	
	private final boolean mHost;
	
	private final ValueAnimator mAnimator;
	
	private final CharacterInstance mChar;
	
	private final ImageButton mHealButton;
	
	private final ImageButton mHurtButton;
	
	private final ImageButton mHeavyHurt;
	
	private final ProgressBar mValueBar;
	
	private final TextView mStepLabel;
	
	private int[] mSteps;
	
	private int mHeavyWounds = 0;
	
	private int mValue = 0;
	
	/**
	 * Creates a new health controller out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aContext
	 *            the underlying context.
	 * @param aChangeListener
	 *            The listener that is called, when changes happen.
	 * @param aChar
	 *            The parent character.
	 * @param aHost
	 *            Whether this controller is displayed at the host.
	 */
	public HealthControllerInstance(final Element aElement, final Context aContext, final MessageListener aChangeListener,
			final CharacterInstance aChar, final boolean aHost)
	{
		mHost = aHost;
		mChar = aChar;
		final int id = mHost ? R.layout.host_health : R.layout.client_health;
		mContainer = (LinearLayout) View.inflate(aContext, id, null);
		mSteps = DataUtil.parseValues(aElement.getAttribute("steps"));
		mHeavyWounds = Integer.parseInt(aElement.getAttribute("heavy"));
		mValue = Integer.parseInt(aElement.getAttribute("value"));
		mCost = mChar.findItemInstance(aElement.getAttribute("cost"));
		mChangeListener = aChangeListener;
		mAnimator = new ValueAnimator();
		mAnimator.addUpdateListener(this);
		
		mHealButton = (ImageButton) getContainer().findViewById(mHost ? R.id.h_heal_button : R.id.c_heal_button);
		mHurtButton = mHost ? (ImageButton) getContainer().findViewById(R.id.h_hurt_button) : null;
		mHeavyHurt = mHost ? (ImageButton) getContainer().findViewById(R.id.h_heavy_hurt_button) : null;
		mValueBar = (ProgressBar) getContainer().findViewById(mHost ? R.id.h_health_bar : R.id.c_health_bar);
		mStepLabel = (TextView) getContainer().findViewById(mHost ? R.id.h_step_label : R.id.c_step_label);
		
		mHealButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				heal(1);
			}
		});
		
		if (mHost)
		{
			mHeavyHurt.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(final View aV)
				{
					hurt(1, true);
				}
			});
			mHurtButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					hurt(1, false);
				}
			});
		}
	}
	
	/**
	 * Creates a new health controller out of the given health controller creation.
	 * 
	 * @param aHealth
	 *            The controller creation.
	 * @param aContext
	 *            The underlying context.
	 * @param aChangeListener
	 *            The listener that is called, when changes happen.
	 * @param aChar
	 *            The parent character.
	 * @param aHost
	 *            Whether this controller is displayed at the host.
	 */
	public HealthControllerInstance(final HealthControllerCreation aHealth, final Context aContext, final MessageListener aChangeListener,
			final CharacterInstance aChar, final boolean aHost)
	{
		mHost = aHost;
		mChar = aChar;
		final int id = mHost ? R.layout.host_health : R.layout.client_health;
		mContainer = (LinearLayout) View.inflate(aContext, id, null);
		mSteps = aHealth.getSteps();
		mCost = mChar.findItemInstance(aHealth.getCost());
		mChangeListener = aChangeListener;
		mAnimator = new ValueAnimator();
		mAnimator.addUpdateListener(this);
		
		mHealButton = (ImageButton) getContainer().findViewById(mHost ? R.id.h_heal_button : R.id.c_heal_button);
		mHurtButton = mHost ? (ImageButton) getContainer().findViewById(R.id.h_hurt_button) : null;
		mHeavyHurt = mHost ? (ImageButton) getContainer().findViewById(R.id.h_heavy_hurt_button) : null;
		mValueBar = (ProgressBar) getContainer().findViewById(mHost ? R.id.h_health_bar : R.id.c_health_bar);
		mStepLabel = (TextView) getContainer().findViewById(mHost ? R.id.h_step_label : R.id.c_step_label);
		
		mHealButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				heal(1);
			}
		});
		
		if (mHost)
		{
			mHeavyHurt.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(final View aV)
				{
					hurt(1, true);
				}
			});
			mHurtButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					hurt(1, false);
				}
			});
		}
	}
	
	/**
	 * Adds a new step to the front of all health steps.
	 */
	public void addStep()
	{
		final int[] steps = new int[mSteps.length + 1];
		steps[0] = 0;
		for (int i = 0; i < mSteps.length; i++ )
		{
			steps[i + 1] = mSteps[i];
		}
		mSteps = steps;
		mChangeListener.sendChange(new HealthChange(mSteps));
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("health");
		element.setAttribute("steps", DataUtil.parseValues(mSteps));
		element.setAttribute("value", "" + mValue);
		element.setAttribute("heavy", "" + mHeavyWounds);
		element.setAttribute("cost", mCost.getName());
		return element;
	}
	
	/**
	 * @return whether the character is able to do anything anymore.
	 */
	public boolean canAct()
	{
		return mSteps[mValue] != -1;
	}
	
	/**
	 * @return whether the character is able to heal himself.
	 */
	public boolean canHeal()
	{
		if (mHost)
		{
			return mValue > 0;
		}
		if ( !mChar.getMode().getMode().canHeal())
		{
			return false;
		}
		return mHeavyWounds < mValue && mValue > 0 && mCost.getValue() > 0;
	}
	
	/**
	 * @return whether the host can hurt the character.
	 */
	public boolean canHurt()
	{
		return mValue < getStepsCount() - 1;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the current health step and specifically its negative points.
	 */
	public int getStep()
	{
		if (mSteps[mValue] == -1)
		{
			return 0;
		}
		return mSteps[mValue];
	}
	
	/**
	 * @return the number of steps the current health has.
	 */
	public int getStepsCount()
	{
		return mSteps.length;
	}
	
	/**
	 * Heals the character by the given amount of steps if possible.
	 * 
	 * @param aValue
	 *            The number of steps to heal.
	 */
	public void heal(final int aValue)
	{
		boolean heavyWoundsChanged = false;
		for (int i = aValue; i > 0; i-- )
		{
			if (mValue > 0)
			{
				if (mHost && mHeavyWounds == mValue)
				{
					mHeavyWounds-- ;
					heavyWoundsChanged = true;
				}
				mValue-- ;
				mCost.decrease();
			}
		}
		mChangeListener.sendChange(new HealthChange(false, mValue));
		if (heavyWoundsChanged)
		{
			mChangeListener.sendChange(new HealthChange(true, mHeavyWounds));
		}
		updateUI();
	}
	
	/**
	 * Adds the given amount of damage to the characters health.
	 * 
	 * @param aValue
	 *            The amount of health steps that are damaged.
	 * @param aHeavy
	 *            Whether the damage is heavy damage and can't be healed before a night has been gone.
	 */
	public void hurt(final int aValue, final boolean aHeavy)
	{
		mValue += aValue;
		if (mValue >= mSteps.length)
		{
			mValue = mSteps.length - 1;
		}
		if (aHeavy)
		{
			mHeavyWounds += aValue;
			if (mHeavyWounds > mValue)
			{
				mHeavyWounds = mValue;
			}
			mChangeListener.sendChange(new HealthChange(true, mHeavyWounds));
		}
		mChangeListener.sendChange(new HealthChange(false, mValue));
		updateUI();
	}
	
	@Override
	public void onAnimationUpdate(final ValueAnimator aAnimation)
	{
		mValueBar.setProgress((Integer) aAnimation.getAnimatedValue());
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void time(final Type aType, final int aAmount)
	{
		if (aType == Type.DAY)
		{
			if (mHeavyWounds > 0)
			{
				mHeavyWounds-- ;
			}
			updateUI();
		}
	}
	
	/**
	 * Updates the heavy wounds flag.
	 * 
	 * @param aHeavyWounds
	 *            Whether the character has heavy wounds.
	 */
	public void updateHeavyWounds(final int aHeavyWounds)
	{
		mHeavyWounds = aHeavyWounds;
		updateUI();
	}
	
	/**
	 * Updates the health steps.
	 * 
	 * @param aSteps
	 *            The new health steps.
	 */
	public void updateSteps(final int[] aSteps)
	{
		mSteps = aSteps;
		updateUI();
	}
	
	@Override
	public void updateUI()
	{
		if (mAnimator.isRunning())
		{
			mAnimator.cancel();
		}
		mValueBar.setMax(VALUE_MULTIPLICATOR * (mSteps.length - 1));
		mAnimator.setIntValues(mValueBar.getProgress(), VALUE_MULTIPLICATOR * (mSteps.length - mValue - 1));
		mAnimator.start();
		
		if (mValue == getStepsCount() - 1)
		{
			mStepLabel.setText("K.O.");
			mChar.getMode().setMode(Mode.KO, true);
		}
		else
		{
			mStepLabel.setText("" + -getStep());
		}
		ViewUtil.setEnabled(mHealButton, canHeal());
		ViewUtil.setEnabled(mHurtButton, canHurt());
		ViewUtil.setEnabled(mHeavyHurt, canHurt());
	}
	
	/**
	 * Sets the current health level.
	 * 
	 * @param aValue
	 *            The new health level.
	 */
	public void updateValue(final int aValue)
	{
		mValue = aValue;
		updateUI();
	}
}
