package com.deepercreeper.vampireapp.util.view.dialogs;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
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
 * @param <T>
 *            The type of all items that can be selected.
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
	public static interface SelectionListener <S extends Nameable>
	{
		/**
		 * Invoked when the user hit the back button or has touched beside the dialog.
		 */
		public void cancel();
		
		/**
		 * Invoked when any option was selected.
		 * 
		 * @param aItem
		 *            The selected item.
		 */
		public void select(S aItem);
	}
	
	private static boolean				sDialogOpen	= false;
	
	private final List<T>				mItems;
	
	private final ArrayAdapter<T>		mAdapter;
	
	private final ListView				mView;
	
	private final String				mTitle;
	
	private final Context				mContext;
	
	private final SelectionListener<T>	mAction;
	
	private SelectItemDialog(final List<T> aItems, final String aTitle, final Context aContext, final SelectionListener<T> aAction)
	{
		sDialogOpen = true;
		mItems = aItems;
		Collections.sort(mItems);
		mTitle = aTitle;
		mContext = aContext;
		mAction = aAction;
		
		mView = new ListView(aContext);
		mAdapter = new ArrayAdapter<T>(mContext, android.R.layout.simple_list_item_1, mItems);
		mView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				mAction.select(mItems.get(aPosition));
				dismiss();
			}
		});
		mView.setAdapter(mAdapter);
	}
	
	/**
	 * Adds the given option to the list of items.
	 * 
	 * @param aOption
	 *            The option to add.
	 */
	public void addOption(final T aOption)
	{
		if ( !mItems.contains(aOption))
		{
			mAdapter.add(aOption);
		}
	}
	
	@Override
	public void onCancel(final DialogInterface aDialog)
	{
		mAction.cancel();
		super.onCancel(aDialog);
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mTitle).setView(mView);
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
	
	/**
	 * Removes the given option from the items list.
	 * 
	 * @param aOption
	 *            The option to remove.
	 */
	public void removeOption(final T aOption)
	{
		mAdapter.remove(aOption);
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
			final SelectionListener<R> aAction)
	{
		if (sDialogOpen)
		{
			return null;
		}
		return new SelectItemDialog<R>(aItems, aTitle, aContext, aAction);
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
	 * @return the created dialog.
	 */
	public static <R extends Nameable> SelectItemDialog<R> showSelectionDialog(final List<R> aItems, final String aTitle, final Context aContext,
			final SelectionListener<R> aAction)
	{
		if (sDialogOpen)
		{
			return null;
		}
		final SelectItemDialog<R> dialog = new SelectItemDialog<R>(aItems, aTitle, aContext, aAction);
		dialog.show(((Activity) aContext).getFragmentManager(), aTitle);
		return dialog;
	}
}
