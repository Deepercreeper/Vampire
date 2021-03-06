package com.deepercreeper.vampireapp.character.instance.controllers;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.host.change.EPChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This controller is used to control the experience a character collects.<br>
 * It also displays the current amount of experience and allows modifying and reading it.
 * 
 * @author vrl
 */
public class EPControllerInstance implements Viewable
{
	private final LinearLayout mContainer;
	
	private final boolean mHost;
	
	private final MessageListener mChangeListener;
	
	private final CharacterInstance mChar;
	
	private final EditText mEPAmount;
	
	private final ImageButton mGiveEP;
	
	private final TextView mEPLabel;
	
	private int mEP;
	
	/**
	 * Creates a new experience controller with no experience from start.
	 * 
	 * @param aContext
	 *            The underlying context.
	 * @param aChangeListener
	 *            The change listener.
	 * @param aHost
	 *            Whether this controller is host sided.
	 * @param aChar
	 *            The character.
	 */
	public EPControllerInstance(final Context aContext, final MessageListener aChangeListener, final boolean aHost, final CharacterInstance aChar)
	{
		mEP = 0;
		mChar = aChar;
		mChangeListener = aChangeListener;
		mHost = aHost;
		final int id = mHost ? R.layout.host_ep : R.layout.client_ep;
		mContainer = (LinearLayout) View.inflate(aContext, id, null);
		
		mEPAmount = mHost ? (EditText) getContainer().findViewById(R.id.h_ep_amount_text) : null;
		mGiveEP = mHost ? (ImageButton) getContainer().findViewById(R.id.h_give_ep_button) : null;
		mEPLabel = (TextView) getContainer().findViewById(mHost ? R.id.h_ep_label : R.id.c_ep_label);
		
		if (mHost)
		{
			mEPAmount.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void afterTextChanged(final Editable aS)
				{
					updateUI();
				}
				
				@Override
				public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
				{}
				
				@Override
				public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
				{}
			});
			mGiveEP.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					final int amount = Integer.parseInt(mEPAmount.getText().toString());
					mEPAmount.setText("");
					increaseBy(amount);
				}
			});
		}
		updateUI();
	}
	
	/**
	 * Creates a new experience controller with the given experience level.
	 * 
	 * @param aEP
	 *            The experience level.
	 * @param aContext
	 *            The underlying context.
	 * @param aChangeListener
	 *            The change listener.
	 * @param aHost
	 *            Whether this controller is host sided.
	 * @param aChar
	 *            The character.
	 */
	public EPControllerInstance(final int aEP, final Context aContext, final MessageListener aChangeListener, final boolean aHost,
			final CharacterInstance aChar)
	{
		mEP = aEP;
		mChar = aChar;
		mChangeListener = aChangeListener;
		mHost = aHost;
		final int id = mHost ? R.layout.host_ep : R.layout.client_ep;
		mContainer = (LinearLayout) View.inflate(aContext, id, null);
		
		mEPAmount = mHost ? (EditText) getContainer().findViewById(R.id.h_ep_amount_text) : null;
		mGiveEP = mHost ? (ImageButton) getContainer().findViewById(R.id.h_give_ep_button) : null;
		mEPLabel = (TextView) getContainer().findViewById(mHost ? R.id.h_ep_label : R.id.c_ep_label);
		
		if (mHost)
		{
			mEPAmount.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void afterTextChanged(final Editable aS)
				{
					updateUI();
				}
				
				@Override
				public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
				{}
				
				@Override
				public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
				{}
			});
			mGiveEP.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					final int amount = Integer.parseInt(mEPAmount.getText().toString());
					mEPAmount.setText("");
					increaseBy(amount);
				}
			});
		}
		updateUI();
	}
	
	/**
	 * Decreases the current amount of experience by the given number.
	 * 
	 * @param aValue
	 *            The value to subtract.
	 */
	public void decreaseBy(final int aValue)
	{
		mEP -= aValue;
		if (mEP < 0)
		{
			mEP = 0;
		}
		mChangeListener.sendChange(new EPChange(mEP));
		updateUI();
	}
	
	/**
	 * @return the view container for the experience display.
	 */
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the current amount of experience.
	 */
	public int getExperience()
	{
		return mEP;
	}
	
	/**
	 * Increases the current amount of experience by the given number.
	 * 
	 * @param aValue
	 *            The value to add.
	 */
	public void increaseBy(final int aValue)
	{
		mEP += aValue;
		mChangeListener.sendChange(new EPChange(mEP));
		updateUI();
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void updateUI()
	{
		mEPLabel.setText("" + mEP);
		if (mHost)
		{
			boolean canGiveEp = true;
			try
			{
				final int amount = Integer.parseInt(mEPAmount.getText().toString());
				canGiveEp &= amount >= 0;
			}
			catch (final NumberFormatException e)
			{
				canGiveEp = false;
			}
			ViewUtil.setEnabled(mGiveEP, canGiveEp);
		}
		mChar.updateUI();
	}
	
	/**
	 * Updates the current experience value.
	 * 
	 * @param aValue
	 *            The new value.
	 */
	public void updateValue(final int aValue)
	{
		mEP = aValue;
		updateUI();
	}
}
