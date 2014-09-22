package com.deepercreeper.vampireapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.GridLayout;

public class SelectBackgroundDialog extends DialogFragment
{
	private final CharCreator	mCreator;
	
	private final MainActivity	mParent;
	
	private final Background	mOldBackground;
	
	private final ItemHandler	mItems;
	
	private final GridLayout	mGrid;
	
	private final boolean		mAdd;
	
	public SelectBackgroundDialog(final CharCreator aCreator, final MainActivity aParent, final ItemHandler aItems)
	{
		mCreator = aCreator;
		mParent = aParent;
		mItems = aItems;
		mGrid = null;
		mOldBackground = null;
		mAdd = true;
	}
	
	public SelectBackgroundDialog(final GridLayout aGrid, final CharCreator aCreator, final MainActivity aParent, final ItemHandler aItems,
			final Background aOldBackground)
	{
		mCreator = aCreator;
		mParent = aParent;
		mItems = aItems;
		mGrid = aGrid;
		mOldBackground = aOldBackground;
		mAdd = false;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState)
	{
		final List<String> backgroundsList = new ArrayList<String>();
		for (final Background background : mItems.getBackgrounds())
		{
			backgroundsList.add(background.getName());
		}
		for (final Background background : mCreator.getBackgrounds())
		{
			backgroundsList.remove(background.getName());
		}
		final String[] backgrounds = backgroundsList.toArray(new String[backgroundsList.size()]);
		Arrays.sort(backgrounds);
		final AlertDialog.Builder builder = new AlertDialog.Builder(mParent);
		builder.setTitle(getResources().getString(mAdd ? R.string.add_background : R.string.edit_background)).setItems(backgrounds,
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(final DialogInterface dialog, final int which)
					{
						mCreator.addBackground(new CreationBackground(mItems.getBackground(backgrounds[which])));
						if (mAdd)
						{
							addBackground(backgrounds[which]);
						}
						else
						{
							editBackground(backgrounds[which]);
						}
					}
				});
		return builder.create();
	}
	
	private void editBackground(final String aBackground)
	{
		if (aBackground.equals(mOldBackground.getName()))
		{
			return;
		}
		mCreator.removeBackground(mOldBackground);
		mParent.setBackgroundRow(mGrid, mItems.getBackground(aBackground));
	}
	
	private void addBackground(final String aBackground)
	{
		mParent.addBackgroundRow(mItems.getBackground(aBackground));
	}
}
