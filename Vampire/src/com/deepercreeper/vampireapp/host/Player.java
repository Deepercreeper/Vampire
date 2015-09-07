package com.deepercreeper.vampireapp.host;

import java.util.Arrays;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.MoneyDepot;
import com.deepercreeper.vampireapp.character.instance.controllers.MoneyControllerInstance;
import com.deepercreeper.vampireapp.connection.ConnectedDevice;
import com.deepercreeper.vampireapp.connection.ConnectedDevice.MessageType;
import com.deepercreeper.vampireapp.connection.ConnectionListener;
import com.deepercreeper.vampireapp.host.Message.ButtonAction;
import com.deepercreeper.vampireapp.host.Message.MessageGroup;
import com.deepercreeper.vampireapp.host.change.CharacterChange;
import com.deepercreeper.vampireapp.host.change.EPChange;
import com.deepercreeper.vampireapp.host.change.GenerationChange;
import com.deepercreeper.vampireapp.host.change.HealthChange;
import com.deepercreeper.vampireapp.host.change.InsanityChange;
import com.deepercreeper.vampireapp.host.change.InventoryChange;
import com.deepercreeper.vampireapp.host.change.ItemChange;
import com.deepercreeper.vampireapp.host.change.ItemGroupChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.host.change.ModeChange;
import com.deepercreeper.vampireapp.host.change.MoneyChange;
import com.deepercreeper.vampireapp.host.change.RestrictionChange;
import com.deepercreeper.vampireapp.items.ItemProvider;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.ResizeListener;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import com.deepercreeper.vampireapp.util.view.Expander;
import com.deepercreeper.vampireapp.util.view.dialogs.ChooseDifficultyDialog;
import com.deepercreeper.vampireapp.util.view.listeners.DifficultyChooseListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

/**
 * A host side represented character player.
 * 
 * @author vrl
 */
public class Player implements Viewable, TimeListener, MessageListener, ResizeListener
{
	private final Host mHost;
	
	private final ConnectedDevice mDevice;
	
	private final CharacterInstance mChar;
	
	private final String mNumber;
	
	private final Context mContext;
	
	private final ConnectionListener mListener;
	
	private final Expander mExpander;
	
	private final Expander mControllerExpander;
	
	private final LinearLayout mContainer;
	
	private boolean mTimeEnabled = true;
	
	private int mTime = TimeListener.EVENING;
	
	private CheckBox mTimeCheckBox;
	
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
	 *            the item provider.
	 * @param aHost
	 *            The parent host.
	 */
	public Player(final String aCharacter, final String aNumber, final ConnectedDevice aDevice, final ConnectionListener aListener,
			final Context aContext, final ItemProvider aItems, final Host aHost)
	{
		mHost = aHost;
		mNumber = aNumber;
		mDevice = aDevice;
		mContext = aContext;
		mListener = aListener;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_player, null);
		mExpander = Expander.handle(R.id.view_player_button, R.id.view_player_panel, mContainer);
		mControllerExpander = Expander.handle(R.id.view_player_controller_button, R.id.view_player_controller_panel, mContainer, mExpander);
		mChar = new CharacterInstance(aCharacter, aItems, aContext, this, mExpander, mControllerExpander, true);
		
