package com.deepercreeper.vampireapp.util.view.dialogs;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.inventory.Artifact;
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
 * A dialog used for creating inventory items.
 * 
 * @author Vincent
 */
public class CreateInventoryItemDialog extends DefaultDialog<InventoryItemCreationListener, LinearLayout>
{
	private EditText mName;
	
	private EditText mWeight;
	
	private EditText mQuantity;
	
	private Button mOK;
	
	private CreateInventoryItemDialog(final String aTitle, final Context aContext, final InventoryItemCreationListener aListener)
	{
		super(aTitle, aContext, aListener, R.layout.dialog_create_inventory_item, LinearLayout.class);
	}
	
	@Override
	public Dialog createDialog(final Builder aBuilder)
	{
		mOK = (Button) getContainer().findViewById(R.id.dialog_ok_button);
		mName = (EditText) getContainer().findViewById(R.id.dialog_item_name_text);
		mWeight = (EditText) getContainer().findViewById(R.id.dialog_item_weight_text);
		mQuantity = (EditText) getContainer().findViewById(R.id.dialog_item_quantity_text);
		
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
		mOK.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				getListener().itemCreated(new Artifact(mName.getText().toString(), Integer.parseInt(mWeight.getText().toString()),
						Integer.parseInt(mQuantity.getText().toString()), getContext(), null));
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
		enabled &= isNumberOk(mWeight, 0);
		enabled &= isNumberOk(mQuantity, 1);
		ViewUtil.setEnabled(mOK, enabled);
	}
	
	/**
	 * @return whether any of this classes dialogs is open.
	 */
	public static boolean isDialogOpen()
	{
		return isDialogOpen(CreateInventoryItemDialog.class);
	}
	
	/**
	 * Shows a create inventory item dialog.
	 * 
	 * @param aTitle
	 *            The dialog title.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The dialog listener.
	 */
	public static void showCreateInventoryItemDialog(final String aTitle, final Context aContext, final InventoryItemCreationListener aListener)
	{
		if (isDialogOpen())
		{
			return;
		}
		new CreateInventoryItemDialog(aTitle, aContext, aListener).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
