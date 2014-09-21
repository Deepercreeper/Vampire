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

public class SelectSubDisciplineDialog extends DialogFragment
{
	private final CharCreator	mCreator;
	
	private final Discipline	mDiscipline;
	
	private final MainActivity	mParent;
	
	private final boolean		mFirst;
	
	private final GridLayout	mGrid;
	
	public SelectSubDisciplineDialog(final GridLayout aGrid, final CharCreator aCreator, final Discipline aDiscipline,
			final MainActivity aParent, final boolean aFirst)
	{
		mGrid = aGrid;
		mCreator = aCreator;
		mDiscipline = aDiscipline;
		mParent = aParent;
		mFirst = aFirst;
	}
	
	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState)
	{
		final List<String> subDisciplinesList = new ArrayList<>();
		subDisciplinesList.addAll(mDiscipline.getSubDisciplineNames());
		if (mCreator.getDiscipline(mDiscipline).hasSubDiscipline( !mFirst))
		{
			subDisciplinesList.remove(mCreator.getDiscipline(mDiscipline).getSubDiscipline( !mFirst).getDiscipline().getName());
		}
		final String[] subDisciplines = subDisciplinesList.toArray(new String[subDisciplinesList.size()]);
		Arrays.sort(subDisciplines);
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(mParent);
		builder.setTitle((mFirst ? "1. " : "2. ") + mDiscipline.getName() + " " + getResources().getString(R.string.discipline)).setItems(
				subDisciplines, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(final DialogInterface dialog, final int which)
					{
						mCreator.getDiscipline(mDiscipline).setSubDiscipline(
								new CreationDiscipline(mDiscipline.getSubDiscipline(subDisciplines[which])), mFirst);
						mParent.applySubDisciplines(mGrid, mDiscipline, mFirst);
					}
				});
		return builder.create();
	}
}
