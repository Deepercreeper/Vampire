package com.deepercreeper.vampireapp.host;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionListener;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.ResizeHeightAnimation;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * A host side represented character player.
 * 
 * @author vrl
 */
public class Player implements Viewable
{
	private final ConnectedDevice		mDevice;
	
	private final String				mName;
	
	private final String				mNumber;
	
	private final Context				mContext;
	
	private final ConnectionListener	mListener;
	
	private boolean						mOpen	= false;
	
	private LinearLayout				mContainer;
	
	private LinearLayout				mPlayerContainer;
	
	private Button						mButton;
	
	/**
	 * Creates a new player that caches all needed data of the remote character.
	 * 
	 * @param aName
	 *            The player name.
	 * @param aNumber
	 *            The players phone number.
	 * @param aDevice
	 *            The connected player device.
	 * @param aListener
	 *            The connection listener.
	 * @param aContext
	 *            The underlying context.
	 */
	public Player(final String aName, final String aNumber, final ConnectedDevice aDevice, final ConnectionListener aListener, final Context aContext)
	{
		mName = aName;
		mNumber = aNumber;
		mDevice = aDevice;
		mContext = aContext;
		mListener = aListener;
		init();
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
		if (mName == null)
		{
			if (other.mName != null) return false;
		}
		else if ( !mName.equals(other.mName)) return false;
		return true;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
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
		return mName;
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
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
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
