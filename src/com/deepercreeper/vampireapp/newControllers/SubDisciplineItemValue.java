package com.deepercreeper.vampireapp.newControllers;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.newControllers.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class SubDisciplineItemValue extends DisciplineItemValue
{
	private DisciplineItemValue	mParent;
	
	public SubDisciplineItemValue(final SubDisciplineItem aItem, final Context aContext, final UpdateAction aAction)
	{
		super(aItem, aContext, aAction);
	}
	
	public void setParent(final DisciplineItemValue aParent)
	{
		mParent = aParent;
	}
	
	public DisciplineItemValue getParent()
	{
		return mParent;
	}
	
	public void initRow(final TableRow aRow, final int aValueIx)
	{
		final Context context = getContext();
		
		final LayoutParams wrapAll = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		final LayoutParams buttonSize = new LayoutParams(ViewUtil.calcPx(30, context), ViewUtil.calcPx(30, context));
		final LayoutParams valueSize = new LayoutParams(ViewUtil.calcPx(25, context), LayoutParams.WRAP_CONTENT);
		final LayoutParams nameSize = new LayoutParams(ViewUtil.calcPx(80, context), LayoutParams.WRAP_CONTENT);
		
		aRow.removeAllViews();
		
		final TextView number = new TextView(context);
		number.setLayoutParams(wrapAll);
		number.setText(aValueIx + ".");
		aRow.addView(number);
		
		final ImageButton edit = new ImageButton(context);
		edit.setLayoutParams(buttonSize);
		edit.setContentDescription("Edit");
		edit.setImageResource(android.R.drawable.ic_menu_edit);
		edit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				final List<SubDisciplineItem> items = new ArrayList<SubDisciplineItem>();
				items.addAll(mParent.getItem().getSubItems());
				for (final SubDisciplineItemValue value : mParent.getSubValues())
				{
					items.remove(value.getItem());
				}
				
				final SelectionListener<SubDisciplineItem> action = new SelectionListener<SubDisciplineItem>()
				{
					@Override
					public void select(final SubDisciplineItem aItem)
					{
						final SubDisciplineItemValue value = new SubDisciplineItemValue(aItem, getContext(), getAction());
						mParent.setSubValue(aValueIx, value);
						value.initRow(aRow, aValueIx);
					}
				};
				
				new SelectItemDialog<SubDisciplineItem>(items, context.getResources().getString(R.string.edit_discipline), context, action);
			}
		});
		aRow.addView(edit);
		
		final TextView name = new TextView(context);
		name.setLayoutParams(nameSize);
		name.setText(getItem().getName());
		aRow.addView(name);
		
		final GridLayout spinnerGrid = new GridLayout(context);
		spinnerGrid.setLayoutParams(wrapAll);
		{
			final ImageButton decrease = new ImageButton(context);
			final ImageButton increase = new ImageButton(context);
			setDecreaseButton(decrease);
			setIncreaseButton(increase);
			
			final RadioButton[] valueDisplay = new RadioButton[getItem().getMaxValue()];
			
			decrease.setLayoutParams(buttonSize);
			decrease.setContentDescription("Decrease");
			decrease.setImageResource(android.R.drawable.ic_media_previous);
			decrease.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					decrease();
					ViewUtil.applyValue(getValue(), valueDisplay);
					getAction().update();
				}
			});
			spinnerGrid.addView(decrease);
			
			for (int j = 0; j < valueDisplay.length; j++ )
			{
				final RadioButton valuePoint = new RadioButton(context);
				valuePoint.setLayoutParams(valueSize);
				valuePoint.setClickable(false);
				spinnerGrid.addView(valuePoint);
				valueDisplay[j] = valuePoint;
			}
			
			increase.setLayoutParams(buttonSize);
			increase.setContentDescription("Increase");
			increase.setImageResource(android.R.drawable.ic_media_next);
			increase.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					increase();
					ViewUtil.applyValue(getValue(), valueDisplay);
					getAction().update();
				}
			});
			spinnerGrid.addView(increase);
			
			ViewUtil.applyValue(getValue(), valueDisplay);
			getAction().update();
		}
		aRow.addView(spinnerGrid);
	}
	
	@Override
	protected void init()
	{
		// The widgets are generated dynamically
	}
	
	@Override
	public boolean canDecrease(final boolean aCreation)
	{
		final DisciplineItemValue parentValue = getParent();
		final boolean firstSubItem = parentValue.getSubValueIndex(this) == 0;
		if (firstSubItem)
		{
			if (getValue() == DisciplineItem.MIN_FIRST_SUB_VALUE)
			{
				for (int i = 1; i < DisciplineItem.MAX_SUB_DISCIPLINES; i++ )
				{
					if (parentValue.hasSubDiscipline(i))
					{
						if (parentValue.getSubValue(i).getValue() > 0)
						{
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean canIncrease(final boolean aCreation)
	{
		final DisciplineItemValue parentValue = getParent();
		final boolean firstSubItem = parentValue.getSubValueIndex(this) == 0;
		if (firstSubItem || parentValue.getSubValue(0).getValue() >= DisciplineItem.MIN_FIRST_SUB_VALUE)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof SubDisciplineItemValue)
		{
			final SubDisciplineItemValue value = (SubDisciplineItemValue) aO;
			return getItem().equals(value.getItem());
		}
		return false;
	}
	
	@Override
	public SubDisciplineItem getItem()
	{
		return (SubDisciplineItem) super.getItem();
	}
	
	@Override
	public TableRow getContainer()
	{
		return null;
	}
}
