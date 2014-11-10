package com.deepercreeper.vampireapp.controller.disciplines;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.deepercreeper.vampireapp.Clan;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.ResizeAnimation;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.implementations.VariableValueControllerImpl;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A controller of discipline value groups.
 * 
 * @author Vincent
 */
public class DisciplineValueController extends VariableValueControllerImpl<DisciplineItem, DisciplineItemValue>
{
	private Button							mShowPanel;
	
	private LinearLayout					mDisciplinesPanel;
	
	private boolean							mDisciplinesOpen;
	
	private final DisciplineItemValueGroup	mDisciplines;
	
	/**
	 * Creates a new discipline value group.
	 * 
	 * @param aController
	 *            The controller type.
	 * @param aContext
	 *            The context.
	 * @param aMode
	 *            Whether this controller is inside the creation mode.
	 * @param aPoints
	 *            The caller for free or experience points.
	 * @param aUpdateOthers
	 *            The update others action.
	 */
	public DisciplineValueController(final DisciplineController aController, final Context aContext, final CharMode aMode,
			final PointHandler aPoints, final UpdateAction aUpdateOthers)
	{
		super(aController, aContext, aMode, aPoints, aUpdateOthers);
		mDisciplines = new DisciplineItemValueGroup(getController().getDisciplines(), this, getContext(), getCreationMode(), getPoints());
	}
	
	@Override
	public void addItem(final DisciplineItem aItem)
	{
		mDisciplines.addItem(aItem);
		resize();
	}
	
	/**
	 * Used to initialize all disciplines corresponding to the new clan.
	 * 
	 * @param aClan
	 *            The new clan of the current character.
	 */
	public void changeClan(final Clan aClan)
	{
		clear();
		for (final DisciplineItem discipline : aClan.getDisciplines())
		{
			mDisciplines.addItem(discipline);
		}
	}
	
	@Override
	public void clear()
	{
		mDisciplines.clear();
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
	public DisciplineController getController()
	{
		return (DisciplineController) super.getController();
	}
	
	@Override
	public void initLayout(final LinearLayout aLayout)
	{
		final Context context = aLayout.getContext();
		aLayout.removeAllViews();
		
		mDisciplinesOpen = false;
		
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
	
	@Override
	public void release()
	{
		mDisciplines.release();
	}
	
	@Override
	public void resetTempPoints()
	{
		mDisciplines.resetTempPoints();
	}
	
	@Override
	public void resize()
	{
		if ( !mDisciplinesOpen)
		{
			mDisciplines.resize();
		}
	}
	
	@Override
	public void setCreationMode(final CharMode aMode)
	{
		super.setCreationMode(aMode);
		mDisciplines.setCreationMode(aMode);
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
		setPoints(aPoints);
		mDisciplines.setPoints(aPoints);
	}
	
	@Override
	protected void updateValues()
	{
		switch (getCreationMode())
		{
			case MAIN :
				mDisciplines.updateValues(mDisciplines.getValue() < getController().getMaxCreationValue(), true);
				break;
			case POINTS :
				mDisciplines.updateValues(true, true);
				break;
			case NORMAL :
				mDisciplines.updateValues(true, false);
				break;
		}
	}
}
