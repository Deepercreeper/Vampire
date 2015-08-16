package com.deepercreeper.vampireapp.character.instance;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import com.deepercreeper.vampireapp.util.view.CharacterContextMenu;
import com.deepercreeper.vampireapp.util.view.CharacterContextMenu.CharacterListener;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A container for characters, that displays main information about it and prevents the system from<br>
 * loading all characters after system start.
 * 
 * @author vrl
 */
public class CharacterCompound implements Comparable<CharacterCompound>, Viewable
{
	private final Activity mContext;
	
	private final String mName;
	
	private final String mConcept;
	
	private final int mGeneration;
	
	private final String mNature;
	
	private final String mBehavior;
	
	private final int mEP;
	
	private final CharacterListener mListener;
	
	private Button mPlay;
	
	private LinearLayout mContainer;
	
	private long mLastUsed;
	
	/**
	 * Creates a new character compound out of the existing character.
	 * 
	 * @param aCharacter
	 *            The character.
	 * @param aListener
	 *            The character listener, that is used for doing actions to the character.
	 * @param aContext
	 *            the underlying context.
	 */
	public CharacterCompound(final CharacterInstance aCharacter, final CharacterListener aListener, final Activity aContext)
	{
		mContext = aContext;
		mName = aCharacter.getName();
		mConcept = aCharacter.getConcept();
		mGeneration = aCharacter.getGeneration();
		mNature = aCharacter.getNature().getName();
		mBehavior = aCharacter.getBehavior().getName();
		mEP = aCharacter.getEP();
		mLastUsed = 0;
		
		mListener = aListener;
		
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_character_compound, null);
		mPlay = (Button) mContainer.findViewById(R.id.view_compound_play_char_button);
		final TextView concept = (TextView) mContainer.findViewById(R.id.view_compound_concept_label);
		final TextView name = (TextView) mContainer.findViewById(R.id.view_compound_name_label);
		final TextView generation = (TextView) mContainer.findViewById(R.id.view_compound_generation_label);
		final TextView ep = (TextView) mContainer.findViewById(R.id.view_compound_ep_label);
		final TextView behavior = (TextView) mContainer.findViewById(R.id.view_compound_behavior_label);
		final TextView nature = (TextView) mContainer.findViewById(R.id.view_compound_nature_label);
		
