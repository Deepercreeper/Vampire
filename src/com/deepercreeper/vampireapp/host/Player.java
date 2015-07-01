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
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.host.change.MoneyChange;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.ResizeHeightAnimation;
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
	
	private boolean						mTimeEnabled;
	
	private boolean						mOpen	= false;
	
	private int							mTime	= TimeListener.EVENING;
	
	private LinearLayout				mContainer;
	
	private LinearLayout				mPlayerContainer;
	
	private CheckBox					mTimeCheckBox;
	
	private Button						mButton;
	
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
		init();
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
	public void applyMessage(final Message aMessage, ButtonAction aAction)
	{
		switch (aAction)
		{
			case NOTHING :
				break;
			default :
				break;
		}
		// TODO Implement other button actions
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
	
	/**
	 * Closes this player.
	 */
	public void close()
	{
		mOpen = false;
		mButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		resize();
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
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.player_view, null);
		mPlayerContainer = (LinearLayout) mContainer.findViewById(R.id.view_player_panel);
		mButton = (Button) mContainer.findViewById(R.id.view_player_button);
		
		final Button kick = (Button) mContainer.findViewById(R.id.view_kick_button);
		final Button ban = (Button) mContainer.findViewById(R.id.view_ban_button);
		
		mPlayerContainer.addView(mChar.getHealth().getContainer(), 0);
		mPlayerContainer.addView(mChar.getEPController().getContainer(), 1);
		mPlayerContainer.addView(mChar.getGenerationController().getContainer(), 2);
		mPlayerContainer.addView(mChar.getMoney().getContainer(), 3);
		
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
		
		if (mOpen)
		{
			mButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
		}
		else
		{
			mButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		}
		mButton.setText(getName());
		mButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				toggle();
			}
		});
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
	
	/**
	 * @return whether this player is open.
	 */
	public boolean isOpen()
	{
		return mOpen;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(mContainer);
		ViewUtil.release(mTimeCheckBox);
	}
	
	@Override
	public void resize()
	{
		if (mPlayerContainer == null)
		{
			return;
		}
		if (mPlayerContainer.getAnimation() != null && !mPlayerContainer.getAnimation().hasEnded())
		{
			mPlayerContainer.getAnimation().cancel();
		}
		int height = 0;
		if (isOpen())
		{
			height = ViewUtil.calcHeight(mPlayerContainer);
		}
		if (height != mPlayerContainer.getHeight())
		{
			mPlayerContainer.startAnimation(new ResizeHeightAnimation(mPlayerContainer, height));
		}
	}
	
	/**
	 * Sets the display of this player to AFK or active.
	 * 
	 * @param aAFK
	 *            whether the player is AFK.
	 */
	public void setAFK(final boolean aAFK)
	{
		mButton.setText(getName() + (aAFK ? " " + mContext.getString(R.string.afk) : ""));
		if (aAFK) close();
		ViewUtil.setEnabled(mButton, !aAFK);
	}
	
	/**
	 * Toggles whether this player is open.
	 */
	public void toggle()
	{
		mOpen = !mOpen;
		if (mOpen)
		{
			mButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
		}
		else
		{
			mButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		}
		resize();
	}
	
}
