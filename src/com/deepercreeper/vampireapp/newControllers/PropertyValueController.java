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

public class PropertyValueController implements ValueController
{
	private boolean							mCreation;
	
	private final PropertyController		mController;
	
	private final PropertyItemValueGroup	mProperties;
	
	public PropertyValueController(final PropertyController aController, final boolean aCreation)
	{
		mCreation = aCreation;
		mController = aController;
		mProperties = new PropertyItemValueGroup(mController.getProperties(), mCreation);
	}
	
	@Override
	public void setCreation(final boolean aCreation)
	{
		mCreation = aCreation;
		mProperties.setCreation(mCreation);
	}
	
	@Override
	public boolean isCreation()
	{
		return mCreation;
	}
	
	@Override
	public PropertyController getController()
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
		
		final Button showProperties = new Button(context);
		final LinearLayout properties = new LinearLayout(context);
		properties.setLayoutParams(zeroHeight);
		
		showProperties.setLayoutParams(wrapHeight);
		showProperties.setText(R.string.properties);
		showProperties.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		showProperties.setOnClickListener(new OnClickListener()
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
						mProperties.initLayout(properties);
					}
					properties.startAnimation(new ResizeAnimation(properties, properties.getWidth(), ViewUtil.calcHeight(properties)));
					showProperties.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					properties.startAnimation(new ResizeAnimation(properties, properties.getWidth(), 0));
					showProperties.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
			}
		});
		
		aLayout.addView(showProperties);
		aLayout.addView(properties);
	}
}