		concept.setText(mContext.getString(R.string.concept_colon) + " " + mConcept);
		mContainer.setLongClickable(true);
		mContainer.setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(final View aV)
			{
				CharacterContextMenu.showCharacterContextMenu(mListener, mContext, mName);
				return true;
			}
		});
		name.setText(mName);
		generation.setText(mContext.getString(R.string.generation_colon) + " " + mGeneration);
		mPlay.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				use();
				mListener.play(mName);
			}
		});
		ep.setText("EP: " + mEP);
		behavior.setText(mContext.getString(R.string.behavior_colon) + " " + LanguageUtil.instance().getValue(mBehavior));
		nature.setText(mContext.getString(R.string.nature_colon) + " " + LanguageUtil.instance().getValue(mNature));
	}
	
	/**
	 * Creates a new character compound out of the given character data.
	 * 
	 * @param aCharacter
	 *            The XML data that contains the main character information.
	 * @param aListener
	 *            The character listener, that is used for doing actions to the character.
	 * @param aContext
	 *            The underlying context.
	 */
	public CharacterCompound(final String aCharacter, final CharacterListener aListener, final Activity aContext)
	{
		final String[] data = aCharacter.split("\t");
		mContext = aContext;
		mName = data[0];
		mConcept = data[1];
		mGeneration = Integer.parseInt(data[2]);
		mNature = data[3];
		mBehavior = data[4];
		mEP = Integer.parseInt(data[5]);
		if (data.length >= 7)
		{
			mLastUsed = Long.parseLong(data[6]);
		}
		else
		{
			mLastUsed = 0;
		}
		
		mListener = aListener;
		
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_character_compound, null);
		mPlay = (Button) mContainer.findViewById(R.id.view_compound_play_char_button);
		final TextView concept = (TextView) mContainer.findViewById(R.id.view_compound_concept_label);
		final TextView name = (TextView) mContainer.findViewById(R.id.view_compound_name_label);
		final TextView generation = (TextView) mContainer.findViewById(R.id.view_compound_generation_label);
		final TextView ep = (TextView) mContainer.findViewById(R.id.view_compound_ep_label);
		final TextView behavior = (TextView) mContainer.findViewById(R.id.view_compound_behavior_label);
		final TextView nature = (TextView) mContainer.findViewById(R.id.view_compound_nature_label);
		
		concept.setText(mContext.getString(R.string.concept_colon) + " " + mConcept);
		mContainer.setLongClickable(true);
		mContainer.setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(final View aV)
			{
				CharacterContextMenu.showCharacterContextMenu(mListener, mContext, mName);
				return true;
			}
		});
		name.setText(mName);
		generation.setText(mContext.getString(R.string.generation_colon) + " " + mGeneration);
		mPlay.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				use();
				mListener.play(mName);
			}
		});
		ep.setText("EP: " + mEP);
		behavior.setText(mContext.getString(R.string.behavior_colon) + " " + LanguageUtil.instance().getValue(mBehavior));
		nature.setText(mContext.getString(R.string.nature_colon) + " " + LanguageUtil.instance().getValue(mNature));
	}
	
	@Override
	public int compareTo(final CharacterCompound aAnother)
	{
		if (aAnother == null)
		{
			return 1;
		}
		if (getLastUsed() == aAnother.getLastUsed())
		{
			return getName().compareTo(aAnother.getName());
		}
		return Long.valueOf(aAnother.getLastUsed()).compareTo(getLastUsed());
	}
	
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if ( !(obj instanceof CharacterCompound))
		{
			return false;
		}
		final CharacterCompound other = (CharacterCompound) obj;
		if (mLastUsed != other.mLastUsed)
		{
			return false;
		}
		if (mName == null)
		{
			if (other.mName != null)
			{
				return false;
			}
		}
		else if ( !mName.equals(other.mName))
		{
			return false;
		}
		return true;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the time, this character was used last.
	 */
	public long getLastUsed()
	{
		return mLastUsed;
	}
	
	/**
	 * @return the characters name.
	 */
	public String getName()
	{
		return mName;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mBehavior == null) ? 0 : mBehavior.hashCode());
		result = prime * result + ((mConcept == null) ? 0 : mConcept.hashCode());
		result = prime * result + ((mContainer == null) ? 0 : mContainer.hashCode());
		result = prime * result + ((mContext == null) ? 0 : mContext.hashCode());
		result = prime * result + mEP;
		result = prime * result + mGeneration;
		result = prime * result + (int) (mLastUsed ^ (mLastUsed >>> 32));
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		result = prime * result + ((mNature == null) ? 0 : mNature.hashCode());
		return result;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Sets whether this character can be played.
	 * 
	 * @param aEnabled
	 *            Whether this character can be played.
	 */
	public void setPlayingEnabled(final boolean aEnabled)
	{
		ViewUtil.setEnabled(mPlay, aEnabled);
	}
	
	@Override
	public String toString()
	{
		final StringBuilder character = new StringBuilder();
		character.append(mName + "\t");
		character.append(mConcept + "\t");
		character.append(mGeneration + "\t");
		character.append(mNature + "\t");
		character.append(mBehavior + "\t");
		character.append(mEP + "\t");
		character.append(mLastUsed);
		return character.toString();
	}
	
	@Override
	public void updateUI()
	{}
	
	/**
	 * Sets the last used time to the current system time.
	 */
	public void use()
	{
		mLastUsed = System.currentTimeMillis();
	}
}
