package com.deepercreeper.vampireapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.GridLayout;

public class SelectPropertyDialog extends DialogFragment
{
	private final CharCreator	mCreator;
	
	private final MainActivity	mParent;
	
	private final Property		mOldProperty;
	
	private final ItemHandler	mItems;
	
	private final GridLayout	mGrid;
	
	private final boolean		mAdd;
	
	public SelectPropertyDialog(final CharCreator aCreator, final MainActivity aParent, final ItemHandler aItems)
	{
		mCreator = aCreator;
		mParent = aParent;
		mItems = aItems;
		mGrid = null;
		mOldProperty = null;
		mAdd = true;
	}
	
	public SelectPropertyDialog(final GridLayout aGrid, final CharCreator aCreator, final MainActivity aParent, final ItemHandler aItems,
			final Property aOldProperty)
	{
		mCreator = aCreator;
		mParent = aParent;
		mItems = aItems;
		mGrid = aGrid;
		mOldProperty = aOldProperty;
		mAdd = false;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState)
	{
		final List<String> propertiesList = new ArrayList<String>();
		for (final Property property : mItems.getProperties())
		{
			propertiesList.add((property.isNegative() ? "(-) " : "(+) ") + property.getName());
		}
		for (final Property property : mCreator.getProperties())
		{
			propertiesList.remove((property.isNegative() ? "(-) " : "(+) ") + property.getName());
		}
		final String[] properties = propertiesList.toArray(new String[propertiesList.size()]);
		Arrays.sort(properties);
		final AlertDialog.Builder builder = new AlertDialog.Builder((Context) mParent);
		builder.setTitle(getResources().getString(mAdd ? R.string.add_property : R.string.edit_property)).setItems(properties,
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(final DialogInterface dialog, final int which)
					{
						final String propertyName = properties[which].substring(4);
						mCreator.addProperty(new CreationProperty(mItems.getProperty(propertyName)));
						if (mAdd)
						{
							addProperty(propertyName);
						}
						else
						{
							editProperty(propertyName);
						}
					}
				});
		return builder.create();
	}
	
	private void editProperty(final String aProperty)
	{
		if (aProperty.equals(mOldProperty.getName()))
		{
			return;
		}
		mCreator.removeProperty(mOldProperty);
		mParent.setPropertyRow(mGrid, mItems.getProperty(aProperty));
	}
	
	private void addProperty(final String aProperty)
	{
		mParent.addPropertyRow(mItems.getProperty(aProperty));
	}
}
