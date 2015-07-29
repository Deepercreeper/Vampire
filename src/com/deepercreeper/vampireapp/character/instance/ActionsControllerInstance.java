package com.deepercreeper.vampireapp.character.instance;

import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * A controller that handles the usage of actions.
 * 
 * @author Vincent
 */
public class ActionsControllerInstance implements Viewable
{
	private final LinearLayout mContainer;
	
	private final Context mContext;
	
	private boolean mInitialized = false;
	
	/**
	 * Creates a new actions controller.
	 * 
	 * @param aContext
	 *            The underlying context.
	 */
	public ActionsControllerInstance(final Context aContext)
	{
		mContext = aContext;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.client_actions, null);
		
		init();
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			mInitialized = true;
		}
	}
}
