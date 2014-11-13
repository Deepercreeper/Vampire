package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.CharCreator;
import com.deepercreeper.vampireapp.controller.restrictions.Restriction;
import com.deepercreeper.vampireapp.controller.restrictions.Restrictionable;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * The insanity controller handles the list of insanities and updates all UI views after changes.
 * 
 * @author vrl
 */
public class InsanityController implements Restrictionable
{
	private final List<String>		mInsanities		= new ArrayList<String>();
	
	private TableLayout				mTable;
	
	private final Context			mContext;
	
	private final CharCreator		mCreator;
	
	private final Set<Restriction>	mRestrictions	= new HashSet<Restriction>();
	
	/**
	 * Creates a new insanity controller.
	 * 
	 * @param aContext
	 *            The context.
	 * @param aCreator
	 *            The character creator.
	 */
	public InsanityController(final Context aContext, CharCreator aCreator)
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
			update();
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
	
	private TableRow createRow(final int aIndex)
	{
		final TableRow row = new TableRow(mContext);
		row.setLayoutParams(ViewUtil.instance().getTableWrapHeight());
		
		final String insanity = mInsanities.get(aIndex);
		
		final ImageButton removeButton = new ImageButton(mContext);
		removeButton.setLayoutParams(ViewUtil.instance().getRowButtonSize());
		removeButton.setImageResource(android.R.drawable.ic_menu_delete);
		removeButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				remove(insanity);
			}
		});
		
		row.addView(removeButton);
		
		final TextView name = new TextView(mContext);
		name.setLayoutParams(ViewUtil.instance().getRowNameVeryLong());
		name.setGravity(Gravity.CENTER_VERTICAL);
		name.setSingleLine();
		name.setEllipsize(TruncateAt.END);
		name.setText(insanity);
		
		row.addView(name);
		
		return row;
	}
	
	private void remove(final int aIndex)
	{
		mInsanities.remove(aIndex);
		mTable.removeViewAt(aIndex);
		update();
	}
	
	@Override
	public void removeRestriction(Restriction aRestriction)
	{
		mRestrictions.remove(aRestriction);
		update();
	}
	
	@Override
	public void addRestriction(Restriction aRestriction)
	{
		mRestrictions.add(aRestriction);
		update();
	}
	
	/**
	 * @return whether the restrictions are hold currently.
	 */
	public boolean insanitiesOk()
	{
		return mInsanities.size() >= getMinValue() && mInsanities.size() <= getMaxValue();
	}
	
	private void update()
	{
		mCreator.setInsanitiesOk(insanitiesOk());
	}
	
	@Override
	public Set<Restriction> getRestrictions()
	{
		return mRestrictions;
	}
	
	@Override
	public boolean hasRestrictions()
	{
		return !mRestrictions.isEmpty();
	}
	
	@Override
	public int getMinValue()
	{
		int minValue = -1;
		for (final Restriction restriction : mRestrictions)
		{
			if (restriction.getMinValue() > minValue)
			{
				minValue = restriction.getMinValue();
			}
		}
		return minValue;
	}
	
	@Override
	public int getMaxValue()
	{
		int maxValue = Integer.MAX_VALUE;
		for (final Restriction restriction : mRestrictions)
		{
			if (restriction.getMaxValue() < maxValue)
			{
				maxValue = restriction.getMaxValue();
			}
		}
		return maxValue;
	}
}
