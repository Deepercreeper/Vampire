package com.deepercreeper.vampireapp.host.controllers;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.HealthControllerInstance;

/**
 * Represents the host side player health.
 * 
 * @author Vincent
 */
public class PlayerHealth
{
	private final GridLayout			mContainer;
	
	private final TextView				mStep;
	
	private final ProgressBar			mValueBar;
	
	private HealthControllerInstance	mHealth;
	
	/**
	 * Creates a new player health.
	 * 
	 * @param aHealth
	 *            The initial health.
	 * @param aContext
	 *            The underlying context.
	 */
	public PlayerHealth(final HealthControllerInstance aHealth, final Context aContext)
	{
		mContainer = (GridLayout) View.inflate(aContext, R.layout.player_health, null);
		mStep = (TextView) mContainer.findViewById(R.id.health_step);
		mValueBar = (ProgressBar) mContainer.findViewById(R.id.health_bar);
		update(aHealth);
	}
	
	/**
	 * Called when the character instance has changes in health.
	 * 
	 * @param aHealth
	 *            The new health.
	 */
	public void update(final HealthControllerInstance aHealth)
	{
		mHealth = aHealth;
		mStep.setText(mHealth.getStep() == 0 ? "0" : "-" + mHealth.getStep());
		mValueBar.setMax(mHealth.getStepsCount() - 1);
		mValueBar.setProgress(mHealth.getStepsCount() - mHealth.getStep() - 1);
	}
	
	/**
	 * @return the health container.
	 */
	public GridLayout getHealthContainer()
	{
		return mContainer;
	}
}
