package com.deepercreeper.vampireapp.character.controllers;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.Money;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class MoneyController implements Saveable
{
	private static final String			TAG				= "MoneyController";
	
	private final Money					mMoney;
	
	private final Map<String, Integer>	mValues			= new HashMap<String, Integer>();
	
	private final RelativeLayout		mContainer;
	
	private final Context				mContext;
	
	private TextView					mMoneyDisplay;
	
	private boolean						mInitialized	= false;
	
	public MoneyController(final Money aMoney, final Context aContext)
	{
		mMoney = aMoney;
		for (final String currency : mMoney.getCurrencies())
		{
			mValues.put(currency, 0);
		}
		mContext = aContext;
		mContainer = (RelativeLayout) View.inflate(mContext, R.layout.money, null);
		init();
	}
	
	public MoneyController(final Money aMoney, final Element aElement, final Context aContext)
	{
		mMoney = aMoney;
		for (final String value : DataUtil.parseArray(aElement.getAttribute("values")))
		{
			final String[] currencyAndValue = value.split(":");
			mValues.put(currencyAndValue[1], Integer.parseInt(currencyAndValue[0]));
		}
		mContext = aContext;
		mContainer = (RelativeLayout) View.inflate(mContext, R.layout.money, null);
		init();
	}
	
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	public void init()
	{
		if ( !mInitialized)
		{
			getContainer().setLayoutParams(ViewUtil.getWrapHeight());
			
			mMoneyDisplay = (TextView) getContainer().findViewById(R.id.money);
			
			mInitialized = true;
		}
		
		updateValue();
	}
	
	public void updateValue()
	{
		mMoneyDisplay.setText("" + getMoney());
	}
	
	public RelativeLayout getContainer()
	{
		return mContainer;
	}
	
	public int getValue(final String aCurrency)
	{
		return mValues.get(aCurrency);
	}
	
	public void add(final String aCurrency, final int aValue)
	{
		mValues.put(aCurrency, getValue(aCurrency) + aValue);
	}
	
	public void remove(final String aCurrency, final int aValue)
	{
		if (getValue(aCurrency) < aValue)
		{
			Log.w(TAG, "Tried to remove more money than in pocket.");
			return;
		}
		mValues.put(aCurrency, getValue(aCurrency) - aValue);
	}
	
	public String getMoney()
	{
		return serializeValues(true, " ");
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("money");
		element.setAttribute("values", serializeValues(false, ":"));
		return element;
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
