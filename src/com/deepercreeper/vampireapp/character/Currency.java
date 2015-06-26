package com.deepercreeper.vampireapp.character;

/**
 * Represents default money settings.
 * 
 * @author vrl
 */
public class Currency
{
	private final String[]	mCurrencies;
	
	/**
	 * Creates new default currency settings.
	 * 
	 * @param aCurrencies
	 *            The list of currencies.
	 */
	public Currency(final String[] aCurrencies)
	{
		mCurrencies = aCurrencies;
	}
	
	/**
	 * @return an array of possible currencies.
	 */
	public String[] getCurrencies()
	{
		return mCurrencies;
	}
}
