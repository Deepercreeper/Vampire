package com.deepercreeper.vampireapp.host;

import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A message that can be sent between host and client and needs user appreciation.
 * 
 * @author Vincent
 */
public class Message implements Saveable, Viewable
{
	/**
	 * A builder for messages.
	 * 
	 * @author Vincent
	 */
	public static class Builder
	{
		private static final String TAG = "Builder";
		
		private final int mMessageId;
		
		private final Context mContext;
		
		private MessageType mType = MessageType.INFO;
		
		private MessageGroup mGroup = MessageGroup.SINGLE;
		
		private boolean mModeDepending = false;
		
		private boolean mAllArguments = true;
		
		private String mSender = "";
		
		private String[] mArguments = new String[0];
		
		private boolean[] mTranslated = new boolean[0];
		
		private ButtonAction mYesAction = ButtonAction.NOTHING;
		
		private ButtonAction mNoAction = ButtonAction.NOTHING;
		
		private String[] mSaveables = new String[0];
		
		private MessageListener mMessageListener = null;
		
		/**
		 * Creates a new message builder.
		 * 
		 * @param aMessageId
		 *            The message id.
		 * @param aContext
		 *            The underlying context.
		 */
		public Builder(final int aMessageId, final Context aContext)
		{
			mMessageId = aMessageId;
			mContext = aContext;
		}
		
		/**
		 * @return a message that has the set attributes.
		 */
		public Message create()
		{
			return new Message(mType, mGroup, mModeDepending, mSender, mMessageId, mArguments, mTranslated, mAllArguments, mContext, mMessageListener,
					mYesAction, mNoAction, mSaveables);
		}
		
		/**
		 * Sets the message group of the message. The default value is {@link MessageGroup#SINGLE}.
		 * 
		 * @param aGroup
		 *            The message group.
		 * @return this.
		 */
		public Builder setGroup(final MessageGroup aGroup)
		{
			mGroup = aGroup;
			return this;
		}
		
		/**
		 * Sets whether all arguments should be added to the <code>{x}</code> item. The default value is {@code true}.
		 * 
		 * @param aAllArguments
		 *            The flag.
		 * @return this.
		 */
		public Builder setAllArguments(final boolean aAllArguments)
		{
			mAllArguments = aAllArguments;
			return this;
		}
		
		/**
		 * Sets whether the message is mode depending. The default value is {@code false}.
		 * 
		 * @param aModeDepending
		 *            Whether the message is mode depending.
		 * @return this.
		 */
		public Builder setModeDepending(final boolean aModeDepending)
		{
			mModeDepending = aModeDepending;
			return this;
		}
		
		/**
		 * Sets the message listener of the message. The default value is {@code null}.
		 * 
		 * @param aMessageListener
		 *            The message listener.
		 * @return this.
		 */
		public Builder setMessageListener(final MessageListener aMessageListener)
		{
			mMessageListener = aMessageListener;
			return this;
		}
		
		/**
		 * Sets the yes-action of the message. The default value is {@link ButtonAction#NOTHING}.
		 * 
		 * @param aYesAction
		 *            The yes-action.
		 * @return this.
		 */
		public Builder setYesAction(final ButtonAction aYesAction)
		{
			mYesAction = aYesAction;
			return this;
		}
		
		/**
		 * Sets the no-action of the message. The default value is {@link ButtonAction#NOTHING}.
		 * 
		 * @param aNoAction
		 *            The no-action.
		 * @return this.
		 */
		public Builder setNoAction(final ButtonAction aNoAction)
		{
			mNoAction = aNoAction;
			return this;
		}
		
		/**
		 * Sets the sender of the message. The default value is an empty String.
		 * 
		 * @param aSender
		 *            The sender.
		 * @return this.
		 */
		public Builder setSender(final String aSender)
		{
			mSender = aSender;
			return this;
		}
		
		/**
		 * Sets the arguments of the message. The default value is an empty String array.<br>
		 * If the translated arguments array has not the same length it is set to an boolean array filled with {@code false}.
		 * 
		 * @param aArguments
		 *            The arguments.
		 * @return this.
		 */
		public Builder setArguments(final String... aArguments)
		{
			mArguments = aArguments;
			if (mTranslated.length != mArguments.length)
			{
				mTranslated = new boolean[mArguments.length];
			}
			return this;
		}
		
		/**
		 * Sets the translated flags of the message. The default value is an empty boolean array.
		 * 
		 * @param aTranslated
		 *            The translated flags.
		 * @return this.
		 */
		public Builder setTranslated(final boolean... aTranslated)
		{
			mTranslated = aTranslated;
			if (mTranslated.length != mArguments.length)
			{
				Log.w(TAG, "Tried to set the translated array to another length.");
			}
			return this;
		}
		
