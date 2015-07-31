package com.deepercreeper.vampireapp.character.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.host.Message;
import com.deepercreeper.vampireapp.host.Message.ButtonAction;
import com.deepercreeper.vampireapp.host.Message.MessageGroup;
import com.deepercreeper.vampireapp.host.change.InsanityChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.lists.controllers.creations.InsanityControllerCreation;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.mechanics.Duration.DurationListener;
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
import android.widget.TextView;

/**
 * A controller for all insanity instances.
 * 
 * @author vrl
 */
public class InsanityControllerInstance implements TimeListener, Saveable, Viewable
{
	private class InsanityDurationListener implements DurationListener
	{
		private final String mInsanity;
		
		public InsanityDurationListener(final String aInsanity)
		{
			mInsanity = aInsanity;
		}
		
		@Override
		public void onDue()
		{
			removeInsanity(mInsanity, true);
			final int id = mHost ? R.string.player_lost_insanity : R.string.lost_insanity;
			final String sender = mHost ? mMessageListener.getCharacter().getName() : "";
			mMessageListener.showMessage(new Message(MessageGroup.SINGLE, false, sender, id, new String[] { mInsanity }, mContext, mMessageListener,
					ButtonAction.NOTHING));
		}
		
		@Override
		public void timeUpdated()
		{
			// TODO Prevent this by adding insanity instances
			final TextView durationLabel = (TextView) mExpander.getContainer().getChildAt(mInsanities.indexOf(mInsanity))
					.findViewById(mHost ? R.id.h_insanity_duration_label : R.id.c_insanity_duration_label);
			durationLabel.setText(getDurationOf(mInsanity).getName(mContext));
		}
	}
	
	private final List<String> mInsanities = new ArrayList<String>();
	
	private final Map<String, Duration> mInsanityDurations = new HashMap<String, Duration>();
	
	private final LinearLayout mContainer;
	
	private final MessageListener mMessageListener;
	
	private final boolean mHost;
	
	private final Context mContext;
	
	private final Expander mExpander;
	
	private boolean mInitialized = false;
	
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
		
		init();
		
		for (Element insanity : DataUtil.getChildren(aElement, "insanity"))
		{
			final Duration duration = Duration.create(DataUtil.getElement(insanity, "duration"));
			addInsanity(CodingUtil.decode(insanity.getAttribute("name")), duration, true);
		}
		
		updateButton();
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
		
		init();
		
		for (final String insanity : aController.getInsanities())
		{
			addInsanity(insanity, Duration.FOREVER, true);
		}
		
		updateButton();
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
		if (mInsanities.contains(aInsanity))
		{
			return;
		}
		aDuration.addListener(new InsanityDurationListener(aInsanity));
		mInsanities.add(aInsanity);
		mInsanityDurations.put(aInsanity, aDuration);
		final View insanity = View.inflate(mContext, mHost ? R.layout.host_insanity : R.layout.client_insanity, null);
		((TextView) insanity.findViewById(mHost ? R.id.h_insanity_name_label : R.id.c_insanity_name_label)).setText(aInsanity);
		((TextView) insanity.findViewById(mHost ? R.id.h_insanity_duration_label : R.id.c_insanity_duration_label))
				.setText(getDurationOf(aInsanity).getName(mContext));
		if (mHost)
		{
			insanity.findViewById(R.id.h_remove_insanity_button).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					removeInsanity(aInsanity, false);
				}
			});
		}
		if (mHost)
		{
			mExpander.getContainer().addView(insanity, mExpander.getContainer().getChildCount() - 1);
		}
		else
		{
			mExpander.getContainer().addView(insanity);
		}
		mExpander.resize();
		updateButton();
		if ( !aSilent)
		{
			mMessageListener.sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.got_insanity, new String[] { aInsanity }, mContext,
					null, ButtonAction.NOTHING));
			mMessageListener.sendChange(new InsanityChange(aInsanity, aDuration));
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("insanities");
		for (final String insanity : getInsanities())
		{
			final Element insanityElement = aDoc.createElement("insanity");
			insanityElement.setAttribute("name", CodingUtil.encode(insanity));
			insanityElement.appendChild(getDurationOf(insanity).asElement(aDoc));
			element.appendChild(insanityElement);
		}
		return element;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @param aInsanity
	 *            The insanity.
	 * @return the duration left for the given insanity.
	 */
	public Duration getDurationOf(final String aInsanity)
	{
		return mInsanityDurations.get(aInsanity);
	}
	
	/**
	 * @return a list of all insanities.
	 */
	public List<String> getInsanities()
	{
		return mInsanities;
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
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
			
			mInitialized = true;
		}
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Removes the given insanity.
	 * 
	 * @param aInsanity
	 *            The insanity to remove.
	 * @param aSilent
	 *            Whether a change should be sent or not.
	 */
	public void removeInsanity(final String aInsanity, final boolean aSilent)
	{
		mExpander.getContainer().removeViewAt(mInsanities.indexOf(aInsanity));
		mInsanities.remove(aInsanity);
		mInsanityDurations.remove(aInsanity);
		mExpander.resize();
		updateButton();
		if ( !aSilent)
		{
			mMessageListener.sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.lost_insanity, new String[] { aInsanity }, mContext,
					null, ButtonAction.NOTHING));
			mMessageListener.sendChange(new InsanityChange(aInsanity));
		}
	}
	
	@Override
	public void time(final Type aType, final int aAmount)
	{
		final Set<Duration> durations = new HashSet<Duration>();
		durations.addAll(mInsanityDurations.values());
		for (final Duration duration : durations)
		{
			duration.time(aType, aAmount);
		}
	}
	
	private void updateButton()
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
