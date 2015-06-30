package com.deepercreeper.vampireapp.util.view;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.Log;

/**
 * Each host has a context menu that contains options that can be done.
 * 
 * @author vrl
 */
public class HostContextMenu extends DialogFragment
{
	private static final String	TAG	= "HostContextMenu";
	
	/**
	 * A listener that is invoked, when host context menu events occur.
	 * 
	 * @author vrl
	 */
	public static interface HostListener
	{
		/**
		 * The selected host is going to be deleted.
		 * 
		 * @param aName
		 *            The host name.
		 */
		public void deleteHost(String aName);
		
		/**
		 * The host is going to be started.
		 * 
		 * @param aName
		 *            The host name.
		 */
		public void playHost(String aName);
	}
	
	private enum Action
	{
		DELETE(R.string.delete_host, 0);
		
		private final int	mResId;
		
		private final int	mIndex;
		
		private Action(final int aResId, final int aIndex)
		{
			mResId = aResId;
			mIndex = aIndex;
		}
		
		public int getResId()
		{
			return mResId;
		}
	}
	
	private static boolean		sDialogOpen	= false;
	
	private final HostListener	mListener;
	
	private final Context		mContext;
	
	private final String		mName;
	
	private HostContextMenu(final HostListener aListener, final Context aContext, final String aName)
	{
		sDialogOpen = true;
		mContext = aContext;
		mListener = aListener;
		mName = aName;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		final String[] actionNames = new String[Action.values().length];
		
		final Map<String, Action> actions = new HashMap<String, Action>();
		for (final Action action : Action.values())
		{
			final String item = mContext.getString(action.getResId());
			actionNames[action.mIndex] = item;
			actions.put(item, action);
		}
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mName).setItems(actionNames, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(final DialogInterface dialog, final int which)
			{
				selectItem(actions.get(actionNames[which]));
			}
		});
		return builder.create();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		sDialogOpen = false;
	}
	
	/**
	 * @return whether any dialog is open at this time.
	 */
	public static boolean isDialogOpen()
	{
		return sDialogOpen;
	}
	
	private void selectItem(final Action aAction)
	{
		switch (aAction)
		{
			case DELETE :
				mListener.deleteHost(mName);
				break;
		}
	}
	
	/**
	 * Shows the context menu for the given host.
	 * 
	 * @param aListener
	 *            The event listener.
	 * @param aContext
	 *            The underlying context.
	 * @param aName
	 *            The host name.
	 */
	public static void showCharacterContextMenu(final HostListener aListener, final Activity aContext, final String aName)
	{
		if (sDialogOpen)
		{
			Log.i(TAG, "Tried to open a dialog twice.");
			return;
		}
		new HostContextMenu(aListener, aContext, aName).show((aContext).getFragmentManager(), aName);
	}
	
}
