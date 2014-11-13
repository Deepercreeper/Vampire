package com.deepercreeper.vampireapp.controller.restrictions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Some clans have restrictions, that define whether values or attributes have to have a specific value.<br>
 * Each restriction is represented by an instance of this class.
 * 
 * @author vrl
 */
public class Restriction
{
	private static final HashMap<String, RestrictionKey>	RESTRICTION_KEYS	= new HashMap<String, RestrictionKey>();
	
	private static final HashMap<String, RestrictionType>	RESTRICTION_TYPES	= new HashMap<String, RestrictionType>();
	
	/**
	 * Each restriction has a key for that its going to be done.
	 * 
	 * @author vrl
	 */
	public static enum RestrictionKey
	{
		/**
		 * Represents all simple items.
		 */
		SIMPLE("Simple"),
		
		/**
		 * Represents all background items.
		 */
		BACKGROUND("Background"),
		
		/**
		 * Represents discipline items.
		 */
		DISCIPLINE("Discipline"),
		
		/**
		 * Represents property items.
		 */
		PROPERTY("Property"),
		
		/**
		 * Represents insanities.
		 */
		INSANITY("Insanity"),
		
		/**
		 * Represents the volition.
		 */
		VOLITION("Volition"),
		
		/**
		 * Represents the path.
		 */
		PATH("Path"),
		
		/**
		 * Represents the generation.
		 */
		GENERATION("Generation");
		
		private final String	mKey;
		
		private RestrictionKey(String aKey)
		{
			mKey = aKey;
			RESTRICTION_KEYS.put(mKey, this);
		}
		
		/**
		 * @return the restriction key.
		 */
		public String getKey()
		{
			return mKey;
		}
	}
	
	private static enum RestrictionType
	{
		EQUALS("="), SMALLER("<"), LARGER(">"), BETWEEN("?");
		
		private final String	mDelim;
		
		private RestrictionType(String aDelim)
		{
			mDelim = aDelim;
			RESTRICTION_TYPES.put(mDelim, this);
		}
		
		/**
		 * @return the restriction type delimiter.
		 */
		private String getDelim()
		{
			return mDelim;
		}
	}
	
	/**
	 * The delimiter that stays between two restrictions.
	 */
	public static final String			RESTRICTIONS_DELIM	= ",";
	
	private static final String			KEY_DELIM			= "@", BETWEEN_DELIM = "-";
	
	private final String				mKey;
	
	private final int					mMinValue;
	
	private final int					mMaxValue;
	
	private final Set<Restrictionable>	mParents			= new HashSet<Restrictionable>();
	
	private Restriction(final String aKey, final int aMinValue, final int aMaxValue)
	{
		mKey = aKey;
		mMinValue = aMinValue;
		mMaxValue = aMaxValue;
	}
	
	/**
	 * @return whether this restriction has a key with a group.<br>
	 *         E.g. <code>Simple@Appearance</code>
	 */
	public boolean hasGroup()
	{
		return mKey.contains(KEY_DELIM);
	}
	
	/**
	 * @return the key group if existing.
	 */
	public String getGroup()
	{
		return mKey.split(KEY_DELIM)[0];
	}
	
	/**
	 * Adds a restricted parent to this restriction. That makes sure,<br>
	 * that the removal of restrictions is done for each restricted value of this restriction.
	 * 
	 * @param aParent
	 *            The parent.
	 */
	public void addParent(final Restrictionable aParent)
	{
		mParents.add(aParent);
	}
	
	/**
	 * Removes this restriction from each restricted parent.
	 */
	public void clear()
	{
		for (final Restrictionable parent : mParents)
		{
			parent.removeRestriction(this);
		}
		mParents.clear();
	}
	
	/**
	 * @return The key for the value that is restricted by this restriction.
	 */
	public String getKey()
	{
		if (hasGroup())
		{
			return mKey.substring(mKey.indexOf(KEY_DELIM) + 1);
		}
		return mKey;
	}
	
	/**
	 * @return the maximum value for the restricted value.
	 */
	public int getMaxValue()
	{
		return mMaxValue;
	}
	
	/**
	 * @return the minimum value for the restricted value.
	 */
	public int getMinValue()
	{
		return mMinValue;
	}
	
	@Override
	public String toString()
	{
		return mKey + ": " + mMinValue + "-" + mMaxValue;
	}
	
	/**
	 * Creates a new restriction out of the given data.
	 * 
	 * @param aData
	 *            The restriction data.
	 * @return a new restriction.
	 */
	public static Restriction create(final String aData)
	{
		String[] data;
		int min = -1;
		int max = Integer.MAX_VALUE;
		if (aData.contains(RestrictionType.SMALLER.getDelim()))
		{
			data = aData.split(RestrictionType.SMALLER.getDelim());
			max = Integer.parseInt(data[1]) - 1;
		}
		else if (aData.contains(RestrictionType.LARGER.getDelim()))
		{
			data = aData.split(RestrictionType.LARGER.getDelim());
			min = Integer.parseInt(data[1]) + 1;
		}
		else if (aData.contains(RestrictionType.BETWEEN.getDelim()))
		{
			data = aData.split(RestrictionType.BETWEEN.getDelim());
			final String[] range = data[1].split(BETWEEN_DELIM);
			min = Integer.parseInt(range[0]);
			max = Integer.parseInt(range[1]);
		}
		else
		{
			data = aData.split(RestrictionType.EQUALS.getDelim());
			max = min = Integer.parseInt(data[1]);
		}
		return new Restriction(data[0], min, max);
	}
}
