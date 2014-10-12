package com.deepercreeper.vampireapp.newControllers;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.ResizeAnimation;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class BackgroundValueController implements ValueController<BackgroundItem>, VariableValueGroup<BackgroundItem, BackgroundItemValue>
{
	private boolean							mCreation;
	
	private final Context					mContext;
	
	private Button							mShowPanel;
	
	private boolean							mBackgroundsOpen	= false;
	
	private final BackgroundController		mController;
	
	private final BackgroundItemValueGroup	mBackgrounds;
	
	public BackgroundValueController(final BackgroundController aController, final Context aContext, final boolean aCreation)
	{
		mCreation = aCreation;
		mController = aController;
		mContext = aContext;
		mBackgrounds = new BackgroundItemValueGroup(mController.getBackgrounds(), this, aContext, mCreation);
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
	public void setCreation(final boolean aCreation)
	{
		mCreation = aCreation;
		mBackgrounds.setCreation(mCreation);
	}
	
	@Override
	public void updateValues()
	{
		mBackgrounds.updateValues(mBackgrounds.getValue() < mController.getMaxCreationValue(), true);
	}
	
	@Override
	public boolean isCreation()
	{
		return mCreation;
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
		
		final LayoutParams wrapHeight = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams zeroHeight = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		
		mShowPanel = new Button(mContext);
		final LinearLayout backgrounds = new LinearLayout(mContext);
		backgrounds.setLayoutParams(zeroHeight);
		
		mShowPanel.setLayoutParams(wrapHeight);
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
