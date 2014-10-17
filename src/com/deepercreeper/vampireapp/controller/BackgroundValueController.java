package com.deepercreeper.vampireapp.controller;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.ResizeAnimation;
import com.deepercreeper.vampireapp.controller.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A controller for background value groups.
 * 
 * @author Vincent
 */
public class BackgroundValueController implements ValueController<BackgroundItem>, VariableValueGroup<BackgroundItem, BackgroundItemValue>
{
	private Mode					mMode;
	
	private final UpdateAction				mUpdateOthers;
	
	private PointHandler					mPoints;
	
	private final Context					mContext;
	
	private Button							mShowPanel;
	
	private boolean							mBackgroundsOpen;
	
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
	 * @param aPoints
	 *            The caller for free or experience points.
	 * @param aUpdateOthers
	 *            The update others action.
	 */
	public BackgroundValueController(final BackgroundController aController, final Context aContext, final Mode aMode,
			final PointHandler aPoints, final UpdateAction aUpdateOthers)
	{
		mMode = aMode;
		mPoints = aPoints;
		mUpdateOthers = aUpdateOthers;
		mController = aController;
		mContext = aContext;
		mBackgrounds = new BackgroundItemValueGroup(mController.getBackgrounds(), this, aContext, mMode, mPoints);
	}
	
	@Override
	public void setPoints(final com.deepercreeper.vampireapp.controller.ValueController.PointHandler aPoints)
	{
		mPoints = aPoints;
		mBackgrounds.setPoints(mPoints);
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
	public void resetTempPoints()
	{
		mBackgrounds.resetTempPoints();
	}
	
	@Override
	public void resize()
	{
		if ( !mBackgroundsOpen)
		{
			mBackgrounds.resize();
		}
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
	public Mode getCreationMode()
	{
		return mMode;
	}
	
	@Override
	public void setCreationMode(final Mode aMode)
	{
		mMode = aMode;
		mBackgrounds.setCreationMode(mMode);
	}
	
	@Override
	public void updateValues(final boolean aUpdateOthers)
	{
		switch (mMode)
		{
			case CREATION :
				mBackgrounds.updateValues(mBackgrounds.getValue() < mController.getMaxCreationValue(), true);
				break;
			case FREE_POINTS :
				mBackgrounds.updateValues(true, true);
				break;
			case NORMAL :
				mBackgrounds.updateValues(true, false);
				break;
		}
		if (aUpdateOthers)
		{
			mUpdateOthers.update();
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
		
		mBackgroundsOpen = false;
		
		mShowPanel = new Button(mContext);
		final LinearLayout backgrounds = new LinearLayout(mContext);
		backgrounds.setLayoutParams(ViewUtil.instance().getZeroHeight());
		
		mShowPanel.setLayoutParams(ViewUtil.instance().getWrapHeight());
		mShowPanel.setText(R.string.backgrounds);
		mShowPanel.setEnabled(mMode != Mode.FREE_POINTS || !mBackgrounds.getValuesList().isEmpty());
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