		init();
	}
	
	@Override
	public void applyChange(final String aChange, final String aType)
	{
		final Document doc = DataUtil.loadDocument(aChange);
		final Element element = DataUtil.getElement(doc, aType);
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
			change = new InventoryChange(element, mContext, mChar);
		}
		else if (aType.equals(ItemChange.TAG_NAME))
		{
			change = new ItemChange(element);
		}
		else if (aType.equals(InsanityChange.TAG_NAME))
		{
			change = new InsanityChange(element);
		}
		else if (aType.equals(ItemGroupChange.TAG_NAME))
		{
			change = new ItemGroupChange(element);
		}
		else if (aType.equals(ModeChange.TAG_NAME))
		{
			change = new ModeChange(element);
		}
		else if (aType.equals(RestrictionChange.TAG_NAME))
		{
			change = new RestrictionChange(element, mContext, this, true);
		}
		
		// MARK Changes
		
		if (change != null)
		{
			change.applyChange(mChar);
		}
	}
	
	@Override
	public boolean applyMessage(final Message aMessage, final ButtonAction aAction)
	{
		final boolean release = true;
		final MoneyControllerInstance money = mChar.getMoney();
		switch (aAction)
		{
			case NOTHING :
				break;
			case ACCEPT_DELETE :
				final String deletedDepot = aMessage.getSaveable(0);
				money.getDepot(deletedDepot).takeAll();
				money.removeDepot(deletedDepot, false);
				sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.accept_delete, aMessage.getArguments(), mContext, null,
						ButtonAction.NOTHING));
				break;
			case DENY_DELETE :
				sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.deny_take_depot, new String[] { aMessage.getArgument(0) }, mContext,
						null, ButtonAction.NOTHING, aMessage.getSaveables()));
				break;
			case ACCEPT_TAKE :
				final Map<String, Integer> takeValues = MoneyDepot.deserializeValues(",", " ", aMessage.getSaveable(0), money.getCurrency());
				final String takeDepotName = aMessage.getSaveable(1);
				money.getDepot(takeDepotName).remove(takeValues);
				money.getDefaultDepot().add(takeValues);
				sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.accept_take, aMessage.getArguments(), mContext, null,
						ButtonAction.NOTHING));
				break;
			case DENY_TAKE :
				sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.deny_take_depot, new String[] { aMessage.getArgument(1) }, mContext,
						null, ButtonAction.NOTHING, aMessage.getSaveables()));
				break;
			case ACCEPT_DEPOT :
				final Map<String, Integer> depotValues = MoneyDepot.deserializeValues(",", " ", aMessage.getSaveable(0), money.getCurrency());
				final String depotDepotName = aMessage.getSaveable(1);
				money.getDefaultDepot().remove(depotValues);
				money.getDepot(depotDepotName).add(depotValues);
				sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.accept_depot, aMessage.getArguments(), mContext, null,
						ButtonAction.NOTHING));
				break;
			case ACCEPT_ACTION :
				chooseDifficulty(aMessage.getArguments());
				break;
			case DENY_ACTION :
				sendMessage(new Message(MessageGroup.ACTION, true, "", R.string.deny_use, new String[] { aMessage.getArgument(0) },
						new boolean[] { true }, mContext, null, ButtonAction.NOTHING));
				break;
			case DENY_DEPOT :
				sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.deny_take_depot, new String[] { aMessage.getArgument(1) }, mContext,
						null, ButtonAction.NOTHING, aMessage.getSaveables()));
				break;
			case ACCEPT_INCREASE :
				mChar.findItemInstance(aMessage.getSaveable(0)).increase(false, true);
				sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.accept_increase, aMessage.getArguments(),
						new boolean[] { true, false }, mContext, null, ButtonAction.NOTHING));
				break;
			case DENY_INCREASE :
				sendMessage(new Message(MessageGroup.SINGLE, false, "", R.string.deny_increase, new String[] { aMessage.getArgument(0) },
						new boolean[] { true }, mContext, null, ButtonAction.NOTHING));
				break;
			default :
				break;
		}
		// MARK Message buttons
		
		if (release)
		{
			mHost.releaseMessage(aMessage);
		}
		return release;
	}
	
	private void chooseDifficulty(final String[] aArguments)
	{
		final DifficultyChooseListener listener = new DifficultyChooseListener()
		{
			@Override
			public void difficultyChosen(final int aDifficulty)
			{
				final String[] args = Arrays.copyOf(aArguments, aArguments.length + 1);
				args[args.length - 1] = "" + aDifficulty;
				sendMessage(new Message(MessageGroup.ACTION, true, "", R.string.accept_use, aArguments, new boolean[] { true, false, false },
						mContext, null, ButtonAction.ACCEPT_ACTION, args));
			}
			
			@Override
			public void cancel()
			{
				sendMessage(new Message(MessageGroup.ACTION, true, "", R.string.deny_use, new String[] { aArguments[0] }, new boolean[] { true },
						mContext, null, ButtonAction.NOTHING));
			}
		};
		ChooseDifficultyDialog.showChooseDifficultyDialog(
				DataUtil.buildMessage(R.string.choose_difficulty, new String[] { LanguageUtil.instance().getValue(aArguments[0]) }, mContext),
				mContext, listener);
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
	public CharacterInstance getCharacter()
	{
		return mChar;
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
		return mChar.getName();
	}
	
	/**
	 * @return the players phone number.
	 */
	public String getNumber()
	{
		return mNumber;
	}
	
	/**
	 * @return the checkbox used for enabling the time management.
	 */
	public CheckBox getPlayerCheckBox()
	{
		return mTimeCheckBox;
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
	public void release()
	{
		ViewUtil.release(mContainer);
		ViewUtil.release(mTimeCheckBox);
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
	public void sendChange(final CharacterChange aChange)
	{
		mDevice.send(MessageType.UPDATE, DataUtil.serialize(aChange), aChange.getType());
	}
	
	@Override
	public void sendMessage(final Message aMessage)
	{
		mDevice.send(MessageType.MESSAGE, DataUtil.serialize(aMessage));
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
	
	@Override
	public void showMessage(final Message aMessage)
	{
		mHost.addMessage(aMessage);
	}
	
	@Override
	public void time(final Type aType, final int aAmount)
	{
		if (mTimeEnabled)
		{
			if (aType != Type.SET)
			{
				mChar.time(aType, aAmount);
				mDevice.send(MessageType.TIME, aType.name(), "" + aAmount);
			}
			else
			{
				final int difference = (aAmount + 24 - mTime) % 24;
				mChar.time(Type.HOUR, difference);
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
	
	@Override
	public void updateMessages()
	{
		// Do nothing
	}
	
	/**
	 * Updates the checkbox and displays the current player time behind it.
	 */
	public void updateTime()
	{
		mTimeCheckBox.setText(getName() + " - " + mTime + ":00");
	}
	
	@Override
	public void updateUI()
	{}
	
	private void init()
	{
		mExpander.init();
		
		mChar.updateUI();
		
		final LinearLayout playerContainer = mExpander.getContainer();
		final Button kick = (Button) mContainer.findViewById(R.id.view_kick_button);
		final Button ban = (Button) mContainer.findViewById(R.id.view_ban_button);
		
		int i = 0;
		playerContainer.addView(mChar.getMode().getContainer(), i++ );
		playerContainer.addView(mChar.getHealth().getContainer(), i++ );
		playerContainer.addView(mChar.getEPController().getContainer(), i++ );
		playerContainer.addView(mChar.getGenerationController().getContainer(), i++ );
		playerContainer.addView(mChar.getMoney().getContainer(), i++ );
		playerContainer.addView(mChar.getInventory().getContainer(), i++ );
		playerContainer.addView(mChar.getInsanities().getContainer(), i++ );
		playerContainer.addView(mChar.getRestrictions().getContainer(), i++ );
		
		mControllerExpander.init();
		
		final LinearLayout controllerPanel = mControllerExpander.getContainer();
		for (final ItemControllerInstance controller : mChar.getControllers())
		{
			if ( !controller.isEmpty() || controller.hasMutableGroup())
			{
				controller.close();
				controllerPanel.addView(controller.getContainer());
			}
		}
		
		mTimeCheckBox = (CheckBox) View.inflate(mContext, R.layout.view_time_checkbox, null);
		mTimeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(final CompoundButton aButtonView, final boolean aIsChecked)
			{
				mTimeEnabled = aIsChecked;
			}
		});
		updateTime();
		
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
}