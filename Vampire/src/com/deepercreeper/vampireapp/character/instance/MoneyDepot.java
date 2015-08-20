package com.deepercreeper.vampireapp.character.instance;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.Currency;
import com.deepercreeper.vampireapp.character.instance.controllers.MoneyControllerInstance;
import com.deepercreeper.vampireapp.host.Message;
import com.deepercreeper.vampireapp.host.Message.ButtonAction;
import com.deepercreeper.vampireapp.host.Message.MessageGroup;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.host.change.MoneyChange;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import com.deepercreeper.vampireapp.util.view.dialogs.MoneyAmountDialog;
import com.deepercreeper.vampireapp.util.view.listeners.MoneyAmountListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	public static final Comparator<MoneyDepot> COMPARATOR = new Comparator<MoneyDepot>()
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
	
	private final Map<String, Integer> mValues;
	
	private final LinearLayout mContainer;
	
	private final boolean mHost;
	
	private final Context mContext;
	
	private final Currency mCurrency;
	
	private final boolean mDefault;
	
	private final MoneyControllerInstance mController;
	
	private final TextView mNameText;
	
	private final TextView mValueText;
	
	private final ImageButton mTakeButton;
	
	private final ImageButton mDepotButton;
	
	private final ImageButton mDeleteButton;
	
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
		mTakeButton = (ImageButton) getContainer().findViewById(mHost ? R.id.h_take_button : R.id.c_take_button);
		mDepotButton = (ImageButton) getContainer().findViewById(mHost ? R.id.h_depot_button : R.id.c_depot_button);
		mDeleteButton = (ImageButton) getContainer().findViewById(mHost ? R.id.h_delete_depot_button : R.id.c_delete_depot_button);
		mNameText = (TextView) getContainer().findViewById(mHost ? R.id.h_depot_name_label : R.id.c_depot_name_label);
		mValueText = (TextView) getContainer().findViewById(mHost ? R.id.h_depot_value_text : R.id.c_depot_value_text);
		
		mTakeButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				take();
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
		}
		else
		{
			if (mHost)
			{
				ViewUtil.hideWidth(mTakeButton);
			}
			else
			{
				ViewUtil.hideWidth(mDepotButton);
			}
			ViewUtil.hideWidth(mDeleteButton);
		}
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
		mTakeButton = (ImageButton) getContainer().findViewById(mHost ? R.id.h_take_button : R.id.c_take_button);
		mDepotButton = (ImageButton) getContainer().findViewById(mHost ? R.id.h_depot_button : R.id.c_depot_button);
		mDeleteButton = (ImageButton) getContainer().findViewById(mHost ? R.id.h_delete_depot_button : R.id.c_delete_depot_button);
		mNameText = (TextView) getContainer().findViewById(mHost ? R.id.h_depot_name_label : R.id.c_depot_name_label);
		mValueText = (TextView) getContainer().findViewById(mHost ? R.id.h_depot_value_text : R.id.c_depot_value_text);
		
		mTakeButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				take();
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
		}
		else
		{
			if (mHost)
			{
				ViewUtil.hideWidth(mTakeButton);
			}
			else
			{
				ViewUtil.hideWidth(mDepotButton);
			}
			ViewUtil.hideWidth(mDeleteButton);
		}
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
			getValues().put(currency, getValues().get(currency) + aValues.get(currency));
		}
		mController.updateUI();
		getMessageListener().sendChange(new MoneyChange(getName(), getValues()));
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(CodingUtil.encode(getName()));
		element.setAttribute("values", serializeValues(",", ":", getValues(), false, mCurrency));
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
				final String[] args;
				if (mDefault)
				{
					args = new String[] { serializeValues(", ", " ", aMap, true, mCurrency) };
					getMessageListener().sendMessage(
							new Message(MessageGroup.SINGLE, false, "", R.string.money_sent, args, mContext, null, ButtonAction.NOTHING));
					add(aMap);
				}
				else
				{
					if (mHost)
					{
						mController.getDefaultDepot().remove(aMap);
						add(aMap);
					}
					else
					{
						args = new String[] { serializeValues(", ", " ", aMap, true, mCurrency), getName() };
						getMessageListener().sendMessage(new Message(MessageGroup.MONEY, false, mController.getCharacter().getName(),
								R.string.ask_depot_money, args, mContext, null, ButtonAction.ACCEPT_DEPOT, ButtonAction.DENY_DEPOT,
								serializeValues(",", " ", aMap, false, mCurrency), getName()));
					}
				}
			}
		};
		Map<String, Integer> maxValues;
		if (mDefault)
		{
			maxValues = mCurrency.getMaxAmounts();
		}
		else
		{
			maxValues = defaultDepot.getValues();
		}
		MoneyAmountDialog.showMoneyAmountDialog(mCurrency, maxValues, mContext.getString(R.string.choose_money_amount), mContext, listener);
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
	 * @return the message listener of the parent controller.
	 */
	public MessageListener getMessageListener()
	{
		return mController.getMessageListener();
	}
	
	/**
	 * @param aCurrency
	 *            The currency.
	 * @return the number of units from the given currency.
	 */
	public int getValue(final String aCurrency)
	{
		return getValues().get(aCurrency);
	}
	
	/**
	 * @return the current amount of money inside this depot.
	 */
	public Map<String, Integer> getValues()
	{
		return mValues;
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
		for (final int value : getValues().values())
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
			int newValue = getValues().get(currency) - aValues.get(currency);
			if (newValue < 0)
			{
				newValue = 0;
			}
			getValues().put(currency, newValue);
		}
		mController.updateUI();
		getMessageListener().sendChange(new MoneyChange(getName(), getValues()));
	}
	
	/**
	 * Asks the user how much money he wants to take.
	 */
	public void take()
	{
		final MoneyAmountListener listener = new MoneyAmountListener()
		{
			@Override
			public void amountSelected(final Map<String, Integer> aMap)
			{
				final String[] args;
				if (mDefault)
				{
					args = new String[] { serializeValues(", ", " ", aMap, true, mCurrency) };
					remove(aMap);
					getMessageListener().sendMessage(new Message(MessageGroup.SINGLE, false, mController.getCharacter().getName(),
							R.string.money_sent, args, mContext, null, ButtonAction.NOTHING));
				}
				else
				{
					if (mHost)
					{
						remove(aMap);
						mController.getDefaultDepot().add(aMap);
					}
					else
					{
						args = new String[] { serializeValues(", ", " ", aMap, true, mCurrency), getName() };
						getMessageListener().sendMessage(new Message(MessageGroup.MONEY, false, mController.getCharacter().getName(),
								R.string.ask_take_money, args, mContext, null, ButtonAction.ACCEPT_TAKE, ButtonAction.DENY_TAKE,
								serializeValues(",", " ", aMap, false, mCurrency), getName()));
					}
				}
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
		getMessageListener().sendChange(new MoneyChange(getName(), getValues()));
	}
	
	@Override
	public void updateUI()
	{
		if ( !isDefault())
		{
			final MoneyDepot defaultDepot = mController.getDefaultDepot();
			if (defaultDepot != null)
			{
				ViewUtil.setEnabled(mDepotButton,
						!defaultDepot.isEmpty() && (mHost || mController.getCharacter().getMode().getMode().canUseAction()));
			}
		}
		mValueText.setText(serializeValues("\n", " ", getValues(), false, mCurrency));
		ViewUtil.setEnabled(mTakeButton, !isEmpty() && (mHost || mController.getCharacter().getMode().getMode().canUseAction()));
	}
	
	/**
	 * Updates the values inside this depot.
	 * 
	 * @param aMap
	 *            The values map.
	 */
	public void updateValues(final Map<String, Integer> aMap)
	{
		getValues().putAll(aMap);
		mController.updateUI();
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
	
	/**
	 * @param aValueDelimiter
	 *            The delimiter, that splits all value currency pairs.
	 * @param aCurrencyDelimiter
	 *            The delimiter, that splits each value from its currency.
	 * @param aValues
	 *            The money string.
	 * @param aCurrency
	 *            The currency.
	 * @return a money amount out of the given string.
	 */
	public static Map<String, Integer> deserializeValues(final String aValueDelimiter, final String aCurrencyDelimiter, final String aValues,
			final Currency aCurrency)
	{
		final Map<String, Integer> map = new HashMap<String, Integer>();
		for (final String currencyValue : aValues.split(aValueDelimiter))
		{
			final String[] valueAndCurrency = currencyValue.split(aCurrencyDelimiter);
			if (aCurrency.contains(valueAndCurrency[1]))
			{
				map.put(valueAndCurrency[1], Integer.parseInt(valueAndCurrency[0]));
			}
		}
		return map;
	}
	
	/**
	 * @param aValueDelimiter
	 *            The delimiter, that splits all value currency pairs.
	 * @param aCurrencyDelimiter
	 *            The delimiter, that splits each value from its currency.
	 * @param aValues
	 *            The money amount.
	 * @param aHideEmpty
	 *            Whether empty amounts should be hidden.
	 * @param aCurrency
	 *            The currency.
	 * @return a string, representing the given amount of money.
	 */
	public static String serializeValues(final String aValueDelimiter, final String aCurrencyDelimiter, final Map<String, Integer> aValues,
			final boolean aHideEmpty, final Currency aCurrency)
	{
		final StringBuilder money = new StringBuilder();
		boolean first = true;
		for (final String currency : aCurrency.getCurrencies())
		{
			if (aHideEmpty && aValues.get(currency) == 0)
			{
				continue;
			}
			if (first)
			{
				first = false;
			}
			else
			{
				money.append(aValueDelimiter);
			}
			money.append(aValues.get(currency) + aCurrencyDelimiter + currency);
		}
		return money.toString();
	}
}
