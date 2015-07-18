package com.deepercreeper.vampireapp.util.view.dialogs;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.interfaces.Nameable;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.mechanics.TimeListener.Type;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.listeners.InsanityCreationListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * A dialog used to create insanities with their duration.
 * 
 * @author vrl
 */
public class CreateInsanityDialog extends DefaultDialog<InsanityCreationListener, LinearLayout>
{
	private EditText mName;
	
	private EditText mValue;
	
	private ArrayAdapter<Nameable> mDuration;
	
	private Button mOK;
	
	private boolean mForever = false;
	
	private CreateInsanityDialog(final String aTitle, final Context aContext, final InsanityCreationListener aListener)
	{
		super(aTitle, aContext, aListener, R.layout.dialog_create_insanity, LinearLayout.class);
		
		mDuration = new ArrayAdapter<Nameable>(getContext(), android.R.layout.simple_spinner_dropdown_item, Type.getTypesList(getContext()));
	}
	
	@Override
	protected Dialog createDialog(Builder aBuilder)
	{
		mOK = (Button) getContainer().findViewById(R.id.dialog_insanity_ok_button);
		mName = (EditText) getContainer().findViewById(R.id.dialog_insanity_name_text);
		mValue = (EditText) getContainer().findViewById(R.id.dialog_insanity_value_text);
		final Spinner durationSpinner = (Spinner) getContainer().findViewById(R.id.dialog_insanity_duration_spinner);
		CheckBox forever = (CheckBox) getContainer().findViewById(R.id.dialog_insanity_forever_box);
		
		durationSpinner.setAdapter(mDuration);
		forever.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton aButtonView, boolean aIsChecked)
			{
				mForever = aIsChecked;
				ViewUtil.setEnabled(durationSpinner, !mForever);
				ViewUtil.setEnabled(mValue, !mForever);
				updateOKButton();
			}
		});
		TextWatcher listener = new TextWatcher()
		{
			@Override
			public void afterTextChanged(final Editable aS)
			{
				updateOKButton();
			}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{}
			
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{}
		};
		mName.addTextChangedListener(listener);
		mValue.addTextChangedListener(listener);
		mOK.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View aV)
			{
				String insanity = mName.getText().toString();
				Duration duration = Duration.FOREVER;
				if ( !mForever)
				{
					duration = new Duration(Type.getTypeOf(mDuration.getItem(durationSpinner.getSelectedItemPosition())),
							Integer.parseInt(mValue.getText().toString()));
				}
				getListener().insanityCreated(insanity, duration);
				dismiss();
			}
		});
		
		updateOKButton();
		
		return aBuilder.create();
	}
	
	private void updateOKButton()
	{
		boolean enabled = true;
		enabled &= isNameOk(mName);
		enabled &= mForever || isNumberOk(mValue, 1);
		ViewUtil.setEnabled(mOK, enabled);
	}
	
	/**
	 * @return whether any of this classes dialogs is open.
	 */
	public static boolean isDialogOpen()
	{
		return isDialogOpen(CreateInsanityDialog.class);
	}
	
	/**
	 * Shows a create insanity dialog.
	 * 
	 * @param aTitle
	 *            The dialog title.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The dialog listener.
	 */
	public static void showCreateInsanityDialog(final String aTitle, final Context aContext, final InsanityCreationListener aListener)
	{
		if (isDialogOpen())
		{
			return;
		}
		new CreateInsanityDialog(aTitle, aContext, aListener).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
