package com.deepercreeper.vampireapp.util.view;

import java.util.Collections;
import java.util.List;
import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
		
		public void cancel();
	}
	
	private static boolean						sDialogOpen	= false;
	
	private final List<T>						mNamables;
	
	private final ArrayAdapter<T>				mAdapter;
	
	private final ListView						mView;
	
	private final String						mTitle;
	
	private final Context						mContext;
	
	private final NamableSelectionListener<T>	mAction;
	
	private SelectItemDialog(final List<T> aItems, final String aTitle, final Context aContext, final NamableSelectionListener<T> aAction)
	{
		sDialogOpen = true;
		mNamables = aItems;
		Collections.sort(mNamables);
		mTitle = aTitle;
		mContext = aContext;
		mAction = aAction;
		
		mView = new ListView(aContext);
		mAdapter = new ArrayAdapter<T>(mContext, R.layout.simple_list_item_1, mNamables);
		mView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> aParent, View aView, int aPosition, long aId)
			{
				mAction.select(mNamables.get(aPosition));
			}
		});
		mView.setAdapter(mAdapter);
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mTitle).setView(mView);
		return builder.create();
	}
	
	@Override
	public void onCancel(DialogInterface aDialog)
	{
		
		super.onCancel(aDialog);
	}
	
	public void addOption(T aOption)
	{
		mAdapter.add(aOption);
	}
	
	public void removeOption(T aOption)
	{
		mAdapter.remove(aOption);
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
	 * Creates an item selection dialog that invokes the selection listener when any item was selected.<br>
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
	 * @return the created dialog.
	 */
	public static <R extends Nameable> SelectItemDialog<R> createSelectionDialog(final List<R> aItems, final String aTitle, final Context aContext,
			final NamableSelectionListener<R> aAction)
	{
		if (sDialogOpen)
		{
			return null;
		}
		return new SelectItemDialog<R>(aItems, aTitle, aContext, aAction);
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
	 * @return the created dialog.
	 */
	public static <R extends Nameable> SelectItemDialog<R> showSelectionDialog(final List<R> aItems, final String aTitle, final Context aContext,
			final NamableSelectionListener<R> aAction)
	{
		if (sDialogOpen)
		{
			return null;
		}
		SelectItemDialog<R> dialog = new SelectItemDialog<R>(aItems, aTitle, aContext, aAction);
		dialog.show(((Activity) aContext).getFragmentManager(), aTitle);
		return dialog;
	}
}
