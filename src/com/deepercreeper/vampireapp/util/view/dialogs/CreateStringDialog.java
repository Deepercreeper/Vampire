package com.deepercreeper.vampireapp.util.view.dialogs;

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
import android.widget.EditText;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * Used for creating a string and returning it to the given listener.
 * 
 * @author vrl
 */
public class CreateStringDialog extends DialogFragment
{
	/**
	 * A listener that is invoked when a string was created.
	 * 
	 * @author vrl
	 */
	public interface CreationListener
	{
		/**
		 * Invoked when the given string was created.
		 * 
		 * @param aString
		 *            The new created string.
		 */
		public void create(String aString);
	}
	
	/**
	 * @return whether any dialog is open at this time.
	 */
	public static boolean isDialogOpen()
	{
		return sDialogOpen;
	}
	
	/**
	 * Shows a create string dialog that returns the new created string to the listener.<br>
	 * Only one dialog can be shown at one time.
	 * 
	 * @param aTitle
	 *            The dialog title.
	 * @param aMessage
	 *            The dialog message.
	 * @param aContext
	 *            The context.
	 * @param aListener
	 *            The string creation listener.
	 */
	public static void showCreateStringDialog(final String aTitle, final String aMessage, final Context aContext, final CreationListener aListener)
	{
		if (sDialogOpen)
		{
			return;
		}
		new CreateStringDialog(aTitle, aMessage, aContext, aListener).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
	
	private static boolean			sDialogOpen	= false;
	
	private final String			mTitle;
	
	private final String			mMessage;
	
	private final EditText			mValue;
	
	private final Context			mContext;
	
	private final CreationListener	mListener;
	
	private CreateStringDialog(final String aTitle, final String aMessage, final Context aContext, final CreationListener aListener)
	{
		sDialogOpen = true;
		mTitle = aTitle;
		mMessage = aMessage;
		mContext = aContext;
		mValue = new EditText(mContext);
		mValue.setLayoutParams(ViewUtil.getWrapHeight());
		mValue.setSingleLine();
		mListener = aListener;
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
}
