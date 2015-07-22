package com.deepercreeper.vampireapp.mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.interfaces.ItemFinder;

/**
 * The default implementation of an action.
 * 
 * @author vrl
 */
public class ActionImpl implements Action
{
	private final ActionType					mType;
	
	private final int							mMinLevel;
	
	private final int							mMinDices;
	
	private final List<ItemInstance>			mDices		= new ArrayList<ItemInstance>();
	
	private final Map<ItemInstance, Integer>	mCosts		= new HashMap<ItemInstance, Integer>();
	
	private final List<ItemInstance>			mCostDices	= new ArrayList<ItemInstance>();
	
	private final String[]						mDiceNames;
	
	private final String[]						mCostDiceNames;
	
	private final String[]						mCostNames;
	
	private final String						mName;
	
	private final String						mId;
	
	/**
	 * Creates a new action.
	 * 
	 * @param aName
	 *            The action name.
	 * @param aId
	 *            The action Id.
	 * @param aType
	 *            The action type.
	 * @param aMinLevel
	 *            The minimum item level of this action.
	 * @param aMinDices
	 *            The minimum number of dices that are used to do this action.
	 * @param aDiceNames
	 *            A list of names for the items whose value are added to the minimum dice number.
	 * @param aCostDiceNames
	 *            These items can be spent by a user define amount to have a special effect.
	 * @param aCostNames
	 *            A list of item names and a value for each. It defines, how many item points<br>
	 *            need to be spent for using this action.
	 */
	public ActionImpl(final String aName, final String aId, final ActionType aType, final int aMinLevel, final int aMinDices,
			final String[] aDiceNames, final String[] aCostDiceNames, final String[] aCostNames)
	{
		mName = aName;
		mId = aId;
		mType = aType;
		mMinLevel = aMinLevel;
		mMinDices = aMinDices;
		mDiceNames = aDiceNames;
		mCostDiceNames = aCostDiceNames;
		mCostNames = aCostNames;
	}
	
	@Override
	public String getId()
	{
		return mId;
	}
	
	@Override
	public int getMinLevel()
	{
		return mMinLevel;
	}
	
	@Override
	public String getDisplayName()
	{
		return LanguageUtil.instance().getValue(getName());
	}
	
	@Override
	public String getName()
	{
		return mName;
	}
	
	@Override
	public int getMinDices()
	{
		return mMinDices;
	}
	
	@Override
	public void init(final ItemFinder aFinder)
	{
		mDices.clear();
		mCostDices.clear();
		mCosts.clear();
		
		for (final String dice : mDiceNames)
		{
			mDices.add(aFinder.findItemInstance(dice));
		}
		for (final String costDice : mCostDiceNames)
		{
			mCostDices.add(aFinder.findItemInstance(costDice));
		}
		for (final String cost : mCostNames)
		{
			final String costName = cost.split("=")[0];
			final int costValue = Integer.parseInt(cost.split("=")[1]);
			mCosts.put(aFinder.findItemInstance(costName), costValue);
		}
	}
	
	@Override
	public boolean canUse(final int aLevel)
	{
		if (aLevel < getMinLevel())
		{
			return false;
		}
		for (final ItemInstance cost : mCosts.keySet())
		{
			if (cost.getValue() < mCosts.get(cost))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int getDefaultDices()
	{
		int dices = mMinDices;
		for (final ItemInstance dice : mDices)
		{
			dices += dice.getValue();
		}
		return dices;
	}
	
	@Override
	public ActionType getType()
	{
		return mType;
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
}
