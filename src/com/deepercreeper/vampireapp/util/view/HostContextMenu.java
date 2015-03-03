package com.deepercreeper.vampireapp.util.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

public class HostContextMenu extends DialogFragment
{
	private static final String	TAG	= "HostContextMenu";
	
	public static interface HostListener
	{
		public void deleteHost(String aName);
		
		public void playHost(String aName);
	}
	
	private enum Action
	{
		// TODO Action position
		PLAY(R.string.play_host), DELETE(R.string.delete_host);
		
		private final int	mResId;
		
		private Action(final int aResId)
		{
			mResId = aResId;
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
		final List<String> items = new ArrayList<String>();
		final Map<String, Action> actions = new HashMap<String, Action>();
		for (final Action action : Action.values())
		{
			final String item = mContext.getString(action.getResId());
			items.add(item);
			actions.put(item, action);
		}
		Collections.sort(items);
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mName).setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(final DialogInterface dialog, final int which)
			{
				selectItem(actions.get(items.get(which)));
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
			case PLAY :
				mListener.playHost(mName);
				break;
		}
	}
	
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
