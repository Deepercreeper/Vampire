package com.deepercreeper.vampireapp.character.instance.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.controllers.InsanityControllerCreation;
import com.deepercreeper.vampireapp.character.creation.controllers.InsanityCreation;
import com.deepercreeper.vampireapp.host.Message;
import com.deepercreeper.vampireapp.host.Message.ButtonAction;
import com.deepercreeper.vampireapp.host.Message.MessageGroup;
import com.deepercreeper.vampireapp.host.change.InsanityChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.ResizeListener;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import com.deepercreeper.vampireapp.util.view.Expander;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateInsanityDialog;
import com.deepercreeper.vampireapp.util.view.listeners.InsanityCreationListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * A controller for all insanity instances.
 * 
 * @author vrl
 */
public class InsanityControllerInstance implements TimeListener, Saveable, Viewable
{
	private final List<InsanityInstance> mInsanities = new ArrayList<InsanityInstance>();
	
	private final LinearLayout mContainer;
	
	private final MessageListener mMessageListener;
	
	private final boolean mHost;
	
	private final Context mContext;
	
	private final Expander mExpander;
	
	/**
	 * Creates a new insanity controller out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aContext
	 *            The underlying context.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aResizeListener
	 *            The parent resize listener.
	 */
	public InsanityControllerInstance(final Element aElement, final Context aContext, final boolean aHost, final MessageListener aMessageListener,
			final ResizeListener aResizeListener)
	{
		mHost = aHost;
		mContext = aContext;
		mMessageListener = aMessageListener;
		final int id = mHost ? R.layout.host_insanities : R.layout.client_insanities;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		final int buttonId = mHost ? R.id.h_insanities_button : R.id.c_insanities_button;
		final int containerId = mHost ? R.id.h_insanities_panel : R.id.c_insanities_panel;
		mExpander = Expander.handle(buttonId, containerId, getContainer(), aResizeListener);
		
		mExpander.init();
		
		if (mHost)
		{
			getContainer().findViewById(R.id.h_add_insanity_button).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					addInsanity();
				}
			});
		}
		
		for (final Element insanity : DataUtil.getChildren(aElement, "insanity"))
		{
			final Duration duration = Duration.create(DataUtil.getElement(insanity, "duration"));
			addInsanity(CodingUtil.decode(insanity.getAttribute("name")), duration, true);
		}
		
		updateUI();
	}
	
	/**
	 * Creates a new insanity controller out of the given insanity controller creation.
	 * 
	 * @param aController
	 *            The insanity controller creation.
	 * @param aContext
	 *            The underlying context.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aResizeListener
	 *            The parent resize listener.
	 */
	public InsanityControllerInstance(final InsanityControllerCreation aController, final Context aContext, final boolean aHost,
			final MessageListener aMessageListener, final ResizeListener aResizeListener)
	{
		mHost = aHost;
		mContext = aContext;
		mMessageListener = aMessageListener;
		final int id = mHost ? R.layout.host_insanities : R.layout.client_insanities;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		final int buttonId = mHost ? R.id.h_insanities_button : R.id.c_insanities_button;
		final int containerId = mHost ? R.id.h_insanities_panel : R.id.c_insanities_panel;
		mExpander = Expander.handle(buttonId, containerId, getContainer(), aResizeListener);
		
		mExpander.init();
		
		if (mHost)
		{
			getContainer().findViewById(R.id.h_add_insanity_button).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					addInsanity();
				}
			});
		}
		
		for (final InsanityCreation insanity : aController.getInsanities())
		{
			addInsanity(insanity.getName(), Duration.FOREVER, true);
		}
		
		updateUI();
	}
	
	/**
	 * Adds a new insanity.
	 */
	public void addInsanity()
	{
		final InsanityCreationListener listener = new InsanityCreationListener()
		{
			@Override
			public void insanityCreated(final String aName, final Duration aDuration)
			{
				addInsanity(aName, aDuration, false);
			}
		};
		CreateInsanityDialog.showCreateInsanityDialog(mContext.getString(R.string.create_insanity), mContext, mInsanities, listener);
	}
	
	/**
	 * Adds an insanity and the duration for it.
	 * 
	 * @param aInsanity
	 *            The insanity.
	 * @param aDuration
	 *            The insanity duration.
	 * @param aSilent
	 *            Whether a change should be sent.
	 */
	public void addInsanity(final String aInsanity, final Duration aDuration, final boolean aSilent)
	{
		InsanityInstance insanity = new InsanityInstance(aInsanity, aDuration, mContext, this, mMessageListener, mHost);
		if (mInsanities.contains(insanity))
		{
			return;
		}
		mInsanities.add(insanity);
		if (mHost)
		{
			mExpander.getContainer().addView(insanity.getContainer(), mExpander.getContainer().getChildCount() - 1);
		}
		else
		{
			mExpander.getContainer().addView(insanity.getContainer());
		}
		mExpander.resize();
		updateUI();
		if ( !aSilent)
		{
			mMessageListener.sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.got_insanity, new String[] { insanity.getName() },
					mContext, null, ButtonAction.NOTHING));
			mMessageListener.sendChange(new InsanityChange(insanity.getName(), insanity.getDuration()));
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("insanities");
		for (final InsanityInstance insanity : getInsanities())
		{
			element.appendChild(insanity.asElement(aDoc));
		}
		return element;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return a list of all insanities.
	 */
	public List<InsanityInstance> getInsanities()
	{
		return mInsanities;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Removes the insanity with the given name.
	 * 
	 * @param aInsanity
	 *            The insanity name.
	 * @param aSilent
	 *            Whether changes should be sent.
	 */
	public void removeInsanity(String aInsanity, boolean aSilent)
	{
		for (InsanityInstance insanity : getInsanities())
		{
			if (insanity.getName().equals(aInsanity))
			{
				removeInsanity(insanity, aSilent);
			}
		}
	}
	
	/**
	 * Removes the given insanity.
	 * 
	 * @param aInsanity
	 *            The insanity to remove.
	 * @param aSilent
	 *            Whether a change should be sent or not.
	 */
	public void removeInsanity(final InsanityInstance aInsanity, final boolean aSilent)
	{
		aInsanity.release();
		mInsanities.remove(aInsanity);
		
		mExpander.resize();
		updateUI();
		if ( !aSilent)
		{
			mMessageListener.sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.lost_insanity, new String[] { aInsanity.getName() },
					mContext, null, ButtonAction.NOTHING));
			mMessageListener.sendChange(new InsanityChange(aInsanity.getName()));
		}
	}
	
	@Override
	public void time(final Type aType, final int aAmount)
	{
		final Set<TimeListener> listeners = new HashSet<TimeListener>();
		listeners.addAll(getInsanities());
		for (final TimeListener listener : listeners)
		{
			listener.time(aType, aAmount);
		}
	}
	
	@Override
	public void updateUI()
	{
		if (mHost)
		{
			return;
		}
		final boolean hasInsanities = !getInsanities().isEmpty();
		if ( !hasInsanities)
		{
			mExpander.close();
		}
		ViewUtil.setEnabled(mExpander.getButton(), hasInsanities);
	}
}
