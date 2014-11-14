package com.deepercreeper.vampireapp.controller.properties;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.implementations.VariableCreationValueControllerImpl;
import com.deepercreeper.vampireapp.controller.interfaces.ItemCreationValue;
import com.deepercreeper.vampireapp.controller.restrictions.Restriction;
import com.deepercreeper.vampireapp.creation.CharMode;
import com.deepercreeper.vampireapp.util.ResizeAnimation;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A controller for property value groups.
 * 
 * @author Vincent
 */
public class PropertyCreationValueController extends VariableCreationValueControllerImpl<PropertyItem, PropertyItemCreationValue>
{
	private Button							mShowPanel;
	
	private boolean							mPropertiesOpen;
	
	private final PropertyItemCreationValueGroup	mProperties;
	
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
	public PropertyCreationValueController(final PropertyController aController, final Context aContext, final CharMode aMode)
	{
		super(aController, aContext, aMode, null, null);
		mProperties = new PropertyItemCreationValueGroup(getController().getProperties(), this, getContext(), getCreationMode());
	}
	
	@Override
	public void addItem(final PropertyItem aItem)
	{
		mProperties.addItem(aItem);
		resize();
	}
	
	@Override
	public void clear()
	{
		mProperties.clear();
		super.clear();
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
	public void addRestriction(final Restriction aRestriction)
	{
		mProperties.getValue(aRestriction.getKey()).addRestriction(aRestriction);
	}
	
	@Override
	public PropertyController getController()
	{
		return (PropertyController) super.getController();
	}
	
	@Override
	public List<ItemCreationValue<PropertyItem>> getDescriptionValues()
	{
		final List<ItemCreationValue<PropertyItem>> list = new ArrayList<ItemCreationValue<PropertyItem>>();
		list.addAll(mProperties.getDescriptionValues());
		return list;
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
	public void release()
	{
		mProperties.release();
	}
	
	@Override
	public void resetTempPoints()
	{
		return;
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
	public void setCreationMode(final CharMode aMode)
	{
		super.setCreationMode(aMode);
		mProperties.setCreationMode(aMode);
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
	public void setPoints(final PointHandler aPoints)
	{
		return;
	}
	
	@Override
	public void updateValues(final boolean aUpdateOthers)
	{
		updateValues();
	}
	
	@Override
	protected void updateValues()
	{
		switch (getCreationMode())
		{
			case MAIN :
				mProperties.updateValues(true, true);
				break;
			case POINTS :
				mProperties.updateValues(false, false);
				break;
			case DESCRIPTIONS :
				mProperties.updateValues(false, false);
				break;
		}
	}
}
