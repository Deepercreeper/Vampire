package com.deepercreeper.vampireapp.controllers.dialog;

import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;

/**
 * Used for selecting items from a list and then invoking the given action.
 * 
 * @author Vincent
 */
public class SelectItemDialog extends DialogFragment
{
	/**
	 * A listener that is invoked when a selection was made.
	 * 
	 * @author Vincent
	 */
	public static interface SelectionListener
	{
		/**
		 * Invoked when the given item was selected.
		 * 
		 * @param aItem
		 *            The item that was selected.
		 */
		public void select(Item aItem);
	}
	
	private static boolean				sDialogOpen	= false;
	
	private final HashMap<String, Item>	mItems		= new HashMap<String, Item>();
	
	private final String[]				mNames;
	
	private final String				mTitle;
	
	private final Context				mContext;
	
	private final SelectionListener		mAction;
	
	private SelectItemDialog(final List<Item> aItems, final String aTitle, final Context aContext, final SelectionListener aAction)
	{
		sDialogOpen = true;
		mNames = new String[aItems.size()];
		for (int i = 0; i < mNames.length; i++ )
		{
			final Item item = aItems.get(i);
			mItems.put(item.getName(), item);
			mNames[i] = item.getName();
		}
		mTitle = aTitle;
		mContext = aContext;
		mAction = aAction;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mTitle).setItems(mNames, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(final DialogInterface dialog, final int which)
			{
				mAction.select(mItems.get(mNames[which]));
			}
		});
		return builder.create();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		sDialogOpen = false;
	}
	
	/**
	 * @return whether any dialog is open at this time.
	 */
	public static boolean isDialogOpen()
	{
		return sDialogOpen;
	}
	
	/**
	 * Shows an item selection dialog that invokes the selection listener when any item was selected.<br>
	 * Only one dialog can be shown at one time.
	 * 
	 * @param aItems
	 *            A list of items that are able to be selected inside the dialog.
	 * @param aTitle
	 *            The dialog title.
	 * @param aContext
	 *            The context.
	 * @param aAction
	 *            The selection action.
	 */
	public static void showSelectionDialog(final List<Item> aItems, final String aTitle, final Context aContext, final SelectionListener aAction)
	{
		if (sDialogOpen)
		{
			return;
		}
		new SelectItemDialog(aItems, aTitle, aContext, aAction).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
