package com.deepercreeper.vampireapp.util.view.listeners;

import java.util.Map;

/**
 * A listener for money amount choose.
 * 
 * @author Vincent
 */
public interface MoneyAmountChooseListener
{
	/**
	 * Invoked, when the money amount was chosen.
	 * 
	 * @param aMap
	 *            A map that puts an amount to each currency.
	 */
	public void amountSelected(Map<String, Integer> aMap);
}