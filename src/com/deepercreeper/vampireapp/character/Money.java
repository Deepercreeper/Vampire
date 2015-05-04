package com.deepercreeper.vampireapp.character;

/**
 * Repersents default money settings.
 * 
 * @author vrl
 */
public class Money
{
	private final String[]	mCurrencies;
	
	/**
	 * Creates new default currency settings.
	 * 
	 * @param aCurrencies
	 *            The list of currencies.
	 */
	public Money(final String[] aCurrencies)
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
