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
 * A controller of discipline value groups.
 * 
 * @author Vincent
 */
public class DisciplineValueController implements ValueController<DisciplineItem>, VariableValueGroup<DisciplineItem, DisciplineItemValue>
{
	private boolean							mCreation;
	
	private final Context					mContext;
	
	private Button							mShowPanel;
	
	private LinearLayout					mDisciplinesPanel;
	
	private boolean							mDisciplinesOpen	= false;
	
	private final DisciplineController		mController;
	
	private final DisciplineItemValueGroup	mDisciplines;
	
	/**
	 * Creates a new discipline value group.
	 * 
	 * @param aController
	 *            The controller type.
	 * @param aContext
	 *            The context.
	 * @param aCreation
	 *            Whether this controller is inside the creation mode.
	 */
	public DisciplineValueController(final DisciplineController aController, final Context aContext, final boolean aCreation)
	{
		mCreation = aCreation;
		mContext = aContext;
		mController = aController;
		mDisciplines = new DisciplineItemValueGroup(mController.getDisciplines(), this, mContext, mCreation);
	}
	
	@Override
	public void addItem(final DisciplineItem aItem)
	{
		mDisciplines.addItem(aItem);
		resize();
	}
	
	@Override
	public void close()
	{
		mDisciplinesOpen = false;
		if (mDisciplinesPanel != null)
		{
			mDisciplinesPanel.startAnimation(new ResizeAnimation(mDisciplinesPanel, mDisciplinesPanel.getWidth(), 0));
		}
		if (mShowPanel != null)
		{
			mShowPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		}
	}
	
	@Override
	public void clear()
	{
		mDisciplines.clear();
	}
	
	@Override
	public void resize()
	{
		mDisciplines.resize();
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
		mDisciplines.setCreation(mCreation);
	}
	
	@Override
	public boolean isCreation()
	{
		return mCreation;
	}
	
	@Override
	public void updateValues()
	{
		mDisciplines.updateValues(mDisciplines.getValue() < mController.getMaxCreationValue(), true);
	}
	
	@Override
	public DisciplineController getController()
	{
		return mController;
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{
		final Context context = aLayout.getContext();
		aLayout.removeAllViews();
		
		mShowPanel = new Button(context);
		mDisciplinesPanel = new LinearLayout(context);
		mDisciplinesPanel.setLayoutParams(ViewUtil.instance().getZeroHeight());
		
		mShowPanel.setLayoutParams(ViewUtil.instance().getWrapHeight());
		mShowPanel.setText(R.string.disciplines);
		mShowPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		mShowPanel.setOnClickListener(new OnClickListener()
		{
			private boolean	mInitialized	= false;
			
			@Override
			public void onClick(final View aArg0)
			{
				mDisciplinesOpen = !mDisciplinesOpen;
				if (mDisciplinesOpen)
				{
					if ( !mInitialized)
					{
						mDisciplines.initLayout(mDisciplinesPanel);
						mInitialized = true;
					}
					mDisciplinesPanel.startAnimation(new ResizeAnimation(mDisciplinesPanel, mDisciplinesPanel.getWidth(), ViewUtil
							.calcHeight(mDisciplinesPanel)));
					mShowPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					mDisciplinesPanel.startAnimation(new ResizeAnimation(mDisciplinesPanel, mDisciplinesPanel.getWidth(), 0));
					mShowPanel.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
				}
			}
		});
		
		aLayout.addView(mShowPanel);
		aLayout.addView(mDisciplinesPanel);
	}
}
