package com.deepercreeper.vampireapp.util.view;

import java.util.Arrays;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import com.deepercreeper.vampireapp.items.interfaces.Nameable;

/**
 * Used for selecting items from a list and then invoking the given action.
 * 
 * @author Vincent
 */
public class SelectItemDialog <T extends Nameable> extends DialogFragment
{
	/**
	 * A listener that is invoked when a selection was made.
	 * 
	 * @author Vincent
	 * @param <S>
	 *            The type of nameable that is selected.
	 */
	public static interface NamableSelectionListener <S extends Nameable>
	{
		public void select(S aDevice);
	}
	
	private static boolean						sDialogOpen	= false;
	
	private final T[]							mNamables;
	
	private final String						mTitle;
	
	private final Context						mContext;
	
	private final NamableSelectionListener<T>	mAction;
	
	private SelectItemDialog(final T[] aNamables, final String aTitle, final Context aContext, final NamableSelectionListener<T> aAction)
	{
		sDialogOpen = true;
		mNamables = aNamables;
		Arrays.sort(mNamables);
		mTitle = aTitle;
		mContext = aContext;
		mAction = aAction;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mTitle).setItems(mNamables, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(final DialogInterface dialog, final int which)
			{
				mAction.select(mNamables[which]);
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
		// TODO Check occurrences of showDialog
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
	public static <R extends Nameable> void showSelectionDialog(final R[] aItems, final String aTitle, final Context aContext,
			final NamableSelectionListener<R> aAction)
	{
		if (sDialogOpen)
		{
			return;
		}
		new SelectItemDialog<R>(aItems, aTitle, aContext, aAction).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
