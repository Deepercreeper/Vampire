package com.deepercreeper.vampireapp.util.view;

import java.lang.reflect.Field;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import com.deepercreeper.vampireapp.R;

/**
 * Sometimes the user has to answer a yes no question. This dialog allows to ask those questions.
 * 
 * @author vrl
 */
public class YesNoDialog extends DialogFragment
{
	/**
	 * This listener listens for the answer of the user.
	 * 
	 * @author vrl
	 */
	public static interface YesNoListener
	{
		/**
		 * Invoked when the user hits the yes or the no button.
		 * 
		 * @param aYes
		 *            Whether yes or no was hit.
		 */
		public void select(boolean aYes);
	}
	
	private static boolean		sDialogOpen	= false;
	
	private final String		mTitle;
	
	private final String		mMessage;
	
	private final Context		mContext;
	
	private final YesNoListener	mAction;
	
	private YesNoDialog(final String aTitle, final String aMessage, final Context aContext, final YesNoListener aAction)
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
	
	@Override
	public void onDetach()
	{
		super.onDetach();
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
	 * Shows a yes no dialog with the given specifications.
	 * 
	 * @param aTitle
	 *            The dialog title.
	 * @param aMessage
	 *            The message that should be shown.
	 * @param aContext
	 *            The underlying context.
	 * @param aAction
	 *            The result action.
	 */
	public static void showYesNoDialog(final String aTitle, final String aMessage, final Context aContext, final YesNoListener aAction)
	{
		if (sDialogOpen)
		{
			return;
		}
		new YesNoDialog(aTitle, aMessage, aContext, aAction).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
}
