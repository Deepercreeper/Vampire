package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class InsanityController
{
	private final List<String>	mInsanities	= new ArrayList<String>();
	
	private TableLayout			mTable;
	
	private final Context		mContext;
	
	public InsanityController(final Context aContext)
	{
		mContext = aContext;
	}
	
	public void addInsanity(final String aInsanity)
	{
		if ( !mInsanities.contains(aInsanity))
		{
			mInsanities.add(aInsanity);
			final int index = mInsanities.indexOf(aInsanity);
			mTable.addView(createRow(index), index);
		}
	}
	
	public void clear()
	{
		mTable.removeAllViews();
		mInsanities.clear();
	}
	
	public List<String> getInsanities()
	{
		return mInsanities;
	}
	
	public void init(final TableLayout aTable)
	{
		mTable = aTable;
	}
	
	public void release()
	{
		clear();
		mTable = null;
	}
	
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
	}
}
