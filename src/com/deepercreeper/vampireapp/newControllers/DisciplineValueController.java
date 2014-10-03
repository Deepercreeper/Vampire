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

public class DisciplineValueController implements ValueController<DisciplineItem>
{
	private boolean							mCreation;
	
	private final DisciplineController		mController;
	
	private final DisciplineItemValueGroup	mDisciplines;
	
	public DisciplineValueController(final DisciplineController aController, final boolean aCreation)
	{
		mCreation = aCreation;
		mController = aController;
		mDisciplines = new DisciplineItemValueGroup(mController.getDisciplines(), this, mCreation);
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
		
		final LayoutParams wrapHeight = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams zeroHeight = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		
		aLayout.setLayoutParams(wrapHeight);
		
		final Button showDisciplines = new Button(context);
		final LinearLayout disciplines = new LinearLayout(context);
		disciplines.setLayoutParams(zeroHeight);
		
		showDisciplines.setLayoutParams(wrapHeight);
		showDisciplines.setText(R.string.disciplines);
		showDisciplines.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		showDisciplines.setOnClickListener(new OnClickListener()
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
						mDisciplines.initLayout(disciplines);
					}
					disciplines.startAnimation(new ResizeAnimation(disciplines, disciplines.getWidth(), ViewUtil.calcHeight(disciplines)));
					showDisciplines.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
				else
				{
					disciplines.startAnimation(new ResizeAnimation(disciplines, disciplines.getWidth(), 0));
					showDisciplines.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
				}
			}
		});
		
		aLayout.addView(showDisciplines);
		aLayout.addView(disciplines);
	}
}
