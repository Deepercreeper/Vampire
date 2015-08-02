package com.deepercreeper.vampireapp.character.creation;

import java.util.ArrayList;
import java.util.List;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.CreationRestrictionableImpl;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateStringDialog;
import com.deepercreeper.vampireapp.util.view.listeners.StringCreationListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The insanity controller handles the list of insanities and updates all UI views after changes.
 * 
 * @author vrl
 */
public class InsanityControllerCreation extends CreationRestrictionableImpl implements Viewable
{
	private final List<String> mInsanities = new ArrayList<String>();
	
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
		
		init();
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
			mContainer.addView(createInsanityPanel(index), index);
			updateRestrictions();
		}
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
	
	@Override
	public void init()
	{
		Button addInsanity = (Button) getContainer().findViewById(R.id.view_add_insanity_button);
		addInsanity.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View aV)
			{
				addInsanity();
			}
		});
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Removes all insanities and updates the UI.
	 */
	public void clear()
	{
		getContainer().removeAllViews();
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
	 * @return whether the number of insanities is possible for creation currently.
	 */
	public boolean isOk()
	{
		return isValueOk(mInsanities.size(), CreationRestrictionType.INSANITY);
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
		mChar.setInsanitiesOk(isOk());
	}
	
	private LinearLayout createInsanityPanel(final int aIndex)
	{
		final String insanity = mInsanities.get(aIndex);
		
		final LinearLayout insanityPanel = (LinearLayout) View.inflate(mContext, R.layout.view_insanity, null);
		insanityPanel.findViewById(R.id.view_remove_insanity_button).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				remove(insanity);
			}
		});
		((TextView) insanityPanel.findViewById(R.id.view_insanity_label)).setText(insanity);
		
		return insanityPanel;
	}
	
	private void remove(final int aIndex)
	{
		mInsanities.remove(aIndex);
		mContainer.removeViewAt(aIndex);
		updateRestrictions();
	}
}
