package com.deepercreeper.vampireapp.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.EditText;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class CreateStringDialog extends DialogFragment
{
	private static boolean			sDialogOpen	= false;
	
	private final String			mTitle;
	
	private final String			mMessage;
	
	private final EditText			mValue;
	
	private final Context			mContext;
	
	private final CreationListener	mListener;
	
	public interface CreationListener
	{
		public void create(String aString);
	}
	
	public CreateStringDialog(final String aTitle, final String aMessage, final Context aContext, final CreationListener aListener)
	{
		sDialogOpen = true;
		mTitle = aTitle;
		mMessage = aMessage;
		mContext = aContext;
		mValue = new EditText(mContext);
		mValue.setLayoutParams(ViewUtil.instance().getWrapHeight());
		mValue.setSingleLine();
		mListener = aListener;
		
		if ( !(aContext instanceof Activity))
		{
			throw new IllegalStateException("The current context is no activity!");
		}
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mTitle).setMessage(mMessage).setView(mValue).setPositiveButton("OK", new OnClickListener()
		{
			@Override
			public void onClick(final DialogInterface aDialog, final int aWhich)
			{
				mListener.create(mValue.getText().toString());
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
	
	public static void showCreateStringDialog(final String aTitle, final String aMessage, final Context aContext, final CreationListener aListener)
	{
		if (sDialogOpen)
		{
			return;
		}
		new CreateStringDialog(aTitle, aMessage, aContext, aListener).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
