package com.deepercreeper.vampireapp.lists.items;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Instances of this class represent user given descriptions for one description field each.<br>
 * They are used to define characters more precisely.
 * 
 * @author vrl
 */
public class DescriptionCreation extends Named implements Viewable
{
	private final Description mItem;
	
	private final EditText mContainer;
	
	private String mValue;
	
	/**
	 * Creates a new description value out of the given description type.
	 * 
	 * @param aItem
	 *            The description type.
	 * @param aContext
	 *            The underlying context.
	 */
	public DescriptionCreation(final Description aItem, final Context aContext)
	{
		super(aItem.getName());
		mContainer = (EditText) View.inflate(aContext, R.layout.view_description_creation, null);
		mItem = aItem;
		getContainer().setHint(getDisplayName());
		getContainer().addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(final Editable aS)
			{
				mValue = getContainer().getText().toString();
			}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{
				// Do nothing
			}
			
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{
				// Do nothing
			}
		});
	}
	
	/**
	 * Creates a new description creation for the given item.
	 * 
	 * @param aItem
	 *            The description needing item.
	 * @param aContext
	 *            The underlying context.
	 */
	public DescriptionCreation(final ItemCreation aItem, final Context aContext)
	{
		super(aItem.getName());
		mContainer = (EditText) View.inflate(aContext, R.layout.view_description_creation, null);
		mItem = null;
		getContainer().setHint(getDisplayName());
		getContainer().addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void afterTextChanged(final Editable aS)
			{
				aItem.setDescription(getContainer().getText().toString());
			}
			
			@Override
			public void beforeTextChanged(final CharSequence aS, final int aStart, final int aCount, final int aAfter)
			{
				// Do nothing
			}
			
			@Override
			public void onTextChanged(final CharSequence aS, final int aStart, final int aBefore, final int aCount)
			{
				// Do nothing
			}
		});
	}
	
	@Override
	public EditText getContainer()
	{
		return mContainer;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void updateUI()
	{}
	
	/**
	 * Removes the user defined description.
	 */
	public void clear()
	{
		mValue = "";
		getContainer().setText(mValue);
	}
	
	/**
	 * @return the description type.
	 */
	public Description getItem()
	{
		return mItem;
	}
	
	/**
	 * @return the current user defined description.
	 */
	public String getValue()
	{
		return mValue;
	}
}
