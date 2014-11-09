package com.deepercreeper.vampireapp.controller.disciplines;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.SelectItemDialog;
import com.deepercreeper.vampireapp.controller.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.controller.implementations.ItemValueImpl;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController;
import com.deepercreeper.vampireapp.controller.interfaces.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * The value of the discipline items.
 * 
 * @author Vincent
 */
public class DisciplineItemValue extends ItemValueImpl<DisciplineItem>
{
	private int									mValue;
	
	protected int								mTempPoints;
	
	private final LinearLayout					mContainer;
	
	private ImageButton							mIncreaseButton;
	
	private ImageButton							mDecreaseButton;
	
	protected RadioButton[]						mValueDisplay;
	
	private final HashSet<ImageButton>			mEditButtons	= new HashSet<ImageButton>();
	
	private final List<SubDisciplineItemValue>	mSubValues		= new ArrayList<SubDisciplineItemValue>();
	
	/**
	 * Creates a new discipline item value.
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
	public DisciplineItemValue(final DisciplineItem aItem, final Context aContext, final UpdateAction aAction, final DisciplineItemValueGroup aGroup,
			final CharMode aMode, final PointHandler aPoints)
	{
		super(aItem, aContext, aAction, aGroup, aMode, aPoints);
		mContainer = new LinearLayout(getContext());
		mIncreaseButton = new ImageButton(getContext());
		mDecreaseButton = new ImageButton(getContext());
		mValue = getItem().getStartValue();
		init();
	}
	
	@Override
	public void setPoints(final PointHandler aPoints)
	{
		super.setPoints(aPoints);
		if (getItem().isParentItem())
		{
			for (final SubDisciplineItemValue subValue : mSubValues)
			{
				subValue.setPoints(getPoints());
			}
		}
	}
	
	@Override
	public int getTempPoints()
	{
		return mTempPoints;
	}
	
	@Override
	public void release()
	{
		if (getItem().isParentItem())
		{
			for (final SubDisciplineItemValue subValue : mSubValues)
			{
				subValue.release();
			}
		}
		ViewUtil.release(mContainer, false);
	}
	
	@Override
	public void setCreationMode(final CharMode aMode)
	{
		super.setCreationMode(aMode);
		if (getItem().isParentItem())
		{
			for (final ImageButton editButton : mEditButtons)
			{
				editButton.setEnabled(getCreationMode() == CharMode.MAIN);
			}
			for (final SubDisciplineItemValue subValue : mSubValues)
			{
				subValue.setCreationMode(getCreationMode());
			}
		}
	}
	
	protected void init()
	{
		if (getItem().isParentItem())
		{
			initParentDiscipline();
		}
		else
		{
			initDiscipline();
		}
	}
	
	private void initParentDiscipline()
	{
		mContainer.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		
		mEditButtons.clear();
		
		final TableLayout table = new TableLayout(getContext());
		table.setLayoutParams(ViewUtil.instance().getWrapHeight());
		
		final LinearLayout parentRow = new LinearLayout(getContext());
		parentRow.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		{
			final TextView parentName = new TextView(getContext());
			parentName.setLayoutParams(ViewUtil.instance().getWrapAll());
			parentName.setText(getItem().getName() + ":");
			parentRow.addView(parentName);
		}
		table.addView(parentRow);
		
		for (int i = 0; i < DisciplineItem.MAX_SUB_DISCIPLINES; i++ )
		{
			if (hasSubDiscipline(i))
			{
				final TableRow subRow = new TableRow(getContext());
				subRow.setLayoutParams(ViewUtil.instance().getTableWrapAll());
				mSubValues.get(i).initRow(subRow, i);
			}
			else
			{
				table.addView(createSubDisciplineRow(i));
			}
		}
		
		mContainer.addView(table);
	}
	
	protected void setIncreaseButton(final ImageButton aIncreaseButton)
	{
		mIncreaseButton = aIncreaseButton;
	}
	
	protected void setDecreaseButton(final ImageButton aDecreaseButton)
	{
		mDecreaseButton = aDecreaseButton;
	}
	
	protected ImageButton getIncreaseButton()
	{
		return mIncreaseButton;
	}
	
	protected ImageButton getDecreaseButton()
	{
		return mDecreaseButton;
	}
	
	private void initDiscipline()
	{
		mContainer.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		
		final TextView valueName = new TextView(getContext());
		valueName.setLayoutParams(ViewUtil.instance().getRowNameLong());
		valueName.setGravity(Gravity.CENTER_VERTICAL);
		valueName.setSingleLine();
		valueName.setEllipsize(TruncateAt.END);
		valueName.setText(getItem().getName());
		mContainer.addView(valueName);
		
		final GridLayout spinnerGrid = new GridLayout(getContext());
		spinnerGrid.setLayoutParams(ViewUtil.instance().getRowWrapAll());
		{
			mValueDisplay = new RadioButton[getItem().getMaxValue()];
			
			mDecreaseButton.setLayoutParams(ViewUtil.instance().getButtonSize());
			mDecreaseButton.setContentDescription("Decrease");
			mDecreaseButton.setImageResource(android.R.drawable.ic_media_previous);
			mDecreaseButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					decrease();
					refreshValue();
					getUpdateAction().update();
				}
			});
			spinnerGrid.addView(mDecreaseButton);
			
			for (int i = 0; i < mValueDisplay.length; i++ )
			{
				final RadioButton valuePoint = new RadioButton(getContext());
				valuePoint.setLayoutParams(ViewUtil.instance().getValueSize());
				valuePoint.setClickable(false);
				spinnerGrid.addView(valuePoint);
				mValueDisplay[i] = valuePoint;
			}
			
			mIncreaseButton.setLayoutParams(ViewUtil.instance().getButtonSize());
			mIncreaseButton.setContentDescription("Increase");
			mIncreaseButton.setImageResource(android.R.drawable.ic_media_next);
			mIncreaseButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					increase();
					refreshValue();
					getUpdateAction().update();
				}
			});
			spinnerGrid.addView(mIncreaseButton);
			
			refreshValue();
		}
		mContainer.addView(spinnerGrid);
	}
	
	@Override
	public void refreshValue()
	{
		if (getItem().isParentItem())
		{
			for (final SubDisciplineItemValue subValue : mSubValues)
			{
				subValue.refreshValue();
			}
		}
		else
		{
			ViewUtil.applyValue(getValue(), mValueDisplay);
		}
	}
	
	private TableRow createSubDisciplineRow(final int aValueIx)
	{
		final TableRow subRow = new TableRow(getContext());
		subRow.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		
		final GridLayout numberAndName = new GridLayout(getContext());
		numberAndName.setLayoutParams(ViewUtil.instance().getRowWrapAll());
		{
			final TextView number = new TextView(getContext());
			number.setLayoutParams(ViewUtil.instance().getNumberSize());
			number.setText((aValueIx + 1) + ".");
			number.setEllipsize(TruncateAt.END);
			number.setGravity(Gravity.CENTER_VERTICAL);
			number.setSingleLine();
			numberAndName.addView(number);
			
			final ImageButton edit = new ImageButton(getContext());
			edit.setLayoutParams(ViewUtil.instance().getButtonSize());
			edit.setContentDescription("Edit");
			edit.setImageResource(android.R.drawable.ic_menu_add);
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
					items.addAll(getItem().getSubItems());
					for (final SubDisciplineItemValue value : getSubValues())
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
							setSubValue(aValueIx, value);
							value.initRow(subRow, aValueIx);
						}
					};
					SelectItemDialog.<SubDisciplineItem> showSelectionDialog(items, getContext().getResources().getString(R.string.edit_discipline),
							getContext(), action);
				}
			});
			edit.setEnabled(getCreationMode() == CharMode.MAIN);
			mEditButtons.add(edit);
			numberAndName.addView(edit);
		}
		subRow.addView(numberAndName);
		return subRow;
	}
	
	/**
	 * Returns the index of the given sub discipline value.
	 * 
	 * @param aSubValue
	 *            The sub value.
	 * @return the sub discipline value index.
	 */
	public int getSubValueIndex(final SubDisciplineItemValue aSubValue)
	{
		for (int i = 0; i < mSubValues.size(); i++ )
		{
			if (aSubValue.equals(mSubValues.get(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public void setDecreasable(final boolean aEnabled)
	{
		mDecreaseButton.setEnabled(aEnabled);
	}
	
	@Override
	public void setIncreasable(final boolean aEnabled)
	{
		mIncreaseButton.setEnabled(aEnabled);
	}
	
	@Override
	public void resetTempPoints()
	{
		if (getItem().isParentItem())
		{
			for (final SubDisciplineItemValue subValue : mSubValues)
			{
				subValue.resetTempPoints();
			}
		}
		else
		{
			mTempPoints = 0;
			refreshValue();
		}
	}
	
	@Override
	public boolean canIncrease(final CharMode aMode)
	{
		switch (aMode)
		{
			case MAIN :
				return canIncrease() && mValue < getItem().getMaxStartValue();
			case POINTS :
				return canIncrease() && getPoints().getPoints() >= getItem().getFreePointsCost();
			case NORMAL :
				return canIncrease();
		}
		return false;
	}
	
	@Override
	public boolean canDecrease(final CharMode aMode)
	{
		switch (aMode)
		{
			case MAIN :
				return canDecrease();
			case POINTS :
				return mTempPoints > 0;
			case NORMAL :
				return false;
		}
		return false;
	}
	
	@Override
	public boolean canIncrease()
	{
		return getValue() < getItem().getMaxValue();
	}
	
	@Override
	public boolean canDecrease()
	{
		return getValue() > getItem().getStartValue();
	}
	
	/**
	 * Returns the sub discipline value at the given position.
	 * 
	 * @param aPos
	 *            The sub discipline position.
	 * @return the sub discipline value at the given position.
	 */
	public SubDisciplineItemValue getSubValue(final int aPos)
	{
		if (aPos >= mSubValues.size())
		{
			return null;
		}
		return mSubValues.get(aPos);
	}
	
	/**
	 * Sets the sub discipline item value at the given position.
	 * 
	 * @param aPos
	 *            The sub discipline value position.
	 * @param aSubValue
	 *            The sub discipline value to set.
	 */
	public void setSubValue(final int aPos, final SubDisciplineItemValue aSubValue)
	{
		if (mSubValues.size() <= aPos)
		{
			mSubValues.add(aPos, aSubValue);
		}
		else
		{
			mSubValues.set(aPos, aSubValue);
		}
		aSubValue.setParent(this);
	}
	
	/**
	 * This returns whether this discipline has a sub discipline value at the<br>
	 * given position if this is a parent discipline.
	 * 
	 * @param aPos
	 *            The sub discipline position.
	 * @return {@code false} if the sub discipline at the given position is {@code null} or not.
	 */
	public boolean hasSubDiscipline(final int aPos)
	{
		return mSubValues.size() > aPos && mSubValues.get(aPos) != null;
	}
	
	/**
	 * @return a list of all sub discipline item values.
	 */
	public List<SubDisciplineItemValue> getSubValues()
	{
		return mSubValues;
	}
	
	@Override
	public int getValue()
	{
		return mValue + mTempPoints;
	}
	
	@Override
	public void increase()
	{
		if (canIncrease())
		{
			if (getCreationMode() == CharMode.POINTS)
			{
				mTempPoints++ ;
				getPoints().decrease(getItem().getFreePointsCost());
			}
			else
			{
				mValue++ ;
			}
		}
	}
	
	@Override
	protected DisciplineItemValueGroup getGroup()
	{
		return (DisciplineItemValueGroup) super.getGroup();
	}
	
	@Override
	public void decrease()
	{
		if (canDecrease())
		{
			if (getCreationMode() == CharMode.POINTS)
			{
				mTempPoints-- ;
				getPoints().increase(getItem().getFreePointsCost());
			}
			else
			{
				mValue-- ;
			}
		}
	}
}