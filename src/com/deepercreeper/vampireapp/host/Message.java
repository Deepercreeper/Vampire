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
	 * All types of host client messages.
	 * 
	 * @author Vincent
	 */
	public static enum MessageType
	{
		/**
		 * One of them is informed about something.
		 */
		INFO
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
		switch (type)
		{
			case INFO :
				return new Message(sender, messageId, arguments, aContext, aListener);
			default :
				return null;
		}
	}
	
	private static final String		TAG_NAME	= "message";
	
	private final MessageType		mType;
	
	private final int				mMessageId;
	
	private final String			mSender;
	
	private final String[]			mArguments;
	
	private final MessageListener	mListener;
	
	private final Context			mContext;
	
	private final LinearLayout		mContainer;
	
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
	 */
	public Message(final String aSender, final int aMessageId, final String[] aArguments, final Context aContext, final MessageListener aListener)
	{
		this(MessageType.INFO, aSender, aMessageId, aArguments, aContext, aListener, R.layout.message_info);
		init();
	}
	
	private Message(final MessageType aType, final String aSender, final int aMessageId, final String[] aArguments, final Context aContext,
			final MessageListener aListener, final int aViewId)
	{
		mType = aType;
		mSender = aSender;
		mMessageId = aMessageId;
		mArguments = aArguments;
		mListener = aListener;
		mContext = aContext;
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
		switch (mType)
		{
			case INFO :
				break;
			default :
				break;
		}
		return element;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
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
				initOkButton(R.id.m_info_ok_button);
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
	
	private void initOkButton(final int aButtonId)
	{
		final Button button = (Button) getContainer().findViewById(aButtonId);
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				release();
			}
		});
	}
}
