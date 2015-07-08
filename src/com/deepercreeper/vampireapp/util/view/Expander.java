package com.deepercreeper.vampireapp.util.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * An expander handles the expandable container view.
 * 
 * @author Vincent
 */
public class Expander implements ResizeListener
{
	private Button					mButton;
	
	private LinearLayout			mContainer;
	
	private final ResizeListener	mParent;
	
	private final Context			mContext;
	
	private final int				mButtonId;
	
	private final int				mContainerId;
	
	private boolean					mOpen;
	
	private Expander(int aButtonId, int aContainerId, Context aContext, ResizeListener aParent)
	{
		mButtonId = aButtonId;
		mContainerId = aContainerId;
		mParent = aParent;
		mContext = aContext;
	}
	
	private Expander(int aButtonId, int aContainerId, Context aContext)
	{
		this(aButtonId, aContainerId, aContext, null);
	}
	
	/**
	 * Initializes the views by inflating them.
	 */
	public void init()
	{
		mButton = (Button) View.inflate(mContext, mButtonId, null);
		mContainer = (LinearLayout) View.inflate(mContext, mContainerId, null);
		
		mButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				toggleOpen();
			}
		});
		
		close();
	}
	
	/**
	 * Closes the expander.
	 */
	public void close()
	{
		mOpen = false;
		mButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		resize();
	}
	
	@Override
	public void resize()
	{
		ViewUtil.resize(mParent, mOpen, mContainer);
	}
	
	/**
	 * @return the currently set expand button.
	 */
	public Button getButton()
	{
		return mButton;
	}
	
	/**
	 * @return the currently set expandable panel.
	 */
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * Toggles whether the expander is open.
	 */
	public void toggleOpen()
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
	
	/**
	 * @return whether this expander is open.
	 */
	public boolean isOpen()
	{
		return mOpen;
	}
	
	/**
	 * Creates a new expander that handles the given arguments.
	 * 
	 * @param aButtonId
	 *            The expand button id.
	 * @param aContainerId
	 *            The expandable container id.
	 * @param aContext
	 *            The underlying context.
	 * @param aParent
	 *            The resize parent.
	 * @return the created expander.
	 */
	public static Expander handle(int aButtonId, int aContainerId, Context aContext, ResizeListener aParent)
	{
		return new Expander(aButtonId, aContainerId, aContext, aParent);
	}
	
	/**
	 * Creates a new expander that handles the given arguments.
	 * 
	 * @param aButtonId
	 *            The expand button id.
	 * @param aContainerId
	 *            The expandable container id.
	 * @param aContext
	 *            The underlying context.
	 * @return the created expander.
	 */
	public static Expander handle(int aButtonId, int aContainerId, Context aContext)
	{
		return new Expander(aButtonId, aContainerId, aContext);
	}
}
