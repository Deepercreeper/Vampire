package com.deepercreeper.vampireapp.character.instance;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.Currency;
import com.deepercreeper.vampireapp.host.change.ChangeListener;
import com.deepercreeper.vampireapp.host.change.MoneyChange;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Viewable;
import com.deepercreeper.vampireapp.util.view.dialogs.MoneyAmountDialog;
import com.deepercreeper.vampireapp.util.view.dialogs.MoneyAmountDialog.MoneyAmountListener;

/**
 * A money depot contains a number of coins for each currency and can be deleted.<br>
 * The user can store and take to and from a depot
 * 
 * @author Vincent
 */
public class MoneyDepot extends Named implements Saveable, Viewable
{
	/**
	 * The default comparator, that takes the default depot to the top.
	 */
	public static final Comparator<MoneyDepot>	COMPARATOR		= new Comparator<MoneyDepot>()
																{
																	@Override
																	public int compare(final MoneyDepot aLhs, final MoneyDepot aRhs)
																	{
																		if (aLhs.isDefault())
																		{
																			return -1;
																		}
																		if (aRhs.isDefault())
																		{
																			return 1;
																		}
																		return aLhs.getName().compareTo(aRhs.getName());
																	}
																};
	
	private final Map<String, Integer>			mValues;
	
	private final LinearLayout					mContainer;
	
	private final boolean						mHost;
	
	private final Context						mContext;
	
	private final Currency						mCurrency;
	
	private final boolean						mDefault;
	
	private final MoneyControllerInstance		mController;
	
	private boolean								mInitialized	= false;
	
	private TextView							mNameText;
	
	private TextView							mValueText;
	
	private ImageButton							mTakeButton;
	
	private ImageButton							mDepotButton;
	
	private ImageButton							mDeleteButton;
	
	/**
	 * Creates a new money depot.
	 * 
	 * @param aName
	 *            The depot name.
	 * @param aContext
	 *            The underlying context.
	 * @param aHost
	 *            Whether this is a host sided depot.
	 * @param aDefault
	 *            Whether this depot is removable.
	 * @param aController
	 *            The parent controller.
	 */
	public MoneyDepot(final String aName, final Context aContext, final boolean aHost, final boolean aDefault,
			final MoneyControllerInstance aController)
	{
		super(aName);
		mController = aController;
		mHost = aHost;
		mCurrency = mController.getCurrency();
		mContext = aContext;
		mDefault = aDefault;
		mValues = new HashMap<String, Integer>();
		for (final String currency : mCurrency.getCurrencies())
		{
			mValues.put(currency, 0);
		}
		final int id = mHost ? R.layout.host_depot : R.layout.client_depot;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		init();
	}
	
	/**
	 * Creates a new money depot out of the given XML data.
	 * 
	 * @param aName
	 *            The depot name.
	 * @param aElement
	 *            The data.
	 * @param aContext
	 *            The underlying context.
	 * @param aHost
	 *            Whether this is a host sided depot.
	 * @param aController
	 *            The parent controller.
	 */
	public MoneyDepot(final String aName, final Element aElement, final Context aContext, final boolean aHost,
			final MoneyControllerInstance aController)
	{
		super(aName);
		mController = aController;
		mHost = aHost;
		mCurrency = mController.getCurrency();
		mContext = aContext;
		mDefault = Boolean.valueOf(aElement.getAttribute("default"));
		mValues = createMap(aElement.getAttribute("values"));
		final int id = mHost ? R.layout.host_depot : R.layout.client_depot;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		init();
	}
	
	/**
	 * Adds the given amounts of money to this depot.
	 * 
	 * @param aValues
	 *            The money amounts.
	 */
	public void add(final Map<String, Integer> aValues)
	{
		for (final String currency : aValues.keySet())
		{
			mValues.put(currency, mValues.get(currency) + aValues.get(currency));
		}
		mController.updateValues();
		getChangeListener().sendChange(new MoneyChange(getName(), getValues()));
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(CodingUtil.encode(getName()));
		element.setAttribute("values", serializeValues(",", ":"));
		element.setAttribute("default", "" + mDefault);
		return element;
	}
	
	/**
	 * Removes this depot from the depots list and takes all the money inside this depot.
	 */
	public void delete()
	{
		mController.removeDepot(getName(), false);
	}
	
	/**
	 * Asks the user how much money to depot here.
	 */
	public void depot()
	{
		final MoneyDepot defaultDepot = mController.getDefaultDepot();
		final MoneyAmountListener listener = new MoneyAmountListener()
		{
			@Override
			public void amountSelected(final Map<String, Integer> aMap)
			{
				defaultDepot.remove(aMap);
				add(aMap);
			}
		};
		MoneyAmountDialog.showMoneyAmountDialog(mCurrency, defaultDepot.getValues(), mContext.getString(R.string.choose_money_amount), mContext,
				listener);
	}
	
