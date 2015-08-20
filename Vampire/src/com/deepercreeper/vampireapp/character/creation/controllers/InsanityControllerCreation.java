package com.deepercreeper.vampireapp.character.creation.controllers;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.RestrictionableCreationImpl;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateStringDialog;
import com.deepercreeper.vampireapp.util.view.listeners.StringCreationListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * The insanity controller handles the list of insanities and updates all UI views after changes.
 * 
 * @author vrl
 */
public class InsanityControllerCreation extends RestrictionableCreationImpl
{
	private final List<InsanityCreation> mInsanities = new ArrayList<InsanityCreation>();
	
	private final LinearLayout mContainer;
	
	private final Context mContext;
	
	private final CharacterCreation mChar;
	
	/**
	 * Creates a new insanity controller.
	 * 
	 * @param aContext
	 *            The context.
	 * @param aChar
	 *            The character creator.
	 */
	public InsanityControllerCreation(final Context aContext, final CharacterCreation aChar)
	{
		mContext = aContext;
		mChar = aChar;
		
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_insanity_controller_creation, null);
		
		final Button addInsanity = (Button) getContainer().findViewById(R.id.view_add_insanity_button);
		addInsanity.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				addInsanity();
			}
		});
	}
	
	/**
	 * Asks the user for a name for the new insanity.
	 */
	public void addInsanity()
	{
		final StringCreationListener listener = new StringCreationListener()
		{
			@Override
			public void create(final String aString)
			{
				addInsanity(aString);
			}
		};
		CreateStringDialog.showCreateStringDialog(mContext.getString(R.string.add_insanity), mContext.getString(R.string.add_insanity_message),
				mContext, listener);
	}
	
	/**
	 * Adds a new insanity to the list.
	 * 
	 * @param aInsanity
	 *            The new insanity.
	 */
	public void addInsanity(final String aInsanity)
	{
		InsanityCreation insanity = new InsanityCreation(aInsanity, mContext, this);
		if ( !mInsanities.contains(insanity))
		{
			mInsanities.add(insanity);
			final int index = mInsanities.indexOf(insanity);
			mContainer.addView(insanity.getContainer(), index);
			updateUI();
		}
	}
	
	/**
	 * Removes all insanities and updates the UI.
	 */
	public void clear()
	{
		for (InsanityCreation insanity : getInsanities())
		{
			insanity.release();
		}
		mInsanities.clear();
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return a list of all insanities.
	 */
	public List<InsanityCreation> getInsanities()
	{
		return mInsanities;
	}
	
	/**
	 * @return whether the number of insanities is possible for creation currently.
	 */
	public boolean isOk()
	{
		return isValueOk(mInsanities.size(), CreationRestrictionType.INSANITY);
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Removes the given insanity from the list.
	 * 
	 * @param aInsanity
	 *            The insanity to remove.
	 */
	public void remove(final InsanityCreation aInsanity)
	{
		aInsanity.release();
		mInsanities.remove(aInsanity);
		updateUI();
	}
	
	@Override
	public void updateUI()
	{
		mChar.setInsanitiesOk(isOk());
	}
}
