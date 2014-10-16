package com.deepercreeper.vampireapp.controller;

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
import com.deepercreeper.vampireapp.controller.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * The value of the discipline items.
 * 
 * @author Vincent
 */
public class DisciplineItemValue implements ItemValue<DisciplineItem>
{
	protected CreationMode						mMode;
	
	private final DisciplineItem				mItem;
	
	private int									mValue;
	
	private int									mTempPoints;
	
	private final Context						mContext;
	
	private final LinearLayout					mContainer;
	
	private ImageButton							mIncreaseButton;
	
	private ImageButton							mDecreaseButton;
	
	protected RadioButton[]						mValueDisplay;
	
	private final HashSet<ImageButton>			mEditButtons	= new HashSet<ImageButton>();
	
	private final UpdateAction					mAction;
	
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
	 * @param aMode
	 *            The current creation mode.
	 */
	public DisciplineItemValue(final DisciplineItem aItem, final Context aContext, final UpdateAction aAction, final CreationMode aMode)
	{
		mMode = aMode;
		mItem = aItem;
		mContext = aContext;
		mAction = aAction;
		mContainer = new LinearLayout(mContext);
		mIncreaseButton = new ImageButton(mContext);
		mDecreaseButton = new ImageButton(mContext);
		mValue = mItem.getStartValue();
		init();
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
	public void setCreationMode(final CreationMode aMode)
	{
		mMode = aMode;
		if (mItem.isParentItem())
		{
			for (final ImageButton editButton : mEditButtons)
			{
				editButton.setEnabled(mMode == CreationMode.CREATION);
			}
			for (final SubDisciplineItemValue subValue : mSubValues)
			{
				subValue.setCreationMode(mMode);
			}
		}
	}
	
	@Override
	public CreationMode getCreationMode()
	{
		return mMode;
	}
	
	protected void init()
	{
		if (mItem.isParentItem())
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
		
		final TableLayout table = new TableLayout(mContext);
		table.setLayoutParams(ViewUtil.instance().getWrapHeight());
		
		final LinearLayout parentRow = new LinearLayout(mContext);
		parentRow.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		{
			final TextView parentName = new TextView(mContext);
			parentName.setLayoutParams(ViewUtil.instance().getWrapAll());
			parentName.setText(getItem().getName() + ":");
			parentRow.addView(parentName);
		}
		table.addView(parentRow);
		
		for (int i = 0; i < DisciplineItem.MAX_SUB_DISCIPLINES; i++ )
		{
			if (hasSubDiscipline(i))
			{
				final TableRow subRow = new TableRow(mContext);
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
		
		final TextView valueName = new TextView(mContext);
		valueName.setLayoutParams(ViewUtil.instance().getRowNameLong());
		valueName.setGravity(Gravity.CENTER_VERTICAL);
		valueName.setSingleLine();
		valueName.setEllipsize(TruncateAt.END);
		valueName.setText(getItem().getName());
		mContainer.addView(valueName);
		
		final GridLayout spinnerGrid = new GridLayout(mContext);
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
					mAction.update();
				}
			});
			spinnerGrid.addView(mDecreaseButton);
			
			for (int i = 0; i < mValueDisplay.length; i++ )
			{
				final RadioButton valuePoint = new RadioButton(mContext);
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
					mAction.update();
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
		if (mItem.isParentItem())
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
		final TableRow subRow = new TableRow(mContext);
		subRow.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		
		final GridLayout numberAndName = new GridLayout(mContext);
		numberAndName.setLayoutParams(ViewUtil.instance().getRowWrapAll());
		{
			final TextView number = new TextView(mContext);
			number.setLayoutParams(ViewUtil.instance().getNumberSize());
			number.setText((aValueIx + 1) + ".");
			number.setEllipsize(TruncateAt.END);
			number.setGravity(Gravity.CENTER_VERTICAL);
			number.setSingleLine();
			numberAndName.addView(number);
			
			final ImageButton edit = new ImageButton(mContext);
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
							final SubDisciplineItemValue value = new SubDisciplineItemValue(aItem, mContext, mAction, mMode);
							setSubValue(aValueIx, value);
							value.initRow(subRow, aValueIx);
						}
					};
					SelectItemDialog.<SubDisciplineItem> showSelectionDialog(items, mContext.getResources().getString(R.string.edit_discipline),
							mContext, action);
				}
			});
			edit.setEnabled(mMode == CreationMode.CREATION);
			mEditButtons.add(edit);
			numberAndName.addView(edit);
		}
		subRow.addView(numberAndName);
		return subRow;
	}
	
	protected UpdateAction getAction()
	{
		return mAction;
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
	
	protected Context getContext()
	{
		return mContext;
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
		if (mItem.isParentItem())
		{
			for (final SubDisciplineItemValue subValue : mSubValues)
			{
				subValue.resetTempPoints();
			}
		}
		else
		{
			mTempPoints = 0;
		}
	}
	
	@Override
	public boolean canIncrease(final CreationMode aMode)
	{
		switch (aMode)
		{
			case CREATION :
				return canIncrease() && mValue < getItem().getMaxStartValue();
			case FREE_POINTS :
				return canIncrease() && mValue + mTempPoints < getItem().getMaxStartValue();
			case NORMAL :
				return canIncrease();
		}
		return false;
	}
	
	@Override
	public boolean canDecrease(final CreationMode aMode)
	{
		switch (aMode)
		{
			case CREATION :
				return canDecrease();
			case FREE_POINTS :
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
	
	@Override
	public DisciplineItem getItem()
	{
		return mItem;
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
			if (mMode == CreationMode.FREE_POINTS)
			{
				mTempPoints++ ;
			}
			else
			{
				mValue++ ;
			}
		}
	}
	
	@Override
	public void decrease()
	{
		if (canDecrease())
		{
			if (mMode == CreationMode.FREE_POINTS)
			{
				mTempPoints-- ;
			}
			else
			{
				mValue-- ;
			}
		}
	}
}
