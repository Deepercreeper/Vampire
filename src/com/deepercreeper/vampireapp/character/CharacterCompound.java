package com.deepercreeper.vampireapp.character;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.Vampire;
import com.deepercreeper.vampireapp.controllers.dialog.CharacterContextMenu;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class CharacterCompound implements Comparable<CharacterCompound>
{
	private static final String		TAG	= "CharacterCompound";
	
	private final String			mName;
	
	private final String			mConcept;
	
	private final int				mGeneration;
	
	private final String			mNature;
	
	private final String			mBehavior;
	
	private final int				mEP;
	
	private final RelativeLayout	mContainer;
	
	private final String			mData;
	
	private long					mLastUsed;
	
	public CharacterCompound(final String aCharacter, final Vampire aVampire)
	{
		final String[] data = aCharacter.split("\t");
		mName = data[0];
		mConcept = data[1];
		mGeneration = Integer.parseInt(data[2]);
		mNature = data[3];
		mBehavior = data[4];
		mEP = Integer.parseInt(data[5]);
		if (data.length >= 7)
		{
			mLastUsed = Integer.parseInt(data[6]);
		}
		else
		{
			mLastUsed = 0;
		}
		
		mContainer = new RelativeLayout(aVampire.getContext());
		mData = createData();
		init(aVampire);
	}
	
	public CharacterCompound(final CharacterInstance aCharacter, final Vampire aVampire)
	{
		mName = aCharacter.getName();
		mConcept = aCharacter.getConcept();
		mGeneration = aCharacter.getGeneration();
		mNature = aCharacter.getNature().getName();
		mBehavior = aCharacter.getBehavior().getName();
		mEP = aCharacter.getEP();
		mLastUsed = 0;
		
		mContainer = new RelativeLayout(aVampire.getContext());
		mData = createData();
		init(aVampire);
	}
	
	private void init(final Vampire aVampire)
	{
		final Context context = aVampire.getContext();
		View.inflate(context, R.layout.character_compound, mContainer);
		
		final TextView concept = (TextView) mContainer.findViewById(R.id.concept_label);
		concept.setText(mConcept);
		
		final TextView name = (TextView) mContainer.findViewById(R.id.name_label);
		mContainer.setLongClickable(true);
		mContainer.setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(final View aV)
			{
				CharacterContextMenu.showCharacterContextMenu(aVampire, mName);
				return true;
			}
		});
		name.setText(mName);
		
		final TextView generation = (TextView) mContainer.findViewById(R.id.generation_label);
		generation.setText("" + mGeneration);
		
		final Button play = (Button) mContainer.findViewById(R.id.play_button);
		play.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				aVampire.loadChar(mName);
				use();
				aVampire.reloadChars();
			}
		});
		
		final TextView ep = (TextView) mContainer.findViewById(R.id.ep_label);
		ep.setText("EP: " + mEP);
		
		final TextView behavior = (TextView) mContainer.findViewById(R.id.behavior_label);
		behavior.setText(context.getString(R.string.behavior_text) + " " + mBehavior);
		
		final TextView nature = (TextView) mContainer.findViewById(R.id.nature_label);
		nature.setText(context.getString(R.string.nature_text) + " " + mNature);
	}
	
	public long getLastUsed()
	{
		return mLastUsed;
	}
	
	public void use()
	{
		mLastUsed = System.currentTimeMillis();
	}
	
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	public String getName()
	{
		return mName;
	}
	
	public RelativeLayout getContainer()
	{
		return mContainer;
	}
	
	private String createData()
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
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		return result;
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
	public String toString()
	{
		return mData;
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
		if (getLastUsed() > aAnother.getLastUsed())
		{
			return -1;
		}
		return 1;
	}
}
