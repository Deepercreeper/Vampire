package com.deepercreeper.vampireapp.character.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.Currency;
import com.deepercreeper.vampireapp.host.Message;
import com.deepercreeper.vampireapp.host.Message.ButtonAction;
import com.deepercreeper.vampireapp.host.Message.MessageGroup;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.host.change.MoneyChange;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.ResizeListener;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import com.deepercreeper.vampireapp.util.view.Expander;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateStringDialog;
import com.deepercreeper.vampireapp.util.view.listeners.StringCreationListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * This controller is used to control the money and warranty of the character.<br>
 * It displays the current wealth and allows to add or subtract money.
 * 
 * @author vrl
 */
public class MoneyControllerInstance implements Saveable, Viewable
{
	private final List<MoneyDepot> mDepots = new ArrayList<MoneyDepot>();
	
	private final Currency mCurrency;
	
	private final LinearLayout mContainer;
	
	private final Context mContext;
	
	private final boolean mHost;
	
	private final MoneyDepot mDefaultDepot;
	
	private final MessageListener mMessageListener;
	
	private final ResizeListener mResizeListener;
	
	private final CharacterInstance mChar;
	
	private final Expander mExpander;
	
	private LinearLayout mDepotsList;
	
	private boolean mInitialized = false;
	
	/**
	 * Creates a new money controller.
	 * 
	 * @param aCurrency
	 *            the default money settings.
	 * @param aContext
	 *            The underlying context.
	 * @param aHost
	 *            Whether this controller is host sided.
	 * @param aChangeListener
	 *            The change listener.
	 * @param aResizeListener
	 *            The resize listener.
	 * @param aChar
	 *            The parent character.
	 */
	public MoneyControllerInstance(final Currency aCurrency, final Context aContext, final boolean aHost, final MessageListener aChangeListener,
			final ResizeListener aResizeListener, final CharacterInstance aChar)
	{
		mCurrency = aCurrency;
		mHost = aHost;
		mContext = aContext;
		mMessageListener = aChangeListener;
		mResizeListener = aResizeListener;
		mChar = aChar;
		final int id = mHost ? R.layout.host_money : R.layout.client_money;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		mExpander = Expander.handle(mHost ? R.id.h_money_button : R.id.c_money_button, mHost ? R.id.h_money_list : R.id.c_money_list, mContainer,
				mResizeListener);
				
		init();
		
		mDefaultDepot = new MoneyDepot(mContext.getString(R.string.bag), mContext, mHost, true, this);
		addDepot(mDefaultDepot, true);
	}
	
