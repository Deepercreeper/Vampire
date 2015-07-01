package com.deepercreeper.vampireapp.character;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents default money settings.
 * 
 * @author vrl
 */
public class Currency
{
	private static final int			MAX_AMOUNT	= 1000;
	
	private final String[]				mCurrencies;
	
	private final Map<String, Integer>	mMaxAmounts	= new HashMap<String, Integer>();
	
	/**
	 * Creates new default currency settings.
	 * 
	 * @param aCurrencies
	 *            The list of currencies.
	 */
	public Currency(final String[] aCurrencies)
	{
		mCurrencies = aCurrencies;
		for (String currency : getCurrencies())
		{
			mMaxAmounts.put(currency, MAX_AMOUNT);
		}
	}
	
	/**
	 * @return the maximum value, that can be transmitted per money transmission.
	 */
	public Map<String, Integer> getMaxAmounts()
	{
		return mMaxAmounts;
	}
	
	/**
	 * @return an array of possible currencies.
	 */
	public String[] getCurrencies()
	{
		return mCurrencies;
	}
}
