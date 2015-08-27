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
		ITEM
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
		INFO,
		
		/**
		 * Asks the host or the player to approve something.
		 */
		YES_NO
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
	
	private final String[] mSaveables;
	
	private final MessageListener mListener;
	
	private final Context mContext;
	
	private final LinearLayout mContainer;
	
	private final ButtonAction mYesAction;
	
	private final ButtonAction mNoAction;
	
	/**
	 * Creates a yes/no message.
	 * 
	 * @param aGroup
	 *            The message group.
	 * @param aModeDepending
	 *            Whether this message is mode depending.
	 * @param aSender
	 *            The message sender.
	 * @param aMessageId
	 *            The message text.
	 * @param aArguments
	 *            The message arguments.
	 * @param aTranslated
	 *            The arguments, that should be translated.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The message listener.
	 * @param aYesAction
	 *            The yes action.
	 * @param aNoAction
	 *            The no action.
	 * @param aSaveables
	 *            Saveable objects.
	 */
	public Message(final MessageGroup aGroup, final boolean aModeDepending, final String aSender, final int aMessageId, final String[] aArguments,
			final boolean[] aTranslated, final Context aContext, final MessageListener aListener, final ButtonAction aYesAction,
			final ButtonAction aNoAction, final String... aSaveables)
	{
		this(MessageType.YES_NO, aGroup, aModeDepending, aSender, aMessageId, aArguments, aTranslated, aContext, aListener, R.layout.message_yes_no,
				aYesAction, aNoAction, aSaveables);
	}
	
	/**
	 * Creates a info message.
	 * 
	 * @param aGroup
	 *            The message group.
	 * @param aModeDepending
	 *            Whether this message is mode depending.
	 * @param aSender
	 *            The message sender.
	 * @param aMessageId
	 *            The message text.
	 * @param aArguments
	 *            The message arguments.
	 * @param aTranslated
	 *            The arguments, that should be translated.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The message listener.
	 * @param aOkAction
	 *            When the message is approved, this action happens.
	 * @param aSaveables
	 *            Saveable objects.
	 */
	public Message(final MessageGroup aGroup, final boolean aModeDepending, final String aSender, final int aMessageId, final String[] aArguments,
			final boolean[] aTranslated, final Context aContext, final MessageListener aListener, final ButtonAction aOkAction,
			final String... aSaveables)
	{
		this(MessageType.INFO, aGroup, aModeDepending, aSender, aMessageId, aArguments, aTranslated, aContext, aListener, R.layout.message_info,
				aOkAction, ButtonAction.NOTHING, aSaveables);
	}
	
	/**
	 * Creates a yes/no message.
	 * 
	 * @param aGroup
	 *            The message group.
	 * @param aModeDepending
	 *            Whether this message is mode depending.
	 * @param aSender
	 *            The message sender.
	 * @param aMessageId
	 *            The message text.
	 * @param aArguments
	 *            The message arguments.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The message listener.
	 * @param aYesAction
	 *            The yes action.
	 * @param aNoAction
	 *            The no action.
	 * @param aSaveables
	 *            Saveable objects.
	 */
	public Message(final MessageGroup aGroup, final boolean aModeDepending, final String aSender, final int aMessageId, final String[] aArguments,
			final Context aContext, final MessageListener aListener, final ButtonAction aYesAction, final ButtonAction aNoAction,
			final String... aSaveables)
	{
		this(MessageType.YES_NO, aGroup, aModeDepending, aSender, aMessageId, aArguments, new boolean[aArguments.length], aContext, aListener,
				R.layout.message_yes_no, aYesAction, aNoAction, aSaveables);
	}
	
	/**
	 * Creates a info message.
	 * 
	 * @param aGroup
	 *            The message group.
	 * @param aModeDepending
	 *            Whether this message is mode depending.
	 * @param aSender
	 *            The message sender.
	 * @param aMessageId
	 *            The message text.
	 * @param aArguments
	 *            The message arguments.
	 * @param aContext
	 *            The underlying context.
	 * @param aListener
	 *            The message listener.
	 * @param aOkAction
	 *            When the message is approved, this action happens.
	 * @param aSaveables
	 *            Saveable objects.
	 */
	public Message(final MessageGroup aGroup, final boolean aModeDepending, final String aSender, final int aMessageId, final String[] aArguments,
			final Context aContext, final MessageListener aListener, final ButtonAction aOkAction, final String... aSaveables)
	{
		this(MessageType.INFO, aGroup, aModeDepending, aSender, aMessageId, aArguments, new boolean[aArguments.length], aContext, aListener,
				R.layout.message_info, aOkAction, ButtonAction.NOTHING, aSaveables);
	}
	
	private Message(final MessageType aType, final MessageGroup aGroup, final boolean aModeDepending, final String aSender, final int aMessageId,
			final String[] aArguments, final boolean[] aTranslated, final Context aContext, final MessageListener aListener, final int aViewId,
			final ButtonAction aYesAction, final ButtonAction aNoAction, final String... aSaveables)
	{
		mType = aType;
		mGroup = aGroup;
		mSender = aSender;
		mMessageId = aMessageId;
		mModeDepending = aModeDepending;
		mArguments = aArguments;
		mTranslatedArguments = aTranslated;
		mListener = aListener;
		mContext = aContext;
		mYesAction = aYesAction;
		mNoAction = aNoAction;
		mSaveables = aSaveables;
		mContainer = (LinearLayout) View.inflate(mContext, aViewId, null);
		
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
			return DataUtil.buildMessage(mMessageId, LanguageUtil.instance().translateArray(mArguments, mTranslatedArguments), mContext);
		}
		return mSender + ": " + DataUtil.buildMessage(mMessageId, LanguageUtil.instance().translateArray(mArguments, mTranslatedArguments), mContext);
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
		final MessageType type = MessageType.valueOf(messageElement.getAttribute("type"));
		final MessageGroup group = MessageGroup.valueOf(messageElement.getAttribute("group"));
		final int messageId = Integer.parseInt(messageElement.getAttribute("message"));
		String[] arguments = new String[0];
		if (messageElement.hasAttribute("arguments"))
		{
			arguments = DataUtil.parseArray(messageElement.getAttribute("arguments"));
		}
		final String sender = messageElement.getAttribute("sender");
		final ButtonAction yesAction = ButtonAction.valueOf(messageElement.getAttribute("yes-action"));
		final ButtonAction noAction = ButtonAction.valueOf(messageElement.getAttribute("no-action"));
		final boolean modeDepending = Boolean.valueOf(messageElement.getAttribute("mode-depending"));
		final String[] saveables = DataUtil.parseArray(messageElement.getAttribute("saveables"));
		final boolean[] translated = DataUtil.parseFlags(messageElement.getAttribute("translated"));
		switch (type)
		{
			case INFO :
				return new Message(group, modeDepending, sender, messageId, arguments, translated, aContext, aListener, yesAction, saveables);
			case YES_NO :
				return new Message(group, modeDepending, sender, messageId, arguments, translated, aContext, aListener, yesAction, noAction,
						saveables);
			default :
				return null;
		}
	}
}
