package com.deepercreeper.vampireapp.character.controllers;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.Money;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * This controller is used to control the money and warranty of the character.<br>
 * It displays the current wealth and allows to add or subtract money.
 * 
 * @author vrl
 */
public class MoneyController implements Saveable, Viewable
{
	private static final String			TAG				= "MoneyController";
	
	private final Money					mMoney;
	
	private final Map<String, Integer>	mValues			= new HashMap<String, Integer>();
	
	private final LinearLayout			mContainer;
	
	private final Context				mContext;
	
	private final boolean				mHost;
	
	private ImageButton					mAddMoney;
	
	private ImageButton					mRemoveMoney;
	
	private TextView					mMoneyDisplay;
	
	private boolean						mInitialized	= false;
	
	/**
	 * Creates a new money controller.
	 * 
	 * @param aMoney
	 *            the default money settings.
	 * @param aContext
	 *            The underlying context.
	 * @param aHost
	 *            Whether this controller is host sided.
	 */
	public MoneyController(final Money aMoney, final Context aContext, final boolean aHost)
	{
		mMoney = aMoney;
		mHost = aHost;
		for (final String currency : mMoney.getCurrencies())
		{
			mValues.put(currency, 0);
		}
		mContext = aContext;
		final int id = mHost ? R.layout.host_money : R.layout.client_money;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		init();
	}
	
	/**
	 * Creates a new money controller out of the given XML data.
	 * 
	 * @param aMoney
	 *            The default money settings.
	 * @param aElement
	 *            The XML document.
	 * @param aContext
	 *            The underlying context.
	 * @param aHost
	 *            Whether this controller is host sided.
	 */
	public MoneyController(final Money aMoney, final Element aElement, final Context aContext, final boolean aHost)
	{
		mMoney = aMoney;
		mHost = aHost;
		for (final String value : DataUtil.parseArray(aElement.getAttribute("values")))
		{
			final String[] currencyAndValue = value.split(":");
			mValues.put(currencyAndValue[1], Integer.parseInt(currencyAndValue[0]));
		}
		mContext = aContext;
		final int id = mHost ? R.layout.host_money : R.layout.client_money;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		init();
	}
	
	/**
	 * Adds the given amount of the given type of money to the pocket.
	 * 
	 * @param aCurrency
	 *            The type of money that is added.
	 * @param aValue
	 *            The amount of money.
	 */
	public void add(final String aCurrency, final int aValue)
	{
		mValues.put(aCurrency, getValue(aCurrency) + aValue);
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("money");
		element.setAttribute("values", serializeValues(false, ":"));
		return element;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return a display string that tells how much money is inside the characters pocket.
	 */
	public String getMoney()
	{
		return serializeValues(true, " ");
	}
	
	/**
	 * @param aCurrency
	 *            The money type.
	 * @return The current value of the given type of money, the character has.
	 */
	public int getValue(final String aCurrency)
	{
		return mValues.get(aCurrency);
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			getContainer().setLayoutParams(ViewUtil.getWrapHeight());
			
			mMoneyDisplay = (TextView) getContainer().findViewById(R.id.money);
			mAddMoney = mHost ? (ImageButton) getContainer().findViewById(R.id.add_money) : null;
			mRemoveMoney = mHost ? (ImageButton) getContainer().findViewById(R.id.remove_money) : null;
			
			if (mHost)
			{
				mAddMoney.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						addMoney();
					}
				});
				mRemoveMoney.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						removeMoney();
					}
				});
			}
			
			mInitialized = true;
		}
		
		updateValue();
	}
	
	/**
	 * Asks the user how much money to add.
	 */
	public void addMoney()
	{
		// TODO Implement activity
		Toast.makeText(mContext, "Adding money not implemented yet.", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Asks the user how much money to remove.
	 */
	public void removeMoney()
	{
		// TODO Implement activity
		Toast.makeText(mContext, "Removing money not implemented yet.", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Removes the given amount of the given warranty from the pocket.
	 * 
	 * @param aCurrency
	 *            The type of money that is removed.
	 * @param aValue
	 *            The amount of money that is removed.
	 */
	public void remove(final String aCurrency, final int aValue)
	{
		if (getValue(aCurrency) < aValue)
		{
			Log.w(TAG, "Tried to remove more money than in pocket.");
			return;
		}
		mValues.put(aCurrency, getValue(aCurrency) - aValue);
	}
	
	/**
	 * Displays the current amount of money again.
	 */
	public void updateValue()
	{
		mMoneyDisplay.setText("" + getMoney());
	}
	
	private String serializeValues(final boolean aSpaces, final String aDelimiter)
	{
		final StringBuilder money = new StringBuilder();
		boolean first = true;
		for (final String currency : mMoney.getCurrencies())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				money.append(",");
				if (aSpaces)
				{
					money.append(" ");
				}
			}
			money.append(getValue(currency) + aDelimiter + currency);
		}
		return money.toString();
	}
}
