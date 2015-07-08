package com.deepercreeper.vampireapp.host;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionListener;
import com.deepercreeper.vampireapp.host.Message.ButtonAction;
import com.deepercreeper.vampireapp.host.change.CharacterChange;
import com.deepercreeper.vampireapp.host.change.EPChange;
import com.deepercreeper.vampireapp.host.change.GenerationChange;
import com.deepercreeper.vampireapp.host.change.HealthChange;
import com.deepercreeper.vampireapp.host.change.InventoryChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.host.change.MoneyChange;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Expander;
import com.deepercreeper.vampireapp.util.view.ResizeListener;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * A host side represented character player.
 * 
 * @author vrl
 */
public class Player implements Viewable, TimeListener, MessageListener, ResizeListener
{
	private final ConnectedDevice		mDevice;
	
	private final CharacterInstance		mChar;
	
	private final String				mNumber;
	
	private final Context				mContext;
	
	private final ConnectionListener	mListener;
	
	private final Expander				mExpander;
	
	private boolean						mTimeEnabled;
	
	private int							mTime	= TimeListener.EVENING;
	
	private LinearLayout				mContainer;
	
	private CheckBox					mTimeCheckBox;
	
	/**
	 * Creates a new player that caches all needed data of the remote character.
	 * 
	 * @param aCharacter
	 *            The character.
	 * @param aNumber
	 *            The players phone number.
	 * @param aDevice
	 *            The connected player device.
	 * @param aListener
	 *            The connection listener.
	 * @param aContext
	 *            The underlying context.
	 * @param aItems
	 */
	public Player(final String aCharacter, final String aNumber, final ConnectedDevice aDevice, final ConnectionListener aListener,
			final Context aContext, final ItemProvider aItems)
	{
		mChar = new CharacterInstance(aCharacter, aItems, aContext, this, this, true);
		mNumber = aNumber;
		mDevice = aDevice;
		mContext = aContext;
		mListener = aListener;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.player_view, null);
		mExpander = Expander.handle(R.id.view_player_button, R.id.view_player_panel, mContext);
		
