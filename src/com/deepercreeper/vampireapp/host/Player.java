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
import com.deepercreeper.vampireapp.host.connection.PlayerHealth;
import com.deepercreeper.vampireapp.host.connection.change.ChangeListener;
import com.deepercreeper.vampireapp.host.connection.change.CharacterChange;
import com.deepercreeper.vampireapp.host.connection.change.HealthChange;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.ResizeHeightAnimation;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * A host side represented character player.
 * 
 * @author vrl
 */
public class Player implements Viewable, TimeListener, ChangeListener
{
	private final ConnectedDevice		mDevice;
	
	private final CharacterInstance		mChar;
	
	private final String				mNumber;
	
	private final Context				mContext;
	
	private final ConnectionListener	mListener;
	
	private boolean						mTimeEnabled;
	
	private boolean						mOpen	= false;
	
	private int							mTime	= TimeListener.EVENING;
	
	private final PlayerHealth			mHealth;
	
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
		mChar = new CharacterInstance(aCharacter, aItems, aContext, this, true);
		mHealth = new PlayerHealth(mChar.getHealth(), aContext);
		mNumber = aNumber;
		mDevice = aDevice;
		mContext = aContext;
		mListener = aListener;
		init();
	}
	
	@Override
	public void sendChange(CharacterChange aChange)
	{
		mDevice.send(MessageType.UPDATE, FilesUtil.serialize(aChange), aChange.getType());
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
	public void applyChange(String aChange, String aType)
	{
		Document doc = FilesUtil.loadDocument(aChange);
		Element element;
		CharacterChange change = null;
		if (aType.equals(HealthChange.TAG_NAME))
		{
			element = (Element) doc.getElementsByTagName(HealthChange.TAG_NAME).item(0);
			change = new HealthChange(element);
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
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.player, null);
		mPlayerContainer = (LinearLayout) mContainer.findViewById(R.id.player_container);
		mButton = (Button) mContainer.findViewById(R.id.player_button);
		
		final Button kick = (Button) mContainer.findViewById(R.id.kick_player);
		final Button ban = (Button) mContainer.findViewById(R.id.ban_player);
		
		mPlayerContainer.addView(mHealth.getHealthContainer(), 0);
		
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
	
	/**
	 * Starts a animation, that resizes the player container to the right size.
	 */
	public void resize()
	{
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
		mButton.setEnabled( !aAFK);
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
