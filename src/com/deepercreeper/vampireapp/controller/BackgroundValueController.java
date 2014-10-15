package com.deepercreeper.vampireapp.controller;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.ResizeAnimation;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A controller for background value groups.
 * 
 * @author Vincent
 */
public class BackgroundValueController implements ValueController<BackgroundItem>, VariableValueGroup<BackgroundItem, BackgroundItemValue>
{
	private CreationMode					mMode;
	
	private final Context					mContext;
	
	private Button							mShowPanel;
	
	private boolean							mBackgroundsOpen	= false;
	
	private final BackgroundController		mController;
	
	private final BackgroundItemValueGroup	mBackgrounds;
	
	/**
	 * Creates a new background value controller.
	 * 
	 * @param aController
	 *            The controller type.
	 * @param aContext
	 *            The context.
	 * @param aMode
	 *            Whether this controller is in the creation mode.
	 */
	public BackgroundValueController(final BackgroundController aController, final Context aContext, final CreationMode aMode)
	{
		mMode = aMode;
		mController = aController;
		mContext = aContext;
		mBackgrounds = new BackgroundItemValueGroup(mController.getBackgrounds(), this, aContext, mMode);
	}
	
	@Override
	public void release()
	{
		mBackgrounds.release();
	}
	
	@Override
	public void addItem(final BackgroundItem aItem)
	{
		mBackgrounds.addItem(aItem);
		resize();
	}
	
	@Override
	public void resize()
	{
		mBackgrounds.resize();
	}
	
	@Override
	public void close()
	{
		if (mBackgroundsOpen)
		{
			mShowPanel.callOnClick();
		}
	}
	
	@Override
	public void clear()
	{
		mBackgrounds.clear();
		close();
	}
	
	@Override
	public void setEnabled(final boolean aEnabled)
	{
		if (mShowPanel != null)
		{
			mShowPanel.setEnabled(aEnabled);
		}
	}
	
	@Override
	public CreationMode getCreationMode()
	{
		return mMode;
	}
	
	@Override
	public void setCreationMode(final CreationMode aMode)
	{
		mMode = aMode;
		mBackgrounds.setCreationMode(mMode);
	}
	
	@Override
	public void updateValues()
	{
		switch (mMode)
		{
			case CREATION :
				mBackgrounds.updateValues(mBackgrounds.getValue() < mController.getMaxCreationValue(), true);
				break;
			case FREE_POINTS :
				mBackgrounds.updateValues(mBackgrounds.getValue() + mBackgrounds.getTempPoints() < mController.getMaxCreationValue(), true);
				break;
			case NORMAL :
				mBackgrounds.updateValues(true, false);
				break;
		}
	}
	
	@Override
	public BackgroundController getController()
	{
		return mController;
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{
		aLayout.removeAllViews();
		
		mShowPanel = new Button(mContext);
		final LinearLayout backgrounds = new LinearLayout(mContext);
		backgrounds.setLayoutParams(ViewUtil.instance().getZeroHeight());
		
		mShowPanel.setLayoutParams(ViewUtil.instance().getWrapHeight());
		mShowPanel.setText(R.string.backgrounds);
		mShowPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		mShowPanel.setOnClickListener(new OnClickListener()
		{
			private boolean	mInitialized	= false;
			
			@Override
			public void onClick(final View aArg0)
			{
				mBackgroundsOpen = !mBackgroundsOpen;
				if (mBackgroundsOpen)
				{
					if ( !mInitialized)
					{
						mBackgrounds.initLayout(backgrounds);
						mInitialized = true;
					}
					backgrounds.startAnimation(new ResizeAnimation(backgrounds, backgrounds.getWidth(), ViewUtil.calcHeight(backgrounds)));
					mShowPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					backgrounds.startAnimation(new ResizeAnimation(backgrounds, backgrounds.getWidth(), 0));
					mShowPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
				}
			}
		});
		
		aLayout.addView(mShowPanel);
		aLayout.addView(backgrounds);
	}
}
