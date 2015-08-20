package com.deepercreeper.vampireapp.character.instance.controllers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.Mode;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.host.change.ModeChange;
import com.deepercreeper.vampireapp.items.interfaces.Nameable;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * This controller handles the changes and settings of the character mode.
 * 
 * @author Vincent
 */
public class ModeControllerInstance implements Viewable, Saveable
{
	private final CharacterInstance mChar;
	
	private final LinearLayout mContainer;
	
	private final boolean mHost;
	
	private final Context mContext;
	
	private final ArrayAdapter<Nameable> mAdapter;
	
	private final MessageListener mMessageListener;
	
	private final Spinner mSpinner;
	
	private Mode mMode;
	
	/**
	 * Creates a new mode controller.
	 * 
	 * @param aChar
	 *            The parent character.
	 * @param aContext
	 *            The underlying context.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 */
	public ModeControllerInstance(final CharacterInstance aChar, final Context aContext, final MessageListener aMessageListener, final boolean aHost)
	{
		mMode = Mode.DEFAULT;
		mChar = aChar;
		mHost = aHost;
		mContext = aContext;
		mMessageListener = aMessageListener;
		final int id = mHost ? R.layout.host_mode : R.layout.client_mode;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		mAdapter = new ArrayAdapter<Nameable>(mContext, android.R.layout.simple_spinner_dropdown_item, Mode.getModesList(mMode, mContext, mHost));
		
		mSpinner = (Spinner) getContainer().findViewById(mHost ? R.id.h_mode_spinner : R.id.c_mode_spinner);
		mSpinner.setAdapter(mAdapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				setMode(Mode.getModeOf(mAdapter.getItem(aPosition)), false);
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{}
		});
	}
	
	/**
	 * Creates a new mode controller out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 * @param aChar
	 *            The parent character.
	 * @param aContext
	 *            The underlying context.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 */
	public ModeControllerInstance(final Element aElement, final CharacterInstance aChar, final Context aContext,
			final MessageListener aMessageListener, final boolean aHost)
	{
		mMode = Mode.valueOf(aElement.getAttribute("mode"));
		mChar = aChar;
		mHost = aHost;
		mContext = aContext;
		mMessageListener = aMessageListener;
		final int id = mHost ? R.layout.host_mode : R.layout.client_mode;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		mAdapter = new ArrayAdapter<Nameable>(mContext, android.R.layout.simple_spinner_dropdown_item, Mode.getModesList(mMode, mContext, mHost));
		
		mSpinner = (Spinner) getContainer().findViewById(mHost ? R.id.h_mode_spinner : R.id.c_mode_spinner);
		mSpinner.setAdapter(mAdapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(final AdapterView<?> aParent, final View aView, final int aPosition, final long aId)
			{
				setMode(Mode.getModeOf(mAdapter.getItem(aPosition)), false);
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> aParent)
			{}
		});
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("mode");
		element.setAttribute("mode", mMode.name());
		return element;
	}
	
	@Override
	public View getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the current character mode.
	 */
	public Mode getMode()
	{
		return mMode;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	/**
	 * Sets the current mode.
	 * 
	 * @param aMode
	 *            The new mode.
	 * @param aSilent
	 *            Whether a change should be sent.
	 */
	public void setMode(final Mode aMode, final boolean aSilent)
	{
		mMode = aMode;
		mAdapter.clear();
		mAdapter.addAll(Mode.getModesList(mMode, mContext, mHost));
		mSpinner.setSelection(mAdapter.getPosition(Mode.getNameableOf(aMode, mContext)));
		mChar.updateUI();
		if ( !aSilent)
		{
			mMessageListener.sendChange(new ModeChange(getMode()));
		}
	}
	
	@Override
	public void updateUI()
	{}
}