		/**
		 * Sets the saveables of the message. The default value is an empty String array.
		 * 
		 * @param aSaveables
		 *            The saveables.
		 * @return this.
		 */
		public Builder setSaveables(final String... aSaveables)
		{
			mSaveables = aSaveables;
			return this;
		}
		
		/**
		 * Sets the type of the message. The default value is {@link MessageType#INFO}.
		 * 
		 * @param aType
		 *            The message type.
		 * @return this.
		 */
		public Builder setType(final MessageType aType)
		{
			mType = aType;
			return this;
		}
	}
	
	/**
	 * Each button has to have an action, that will be processed, when the button is clicked.
	 * 
	 * @author vrl
	 */
	public static enum ButtonAction
	{
		/**
		 * No operation
		 */
		NOTHING,
		
		/**
		 * Taking money from a depot was accepted
		 */
		ACCEPT_TAKE,
		
		/**
		 * Depot of money was accepted
		 */
		ACCEPT_DEPOT,
		
		/**
		 * Deletion of depot was accepted
		 */
		ACCEPT_DELETE,
		
		/**
		 * Increase of item was accepted
		 */
		ACCEPT_INCREASE,
		
		/**
		 * Using an action was accepted
		 */
		ACCEPT_ACTION,
		
		/**
		 * Taking money from a depot was denied
		 */
		DENY_TAKE,
		
		/**
		 * Depot of money was denied
		 */
		DENY_DEPOT,
		
		/**
		 * Deletion of depot was denied
		 */
		DENY_DELETE,
		
		/**
		 * Increase of item was denied
		 */
		DENY_INCREASE,
		
		/**
		 * Using an action was denied
		 */
		DENY_ACTION,
		
		/**
		 * A host given item was taken
		 */
		TAKE_ITEM,
		
		/**
		 * A host given item was ignored
		 */
		IGNORE_ITEM
	}
	
	/**
	 * Used for checking, whether multiple messages of one type can be displayed at once.
	 * 
	 * @author Vincent
	 */
	public static enum MessageGroup
	{
		/**
		 * These messages are not equal
		 */
		SINGLE,
		
		/**
		 * All money messages
		 */
		MONEY,
		
		/**
		 * All inventory messages
		 */
		INVENTORY,
		
		/**
		 * All item messages
		 */
		ITEM,
		
		/**
		 * All action messages
		 */
		ACTION
	}
	
	/**
	 * All types of host client messages.
	 * 
	 * @author Vincent
	 */
	public static enum MessageType
	{
		/**
		 * One of them is informed about something.
		 */
		INFO(R.layout.message_info),
		
		/**
		 * Asks the host or the player to approve something.
		 */
		YES_NO(R.layout.message_yes_no);
		
		private final int mViewId;
		
		private MessageType(final int aViewId)
		{
			mViewId = aViewId;
		}
		
		/**
		 * @return the view id of this message type.
		 */
		public int getViewId()
		{
			return mViewId;
		}
	}
	
	private static final String TAG_NAME = "message";
	
	private final Set<Button> mButtons = new HashSet<Button>();
	
	private final MessageType mType;
	
	private final MessageGroup mGroup;
	
	private final boolean mModeDepending;
	
	private final int mMessageId;
	
	private final String mSender;
	
	private final String[] mArguments;
	
	private final boolean[] mTranslatedArguments;
	
	private final boolean mAllArguments;
	
	private final String[] mSaveables;
	
	private final MessageListener mListener;
	
	private final Context mContext;
	
	private final LinearLayout mContainer;
	
	private final ButtonAction mYesAction;
	
	private final ButtonAction mNoAction;
	
