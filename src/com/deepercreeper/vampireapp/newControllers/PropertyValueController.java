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

public class PropertyValueController implements ValueController<PropertyItem>, VariableValueGroup<PropertyItem, PropertyItemValue>
{
	private boolean							mCreation;
	
	private final Context					mContext;
	
	private Button							mShowPanel;
	
	private boolean							mPropertiesOpen	= false;
	
	private final PropertyController		mController;
	
	private final PropertyItemValueGroup	mProperties;
	
	public PropertyValueController(final PropertyController aController, final Context aContext, final boolean aCreation)
	{
		mCreation = aCreation;
		mController = aController;
		mContext = aContext;
		mProperties = new PropertyItemValueGroup(mController.getProperties(), this, mContext, mCreation);
	}
	
	@Override
	public void addItem(final PropertyItem aItem)
	{
		mProperties.addItem(aItem);
		resize();
	}
	
	@Override
	public void resize()
	{
		mProperties.resize();
	}
	
	@Override
	public void close()
	{
		if (mPropertiesOpen)
		{
			mShowPanel.callOnClick();
		}
	}
	
	@Override
	public void clear()
	{
		mProperties.clear();
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
		
		mShowPanel = new Button(context);
		final LinearLayout properties = new LinearLayout(context);
		properties.setLayoutParams(zeroHeight);
		
		mShowPanel.setLayoutParams(wrapHeight);
		mShowPanel.setText(R.string.properties);
		mShowPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		mShowPanel.setOnClickListener(new OnClickListener()
		{
			private boolean	mInitialized	= false;
			
			@Override
			public void onClick(final View aArg0)
			{
				mPropertiesOpen = !mPropertiesOpen;
				if (mPropertiesOpen)
				{
					if ( !mInitialized)
					{
						mProperties.initLayout(properties);
						mInitialized = true;
					}
					properties.startAnimation(new ResizeAnimation(properties, properties.getWidth(), ViewUtil.calcHeight(properties)));
					mShowPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					properties.startAnimation(new ResizeAnimation(properties, properties.getWidth(), 0));
					mShowPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
				}
			}
		});
		
		aLayout.addView(mShowPanel);
		aLayout.addView(properties);
	}
	
	@Override
	public void updateValues()
	{
		mProperties.updateValues(true, true);
	}
}
