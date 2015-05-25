package com.deepercreeper.vampireapp.util.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import com.deepercreeper.vampireapp.R;

public class YesNoDialog extends DialogFragment
{
	public static interface YesNoListener
	{
		public void select(boolean aYes);
	}
	
	private static boolean		sDialogOpen	= false;
	
	private final String		mTitle;
	
	private final String		mMessage;
	
	private final Context		mContext;
	
	private final YesNoListener	mAction;
	
	public YesNoDialog(final String aTitle, final String aMessage, final Context aContext, final YesNoListener aAction)
	{
		mTitle = aTitle;
		mMessage = aMessage;
		mContext = aContext;
		mAction = aAction;
	}
	
	/**
	 * @return whether any dialog is open at this time.
	 */
	public static boolean isDialogOpen()
	{
		return sDialogOpen;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(mMessage).setTitle(mTitle).setPositiveButton(R.string.yes, new OnClickListener()
		{
			@Override
			public void onClick(final DialogInterface aDialog, final int aWhich)
			{
				mAction.select(true);
			}
		}).setNegativeButton(R.string.no, new OnClickListener()
		{
			
			@Override
			public void onClick(final DialogInterface aDialog, final int aWhich)
			{
				mAction.select(false);
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
	
	public static void showYesNoDialog(final String aTitle, final String aMessage, final Context aContext, final YesNoListener aAction)
	{
		if (sDialogOpen)
		{
			return;
		}
		new YesNoDialog(aTitle, aMessage, aContext, aAction).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
