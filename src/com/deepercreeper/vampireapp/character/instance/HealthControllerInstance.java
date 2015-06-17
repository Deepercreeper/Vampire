package com.deepercreeper.vampireapp.character.instance;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.HealthControllerCreation;
import com.deepercreeper.vampireapp.host.connection.change.ChangeListener;
import com.deepercreeper.vampireapp.host.connection.change.HealthChange;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.ItemFinder;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * This controller is used to control the health of a character.<br>
 * It handles the health display, hurting and healing the health.
 * 
 * @author vrl
 */
public class HealthControllerInstance implements TimeListener, Saveable, Viewable
{
	private static final String		TAG				= "HealthControllerInstance";
	
	private final RelativeLayout	mContainer;
	
	private final Context			mContext;
	
	private final ItemInstance		mCost;
	
	private final ItemFinder		mItems;
	
	private final ChangeListener	mChangeListener;
	
	private final boolean			mHost;
	
	private ImageButton				mHealButton;
	
	private ProgressBar				mValueBar;
	
	private TextView				mStepLabel;
	
	private boolean					mInitialized	= false;
	
	private int[]					mSteps;
	
	private boolean					mHeavyWounds	= false;
	
	private boolean					mCanHeal		= false;
	
	private int						mValue			= 0;
	
	/**
	 * Creates a new health controller out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aContext
	 *            the underlying context.
	 * @param aItems
	 *            The item finder.
	 * @param aChangeListener
	 *            The listener that is called, when changes happen.
	 * @param aHost
	 *            Whether this controller is displayed at the host.
	 */
	public HealthControllerInstance(final Element aElement, final Context aContext, final ItemFinder aItems, ChangeListener aChangeListener,
			boolean aHost)
	{
		mContext = aContext;
		mHost = aHost;
		mItems = aItems;
		int id = mHost ? R.layout.host_health : R.layout.client_health;
		mContainer = (RelativeLayout) View.inflate(aContext, id, null);
		mSteps = DataUtil.parseValues(aElement.getAttribute("steps"));
		mHeavyWounds = Boolean.valueOf(aElement.getAttribute("heavy"));
		mCanHeal = Boolean.valueOf(aElement.getAttribute("canHeal"));
		mValue = Integer.parseInt(aElement.getAttribute("value"));
		mCost = mItems.findItem(aElement.getAttribute("cost"));
		mChangeListener = aChangeListener;
		init();
	}
	
	/**
	 * Creates a new health controller out of the given health controller creation.
	 * 
	 * @param aHealth
	 *            The controller creation.
	 * @param aContext
	 *            The underlying context.
	 * @param aItems
	 *            The item finder.
	 * @param aChangeListener
	 *            The listener that is called, when changes happen.
	 * @param aHost
	 *            Whether this controller is displayed at the host.
	 */
	public HealthControllerInstance(final HealthControllerCreation aHealth, final Context aContext, final ItemFinder aItems,
			ChangeListener aChangeListener, boolean aHost)
	{
		mContext = aContext;
		mHost = aHost;
		mItems = aItems;
		int id = mHost ? R.layout.host_health : R.layout.client_health;
		mContainer = (RelativeLayout) View.inflate(aContext, id, null);
		mSteps = aHealth.getSteps();
		mCost = mItems.findItem(aHealth.getCost());
		mChangeListener = aChangeListener;
		init();
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
		element.setAttribute("canHeal", "" + mCanHeal);
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
	 * Updates the can heal flag.
	 * 
	 * @param aCanHeal
	 *            Whether the character can heal now.
	 */
	public void updateCanHeal(boolean aCanHeal)
	{
		mCanHeal = aCanHeal;
		updateValue();
	}
	
	/**
	 * Updates the heavy wounds flag.
	 * 
	 * @param aHeavyWounds
	 *            Whether the character has heavy wounds.
	 */
	public void updateHeavyWounds(boolean aHeavyWounds)
	{
		mHeavyWounds = aHeavyWounds;
		updateValue();
	}
	
	/**
	 * Updates the health steps.
	 * 
	 * @param aSteps
	 *            The new health steps.
	 */
	public void updateSteps(int[] aSteps)
	{
		mSteps = aSteps;
		updateValue();
	}
	
	/**
	 * Sets the current health level.
	 * 
	 * @param aValue
	 *            The new health level.
	 */
	public void updateValue(int aValue)
	{
		mValue = aValue;
	}
	
	/**
	 * @return whether the character is able to heal himself.
	 */
	public boolean canHeal()
	{
		// TODO Remove
		Log.i(TAG, "" + (mCost.getValue() > 0));
		return mCanHeal && mValue > 0 && mCost.getValue() > 0;
	}
	
	@Override
	public RelativeLayout getContainer()
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
	 * Heals the character by the given amount of steps if possible.
	 * 
	 * @param aValue
	 *            The number of steps to heal.
	 */
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
		mChangeListener.sendChange(new HealthChange(mValue));
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
			mHeavyWounds = true;
			mChangeListener.sendChange(new HealthChange(true, mHeavyWounds));
		}
		mCanHeal = true;
		if (mHeavyWounds)
		{
			mCanHeal = false;
		}
		mChangeListener.sendChange(new HealthChange(false, mCanHeal));
	}
	
	@Override
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
					// TODO Ask if host present
					heal(1);
				}
			});
			
			mValueBar.getLayoutParams().width = ViewUtil.calcPx(70, mContext)
					+ Math.round(mContext.getResources().getDimension(R.dimen.item_value_bar_width));
			
			mInitialized = true;
		}
		
		updateValue();
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
			mHeavyWounds = false;
			mCanHeal = true;
			mChangeListener.sendChange(new HealthChange(true, mHeavyWounds));
			mChangeListener.sendChange(new HealthChange(false, mCanHeal));
		}
	}
	
	/**
	 * @return the number of steps the current health has.
	 */
	public int getStepsCount()
	{
		return mSteps.length;
	}
	
	/**
	 * Updates the displayed health value and the heal button.
	 */
	public void updateValue()
	{
		mValueBar.setMax(mSteps.length - 1);
		mValueBar.setProgress(mSteps.length - mValue - 1);
		mStepLabel.setText("" + -getStep());
		ViewUtil.setEnabled(mHealButton, canHeal());
	}
}