	private Message(final MessageType aType, final MessageGroup aGroup, final boolean aModeDepending, final String aSender, final int aMessageId,
			final String[] aArguments, final boolean[] aTranslated, final boolean aAllArguments, final Context aContext,
			final MessageListener aListener, final ButtonAction aYesAction, final ButtonAction aNoAction, final String... aSaveables)
	{
		mType = aType;
		mGroup = aGroup;
		mSender = aSender;
		mMessageId = aMessageId;
		mModeDepending = aModeDepending;
		mArguments = aArguments;
		mTranslatedArguments = aTranslated;
		mAllArguments = aAllArguments;
		mListener = aListener;
		mContext = aContext;
		mYesAction = aYesAction;
		mNoAction = aNoAction;
		mSaveables = aSaveables;
		mContainer = (LinearLayout) View.inflate(mContext, mType.getViewId(), null);
		
		switch (mType)
		{
			case INFO :
				initMessageText(R.id.m_info_message_label);
				initButton(R.id.m_info_ok_button, mYesAction);
				break;
			case YES_NO :
				initMessageText(R.id.m_yes_no_message_label);
				initButton(R.id.m_yes_no_positive_button, mYesAction);
				initButton(R.id.m_yes_no_negative_button, mNoAction);
				break;
			default :
				break;
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("type", mType.name());
		element.setAttribute("group", mGroup.name());
		element.setAttribute("message", "" + mMessageId);
		if (mArguments.length > 0)
		{
			element.setAttribute("arguments", DataUtil.parseArray(mArguments));
		}
		element.setAttribute("allArguments", "" + mAllArguments);
		element.setAttribute("sender", mSender);
		element.setAttribute("mode-depending", "" + mModeDepending);
		element.setAttribute("yes-action", mYesAction.name());
		element.setAttribute("no-action", mNoAction.name());
		element.setAttribute("saveables", DataUtil.parseArray(mSaveables));
		element.setAttribute("translated", DataUtil.parseFlags(mTranslatedArguments));
		return element;
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (mGroup.equals(MessageGroup.SINGLE))
		{
			return false;
		}
		if (aO instanceof Message)
		{
			final Message message = (Message) aO;
			return mGroup.equals(message.mGroup);
		}
		return false;
	}
	
	/**
	 * @param aIndex
	 *            The index.
	 * @return the text argument at the given index.
	 */
	public String getArgument(final int aIndex)
	{
		return mArguments[aIndex];
	}
	
	/**
	 * @return the list of message arguments for the message text.
	 */
	public String[] getArguments()
	{
		return mArguments;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @param aIndex
	 *            The index.
	 * @return the serialized saveable at the given index.
	 */
	public String getSaveable(final int aIndex)
	{
		return mSaveables[aIndex];
	}
	
	/**
	 * @return a list of all stored saveables.
	 */
	public String[] getSaveables()
	{
		return mSaveables;
	}
	
	/**
	 * @return an array of booleans indicating, whether the argument at the position should be translated.
	 */
	public boolean[] getTranslatedArguments()
	{
		return mTranslatedArguments;
	}
	
	/**
	 * @return the message type.
	 */
	public MessageType getType()
	{
		return mType;
	}
	
	@Override
	public int hashCode()
	{
		if (mGroup.equals(MessageGroup.SINGLE))
		{
			return super.hashCode();
		}
		return mGroup.name().hashCode();
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Updates the buttons if mode depending.
	 * 
	 * @param aChar
	 *            The parent character.
	 */
	public void update(final CharacterInstance aChar)
	{
		if (mModeDepending)
		{
			for (final Button button : mButtons)
			{
				ViewUtil.setEnabled(button, aChar.getMode().getMode().canUseAction());
			}
		}
	}
	
	@Override
	public void updateUI()
	{}
	
	private String getText()
	{
		if (mSender.isEmpty())
		{
			return DataUtil.buildMessage(mMessageId, LanguageUtil.instance().translateArray(mArguments, mTranslatedArguments), mContext,
					mAllArguments);
		}
		return mSender + ": " + DataUtil.buildMessage(mMessageId, LanguageUtil.instance().translateArray(mArguments, mTranslatedArguments), mContext,
				mAllArguments);
	}
	
	private void initButton(final int aButtonId, final ButtonAction aAction)
	{
		final Button button = (Button) getContainer().findViewById(aButtonId);
		mButtons.add(button);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				if (mListener.applyMessage(Message.this, aAction))
				{
					release();
				}
			}
		});
	}
	
	private void initMessageText(final int aTextId)
	{
		final TextView message = (TextView) getContainer().findViewById(aTextId);
		message.setText(getText());
		message.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				mListener.makeText(getText(), Toast.LENGTH_LONG);
			}
		});
	}
	
	/**
	 * @param aXML
	 *            The data, the message is serialized to.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The message listener.
	 * @return the serialized message.
	 */
	public static Message deserialize(final String aXML, final Context aContext, final MessageListener aListener)
	{
		final Element messageElement = DataUtil.getElement(DataUtil.loadDocument(aXML), TAG_NAME);
		final Builder builder = new Builder(Integer.parseInt(messageElement.getAttribute("message")), aContext);
		builder.setType(MessageType.valueOf(messageElement.getAttribute("type")));
		builder.setGroup(MessageGroup.valueOf(messageElement.getAttribute("group")));
		if (messageElement.hasAttribute("arguments"))
		{
			builder.setArguments(DataUtil.parseArray(messageElement.getAttribute("arguments")));
		}
		if (messageElement.hasAttribute("translated"))
		{
			builder.setTranslated(DataUtil.parseFlags(messageElement.getAttribute("translated")));
		}
		builder.setAllArguments(Boolean.parseBoolean(messageElement.getAttribute("allArguments")));
		builder.setSender(messageElement.getAttribute("sender"));
		builder.setYesAction(ButtonAction.valueOf(messageElement.getAttribute("yes-action")));
		builder.setNoAction(ButtonAction.valueOf(messageElement.getAttribute("no-action")));
		builder.setModeDepending(Boolean.valueOf(messageElement.getAttribute("mode-depending")));
		builder.setSaveables(DataUtil.parseArray(messageElement.getAttribute("saveables")));
		builder.setMessageListener(aListener);
		return builder.create();
	}
}
