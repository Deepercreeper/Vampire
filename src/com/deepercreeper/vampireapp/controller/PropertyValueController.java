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
 * A controller for property value groups.
 * 
 * @author Vincent
 */
public class PropertyValueController implements ValueController<PropertyItem>, VariableValueGroup<PropertyItem, PropertyItemValue>
{
	private CreationMode					mMode;
	
	private final Context					mContext;
	
	private Button							mShowPanel;
	
	private boolean							mPropertiesOpen;
	
	private final PropertyController		mController;
	
	private final PropertyItemValueGroup	mProperties;
	
	/**
	 * Creates a new property value controller.
	 * 
	 * @param aController
	 *            The controller type.
	 * @param aContext
	 *            The context.
	 * @param aMode
	 *            Whether this controller is inside the creation mode.
	 */
	public PropertyValueController(final PropertyController aController, final Context aContext, final CreationMode aMode)
	{
		mMode = aMode;
		mController = aController;
		mContext = aContext;
		mProperties = new PropertyItemValueGroup(mController.getProperties(), this, mContext, mMode);
	}
	
	@Override
	public void release()
	{
		mProperties.release();
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
		if ( !mPropertiesOpen)
		{
			mProperties.resize();
		}
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
	public CreationMode getCreationMode()
	{
		return mMode;
	}
	
	@Override
	public void setCreationMode(final CreationMode aMode)
	{
		mMode = aMode;
		mProperties.setCreationMode(mMode);
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
		
		mPropertiesOpen = false;
		
		mShowPanel = new Button(context);
		final LinearLayout properties = new LinearLayout(context);
		properties.setLayoutParams(ViewUtil.instance().getZeroHeight());
		
		mShowPanel.setLayoutParams(ViewUtil.instance().getWrapHeight());
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
		switch (mMode)
		{
			case CREATION :
				mProperties.updateValues(true, true);
				break;
			case FREE_POINTS :
				mProperties.updateValues(false, false);
				break;
			case NORMAL :
				mProperties.updateValues(false, false);
				break;
		}
	}
}
