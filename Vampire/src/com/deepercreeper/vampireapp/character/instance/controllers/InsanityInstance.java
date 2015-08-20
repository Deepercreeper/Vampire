package com.deepercreeper.vampireapp.character.instance.controllers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.host.Message;
import com.deepercreeper.vampireapp.host.Message.ButtonAction;
import com.deepercreeper.vampireapp.host.Message.MessageGroup;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.implementations.Named;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.mechanics.Duration.DurationListener;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The insanity instance.
 * 
 * @author vrl
 */
public class InsanityInstance extends Named implements Viewable, Saveable, TimeListener, DurationListener
{
	private final InsanityControllerInstance mController;
	
	private final MessageListener mMessageListener;
	
	private final LinearLayout mContainer;
	
	private final Duration mDuration;
	
	private final Context mContext;
	
	private final boolean mHost;
	
	/**
	 * Creates a new insanity.
	 * 
	 * @param aName
	 *            The insanity name.
	 * @param aDuration
	 *            The insanity duration.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The parent controller.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided insantiy.
	 */
	public InsanityInstance(String aName, Duration aDuration, Context aContext, InsanityControllerInstance aController,
			MessageListener aMessageListener, boolean aHost)
	{
		super(aName);
		mController = aController;
		mDuration = aDuration;
		mHost = aHost;
		mContext = aContext;
		mMessageListener = aMessageListener;
		mContainer = (LinearLayout) View.inflate(mContext, mHost ? R.layout.host_insanity : R.layout.client_insanity, null);
		((TextView) getContainer().findViewById(mHost ? R.id.h_insanity_name_label : R.id.c_insanity_name_label)).setText(getName());
		if (mHost)
		{
			getContainer().findViewById(R.id.h_remove_insanity_button).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					mController.removeInsanity(InsanityInstance.this, false);
				}
			});
		}
		updateUI();
		mDuration.addListener(this);
	}
	
	@Override
	public Element asElement(Document aDoc)
	{
		Element element = aDoc.createElement("insanity");
		element.setAttribute("name", CodingUtil.encode(getName()));
		element.appendChild(mDuration.asElement(aDoc));
		return element;
	}
	
	/**
	 * @return the duration this insanity exists.
	 */
	public Duration getDuration()
	{
		return mDuration;
	}
	
	@Override
	public void time(Type aType, int aAmount)
	{
		mDuration.time(aType, aAmount);
	}
	
	@Override
	public void onDue()
	{
		mController.removeInsanity(this, true);
		final int id = mHost ? R.string.player_lost_insanity : R.string.lost_insanity;
		final String sender = mHost ? mMessageListener.getCharacter().getName() : "";
		mMessageListener.showMessage(
				new Message(MessageGroup.SINGLE, false, sender, id, new String[] { getName() }, mContext, mMessageListener, ButtonAction.NOTHING));
	}
	
	@Override
	public void timeUpdated()
	{
		updateUI();
	}
	
	@Override
	public void updateUI()
	{
		((TextView) getContainer().findViewById(mHost ? R.id.h_insanity_duration_label : R.id.c_insanity_duration_label))
				.setText(mDuration.getName(mContext));
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
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
}