		init();
	}
	
	@Override
	public void resize()
	{
		if (mExpander != null)
		{
			mExpander.resize();
		}
	}
	
	@Override
	public void makeText(final int aResId, final int aDuration)
	{
		mListener.makeText(aResId, aDuration);
	}
	
	@Override
	public void makeText(final String aText, final int aDuration)
	{
		mListener.makeText(aText, aDuration);
	}
	
	@Override
	public void sendChange(final CharacterChange aChange)
	{
		mDevice.send(MessageType.UPDATE, FilesUtil.serialize(aChange), aChange.getType());
	}
	
	@Override
	public void sendMessage(final Message aMessage)
	{
		mDevice.send(MessageType.MESSAGE, FilesUtil.serialize(aMessage));
	}
	
	@Override
	public boolean applyMessage(final Message aMessage, final ButtonAction aAction)
	{
		final boolean release = true;
		switch (aAction)
		{
			case NOTHING :
				break;
			case ACCEPT_DELETE :
				sendMessage(new Message("", R.string.accept_delete, aMessage.getArguments(), mContext, null, ButtonAction.ACCEPT_DELETE,
						aMessage.getSaveables()));
				break;
			case DENY_DELETE :
				sendMessage(new Message("", R.string.deny_take_depot, new String[] { aMessage.getArgument(0) }, mContext, null, ButtonAction.NOTHING,
						aMessage.getSaveables()));
				break;
			case ACCEPT_TAKE :
				sendMessage(new Message("", R.string.accept_take, aMessage.getArguments(), mContext, null, ButtonAction.ACCEPT_TAKE,
						aMessage.getSaveables()));
				break;
			case DENY_TAKE :
				sendMessage(new Message("", R.string.deny_take_depot, new String[] { aMessage.getArgument(1) }, mContext, null, ButtonAction.NOTHING,
						aMessage.getSaveables()));
				break;
			case ACCEPT_DEPOT :
				sendMessage(new Message("", R.string.accept_depot, aMessage.getArguments(), mContext, null, ButtonAction.ACCEPT_DEPOT,
						aMessage.getSaveables()));
				break;
			case DENY_DEPOT :
				sendMessage(new Message("", R.string.deny_take_depot, new String[] { aMessage.getArgument(1) }, mContext, null, ButtonAction.NOTHING,
						aMessage.getSaveables()));
				break;
			default :
				break;
		}
		// TODO Implement other button actions
		return release;
	}
	
	@Override
	public void time(final Type aType, final int aAmount)
	{
		if (mTimeEnabled)
		{
			if (aType != Type.SET)
			{
				mDevice.send(MessageType.TIME, aType.name(), "" + aAmount);
			}
			else
			{
				final int difference = (aAmount + 24 - mTime) % 24;
				mDevice.send(MessageType.TIME, Type.HOUR.name(), "" + difference);
			}
			switch (aType)
			{
				case DAY :
					mTime = TimeListener.EVENING;
					break;
				case HOUR :
					mTime = (mTime + aAmount) % 24;
					break;
				case SET :
					mTime = aAmount;
					break;
				default :
					break;
			}
		}
		updateTime();
	}
	
	/**
	 * Updates the checkbox and displays the current player time behind it.
	 */
	public void updateTime()
	{
		mTimeCheckBox.setText(getName() + " - " + mTime + ":00");
	}
	
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Player other = (Player) obj;
		if (mChar == null)
		{
			if (other.mChar != null) return false;
		}
		else if ( !mChar.equals(other.mChar)) return false;
		return true;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the checkbox used for enabling the time management.
	 */
	public CheckBox getPlayerCheckBox()
	{
		return mTimeCheckBox;
	}
	
	/**
	 * @return the players device.
	 */
	public ConnectedDevice getDevice()
	{
		return mDevice;
	}
	
	/**
	 * @return the players name.
	 */
	public String getName()
	{
		return mChar.getName();
	}
	
	@Override
	public void applyChange(final String aChange, final String aType)
	{
		final Document doc = FilesUtil.loadDocument(aChange);
		final Element element = (Element) doc.getElementsByTagName(aType).item(0);
		CharacterChange change = null;
		if (aType.equals(HealthChange.TAG_NAME))
		{
			change = new HealthChange(element);
		}
		else if (aType.equals(EPChange.TAG_NAME))
		{
			change = new EPChange(element);
		}
		else if (aType.equals(GenerationChange.TAG_NAME))
		{
			change = new GenerationChange(element);
		}
		else if (aType.equals(MoneyChange.TAG_NAME))
		{
			change = new MoneyChange(element);
		}
		else if (aType.equals(InventoryChange.TAG_NAME))
		{
			change = new InventoryChange(element, mContext);
		}
		
		// TODO Add other changes
		
		if (change != null)
		{
			change.applyChange(mChar);
		}
	}
	
	/**
	 * @return the players phone number.
	 */
	public String getNumber()
	{
		return mNumber;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mChar == null) ? 0 : mChar.hashCode());
		return result;
	}
	
	@Override
	public void init()
	{
		mExpander.init();
		
		mChar.update();
		
		final LinearLayout playerContainer = mExpander.getContainer();
		final Button kick = (Button) mContainer.findViewById(R.id.view_kick_button);
		final Button ban = (Button) mContainer.findViewById(R.id.view_ban_button);
		
		playerContainer.addView(mChar.getHealth().getContainer(), 0);
		playerContainer.addView(mChar.getEPController().getContainer(), 1);
		playerContainer.addView(mChar.getGenerationController().getContainer(), 2);
		playerContainer.addView(mChar.getMoney().getContainer(), 3);
		playerContainer.addView(mChar.getInventory().getContainer(), 4);
		
		Expander expander = Expander.handle(R.id.view_player_controller_button, R.id.view_player_controller_panel, mContext, mExpander);
		expander.init();
		
		final LinearLayout controllerPanel = expander.getContainer();
		for (final ItemControllerInstance controller : mChar.getControllers())
		{
			if (controller.hasAnyItem())
			{
				controller.release();
				controller.init();
				controller.close();
				controllerPanel.addView(controller.getContainer());
			}
		}
		
		mTimeCheckBox = new CheckBox(mContext);
		mTimeCheckBox.setLayoutParams(ViewUtil.getWrapHeight());
		mTimeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(final CompoundButton aButtonView, final boolean aIsChecked)
			{
				mTimeEnabled = aIsChecked;
			}
		});
		updateTime();
		mTimeCheckBox.setChecked(true);
		
		mExpander.getButton().setText(getName());
		kick.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mDevice.send(MessageType.KICKED);
				mDevice.exit();
			}
		});
		ban.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mListener.banned(Player.this);
			}
		});
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(mContainer);
		ViewUtil.release(mTimeCheckBox);
	}
	
	/**
	 * Sets the display of this player to AFK or active.
	 * 
	 * @param aAFK
	 *            whether the player is AFK.
	 */
	public void setAFK(final boolean aAFK)
	{
		mExpander.getButton().setText(getName() + (aAFK ? " " + mContext.getString(R.string.afk) : ""));
		if (aAFK)
		{
			mExpander.close();
		}
		ViewUtil.setEnabled(mExpander.getButton(), !aAFK);
	}
}
