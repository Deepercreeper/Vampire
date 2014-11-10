package com.deepercreeper.vampireapp.controller.simplesItems;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.TableRow;
import android.widget.TextView;
import com.deepercreeper.vampireapp.controller.CharMode;
import com.deepercreeper.vampireapp.controller.implementations.ItemValueGroupImpl;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A group of simple values.
 * 
 * @author Vincent
 */
public class SimpleItemValueGroup extends ItemValueGroupImpl<SimpleItem, SimpleItemValue>
{
	/**
	 * Creates a new item value group.
	 * 
	 * @param aGroup
	 *            The item group type.
	 * @param aController
	 *            The parent controller.
	 * @param aContext
	 *            The context.
	 * @param aMode
	 *            Whether this value group is inside the creation mode.
	 * @param aPoints
	 *            The caller for free or experience points.
	 */
	public SimpleItemValueGroup(final SimpleItemGroup aGroup, final SimpleValueController aController, final Context aContext, final CharMode aMode,
			final PointHandler aPoints)
	{
		super(aGroup, aController, aContext, aMode, aPoints);
		for (final SimpleItem item : getGroup().getItems())
		{
			addValue(new SimpleItemValue(item, getContext(), getUpdateAction(), this, getCreationMode(), getPoints()));
		}
	}
	
	@Override
	public HashMap<SimpleItem, SimpleItemValue> getValues()
	{
		return super.getValues();
	}
	
	@Override
	public List<SimpleItemValue> getValuesList()
	{
		return super.getValuesList();
	}
	
	@Override
	public void initLayout(final ViewGroup aLayout)
	{
		final Context context = aLayout.getContext();
		
		final TableRow titleRow = new TableRow(context);
		titleRow.setLayoutParams(ViewUtil.instance().getTableWrapAll());
		{
			titleRow.addView(new Space(context));
			
			final TextView title = new TextView(context);
			title.setLayoutParams(ViewUtil.instance().getRowWrapAll());
			title.setText(getGroup().getName());
			title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			titleRow.addView(title);
		}
		aLayout.addView(titleRow);
		
		for (final SimpleItemValue value : getValuesList())
		{
			aLayout.addView(value.getContainer());
			value.refreshValue();
		}
	}
	
	private void addValue(final SimpleItemValue aValue)
	{
		getValuesList().add(aValue);
		getValues().put(aValue.getItem(), aValue);
	}
}
