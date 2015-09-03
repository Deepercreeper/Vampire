package com.deepercreeper.vampireapp.util.view.dialogs;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.Currency;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.listeners.MoneyAmountChooseListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A dialog, that is used to choose, how much money to handle with.
 * 
 * @author Vincent
 */
public class ChooseMoneyAmountDialog extends DefaultDialog<MoneyAmountChooseListener, LinearLayout>
{
	/**
	 * The default maximum money amount.
	 */
	public static final int MAX_VALUE = 10000;
	
	private final Map<String, Integer> mValues = new HashMap<String, Integer>();
	
	private final Map<String, Integer> mMaxValues;
	
	private final Currency mCurrency;
	
	private final Map<String, EditText> mAmounts = new HashMap<String, EditText>();
	
	private Button mOk;
	
	private ChooseMoneyAmountDialog(final Currency aCurrency, final Map<String, Integer> aMaxValues, final String aTitle, final Context aContext,
			final MoneyAmountChooseListener aAction)
	{
		super(aTitle, aContext, aAction, R.layout.dialog_money_choose, LinearLayout.class);
		mCurrency = aCurrency;
		mMaxValues = aMaxValues;
	}
	
	@Override
	public Dialog createDialog(final Builder aBuilder)
	{
		for (final String currency : mCurrency.getCurrencies())
		{
			final LinearLayout currencyView = (LinearLayout) View.inflate(getContext(), R.layout.view_currency_chooser, null);
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
			getContainer().addView(currencyView, getContainer().getChildCount() - 1);
		}
		mOk = (Button) getContainer().findViewById(R.id.dialog_ok_button);
		mOk.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				getListener().amountSelected(mValues);
				dismiss();
			}
		});
		return aBuilder.create();
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
		ViewUtil.setEnabled(mOk, enabled);
	}
	
	/**
	 * @return whether any of this classes dialogs is open.
	 */
	public static boolean isDialogOpen()
	{
		return isDialogOpen(ChooseMoneyAmountDialog.class);
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
			final Context aContext, final MoneyAmountChooseListener aAction)
	{
		if (isDialogOpen())
		{
			return;
		}
		new ChooseMoneyAmountDialog(aCurrency, aMaxValues, aTitle, aContext, aAction).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
