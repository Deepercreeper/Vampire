package com.deepercreeper.vampireapp.character.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.Money;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * This controller is used to control the money and warranty of the character.<br>
 * It displays the current wealth and allows to add or subtract money.
 * 
 * @author vrl
 */
public class MoneyControllerInstance implements Saveable, Viewable
{
	private final List<MoneyDepot>	mDepots			= new ArrayList<MoneyDepot>();
	
	private final Money				mMoney;
	
	private final LinearLayout		mContainer;
	
	private LinearLayout			mDepotsList;
	
	private final Context			mContext;
	
	private final boolean			mHost;
	
	private boolean					mInitialized	= false;
	
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
	public MoneyControllerInstance(final Money aMoney, final Context aContext, final boolean aHost)
	{
		mMoney = aMoney;
		mHost = aHost;
		mContext = aContext;
		final int id = mHost ? R.layout.host_money : R.layout.client_money;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		init();
		
		addDepot(new MoneyDepot(mContext.getString(R.string.bag), mContext, mHost, false, this));
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
	public MoneyControllerInstance(final Money aMoney, final Element aElement, final Context aContext, final boolean aHost)
	{
		mMoney = aMoney;
		mHost = aHost;
		mContext = aContext;
		final int id = mHost ? R.layout.host_money : R.layout.client_money;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		init();
		
		final NodeList childNodes = aElement.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++ )
		{
			final Node child = childNodes.item(i);
			if (child instanceof Element)
			{
				addDepot(new MoneyDepot(CodingUtil.decode(((Element) child).getTagName()), (Element) child, mContext, mHost, this));
			}
		}
	}
	
	/**
	 * Starts the create depot activity.
	 */
	public void addDepot()
	{
		// TODO Implement
	}
	
	/**
	 * Adds the given depot to the depots list.
	 * 
	 * @param aDepot
	 *            The new depot.
	 */
	public void addDepot(final MoneyDepot aDepot)
	{
		for (final MoneyDepot depot : mDepots)
		{
			depot.release();
		}
		mDepots.add(aDepot);
		Collections.sort(mDepots);
		for (final MoneyDepot depot : mDepots)
		{
			mDepotsList.addView(depot.getContainer());
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
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			mDepotsList = (LinearLayout) getContainer().findViewById(R.id.depot_list);
			final ImageButton addDepot = (ImageButton) getContainer().findViewById(R.id.add_depot);
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
	public Money getMoney()
	{
		return mMoney;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Removes the given depot from the depots list.
	 * 
	 * @param aDepot
	 *            The depot to remove.
	 */
	public void removeDepot(final MoneyDepot aDepot)
	{
		if ( !aDepot.isEmpty())
		{
			aDepot.takeAll();
		}
		mDepots.remove(aDepot);
		aDepot.release();
	}
}
