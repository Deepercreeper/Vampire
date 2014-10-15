package com.deepercreeper.vampireapp.controller;

import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class SelectItemDialog <T extends Item> extends DialogFragment
{
	private static boolean	sDialogOpen	= false;
	
	public static interface SelectionListener <S extends Item>
	{
		public void select(S aItem);
	}
	
	private final HashMap<String, T>	mItems	= new HashMap<String, T>();
	
	private final String[]				mNames;
	
	private final String				mTitle;
	
	private final Context				mContext;
	
	private final SelectionListener<T>	mAction;
	
	private SelectItemDialog(final List<T> aItems, final String aTitle, final Context aContext, final SelectionListener<T> aAction)
	{
		sDialogOpen = true;
		mNames = new String[aItems.size()];
		for (int i = 0; i < mNames.length; i++ )
		{
			final T item = aItems.get(i);
			mItems.put(item.getName(), item);
			mNames[i] = item.getName();
		}
		mTitle = aTitle;
		mContext = aContext;
		mAction = aAction;
		
		if ( !(aContext instanceof Activity))
		{
			throw new IllegalStateException("The current context is no activity!");
		}
	}
	
	public static <S extends Item> void showSelectionDialog(final List<S> aItems, final String aTitle, final Context aContext,
			final SelectionListener<S> aAction)
	{
		new SelectItemDialog<S>(aItems, aTitle, aContext, aAction).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
	
	public static boolean isDialogOpen()
	{
		return sDialogOpen;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		sDialogOpen = false;
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
}
