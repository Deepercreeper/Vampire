package com.deepercreeper.vampireapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import com.deepercreeper.vampireapp.ResizeAnimation;
import com.deepercreeper.vampireapp.controller.ItemValue.UpdateAction;
import com.deepercreeper.vampireapp.controller.ValueController.PointHandler;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A discipline value group.
 * 
 * @author Vincent
 */
public class DisciplineItemValueGroup implements ItemValueGroup<DisciplineItem>, VariableValueGroup<DisciplineItem, DisciplineItemValue>
{
	private Mode										mMode;
	
	private PointHandler										mPoints;
	
	private final Context										mContext;
	
	private LinearLayout										mDisciplinesPanel;
	
	private TableLayout											mDisciplinesTable;
	
	private final DisciplineValueController						mController;
	
	private final DisciplineItemGroup							mGroup;
	
	private final UpdateAction									mAction;
	
	private final List<DisciplineItemValue>						mValuesList	= new ArrayList<DisciplineItemValue>();
	
	private final HashMap<DisciplineItem, DisciplineItemValue>	mValues		= new HashMap<DisciplineItem, DisciplineItemValue>();
	
	/**
	 * Creates a discipline item value group.
	 * 
	 * @param aGroup
	 *            The item group type.
	 * @param aController
	 *            The parent controller.
	 * @param aContext
	 *            The context.
	 * @param aMode
	 *            Whether this group is inside creation mode.
	 * @param aPoints
	 *            The caller for free or experience points.
	 */
	public DisciplineItemValueGroup(final DisciplineItemGroup aGroup, final DisciplineValueController aController, final Context aContext,
			final Mode aMode, final PointHandler aPoints)
	{
		mController = aController;
		mPoints = aPoints;
		mContext = aContext;
		mGroup = aGroup;
		mMode = aMode;
		mAction = new UpdateAction()
		{
			@Override
			public void update()
			{
				mController.updateValues(mMode == Mode.FREE_POINTS);
			}
		};
	}
	
	@Override
	public void setPoints(final PointHandler aPoints)
	{
		mPoints = aPoints;
		for (final DisciplineItemValue value : mValuesList)
		{
			value.setPoints(mPoints);
		}
	}
	
	@Override
	public void resetTempPoints()
	{
		for (final DisciplineItemValue value : mValuesList)
		{
			value.resetTempPoints();
		}
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(mDisciplinesTable, true);
		for (final DisciplineItemValue value : mValuesList)
		{
			value.release();
		}
	}
	
	@Override
	public DisciplineValueController getController()
	{
		return mController;
	}
	
	@Override
	public DisciplineItemGroup getGroup()
	{
		return mGroup;
	}
	
	@Override
	public List<DisciplineItemValue> getValuesList()
	{
		return mValuesList;
	}
	
	@Override
	public DisciplineItemValue getValue(final String aName)
	{
		return mValues.get(getGroup().getItem(aName));
	}
	
	@Override
	public void updateValues(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		for (final DisciplineItemValue value : mValuesList)
		{
			if (value.getItem().isParentItem())
			{
				for (final SubDisciplineItemValue subValue : value.getSubValues())
				{
					if (subValue != null)
					{
						subValue.setIncreasable(aCanIncrease && subValue.canIncrease(mMode));
						subValue.setDecreasable(aCanDecrease && subValue.canDecrease(mMode));
					}
				}
			}
			else
			{
				value.setIncreasable(aCanIncrease && value.canIncrease(mMode));
				value.setDecreasable(aCanDecrease && value.canDecrease(mMode));
			}
		}
	}
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final DisciplineItemValue valueItem : mValuesList)
		{
			if (valueItem.getItem().isParentItem())
			{
				for (final SubDisciplineItemValue subValue : valueItem.getSubValues())
				{
					value += subValue.getValue();
				}
			}
			else
			{
				value += valueItem.getValue();
			}
		}
		return value;
	}
	
	@Override
	public int getTempPoints()
	{
		int value = 0;
		for (final DisciplineItemValue valueItem : mValuesList)
		{
			if (valueItem.getItem().isParentItem())
			{
				for (final SubDisciplineItemValue subValue : valueItem.getSubValues())
				{
					value += subValue.getTempPoints();
				}
			}
			else
			{
				value += valueItem.getTempPoints();
			}
		}
		return value;
	}
	
	private void addValue(final DisciplineItemValue aValue)
	{
		mValuesList.add(aValue);
		mValues.put(aValue.getItem(), aValue);
		if (mDisciplinesTable != null)
		{
			mDisciplinesTable.addView(aValue.getContainer());
		}
	}
	
	@Override
	public void addItem(final DisciplineItem aItem)
	{
		addValue(new DisciplineItemValue(aItem, mContext, mAction, mMode, mPoints));
	}
	
	@Override
	public void resize()
	{
		if (mDisciplinesPanel != null)
		{
			mDisciplinesPanel.startAnimation(new ResizeAnimation(mDisciplinesPanel, mDisciplinesPanel.getWidth(), ViewUtil
					.calcHeight(mDisciplinesPanel)));
		}
	}
	
	@Override
	public void clear()
	{
		mValuesList.clear();
		mValues.clear();
		if (mDisciplinesTable != null)
		{
			mDisciplinesTable.removeAllViews();
		}
	}
	
	@Override
	public Mode getCreationMode()
	{
		return mMode;
	}
	
	@Override
	public void setCreationMode(final Mode aMode)
	{
		final boolean resetTempPoints = mMode == Mode.FREE_POINTS && aMode == Mode.CREATION;
		mMode = aMode;
		for (final DisciplineItemValue value : mValuesList)
		{
			value.setCreationMode(mMode);
			if (resetTempPoints)
			{
				value.resetTempPoints();
			}
		}
	}
	
	@Override
	public void initLayout(final ViewGroup aLayout)
	{
		mDisciplinesPanel = (LinearLayout) aLayout;
		final Context context = aLayout.getContext();
		mDisciplinesTable = new TableLayout(context);
		
		mDisciplinesTable.setLayoutParams(ViewUtil.instance().getWrapHeight());
		
		for (final DisciplineItemValue value : mValuesList)
		{
			final ViewGroup container = value.getContainer();
			if (container.getParent() != null)
			{
				ViewUtil.release(container, false);
			}
			mDisciplinesTable.addView(value.getContainer());
			value.refreshValue();
		}
		aLayout.addView(mDisciplinesTable);
		mController.updateValues(false);
	}
}
