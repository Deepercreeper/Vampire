package com.deepercreeper.vampireapp.util.view.dialogs;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.inventory.Armor;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.listeners.InventoryItemCreationListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Dialog used to create armor items.
 * 
 * @author vrl
 */
public class CreateArmorItemDialog extends DefaultDialog<InventoryItemCreationListener, LinearLayout>
{
	private EditText mName;
	
	private EditText mWeight;
	
	private EditText mQuantity;
	
	private EditText mArmor;
	
	private Button mOK;
	
	private CreateArmorItemDialog(String aTitle, Context aContext, InventoryItemCreationListener aListener)
	{
		super(aTitle, aContext, aListener, R.layout.dialog_create_armor_item, LinearLayout.class);
	}
	
	@Override
	protected Dialog createDialog(Builder aBuilder)
	{
		mOK = (Button) getContainer().findViewById(R.id.dialog_armor_ok_button);
		mName = (EditText) getContainer().findViewById(R.id.dialog_armor_name_text);
		mWeight = (EditText) getContainer().findViewById(R.id.dialog_armor_weight_text);
		mQuantity = (EditText) getContainer().findViewById(R.id.dialog_armor_quantity_text);
		mArmor = (EditText) getContainer().findViewById(R.id.dialog_armor_text);
		
		final TextWatcher listener = new TextWatcher()
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
		mQuantity.setText("" + 1);
		mName.addTextChangedListener(listener);
		mWeight.addTextChangedListener(listener);
		mQuantity.addTextChangedListener(listener);
		mArmor.addTextChangedListener(listener);
		mOK.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Armor armor = new Armor(mName.getText().toString().trim(), Integer.parseInt(mWeight.getText().toString()),
						Integer.parseInt(mQuantity.getText().toString()), Integer.parseInt(mArmor.getText().toString()), getContext(), null);
				getListener().itemCreated(armor);
				dismiss();
			}
		});
		
		return aBuilder.create();
	}
	
	private void updateOKButton()
	{
		boolean enabled = true;
		enabled &= isNameOk(mName);
		enabled &= isNumberOk(mWeight, 0);
		enabled &= isNumberOk(mQuantity, 1);
		enabled &= isNumberOk(mArmor, 0);
		ViewUtil.setEnabled(mOK, enabled);
	}
	
	/**
	 * @return whether any of this classes dialogs is open.
	 */
	public static boolean isDialogOpen()
	{
		return isDialogOpen(CreateArmorItemDialog.class);
	}
	
	/**
	 * Shows a create weapon item dialog.
	 * 
	 * @param aTitle
	 *            The dialog title.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The dialog listener.
	 */
	public static void showCreateArmorItemDialog(final String aTitle, final Context aContext, final InventoryItemCreationListener aListener)
	{
		if (isDialogOpen())
		{
			return;
		}
		new CreateArmorItemDialog(aTitle, aContext, aListener).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
