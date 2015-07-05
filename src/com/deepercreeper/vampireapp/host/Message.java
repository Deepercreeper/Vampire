package com.deepercreeper.vampireapp.host;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Viewable;

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
		 * A host given item was taken
		 */
		TAKE_ITEM,
		
		/**
		 * A host given item was ignored
		 */
		IGNORE_ITEM
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
	
	private static final String		TAG_NAME	= "message";
	
	private final MessageType		mType;
	
	private final int				mMessageId;
	
	private final String			mSender;
	
	private final String[]			mArguments;
	
	private final String[]			mSaveables;
	
	private final MessageListener	mListener;
	
	private final Context			mContext;
	
	private final LinearLayout		mContainer;
	
	private final ButtonAction		mYesAction;
	
	private final ButtonAction		mNoAction;
	
	/**
	 * Creates a yes/no message.
	 * 
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
	public Message(final String aSender, final int aMessageId, final String[] aArguments, final Context aContext, final MessageListener aListener,
			final ButtonAction aYesAction, final ButtonAction aNoAction, final String... aSaveables)
	{
		this(MessageType.YES_NO, aSender, aMessageId, aArguments, aContext, aListener, R.layout.message_yes_no, aYesAction, aNoAction, aSaveables);
		init();
	}
	
	/**
	 * Creates a info message.
	 * 
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
	public Message(final String aSender, final int aMessageId, final String[] aArguments, final Context aContext, final MessageListener aListener,
			final ButtonAction aOkAction, final String... aSaveables)
	{
		this(MessageType.INFO, aSender, aMessageId, aArguments, aContext, aListener, R.layout.message_info, aOkAction, ButtonAction.NOTHING,
				aSaveables);
		init();
	}
	
	private Message(final MessageType aType, final String aSender, final int aMessageId, final String[] aArguments, final Context aContext,
			final MessageListener aListener, final int aViewId, final ButtonAction aYesAction, final ButtonAction aNoAction,
			final String... aSaveables)
	{
		mType = aType;
		mSender = aSender;
		mMessageId = aMessageId;
		mArguments = aArguments;
		mListener = aListener;
		mContext = aContext;
		mYesAction = aYesAction;
		mNoAction = aNoAction;
		mSaveables = aSaveables;
		mContainer = (LinearLayout) View.inflate(mContext, aViewId, null);
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("type", mType.name());
		element.setAttribute("message", "" + mMessageId);
		element.setAttribute("arguments", DataUtil.parseArray(mArguments));
		element.setAttribute("sender", mSender);
		element.setAttribute("yes-action", mYesAction.name());
		element.setAttribute("no-action", mNoAction.name());
		element.setAttribute("saveables", DataUtil.parseArray(mSaveables));
		return element;
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
	 * @return a list of all stored saveables.
	 */
	public String[] getSaveables()
	{
		return mSaveables;
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
	 * @param aIndex
	 *            The index.
	 * @return the serialized saveable at the given index.
	 */
	public String getSaveable(final int aIndex)
	{
		return mSaveables[aIndex];
	}
	
	/**
	 * @return the message type.
	 */
	public MessageType getType()
	{
		return mType;
	}
	
	@Override
	public void init()
	{
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
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	private String getText()
	{
		if (mSender.isEmpty())
		{
			return FilesUtil.buildMessage(mMessageId, mArguments, mContext);
		}
		return mSender + ": " + FilesUtil.buildMessage(mMessageId, mArguments, mContext);
	}
	
	private void initButton(final int aButtonId, final ButtonAction aAction)
	{
		final Button button = (Button) getContainer().findViewById(aButtonId);
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
		final Element messageElement = (Element) FilesUtil.loadDocument(aXML).getElementsByTagName(TAG_NAME).item(0);
		final MessageType type = MessageType.valueOf(messageElement.getAttribute("type"));
		final int messageId = Integer.parseInt(messageElement.getAttribute("message"));
		final String[] arguments = DataUtil.parseArray(messageElement.getAttribute("arguments"));
		final String sender = messageElement.getAttribute("sender");
		final ButtonAction yesAction = ButtonAction.valueOf(messageElement.getAttribute("yes-action"));
		final ButtonAction noAction = ButtonAction.valueOf(messageElement.getAttribute("no-action"));
		final String[] saveables = DataUtil.parseArray(messageElement.getAttribute("saveables"));
		switch (type)
		{
			case INFO :
				return new Message(sender, messageId, arguments, aContext, aListener, yesAction, saveables);
			case YES_NO :
				return new Message(sender, messageId, arguments, aContext, aListener, yesAction, noAction, saveables);
			default :
				return null;
		}
	}
}
