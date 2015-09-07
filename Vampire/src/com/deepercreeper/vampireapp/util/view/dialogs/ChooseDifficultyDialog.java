package com.deepercreeper.vampireapp.util.view.dialogs;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.listeners.DifficultyChooseListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * A dialog for choosing the difficulty of any dices action.
 * 
 * @author Vincent
 */
public class ChooseDifficultyDialog extends DefaultDialog<DifficultyChooseListener, LinearLayout>
{
	private EditText mAmount;
	
	private Button mOk;
	
	private ChooseDifficultyDialog(final String aTitle, final Context aContext, final DifficultyChooseListener aListener)
	{
		super(aTitle, aContext, aListener, R.layout.dialog_difficulty_choose, LinearLayout.class);
	}
	
	@Override
	protected Dialog createDialog(final Builder aBuilder)
	{
		mAmount = (EditText) getContainer().findViewById(R.id.dialog_difficulty_value_text);
		mAmount.addTextChangedListener(new TextWatcher()
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
		mAmount.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(final View aV, final boolean aHasFocus)
			{
				if (aHasFocus)
				{
					mAmount.setText("");
				}
			}
		});
		mAmount.setText("6");
		((TextView) getContainer().findViewById(R.id.dialog_difficulty_label)).setText(getContext().getString(R.string.difficulty) + " (1 - 10):");
		mOk = (Button) getContainer().findViewById(R.id.dialog_difficulty_ok_button);
		mOk.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				getListener().difficultyChosen(Integer.parseInt(mAmount.getText().toString()));
				dismiss();
			}
		});
		return aBuilder.create();
	}
	
	/**
	 * @return whether any of this classes dialogs is open.
	 */
	public static boolean isDialogOpen()
	{
		return isDialogOpen(ChooseDifficultyDialog.class);
	}
	
	private void updateOkButton()
	{
		boolean enabled = true;
		enabled &= isNumberOk(mAmount, 1, 10);
		ViewUtil.setEnabled(mOk, enabled);
	}
	
	@Override
	public void onDismiss(final DialogInterface aDialog)
	{
		super.onDismiss(aDialog);
		getListener().cancel();
	}
	
	/**
	 * Shows a choose dices dialog.
	 * 
	 * @param aTitle
	 *            The title.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The listener.
	 */
	public static void showChooseDifficultyDialog(final String aTitle, final Context aContext, final DifficultyChooseListener aListener)
	{
		if (isDialogOpen())
		{
			return;
		}
		new ChooseDifficultyDialog(aTitle, aContext, aListener).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
