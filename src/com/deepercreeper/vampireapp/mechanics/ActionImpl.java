package com.deepercreeper.vampireapp.mechanics;

import java.util.HashMap;
import java.util.Map;
import com.deepercreeper.vampireapp.util.LanguageUtil;

/**
 * The default implementation of an action.
 * 
 * @author vrl
 */
public class ActionImpl implements Action
{
	private final ActionType mType;
	
	private final int mMinLevel;
	
	private final int mMinDices;
	
	private final String[] mDiceNames;
	
	private final String[] mCostDiceNames;
	
	private final Map<String, Integer> mCostNames = new HashMap<String, Integer>();
	
	private final String mName;
	
	/**
	 * Creates a new action.
	 * 
	 * @param aName
	 *            The action name.
	 * @param aType
	 *            The action type.
	 * @param aMinLevel
	 *            The minimum item level of this action.
	 * @param aMinDices
	 *            The minimum number of dices that are used to do this action.
	 * @param aDiceNames
	 *            A list of names for the items whose value are added to the minimum dice number.
	 * @param aCostDiceNames
	 *            These items can be spent by a user defined amount to have a special effect.
	 * @param aCostNames
	 *            A list of item names and a value for each. It defines, how many item points<br>
	 *            need to be spent for using this action.
	 */
	public ActionImpl(final String aName, final ActionType aType, final int aMinLevel, final int aMinDices, final String[] aDiceNames,
			final String[] aCostDiceNames, final String[] aCostNames)
	{
		mName = aName;
		mType = aType;
		mMinLevel = aMinLevel;
		mMinDices = aMinDices;
		mDiceNames = aDiceNames;
		mCostDiceNames = aCostDiceNames;
		
		for (final String cost : aCostNames)
		{
			final String costName = cost.split("=")[0];
			final int costValue = Integer.parseInt(cost.split("=")[1]);
			mCostNames.put(costName, costValue);
		}
	}
	
	@Override
	public int compareTo(final Action aAnother)
	{
		if (getMinLevel() == aAnother.getMinLevel())
		{
			return getName().compareTo(aAnother.getName());
		}
		return getMinLevel() - aAnother.getMinLevel();
	}
	
	@Override
	public String[] getCostDiceNames()
	{
		return mCostDiceNames;
	}
	
	@Override
	public Map<String, Integer> getCostNames()
	{
		return mCostNames;
	}
	
	@Override
	public String[] getDiceNames()
	{
		return mDiceNames;
	}
	
	@Override
	public String getDisplayName()
	{
		return LanguageUtil.instance().getValue(getName());
	}
	
	@Override
	public int getMinDices()
	{
		return mMinDices;
	}
	
	@Override
	public int getMinLevel()
	{
		return mMinLevel;
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	@Override
	public ActionType getType()
	{
		return mType;
	}
}
