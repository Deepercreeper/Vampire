package com.deepercreeper.vampireapp.lists.controllers.creations;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.CreationRestrictionableImpl;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;

/**
 * The insanity controller handles the list of insanities and updates all UI views after changes.
 * 
 * @author vrl
 */
public class InsanityControllerCreation extends CreationRestrictionableImpl
{
	private final List<String>		mInsanities	= new ArrayList<String>();
	
	private TableLayout				mTable;
	
	private final Context			mContext;
	
	private final CharacterCreation	mCreator;
	
	/**
	 * Creates a new insanity controller.
	 * 
	 * @param aContext
	 *            The context.
	 * @param aCreator
	 *            The character creator.
	 */
	public InsanityControllerCreation(final Context aContext, final CharacterCreation aCreator)
	{
		mContext = aContext;
		mCreator = aCreator;
	}
	
	/**
	 * Adds a new insanity to the list.
	 * 
	 * @param aInsanity
	 *            The new insanity.
	 */
	public void addInsanity(final String aInsanity)
	{
		if ( !mInsanities.contains(aInsanity))
		{
			mInsanities.add(aInsanity);
			final int index = mInsanities.indexOf(aInsanity);
			mTable.addView(createRow(index), index);
			updateRestrictions();
		}
	}
	
	/**
	 * Removes all insanities and updates the UI.
	 */
	public void clear()
	{
		mTable.removeAllViews();
		mInsanities.clear();
	}
	
	/**
	 * @return a list of all insanities.
	 */
	public List<String> getInsanities()
	{
		return mInsanities;
	}
	
	/**
	 * Uses the given table as UI container and adds insanity representing view to it.
	 * 
	 * @param aTable
	 *            The table.
	 */
	public void init(final TableLayout aTable)
	{
		mTable = aTable;
	}
	
	/**
	 * @return whether the number of insanities is possible for creation currently.
	 */
	public boolean isOk()
	{
		return isValueOk(mInsanities.size(), CreationRestrictionType.INSANITY);
	}
	
	/**
	 * Releases the current table.
	 */
	public void release()
	{
		clear();
		mTable = null;
	}
	
	/**
	 * Removes the given insanity from the list.
	 * 
	 * @param aInsanity
	 *            The insanity to remove.
	 */
	public void remove(final String aInsanity)
	{
		if (mInsanities.contains(aInsanity))
		{
			remove(mInsanities.indexOf(aInsanity));
		}
	}
	
	@Override
	public void updateRestrictions()
	{
		mCreator.setInsanitiesOk(isOk());
	}
	
	private TableRow createRow(final int aIndex)
	{
		final String insanity = mInsanities.get(aIndex);
		
		final TableRow row = (TableRow) View.inflate(mContext, R.layout.insanity_row, null);
		row.findViewById(R.id.remove_button).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				remove(insanity);
			}
		});
		((TextView) View.inflate(mContext, R.id.insanity_text, null)).setText(insanity);
		
		return row;
	}
	
	private void remove(final int aIndex)
	{
		mInsanities.remove(aIndex);
		mTable.removeViewAt(aIndex);
		updateRestrictions();
	}
}
