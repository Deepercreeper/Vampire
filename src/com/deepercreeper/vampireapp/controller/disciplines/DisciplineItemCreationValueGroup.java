package com.deepercreeper.vampireapp.controller.disciplines;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import com.deepercreeper.vampireapp.controller.implementations.VariableCreationValueGroupImpl;
import com.deepercreeper.vampireapp.controller.interfaces.CreationValueController.PointHandler;
import com.deepercreeper.vampireapp.creation.CharMode;
import com.deepercreeper.vampireapp.util.ResizeAnimation;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A discipline value group.
 * 
 * @author Vincent
 */
public class DisciplineItemCreationValueGroup extends VariableCreationValueGroupImpl<DisciplineItem, DisciplineItemCreationValue>
{
	private LinearLayout	mDisciplinesPanel;
	
	private TableLayout		mDisciplinesTable;
	
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
	public DisciplineItemCreationValueGroup(final DisciplineItemGroup aGroup, final DisciplineCreationValueController aController, final Context aContext,
			final CharMode aMode, final PointHandler aPoints)
	{
		super(aGroup, aController, aContext, aMode, aPoints);
	}
	
	@Override
	public void addItem(final DisciplineItem aItem)
	{
		addValue(new DisciplineItemCreationValue(aItem, getContext(), getUpdateAction(), this, getCreationMode(), getPoints()));
	}
	
	@Override
	public void clear()
	{
		super.clear();
		if (mDisciplinesTable != null)
		{
			mDisciplinesTable.removeAllViews();
		}
	}
	
	@Override
	public int getTempPoints()
	{
		int value = 0;
		for (final DisciplineItemCreationValue valueItem : getValuesList())
		{
			if (valueItem.getItem().isParentItem())
			{
				for (final SubDisciplineItemCreationValue subValue : valueItem.getSubValues())
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
	
	@Override
	public int getValue()
	{
		int value = 0;
		for (final DisciplineItemCreationValue valueItem : getValuesList())
		{
			if (valueItem.getItem().isParentItem())
			{
				for (final SubDisciplineItemCreationValue subValue : valueItem.getSubValues())
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
	public HashMap<DisciplineItem, DisciplineItemCreationValue> getValues()
	{
		return super.getValues();
	}
	
	@Override
	public List<DisciplineItemCreationValue> getValuesList()
	{
		return super.getValuesList();
	}
	
	@Override
	public void initLayout(final ViewGroup aLayout)
	{
		mDisciplinesPanel = (LinearLayout) aLayout;
		final Context context = aLayout.getContext();
		mDisciplinesTable = new TableLayout(context);
		
		mDisciplinesTable.setLayoutParams(ViewUtil.instance().getWrapHeight());
		
		for (final DisciplineItemCreationValue value : getValuesList())
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
		getController().updateValues(false);
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(mDisciplinesTable, true);
		super.release();
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
	public void updateValues(final boolean aCanIncrease, final boolean aCanDecrease)
	{
		for (final DisciplineItemCreationValue value : getValuesList())
		{
			if (value.getItem().isParentItem())
			{
				for (final SubDisciplineItemCreationValue subValue : value.getSubValues())
				{
					if (subValue != null)
					{
						subValue.setIncreasable(aCanIncrease && subValue.canIncrease(getCreationMode()));
						subValue.setDecreasable(aCanDecrease && subValue.canDecrease(getCreationMode()));
					}
				}
			}
			else
			{
				value.setIncreasable(aCanIncrease && value.canIncrease(getCreationMode()));
				value.setDecreasable(aCanDecrease && value.canDecrease(getCreationMode()));
			}
		}
	}
	
	private void addValue(final DisciplineItemCreationValue aValue)
	{
		getValuesList().add(aValue);
		getValues().put(aValue.getItem(), aValue);
		if (mDisciplinesTable != null)
		{
			mDisciplinesTable.addView(aValue.getContainer());
		}
	}
}
