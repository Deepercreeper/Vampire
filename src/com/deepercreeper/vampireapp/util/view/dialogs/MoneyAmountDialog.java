package com.deepercreeper.vampireapp.util.view.dialogs;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.Currency;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A dialog, that is used to choose, how much money to handle with.
 * 
 * @author Vincent
 */
public class MoneyAmountDialog extends DialogFragment
{
	/**
	 * A listener for money amount choose.
	 * 
	 * @author Vincent
	 */
	public static interface MoneyAmountListener
	{
		/**
		 * Invoked, when the money amount was chosen.
		 * 
		 * @param aMap
		 *            A map that puts an amount to each currency.
		 */
		public void amountSelected(Map<String, Integer> aMap);
	}
	
	/**
	 * The default maximum money amount.
	 */
	public static final int				MAX_VALUE	= 10000;
	
	private static boolean				sDialogOpen	= false;
	
	private final Map<String, Integer>	mValues		= new HashMap<String, Integer>();
	
	private final Map<String, Integer>	mMaxValues;
	
	private final String				mTitle;
	
	private final Context				mContext;
	
	private final MoneyAmountListener	mAction;
	
	private final Currency				mCurrency;
	
	private final Map<String, EditText>	mAmounts	= new HashMap<String, EditText>();
	
	private Button						mOK;
	
	private MoneyAmountDialog(final Currency aCurrency, final Map<String, Integer> aMaxValues, final String aTitle, final Context aContext,
			final MoneyAmountListener aAction)
	{
		sDialogOpen = true;
		mTitle = aTitle;
		mContext = aContext;
		mAction = aAction;
		mCurrency = aCurrency;
		mMaxValues = aMaxValues;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		final LinearLayout container = (LinearLayout) View.inflate(mContext, R.layout.dialog_money_choose, null);
		for (final String currency : mCurrency.getCurrencies())
		{
			final LinearLayout currencyView = (LinearLayout) View.inflate(mContext, R.layout.currency_chooser_view, null);
			final EditText amount = (EditText) currencyView.findViewById(R.id.view_currency_value_text);
			final Button max = (Button) currencyView.findViewById(R.id.view_max_currency_value_button);
			
			amount.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void afterTextChanged(final Editable aS)
				{
					updateOkButton();
				}
				
				@Override
				public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
				{}
				
				@Override
				public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
				{}
			});
			amount.setOnFocusChangeListener(new OnFocusChangeListener()
			{
				
				@Override
				public void onFocusChange(final View aV, final boolean aHasFocus)
				{
					if (aHasFocus)
					{
						amount.setText("");
					}
				}
			});
			amount.setText("0");
			mAmounts.put(currency, amount);
			max.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					final int maxValue = mMaxValues.get(currency);
					amount.setText("" + maxValue);
					mValues.put(currency, maxValue);
				}
			});
			((TextView) currencyView.findViewById(R.id.view_currency_label)).setText(currency + " (0 - " + mMaxValues.get(currency) + "):");
			container.addView(currencyView, container.getChildCount() - 1);
		}
		mOK = (Button) container.findViewById(R.id.dialog_ok_button);
		mOK.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mAction.amountSelected(mValues);
				dismiss();
			}
		});
		builder.setTitle(mTitle).setView(container);
		final AlertDialog dialog = builder.create();
		return dialog;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		sDialogOpen = false;
	}
	
	@Override
	public void onDetach()
	{
		super.onDetach();
		// TODO Remove when not necessary anymore
		try
		{
			final Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		}
		catch (final NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
		catch (final IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private void updateOkButton()
	{
		boolean enabled = true;
		for (final String currency : mAmounts.keySet())
		{
			int value = -1;
			try
			{
				value = Integer.parseInt(mAmounts.get(currency).getText().toString());
			}
			catch (final NumberFormatException e)
			{}
			
			if (value < 0 || value > mMaxValues.get(currency))
			{
				enabled = false;
				break;
			}
			mValues.put(currency, value);
		}
		ViewUtil.setEnabled(mOK, enabled);
	}
	
	/**
	 * Shows a money amount dialog with the given specifications.
	 * 
	 * @param aCurrency
	 *            The currency.
	 * @param aMaxValues
	 *            The maximum amounts of money that can be chosen.
	 * @param aTitle
	 *            The dialog title.
	 * @param aContext
	 *            The underlying context.
	 * @param aAction
	 *            The positive action.
	 */
	public static void showMoneyAmountDialog(final Currency aCurrency, final Map<String, Integer> aMaxValues, final String aTitle,
			final Context aContext, final MoneyAmountListener aAction)
	{
		if (sDialogOpen)
		{
			return;
		}
		new MoneyAmountDialog(aCurrency, aMaxValues, aTitle, aContext, aAction).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
