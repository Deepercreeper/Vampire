package com.deepercreeper.vampireapp.controller;

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
import com.deepercreeper.vampireapp.controller.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * The value instance of the sub discipline item.
 * 
 * @author Vincent
 */
public class SubDisciplineItemValue extends DisciplineItemValue
{
	private DisciplineItemValue	mParent;
	
	/**
	 * Creates a new sub discipline item value.
	 * 
	 * @param aItem
	 *            The item type.
	 * @param aContext
	 *            The context.
	 * @param aAction
	 *            The update action.
	 */
	public SubDisciplineItemValue(final SubDisciplineItem aItem, final Context aContext, final UpdateAction aAction)
	{
		super(aItem, aContext, aAction);
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getIncreaseButton());
		ViewUtil.release(getDecreaseButton());
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
			
			final ImageButton edit = new ImageButton(context);
			edit.setLayoutParams(ViewUtil.instance().getButtonSize());
			edit.setContentDescription("Edit");
			edit.setImageResource(android.R.drawable.ic_menu_edit);
			edit.setOnClickListener(new OnClickListener()
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
							final SubDisciplineItemValue value = new SubDisciplineItemValue(aItem, getContext(), getAction());
							mParent.setSubValue(aValueIx, value);
							value.initRow(aRow, aValueIx);
						}
					};
					
					SelectItemDialog.<SubDisciplineItem> showSelectionDialog(items, context.getResources().getString(R.string.edit_discipline),
							context, action);
				}
			});
			numberAndName.addView(edit);
			
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
			
			final RadioButton[] valueDisplay = new RadioButton[getItem().getMaxValue()];
			
			decrease.setLayoutParams(ViewUtil.instance().getButtonSize());
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
				valuePoint.setLayoutParams(ViewUtil.instance().getValueSize());
				valuePoint.setClickable(false);
				spinnerGrid.addView(valuePoint);
				valueDisplay[j] = valuePoint;
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
	public boolean canIncrease(final boolean aCreation)
	{
		final DisciplineItemValue parentValue = getParent();
		final boolean firstSubItem = parentValue.getSubValueIndex(this) == 0;
		if (firstSubItem || parentValue.getSubValue(0).getValue() >= SubDisciplineItem.MIN_FIRST_SUB_VALUE)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return getItem().hashCode();
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
