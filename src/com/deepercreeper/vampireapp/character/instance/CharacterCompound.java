package com.deepercreeper.vampireapp.character.instance;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.CharacterContextMenu;
import com.deepercreeper.vampireapp.util.view.CharacterContextMenu.CharacterListener;

public class CharacterCompound implements Comparable<CharacterCompound>
{
	private static final String	TAG	= "CharacterCompound";
	
	private final Activity		mContext;
	
	private final String		mName;
	
	private final String		mConcept;
	
	private final int			mGeneration;
	
	private final String		mNature;
	
	private final String		mBehavior;
	
	private final int			mEP;
	
	private final String		mData;
	
	private View				mTrimmer;
	
	private RelativeLayout		mContainer;
	
	private long				mLastUsed;
	
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
			mLastUsed = Integer.parseInt(data[6]);
		}
		else
		{
			mLastUsed = 0;
		}
		
		mData = createData();
		init(aListener);
	}
	
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
		
		mData = createData();
		init(aListener);
	}
	
	public void setFirst(final boolean aFirst)
	{
		if (aFirst)
		{
			mTrimmer.setVisibility(View.INVISIBLE);
		}
		else
		{
			mTrimmer.setVisibility(View.VISIBLE);
		}
	}
	
	private void init(final CharacterListener aListener)
	{
		mContainer = (RelativeLayout) View.inflate(mContext, R.layout.character_compound, null);
		
		mTrimmer = mContainer.findViewById(R.id.trimmer);
		
		final TextView concept = (TextView) mContainer.findViewById(R.id.concept_label);
		concept.setText(mConcept);
		
		final TextView name = (TextView) mContainer.findViewById(R.id.name_label);
		mContainer.setLongClickable(true);
		mContainer.setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(final View aV)
			{
				CharacterContextMenu.showCharacterContextMenu(aListener, mContext, mName);
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
				use();
				aListener.play(mName);
			}
		});
		
		final TextView ep = (TextView) mContainer.findViewById(R.id.ep_label);
		ep.setText("EP: " + mEP);
		
		final TextView behavior = (TextView) mContainer.findViewById(R.id.behavior_label);
		behavior.setText(mContext.getString(R.string.behavior_text) + " " + mBehavior);
		
		final TextView nature = (TextView) mContainer.findViewById(R.id.nature_label);
		nature.setText(mContext.getString(R.string.nature_text) + " " + mNature);
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
		return Long.valueOf(aAnother.getLastUsed()).compareTo(getLastUsed());
	}
}