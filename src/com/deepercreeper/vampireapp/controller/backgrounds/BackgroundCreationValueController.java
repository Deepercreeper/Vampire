package com.deepercreeper.vampireapp.controller.backgrounds;

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
import com.deepercreeper.vampireapp.controller.interfaces.ItemCreationValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.restrictions.Restriction;
import com.deepercreeper.vampireapp.creation.CharMode;
import com.deepercreeper.vampireapp.util.ResizeAnimation;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A controller for background value groups.
 * 
 * @author Vincent
 */
public class BackgroundCreationValueController extends VariableCreationValueControllerImpl<BackgroundItem, BackgroundItemCreationValue>
{
	private Button							mShowPanel;
	
	private boolean							mBackgroundsOpen;
	
	private final BackgroundItemCreationValueGroup	mBackgrounds;
	
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
	public BackgroundCreationValueController(final BackgroundController aController, final Context aContext, final CharMode aMode, final PointHandler aPoints, final UpdateAction aUpdateOthers)
	{
		super(aController, aContext, aMode, aPoints, aUpdateOthers);
		mBackgrounds = new BackgroundItemCreationValueGroup(getController().getBackgrounds(), this, aContext, getCreationMode(), getPoints());
	}
	
	@Override
	public void addRestriction(final Restriction aRestriction)
	{
		mBackgrounds.getValue(aRestriction.getKey()).addRestriction(aRestriction);
	}
	
	@Override
	public void addItem(final BackgroundItem aItem)
	{
		mBackgrounds.addItem(aItem);
		// TODO Add restrictions to the new value
		resize();
	}
	
	@Override
	public void clear()
	{
		mBackgrounds.clear();
		super.clear();
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
	public BackgroundController getController()
	{
		return (BackgroundController) super.getController();
	}
	
	@Override
	public List<ItemCreationValue<BackgroundItem>> getDescriptionValues()
	{
		final List<ItemCreationValue<BackgroundItem>> list = new ArrayList<ItemCreationValue<BackgroundItem>>();
		list.addAll(mBackgrounds.getDescriptionValues());
		return list;
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{
		aLayout.removeAllViews();
		
		mBackgroundsOpen = false;
		
		mShowPanel = new Button(getContext());
		final LinearLayout backgrounds = new LinearLayout(getContext());
		backgrounds.setLayoutParams(ViewUtil.instance().getZeroHeight());
		
		mShowPanel.setLayoutParams(ViewUtil.instance().getWrapHeight());
		mShowPanel.setText(R.string.backgrounds);
		mShowPanel.setEnabled(getCreationMode() != CharMode.POINTS || !mBackgrounds.getValuesList().isEmpty());
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
	
	@Override
	public void release()
	{
		mBackgrounds.release();
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
	public void setCreationMode(final CharMode aMode)
	{
		super.setCreationMode(aMode);
		mBackgrounds.setCreationMode(aMode);
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
		super.setPoints(aPoints);
		mBackgrounds.setPoints(aPoints);
	}
	
	@Override
	protected void updateValues()
	{
		switch (getCreationMode())
		{
			case MAIN :
				mBackgrounds.updateValues(mBackgrounds.getValue() < getController().getMaxCreationValue(), true);
				break;
			case POINTS :
				mBackgrounds.updateValues(true, true);
				break;
			case DESCRIPTIONS :
				mBackgrounds.updateValues(false, false);
				break;
		}
	}
}
