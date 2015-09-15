package com.deepercreeper.vampireapp.util.view.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.interfaces.Nameable;
import com.deepercreeper.vampireapp.util.view.listeners.ItemSelectionListener;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Used for selecting items from a list and then invoking the given action.
 * 
 * @author Vincent
 * @param <T>
 *            The type of all items that can be selected.
 */
public class SelectItemDialog <T extends Nameable> extends DefaultDialog<ItemSelectionListener<T>, ListView>
{
	private final List<T> mItems = new ArrayList<T>();
	
	private final Map<T, Boolean> mEnabledStates = new HashMap<T, Boolean>();
	
	private final ArrayAdapter<T> mAdapter;
	
	private SelectItemDialog(final List<T> aItems, final String aTitle, final Context aContext, final ItemSelectionListener<T> aAction)
	{
		super(aTitle, aContext, aAction, R.layout.dialog_select_item, ListView.class);
		mItems.addAll(aItems);
		Collections.sort(mItems);
		
		mAdapter = new ArrayAdapter<T>(getContext(), android.R.layout.simple_list_item_1, mItems)
		{
			@Override
			public boolean isEnabled(int aPosition)
			{
				Boolean value = mEnabledStates.get(mAdapter.getItem(aPosition));
				return value == null || value;
			}
			
			@Override
			public boolean areAllItemsEnabled()
			{
				for (int i = 0; i < mItems.size(); i++ )
				{
					if ( !isEnabled(i))
					{
						return false;
					}
				}
				return true;
			}
		};
		getContainer().setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				getListener().select(mItems.get(aPosition));
				dismiss();
			}
		});
		getContainer().setAdapter(mAdapter);
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
	
	/**
	 * Sets whether the given item should be enabled.
	 * 
	 * @param aOption
	 *            The option.
	 * @param aEnabled
	 *            Whether it should be enabled.
	 */
	public void setOptionEnabled(T aOption, boolean aEnabled)
	{
		if (mItems.contains(aOption))
		{
			mEnabledStates.put(aOption, aEnabled);
		}
	}
	
	@Override
	public Dialog createDialog(final Builder aBuilder)
	{
		return aBuilder.create();
	}
	
	@Override
	protected void cancel()
	{
		getListener().cancel();
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
	public static <I extends Nameable> SelectItemDialog<I> createSelectionDialog(final List<I> aItems, final String aTitle, final Context aContext,
			final ItemSelectionListener<I> aAction)
	{
		if (isDialogOpen())
		{
			return null;
		}
		return new SelectItemDialog<I>(aItems, aTitle, aContext, aAction);
	}
	
	/**
	 * @return whether any of this classes dialogs is open.
	 */
	public static boolean isDialogOpen()
	{
		return isDialogOpen(SelectItemDialog.class);
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
	public static <I extends Nameable> SelectItemDialog<I> showSelectionDialog(final I[] aItems, final String aTitle, final Context aContext,
			final ItemSelectionListener<I> aAction)
	{
		if (isDialogOpen())
		{
			return null;
		}
		final SelectItemDialog<I> dialog = new SelectItemDialog<I>(Arrays.asList(aItems), aTitle, aContext, aAction);
		dialog.show(((Activity) aContext).getFragmentManager(), aTitle);
		return dialog;
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
	public static <I extends Nameable> SelectItemDialog<I> showSelectionDialog(final List<I> aItems, final String aTitle, final Context aContext,
			final ItemSelectionListener<I> aAction)
	{
		if (isDialogOpen())
		{
			return null;
		}
		final SelectItemDialog<I> dialog = new SelectItemDialog<I>(aItems, aTitle, aContext, aAction);
		dialog.show(((Activity) aContext).getFragmentManager(), aTitle);
		return dialog;
	}
}
