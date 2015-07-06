package com.deepercreeper.vampireapp.util.view.dialogs;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateStringDialog.CreationListener;

/**
 * Used for creating a string and returning it to the given listener.
 * 
 * @author vrl
 */
public class CreateStringDialog extends DefaultDialog<CreationListener, EditText>
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
		if (isDialogOpen())
		{
			return;
		}
		new CreateStringDialog(aTitle, aMessage, aContext, aListener).show(((Activity) aContext).getFragmentManager(), aTitle);
	}
	
	private final String	mMessage;
	
	private CreateStringDialog(final String aTitle, final String aMessage, final Context aContext, final CreationListener aListener)
	{
		super(aTitle, aContext, aListener, R.layout.dialog_create_string, EditText.class);
		mMessage = aMessage;
	}
	
	@Override
	public Dialog createDialog(final Builder aBuilder)
	{
		return aBuilder.setMessage(mMessage).setPositiveButton("OK", new OnClickListener()
		{
			@Override
			public void onClick(final DialogInterface aDialog, final int aWhich)
			{
				getListener().create(getContainer().getText().toString());
			}
		}).create();
	}
}
