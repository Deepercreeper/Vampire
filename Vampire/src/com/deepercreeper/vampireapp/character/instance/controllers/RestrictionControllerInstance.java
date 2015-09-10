package com.deepercreeper.vampireapp.character.instance.controllers;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.host.change.RestrictionChange;
import com.deepercreeper.vampireapp.items.implementations.instances.restrictions.RestrictionInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.ResizeListener;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import com.deepercreeper.vampireapp.util.view.Expander;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateRestrictionDialog;
import com.deepercreeper.vampireapp.util.view.listeners.RestrictionCreationListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * A controller for handling all character restrictions.
 * 
 * @author Vincent
 */
public class RestrictionControllerInstance implements Viewable, Saveable, TimeListener
{
	private final LinearLayout mContainer;
	
	private final Context mContext;
	
	private final Expander mExpander;
	
	private final CharacterInstance mChar;
	
	private final MessageListener mMessageListener;
	
	private final List<RestrictionInstance> mRestrictions = new ArrayList<RestrictionInstance>();
	
	private final boolean mHost;
	
	/**
	 * Creates a new restriction controller.
	 * 
	 * @param aChar
	 *            The parent character.
	 * @param aContext
	 *            The underlying context.
	 * @param aResizeListener
	 *            The resize listener. May be {@code null}.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 */
	public RestrictionControllerInstance(final CharacterInstance aChar, final Context aContext, final ResizeListener aResizeListener,
			final MessageListener aMessageListener, final boolean aHost)
	{
		mHost = aHost;
		mChar = aChar;
		mContext = aContext;
		mMessageListener = aMessageListener;
		final int id = mHost ? R.layout.host_restriction_controller : R.layout.client_restriction_controller;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		final int buttonId = mHost ? R.id.h_restrictions_button : R.id.c_restrictions_button;
		final int containerId = mHost ? R.id.h_restrictions_panel : R.id.c_restrictions_panel;
		mExpander = Expander.handle(buttonId, containerId, getContainer(), aResizeListener);
		
		mExpander.init();
		
		if (mHost)
		{
			getContainer().findViewById(R.id.h_add_restriction_button).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					addRestriction();
				}
			});
		}
	}
	
	/**
	 * Creates a new restriction controller out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 * @param aChar
	 *            The parent character.
	 * @param aContext
	 *            The underlying context.
	 * @param aResizeListener
	 *            The resize listener. May be {@code null}.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 */
	public RestrictionControllerInstance(final Element aElement, final CharacterInstance aChar, final Context aContext,
			final ResizeListener aResizeListener, final MessageListener aMessageListener, final boolean aHost)
	{
		mHost = aHost;
		mChar = aChar;
		mContext = aContext;
		mMessageListener = aMessageListener;
		final int id = mHost ? R.layout.host_restriction_controller : R.layout.client_restriction_controller;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		final int buttonId = mHost ? R.id.h_restrictions_button : R.id.c_restrictions_button;
		final int containerId = mHost ? R.id.h_restrictions_panel : R.id.c_restrictions_panel;
		mExpander = Expander.handle(buttonId, containerId, getContainer(), aResizeListener);
		
		mExpander.init();
		
		if (mHost)
		{
			getContainer().findViewById(R.id.h_add_restriction_button).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					addRestriction();
				}
			});
		}
		
		for (final Element restriction : DataUtil.getChildren(aElement, "restriction"))
		{
			addRestriction(new RestrictionInstanceImpl(restriction, mContext, mMessageListener, mHost), true);
		}
	}
	
	/**
	 * Adds a new restriction.
	 */
	public void addRestriction()
	{
		final RestrictionCreationListener listener = new RestrictionCreationListener()
		{
			@Override
			public void restrictionCreated(final RestrictionInstance aRestriction)
			{
				addRestriction(aRestriction, false);
			}
		};
		CreateRestrictionDialog.showCreateRestrictionDialog(mContext.getString(R.string.create_restriction), mContext, mMessageListener, listener,
				mChar);
	}
	
	/**
	 * Adds the given restriction to the character.
	 * 
	 * @param aRestriction
	 *            The new restriction.
	 * @param aSilent
	 *            whether a change should be sent.
	 */
	public void addRestriction(final RestrictionInstance aRestriction, final boolean aSilent)
	{
		final ItemInstance item = mChar.findItemInstance(aRestriction.getItemName());
		if (item != null)
		{
			item.addRestriction(aRestriction);
			mRestrictions.add(aRestriction);
			mExpander.getContainer().addView(aRestriction.getContainer(), mExpander.getContainer().getChildCount() - 1);
			mExpander.resize();
			if ( !aSilent)
			{
				mMessageListener.sendChange(new RestrictionChange(aRestriction, true));
			}
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("restrictions");
		for (final RestrictionInstance restriction : getRestrictions())
		{
			element.appendChild(restriction.asElement(aDoc));
		}
		return element;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return a list of all restrictions.
	 */
	public List<RestrictionInstance> getRestrictions()
	{
		return mRestrictions;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Removes the given restriction from this character.
	 * 
	 * @param aRestriction
	 *            The restriction to remove.
	 */
	public void removeRestriction(final RestrictionInstance aRestriction)
	{
		mRestrictions.remove(aRestriction);
		aRestriction.release();
		mExpander.resize();
	}
	
	/**
	 * @param aRestriction
	 *            The restriction to match.
	 * @return The restriction instance that equals the given restriction.
	 */
	public RestrictionInstance getRestriction(final RestrictionInstance aRestriction)
	{
		for (final RestrictionInstance restriction : getRestrictions())
		{
			if (restriction.equals(aRestriction))
			{
				return restriction;
			}
		}
		return null;
	}
	
	@Override
	public void time(final Type aType, final int aAmount)
	{
		for (final RestrictionInstance restriction : getRestrictions())
		{
			restriction.time(aType, aAmount);
		}
	}
	
	@Override
	public void updateUI()
	{
		for (final RestrictionInstance restriction : getRestrictions())
		{
			restriction.updateUI();
		}
	}
}
