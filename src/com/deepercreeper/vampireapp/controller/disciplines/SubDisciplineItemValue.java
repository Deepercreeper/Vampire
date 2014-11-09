package com.deepercreeper.vampireapp.controller.disciplines;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.SelectItemDialog;
import com.deepercreeper.vampireapp.controller.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * The value instance of the sub discipline item.
 * 
 * @author Vincent
 */
public class SubDisciplineItemValue extends DisciplineItemValue
{
	private DisciplineItemValue	mParent;
	
	private ImageButton			mEditButton;
	
	/**
	 * Creates a new sub discipline item value.
	 * 
	 * @param aItem
	 *            The item type.
	 * @param aContext
	 *            The context.
	 * @param aAction
	 *            The update action.
	 * @param aGroup
	 *            The parent group.
	 * @param aMode
	 *            The current creation mode.
	 * @param aPoints
	 *            The caller for free or experience points.
	 */
	public SubDisciplineItemValue(final SubDisciplineItem aItem, final Context aContext, final UpdateAction aAction,
			final DisciplineItemValueGroup aGroup, final CharMode aMode, final PointHandler aPoints)
	{
		super(aItem, aContext, aAction, aGroup, aMode, aPoints);
	}
	
	@Override
	public void release()
	{
		return;
	}
	
	/**
	 * Sets the parent discipline value.
	 * 
	 * @param aParent
	 *            The new parent discipline.
	 */
	public void setParent(final DisciplineItemValue aParent)
	{
		mParent = aParent;
	}
	
	/**
	 * @return the parent discipline item value.
	 */
	public DisciplineItemValue getParent()
	{
		return mParent;
	}
	
	/**
	 * Initializes a table row so that all needed widgets are added to handle this value.
	 * 
	 * @param aRow
	 *            The row to initialize into.
	 * @param aValueIx
	 *            The sub discipline value index.
	 */
	public void initRow(final TableRow aRow, final int aValueIx)
	{
		final Context context = getContext();
		
		aRow.removeAllViews();
		
		final GridLayout numberAndName = new GridLayout(context);
		numberAndName.setLayoutParams(ViewUtil.instance().getRowWrapAll());
		{
			final TextView number = new TextView(context);
			number.setLayoutParams(ViewUtil.instance().getNumberSize());
			number.setGravity(Gravity.CENTER_VERTICAL);
			number.setText((aValueIx + 1) + ".");
			numberAndName.addView(number);
			
			mEditButton = new ImageButton(context);
			mEditButton.setLayoutParams(ViewUtil.instance().getButtonSize());
			mEditButton.setContentDescription("Edit");
			mEditButton.setEnabled(getCreationMode() == CharMode.MAIN);
			mEditButton.setImageResource(android.R.drawable.ic_menu_edit);
			mEditButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					if (SelectItemDialog.isDialogOpen())
					{
						return;
					}
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
							final SubDisciplineItemValue value = new SubDisciplineItemValue(aItem, getContext(), getUpdateAction(), getGroup(),
									getCreationMode(), getPoints());
							mParent.setSubValue(aValueIx, value);
							value.initRow(aRow, aValueIx);
						}
					};
					
					SelectItemDialog.<SubDisciplineItem> showSelectionDialog(items, context.getResources().getString(R.string.edit_discipline),
							context, action);
				}
			});
			numberAndName.addView(mEditButton);
			
			final TextView name = new TextView(context);
			name.setLayoutParams(ViewUtil.instance().getNameSizeShort());
			name.setGravity(Gravity.CENTER_VERTICAL);
			name.setEllipsize(TruncateAt.END);
			name.setSingleLine();
			name.setText(getItem().getName());
			numberAndName.addView(name);
		}
		aRow.addView(numberAndName);
		
		final GridLayout spinnerGrid = new GridLayout(context);
		spinnerGrid.setLayoutParams(ViewUtil.instance().getRowWrapAll());
		{
			final ImageButton decrease = new ImageButton(context);
			final ImageButton increase = new ImageButton(context);
			setDecreaseButton(decrease);
			setIncreaseButton(increase);
			
			mValueDisplay = new RadioButton[getItem().getMaxValue()];
			
			decrease.setLayoutParams(ViewUtil.instance().getButtonSize());
			decrease.setContentDescription("Decrease");
			decrease.setImageResource(android.R.drawable.ic_media_previous);
			decrease.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					decrease();
					refreshValue();
					getUpdateAction().update();
				}
			});
			spinnerGrid.addView(decrease);
			
			for (int j = 0; j < mValueDisplay.length; j++ )
			{
				final RadioButton valuePoint = new RadioButton(context);
				valuePoint.setLayoutParams(ViewUtil.instance().getValueSize());
				valuePoint.setClickable(false);
				spinnerGrid.addView(valuePoint);
				mValueDisplay[j] = valuePoint;
			}
			
			increase.setLayoutParams(ViewUtil.instance().getButtonSize());
			increase.setContentDescription("Increase");
			increase.setImageResource(android.R.drawable.ic_media_next);
			increase.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					increase();
					refreshValue();
					getUpdateAction().update();
				}
			});
			spinnerGrid.addView(increase);
			
			refreshValue();
			getUpdateAction().update();
		}
		aRow.addView(spinnerGrid);
	}
	
	@Override
	public void resetTempPoints()
	{
		mTempPoints = 0;
		refreshValue();
	}
	
	@Override
	protected void init()
	{
		// The widgets are generated dynamically
	}
	
	@Override
	public boolean canDecrease(final CharMode aMode)
	{
		switch (aMode)
		{
			case MAIN :
				return canDecreaseCreation();
			case POINTS :
				return getTempPoints() > 0;
			case NORMAL :
				return false;
		}
		return false;
	}
	
	private boolean canDecreaseCreation()
	{
		final DisciplineItemValue parentValue = getParent();
		final boolean firstSubItem = parentValue.getSubValueIndex(this) == 0;
		if (firstSubItem)
		{
			if (getValue() == SubDisciplineItem.MIN_FIRST_SUB_VALUE)
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
		return canDecrease();
	}
	
	@Override
	public boolean canIncrease(final CharMode aMode)
	{
		switch (aMode)
		{
			case MAIN :
				return canIncreaseValue();
			case POINTS :
				return canIncreaseValue() && getPoints().getPoints() >= getItem().getFreePointsCost();
			case NORMAL :
				return canIncrease();
		}
		return false;
	}
	
	private boolean canIncreaseValue()
	{
		final DisciplineItemValue parentValue = getParent();
		final boolean firstSubItem = parentValue.getSubValueIndex(this) == 0;
		if (firstSubItem || parentValue.getSubValue(0).getValue() >= SubDisciplineItem.MIN_FIRST_SUB_VALUE)
		{
			return canIncrease();
		}
		return false;
	}
	
	@Override
	public void setCreationMode(final CharMode aMode)
	{
		setCreationMode(aMode);
		mEditButton.setEnabled(getCreationMode() == CharMode.MAIN);
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
