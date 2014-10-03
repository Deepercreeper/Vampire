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

public class BackgroundValueController implements ValueController<BackgroundItem>
{
	private boolean							mCreation;
	
	private final BackgroundController		mController;
	
	private final BackgroundItemValueGroup	mBackgrounds;
	
	public BackgroundValueController(final BackgroundController aController, final boolean aCreation)
	{
		mCreation = aCreation;
		mController = aController;
		mBackgrounds = new BackgroundItemValueGroup(mController.getBackgrounds(), this, mCreation);
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
		final Context context = aLayout.getContext();
		aLayout.removeAllViews();
		
		final LayoutParams wrapHeight = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams zeroHeight = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		
		aLayout.setLayoutParams(wrapHeight);
		
		final Button showBackgrounds = new Button(context);
		final LinearLayout backgrounds = new LinearLayout(context);
		backgrounds.setLayoutParams(zeroHeight);
		
		showBackgrounds.setLayoutParams(wrapHeight);
		showBackgrounds.setText(R.string.backgrounds);
		showBackgrounds.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		showBackgrounds.setOnClickListener(new OnClickListener()
		{
			private boolean			mOpen			= false;
			
			private final boolean	mInitialized	= false;
			
			@Override
			public void onClick(final View aArg0)
			{
				mOpen = !mOpen;
				if (mOpen)
				{
					if ( !mInitialized)
					{
						mBackgrounds.initLayout(backgrounds);
					}
					backgrounds.startAnimation(new ResizeAnimation(backgrounds, backgrounds.getWidth(), ViewUtil.calcHeight(backgrounds)));
					showBackgrounds.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					backgrounds.startAnimation(new ResizeAnimation(backgrounds, backgrounds.getWidth(), 0));
					showBackgrounds.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
			}
		});
		
		aLayout.addView(showBackgrounds);
		aLayout.addView(backgrounds);
	}
}