	/**
	 * @return the change listener of the parent controller.
	 */
	public ChangeListener getChangeListener()
	{
		return mController.getChangeListener();
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public String getDisplayName()
	{
		return getName();
	}
	
	/**
	 * @param aCurrency
	 *            The currency.
	 * @return the number of units from the given currency.
	 */
	public int getValue(final String aCurrency)
	{
		return mValues.get(aCurrency);
	}
	
	/**
	 * @return the current amount of money inside this depot.
	 */
	public Map<String, Integer> getValues()
	{
		return mValues;
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			mTakeButton = (ImageButton) getContainer().findViewById(R.id.take_button);
			mDepotButton = (ImageButton) getContainer().findViewById(R.id.depot_button);
			mDeleteButton = (ImageButton) getContainer().findViewById(R.id.delete_button);
			mNameText = (TextView) getContainer().findViewById(R.id.depot_name);
			mValueText = (TextView) getContainer().findViewById(R.id.depot_value);
			
			mTakeButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					take();
				}
			});
			mNameText.setText(getName() + ":");
			if ( !mDefault)
			{
				mDeleteButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						delete();
					}
				});
				mDepotButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						depot();
					}
				});
			}
			else
			{
				ViewUtil.hideWidth(mDepotButton);
				ViewUtil.hideWidth(mDeleteButton);
			}
			
			mInitialized = true;
		}
		
		updateValue();
	}
	
	/**
	 * @return whether this is the default depot.
	 */
	public boolean isDefault()
	{
		return mDefault;
	}
	
	/**
	 * @return whether this depot contains any values.
	 */
	public boolean isEmpty()
	{
		for (final int value : mValues.values())
		{
			if (value > 0)
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Removes the given amounts of money from this depot.
	 * 
	 * @param aValues
	 *            The money amounts.
	 */
	public void remove(final Map<String, Integer> aValues)
	{
		for (final String currency : aValues.keySet())
		{
			int newValue = mValues.get(currency) - aValues.get(currency);
			if (newValue < 0)
			{
				newValue = 0;
			}
			mValues.put(currency, newValue);
		}
		mController.updateValues();
		getChangeListener().sendChange(new MoneyChange(getName(), getValues()));
	}
	
	/**
	 * Asks the user how much money he wants to take.
	 */
	public void take()
	{
		final MoneyDepot defaultDepot = mController.getDefaultDepot();
		final MoneyAmountListener listener = new MoneyAmountListener()
		{
			@Override
			public void amountSelected(final Map<String, Integer> aMap)
			{
				remove(aMap);
				defaultDepot.add(aMap);
			}
		};
		MoneyAmountDialog.showMoneyAmountDialog(mCurrency, getValues(), mContext.getString(R.string.choose_money_amount), mContext, listener);
	}
	
	/**
	 * Takes all of the money.
	 */
	public void takeAll()
	{
		mController.getDefaultDepot().add(getValues());
		remove(getValues());
		getChangeListener().sendChange(new MoneyChange(getName(), getValues()));
	}
	
	/**
	 * Updates the UI of this viewable.
	 */
	public void updateValue()
	{
		if ( !isDefault())
		{
			final MoneyDepot defaultDepot = mController.getDefaultDepot();
			if (defaultDepot != null)
			{
				ViewUtil.setEnabled(mDepotButton, !defaultDepot.isEmpty());
			}
		}
		mValueText.setText(serializeValues("\n", " "));
		ViewUtil.setEnabled(mTakeButton, !isEmpty());
	}
	
	/**
	 * Updates the values inside this depot.
	 * 
	 * @param aMap
	 *            The values map.
	 */
	public void updateValues(final Map<String, Integer> aMap)
	{
		mValues.putAll(aMap);
		mController.updateValues();
	}
	
	private Map<String, Integer> createMap(final String aValues)
	{
		final Map<String, Integer> map = new HashMap<String, Integer>();
		for (final String value : DataUtil.parseArray(aValues))
		{
			final String[] currencyAndValue = value.split(":");
			map.put(currencyAndValue[1], Integer.parseInt(currencyAndValue[0]));
		}
		return map;
	}
	
	private String serializeValues(final String aValueDelimiter, final String aCurrencyDelimiter)
	{
		final StringBuilder money = new StringBuilder();
		boolean first = true;
		for (final String currency : mCurrency.getCurrencies())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				money.append(aValueDelimiter);
			}
			money.append(getValue(currency) + aCurrencyDelimiter + currency);
		}
		return money.toString();
	}
}
