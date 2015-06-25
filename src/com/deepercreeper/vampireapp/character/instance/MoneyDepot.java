package com.deepercreeper.vampireapp.character.instance;

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
import com.deepercreeper.vampireapp.character.Money;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * A money depot contains a number of coins for each currency and can be deleted.<br>
 * The user can store and take to and from a depot
 * 
 * @author Vincent
 */
public class MoneyDepot extends Named implements Saveable, Viewable
{
	private final Map<String, Integer>		mValues;
	
	private final LinearLayout				mContainer;
	
	private final boolean					mHost;
	
	private final Context					mContext;
	
	private final Money						mMoney;
	
	private final boolean					mRemovable;
	
	private final MoneyControllerInstance	mController;
	
	private boolean							mInitialized	= false;
	
	private TextView						mNameText;
	
	private TextView						mValueText;
	
	private ImageButton						mTakeButton;
	
	private ImageButton						mDepotButton;
	
	private ImageButton						mDeleteButton;
	
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
		mMoney = mController.getMoney();
		mContext = aContext;
		mRemovable = Boolean.valueOf(aElement.getAttribute("removable"));
		mValues = createMap(aElement.getAttribute("values"));
		final int id = mHost ? R.layout.host_depot : R.layout.client_depot;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		init();
	}
	
	/**
	 * Creates a new money depot.
	 * 
	 * @param aName
	 *            The depot name.
	 * @param aContext
	 *            The underlying context.
	 * @param aHost
	 *            Whether this is a host sided depot.
	 * @param aRemovable
	 *            Whether this depot is removable.
	 * @param aController
	 *            The parent controller.
	 */
	public MoneyDepot(final String aName, final Context aContext, final boolean aHost, final boolean aRemovable,
			final MoneyControllerInstance aController)
	{
		super(aName);
		mController = aController;
		mHost = aHost;
		mMoney = mController.getMoney();
		mContext = aContext;
		mRemovable = aRemovable;
		mValues = new HashMap<String, Integer>();
		for (final String currency : mMoney.getCurrencies())
		{
			mValues.put(currency, 0);
		}
		final int id = mHost ? R.layout.host_depot : R.layout.client_depot;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		init();
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(CodingUtil.encode(getName()));
		element.setAttribute("values", serializeValues(false, ":"));
		element.setAttribute("removable", "" + mRemovable);
		return element;
	}
	
	/**
	 * Removes this depot from the depots list and takes all the money inside this depot.
	 */
	public void delete()
	{
		mController.removeDepot(this);
	}
	
	/**
	 * Asks the user how much money to depot here.
	 */
	public void depot()
	{
		// TODO Open depot activity
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
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
			mDepotButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					depot();
				}
			});
			mNameText.setText(getName() + ":");
			if (mRemovable)
			{
				mDepotButton.setOnClickListener(new OnClickListener()
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
				ViewUtil.hideWidth(mDeleteButton);
			}
			
			mInitialized = true;
		}
		
		updateValue();
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
	 * Asks the user how much money he wants to take.
	 */
	public void take()
	{
		// TODO Open take activity
	}
	
	/**
	 * Takes all of the money.
	 */
	public void takeAll()
	{
		// TODO Implement
	}
	
	/**
	 * Updates the UI of this viewable.
	 */
	public void updateValue()
	{
		mValueText.setText(serializeValues(true, " "));
		ViewUtil.setEnabled(mTakeButton, !isEmpty());
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
