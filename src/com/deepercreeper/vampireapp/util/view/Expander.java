package com.deepercreeper.vampireapp.util.view;

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
	private final Button			mButton;
	
	private final LinearLayout		mContainer;
	
	private final ResizeListener	mParent;
	
	private boolean					mOpen;
	
	private Expander(final LinearLayout aContainer, final Button aButton, final ResizeListener aParent)
	{
		mContainer = aContainer;
		mButton = aButton;
		mParent = aParent;
		
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
	
	private Expander(final LinearLayout aContainer, final Button aButton)
	{
		this(aContainer, aButton, null);
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
	 * Creates a new expander that handles the given arguments.
	 * 
	 * @param aContainer
	 *            The expandable container.
	 * @param aButton
	 *            The expand button.
	 * @param aParent
	 *            The resize parent.
	 * @return the created expander.
	 */
	public static Expander handle(final LinearLayout aContainer, final Button aButton, final ResizeListener aParent)
	{
		return new Expander(aContainer, aButton, aParent);
	}
	
	/**
	 * Creates a new expander that handles the given arguments.
	 * 
	 * @param aContainer
	 *            The expandable container.
	 * @param aButton
	 *            The expand button.
	 * @return the created expander.
	 */
	public static Expander handle(final LinearLayout aContainer, final Button aButton)
	{
		return new Expander(aContainer, aButton);
	}
}
