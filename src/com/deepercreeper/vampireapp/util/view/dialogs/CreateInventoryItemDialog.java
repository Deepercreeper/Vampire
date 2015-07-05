package com.deepercreeper.vampireapp.util.view.dialogs;

import java.lang.reflect.Field;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.InventoryItem;

/**
 * A dialog used for creating inventory items.
 * 
 * @author Vincent
 */
public class CreateInventoryItemDialog extends DialogFragment
{
	/**
	 * The inventory item created listener.
	 * 
	 * @author Vincent
	 */
	public static interface ItemCreationListener
	{
		/**
		 * Invoked, when an inventory item was created.
		 * 
		 * @param aItem
		 *            The newly created item.
		 */
		public void itemCreated(InventoryItem aItem);
	}
	
	private static boolean				sDialogOpen	= false;
	
	private final String				mTitle;
	
	private final Context				mContext;
	
	private final ItemCreationListener	mListener;
	
	private EditText					mName;
	
	private EditText					mWeight;
	
	private Button						mOK;
	
	private CreateInventoryItemDialog(final String aTitle, final Context aContext, final ItemCreationListener aListener)
	{
		sDialogOpen = true;
		mTitle = aTitle;
		mContext = aContext;
		mListener = aListener;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		
		final LinearLayout container = (LinearLayout) View.inflate(mContext, R.layout.create_inventory_item_dialog, null);
		mOK = (Button) container.findViewById(R.id.dialog_ok_button);
		mName = (EditText) container.findViewById(R.id.dialog_item_name_text);
		mWeight = (EditText) container.findViewById(R.id.dialog_item_weight_text);
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
		mName.addTextChangedListener(listener);
		mWeight.addTextChangedListener(listener);
		mOK.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mListener.itemCreated(new InventoryItem(mName.getText().toString(), Integer.parseInt(mWeight.getText().toString()), mContext, null));
				dismiss();
			}
		});
		
		builder.setTitle(mTitle).setView(container);
		
		return builder.create();
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
	
	private void updateOKButton()
	{
		boolean enabled = true;
		if (mName.getText().toString().trim().isEmpty())
		{
			enabled = false;
		}
		int weight = -1;
		try
		{
			weight = Integer.parseInt(mWeight.getText().toString());
		}
		catch (final NumberFormatException e)
		{}
		if (weight < 0)
		{
			enabled = false;
		}
		mOK.setEnabled(enabled);
	}
	
	/**
	 * @return whether any dialog is currently open.
	 */
	public static boolean isDialogOpen()
	{
		return sDialogOpen;
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
	public static void showCreateInventoryItemDialog(final String aTitle, final Context aContext, final ItemCreationListener aListener)
	{
		if (sDialogOpen)
		{
			return;
		}
		new CreateInventoryItemDialog(aTitle, aContext, aListener).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
