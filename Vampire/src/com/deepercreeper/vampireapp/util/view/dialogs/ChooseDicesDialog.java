package com.deepercreeper.vampireapp.util.view.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.listeners.DicesChooseListener;
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
 * A dialog for choosing an amount for each dice item.
 * 
 * @author vrl
 */
public class ChooseDicesDialog extends DefaultDialog<DicesChooseListener, LinearLayout>
{
	private final List<ItemInstance> mDiceItems;
	
	private final Map<ItemInstance, EditText> mAmounts = new HashMap<ItemInstance, EditText>();
	
	private final Map<ItemInstance, Integer> mMaxValues = new HashMap<ItemInstance, Integer>();
	
	private final Map<ItemInstance, Integer> mValues = new HashMap<ItemInstance, Integer>();
	
	private Button mOk;
	
	private ChooseDicesDialog(final String aTitle, final List<ItemInstance> aDiceItems, final Context aContext, final DicesChooseListener aListener)
	{
		super(aTitle, aContext, aListener, R.layout.dialog_dices_choose, LinearLayout.class);
		
		mDiceItems = aDiceItems;
		for (final ItemInstance diceItem : mDiceItems)
		{
			mMaxValues.put(diceItem, diceItem.getMaxDecreasure());
		}
	}
	
	@Override
	protected Dialog createDialog(final Builder aBuilder)
	{
		for (final ItemInstance diceItem : mDiceItems)
		{
			final LinearLayout itemView = (LinearLayout) View.inflate(getContext(), R.layout.view_dice_chooser, null);
			final EditText amount = (EditText) itemView.findViewById(R.id.view_dice_value_text);
			final Button max = (Button) itemView.findViewById(R.id.view_max_dice_value_button);
			
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
			if (mMaxValues.get(diceItem) == 0)
			{
				ViewUtil.setEnabled(amount, false);
			}
			mAmounts.put(diceItem, amount);
			max.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					final int maxValue = mMaxValues.get(diceItem);
					amount.setText("" + maxValue);
					mValues.put(diceItem, maxValue);
				}
			});
			((TextView) itemView.findViewById(R.id.view_dice_label)).setText(diceItem.getDisplayName() + " (0 - " + mMaxValues.get(diceItem) + "):");
			getContainer().addView(itemView, getContainer().getChildCount() - 1);
		}
		mOk = (Button) getContainer().findViewById(R.id.dialog_dices_ok_button);
		mOk.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				getListener().choseDices(mValues);
				dismiss();
			}
		});
		return aBuilder.create();
	}
	
	private void updateOkButton()
	{
		boolean enabled = true;
		for (final ItemInstance diceItem : mAmounts.keySet())
		{
			int value = -1;
			try
			{
				value = Integer.parseInt(mAmounts.get(diceItem).getText().toString());
			}
			catch (final NumberFormatException e)
			{}
			
			if (value < 0 || value > mMaxValues.get(diceItem))
			{
				enabled = false;
				break;
			}
			mValues.put(diceItem, value);
		}
		ViewUtil.setEnabled(mOk, enabled);
	}
	
	/**
	 * @return whether any of this classes dialogs is open.
	 */
	public static boolean isDialogOpen()
	{
		return isDialogOpen(ChooseDicesDialog.class);
	}
	
	/**
	 * Shows a choose dices dialog.
	 * 
	 * @param aDiceItems
	 *            The dice items that are going to be chosen.
	 * @param aTitle
	 *            The title.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The listener.
	 */
	public static void showChooseDicesDialog(final List<ItemInstance> aDiceItems, final String aTitle, final Context aContext,
			final DicesChooseListener aListener)
	{
		if (isDialogOpen())
		{
			return;
		}
		new ChooseDicesDialog(aTitle, aDiceItems, aContext, aListener).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
