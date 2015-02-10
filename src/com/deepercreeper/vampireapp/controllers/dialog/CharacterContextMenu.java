package com.deepercreeper.vampireapp.controllers.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.Vampire;
import com.deepercreeper.vampireapp.util.Log;

public class CharacterContextMenu extends DialogFragment
{
	private static final String	TAG	= "CharacterContextMenu";
	
	private enum Action
	{
		DELETE(R.string.delete_char);
		
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
	
	private static boolean	sDialogOpen	= false;
	
	private final Vampire	mVampire;
	
	private final String	mName;
	
	private CharacterContextMenu(final Vampire aVampire, final String aName)
	{
		sDialogOpen = true;
		mVampire = aVampire;
		mName = aName;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle aSavedInstanceState)
	{
		final List<String> items = new ArrayList<String>();
		final Map<String, Action> actions = new HashMap<String, Action>();
		for (final Action action : Action.values())
		{
			final String item = mVampire.getContext().getString(action.getResId());
			items.add(item);
			actions.put(item, action);
		}
		Collections.sort(items);
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(mVampire.getContext());
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
				mVampire.deleteChar(mName);
				break;
		}
	}
	
	public static void showCharacterContextMenu(final Vampire aVampire, final String aName)
	{
		if (sDialogOpen)
		{
			Log.i(TAG, "Tried to open a dialog twice.");
			return;
		}
		new CharacterContextMenu(aVampire, aName).show(((Activity) aVampire.getContext()).getFragmentManager(), aName);
	}
}
