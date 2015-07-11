package com.deepercreeper.vampireapp.util.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * An expander handles the expandable container view.
 * 
 * @author Vincent
 */
public class Expander implements ResizeListener
{
	private static final String		TAG				= "Expander";
	
	private final ResizeListener	mResizeParent;
	
	private final View				mParent;
	
	private final int				mButtonId;
	
	private final int				mContainerId;
	
	private Button					mButton;
	
	private LinearLayout			mContainer;
	
	private boolean					mInitialized	= false;
	
	private boolean					mOpen;
	
	private Expander(final int aButtonId, final int aContainerId, final View aParent, final ResizeListener aResizeParent)
	{
		mButtonId = aButtonId;
		mContainerId = aContainerId;
		mResizeParent = aResizeParent;
		mParent = aParent;
	}
	
	private Expander(final int aButtonId, final int aContainerId, final View aContainer)
	{
		this(aButtonId, aContainerId, aContainer, null);
	}
	
	/**
	 * @return the parent expander if existing. <code>null</code> otherwise.
	 */
	public Expander getParent()
	{
		if (mResizeParent instanceof Expander)
		{
			return (Expander) mResizeParent;
		}
		return null;
	}
	
	/**
	 * Initializes the views by inflating them.
	 */
	public void init()
	{
		mButton = (Button) mParent.findViewById(mButtonId);
		mContainer = (LinearLayout) mParent.findViewById(mContainerId);
		
		mButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				toggleOpen();
			}
		});
		
		mInitialized = true;
		
		close();
	}
	
	/**
	 * Closes the expander.
	 */
	public void close()
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "Expander not initialized!");
			return;
		}
		mOpen = false;
		mButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		resize();
	}
	
	@Override
	public void resize()
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "Expander not initialized!");
			return;
		}
		ViewUtil.resizeRecursive(this);
	}
	
	/**
	 * @return the currently set expand button.
	 */
	public Button getButton()
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "Expander not initialized!");
			return null;
		}
		return mButton;
	}
	
	/**
	 * @return the currently set expandable panel.
	 */
	public LinearLayout getContainer()
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "Expander not initialized!");
			return null;
		}
		return mContainer;
	}
	
	/**
	 * Toggles whether the expander is open.
	 */
	public void toggleOpen()
	{
		if ( !mInitialized)
		{
			Log.w(TAG, "Expander not initialized!");
			return;
		}
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
	 * @return whether this expander has been initialized yet.
	 */
	public boolean isInitialized()
	{
		return mInitialized;
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
	 * @param aParent
	 *            The parent container.
	 * @param aResizeParent
	 *            The resize parent.
	 * @return the created expander.
	 */
	public static Expander handle(final int aButtonId, final int aContainerId, final View aParent, final ResizeListener aResizeParent)
	{
		return new Expander(aButtonId, aContainerId, aParent, aResizeParent);
	}
	
	/**
	 * Creates a new expander that handles the given arguments.
	 * 
	 * @param aButtonId
	 *            The expand button id.
	 * @param aContainerId
	 *            The expandable container id.
	 * @param aParent
	 *            The parent container.
	 * @return the created expander.
	 */
	public static Expander handle(final int aButtonId, final int aContainerId, final View aParent)
	{
		return new Expander(aButtonId, aContainerId, aParent);
	}
}