	/**
	 * Creates a new money controller out of the given XML data.
	 * 
	 * @param aCurrency
	 *            The default money settings.
	 * @param aElement
	 *            The XML document.
	 * @param aContext
	 *            The underlying context.
	 * @param aHost
	 *            Whether this controller is host sided.
	 * @param aChangeListener
	 *            The change listener.
	 * @param aResizeListener
	 *            The resize listener.
	 * @param aChar
	 *            The parent character.
	 */
	public MoneyControllerInstance(final Currency aCurrency, final Element aElement, final Context aContext, final boolean aHost,
			final MessageListener aChangeListener, final ResizeListener aResizeListener, final CharacterInstance aChar)
	{
		mCurrency = aCurrency;
		mHost = aHost;
		mContext = aContext;
		mMessageListener = aChangeListener;
		mResizeListener = aResizeListener;
		mChar = aChar;
		final int id = mHost ? R.layout.host_money : R.layout.client_money;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		mExpander = Expander.handle(mHost ? R.id.h_money_button : R.id.c_money_button, mHost ? R.id.h_money_list : R.id.c_money_list, mContainer,
				mResizeListener);
				
		init();
		
		MoneyDepot defaultDepot = null;
		final NodeList childNodes = aElement.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++ )
		{
			final Node child = childNodes.item(i);
			if (child instanceof Element)
			{
				final MoneyDepot depot = new MoneyDepot(CodingUtil.decode(((Element) child).getTagName()), (Element) child, mContext, mHost, this);
				addDepot(depot, true);
				if (depot.isDefault())
				{
					defaultDepot = depot;
				}
			}
		}
		mDefaultDepot = defaultDepot;
		updateValues();
	}
	
	/**
	 * @return the message listener.
	 */
	public MessageListener getMessageListener()
	{
		return mMessageListener;
	}
	
	/**
	 * @return the parent char.
	 */
	public CharacterInstance getChar()
	{
		return mChar;
	}
	
	/**
	 * @param aName
	 *            The depot name.
	 * @return the depot with the give name.
	 */
	public MoneyDepot getDepot(final String aName)
	{
		for (final MoneyDepot depot : mDepots)
		{
			if (depot.getName().equals(aName))
			{
				return depot;
			}
		}
		return null;
	}
	
	/**
	 * Starts the create depot activity.
	 */
	public void addDepot()
	{
		if (CreateStringDialog.isDialogOpen())
		{
			return;
		}
		final StringCreationListener listener = new StringCreationListener()
		{
			@Override
			public void create(final String aString)
			{
				final String name = aString.trim();
				if (name.isEmpty() || hasDepot(name))
				{
					addDepot();
				}
				else
				{
					addDepot(new MoneyDepot(aString, mContext, mHost, false, MoneyControllerInstance.this), false);
				}
			}
		};
		CreateStringDialog.showCreateStringDialog(mContext.getString(R.string.create_depot_title), mContext.getString(R.string.create_depot),
				mContext, listener);
	}
	
	/**
	 * @param aName
	 *            The new depot name.
	 * @return whether a depot with the given name already exists.
	 */
	public boolean hasDepot(final String aName)
	{
		for (final MoneyDepot depot : mDepots)
		{
			if (depot.getName().equals(aName))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds the given depot to the depots list.
	 * 
	 * @param aDepot
	 *            The new depot.
	 * @param aSilent
	 *            Whether the host or client should be notified.
	 */
	public void addDepot(final MoneyDepot aDepot, final boolean aSilent)
	{
		for (final MoneyDepot depot : mDepots)
		{
			depot.release();
		}
		mDepots.add(aDepot);
		Collections.sort(mDepots, MoneyDepot.COMPARATOR);
		for (final MoneyDepot depot : mDepots)
		{
			mDepotsList.addView(depot.getContainer());
		}
		if (mExpander != null)
		{
			mExpander.resize();
		}
		if ( !aSilent)
		{
			getMessageListener().sendChange(new MoneyChange(aDepot.getName(), true));
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("money");
		for (final MoneyDepot depot : mDepots)
		{
			element.appendChild(depot.asElement(aDoc));
		}
		return element;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the characters default depot alias his bag.
	 */
	public MoneyDepot getDefaultDepot()
	{
		return mDefaultDepot;
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			mExpander.init();
			
			mDepotsList = (LinearLayout) getContainer().findViewById(mHost ? R.id.h_depot_list : R.id.c_depot_list);
			final Button addDepot = (Button) getContainer().findViewById(mHost ? R.id.h_add_depot_button : R.id.c_add_depot_button);
			
			addDepot.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					addDepot();
				}
			});
			
			mInitialized = true;
		}
	}
	
	/**
	 * @return the money attributes.
	 */
	public Currency getCurrency()
	{
		return mCurrency;
	}
	
	/**
	 * Updates all depot values.
	 */
	public void updateValues()
	{
		for (final MoneyDepot depot : mDepots)
		{
			depot.updateValue();
		}
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Removes the given depot from the depots list.
	 * 
	 * @param aName
	 *            The depot name.
	 * @param aSilent
	 *            Whether the host or client should be notified.
	 */
	public void removeDepot(final String aName, final boolean aSilent)
	{
		final MoneyDepot depot = getDepot(aName);
		if ( !depot.isEmpty())
		{
			getMessageListener().sendMessage(new Message(MessageGroup.MONEY, getChar().getName(), R.string.ask_delete_depot,
					new String[] { depot.getName() }, mContext, null, ButtonAction.ACCEPT_DELETE, ButtonAction.DENY_DELETE, depot.getName()));
		}
		else
		{
			mDepots.remove(depot);
			depot.release();
			mExpander.resize();
			if ( !aSilent)
			{
				getMessageListener().sendChange(new MoneyChange(aName, false));
			}
		}
	}
}
