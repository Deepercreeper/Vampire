package com.deepercreeper.vampireapp.controller.backgrounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controller.dialog.SelectItemDialog;
import com.deepercreeper.vampireapp.controller.dialog.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.controller.implementations.VariableValueGroupImpl;
import com.deepercreeper.vampireapp.controller.interfaces.ValueController.PointHandler;
import com.deepercreeper.vampireapp.creation.CharMode;
import com.deepercreeper.vampireapp.util.ResizeAnimation;
import com.deepercreeper.vampireapp.util.ViewUtil;

/**
 * A value group for background values.
 * 
 * @author Vincent
 */
public class BackgroundItemValueGroup extends VariableValueGroupImpl<BackgroundItem, BackgroundItemValue>
{
	private LinearLayout	mBackgroundsPanel;
	
	private TableLayout		mBackgroundsTable;
	
	/**
	 * Creates a new background value group.
	 * 
	 * @param aGroup
	 *            The item group type.
	 * @param aController
	 *            The parent controller.
	 * @param aContext
	 *            The context.
	 * @param aMode
	 *            Whether this is inside creation mode.
	 * @param aPoints
	 *            The caller for free or experience points.
	 */
	public BackgroundItemValueGroup(final BackgroundItemGroup aGroup, final BackgroundValueController aController, final Context aContext,
			final CharMode aMode, final PointHandler aPoints)
	{
		super(aGroup, aController, aContext, aMode, aPoints);
	}
	
	@Override
	public void addItem(final BackgroundItem aItem)
	{
		addValue(new BackgroundItemValue(aItem, getContext(), getUpdateAction(), this, getCreationMode(), getPoints()));
		resize();
	}
	
	/**
	 * Creates a select item dialog that sets the given value.
	 * 
	 * @param aValue
	 *            The value to edit.
	 */
	public void editValue(final BackgroundItemValue aValue)
	{
		if (SelectItemDialog.isDialogOpen())
		{
			return;
		}
		final List<BackgroundItem> items = new ArrayList<BackgroundItem>();
		items.addAll(getGroup().getItems());
		for (final BackgroundItemValue value : getValuesList())
		{
			items.remove(value.getItem());
		}
		
		final SelectionListener<BackgroundItem> action = new SelectionListener<BackgroundItem>()
		{
			@Override
			public void select(final BackgroundItem aItem)
			{
				setValue(aValue, aItem);
			}
		};
		
		SelectItemDialog.<BackgroundItem> showSelectionDialog(items, getContext().getResources().getString(R.string.edit_background), getContext(),
				action);
	}
	
	@Override
	public HashMap<BackgroundItem, BackgroundItemValue> getValues()
	{
		return super.getValues();
	}
	
	@Override
	public List<BackgroundItemValue> getValuesList()
	{
		return super.getValuesList();
	}
	
	@Override
	public void initLayout(final ViewGroup aLayout)
	{
		mBackgroundsPanel = (LinearLayout) aLayout;
		mBackgroundsTable = new TableLayout(getContext());
		
		mBackgroundsTable.setLayoutParams(ViewUtil.instance().getWrapHeight());
		
		final LinearLayout titleRow = new LinearLayout(getContext());
		titleRow.setLayoutParams(ViewUtil.instance().getWrapHeight());
		if (getCreationMode() == CharMode.MAIN)
		{
			final Button addBackground = new Button(getContext());
			addBackground.setLayoutParams(ViewUtil.instance().getWrapHeight());
			addBackground.setTextSize(14);
			addBackground.setText(getContext().getResources().getString(R.string.add_background));
			addBackground.setEnabled(getValuesList().size() < BackgroundItem.MAX_BACKGROUNDS);
			addBackground.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					if (SelectItemDialog.isDialogOpen())
					{
						return;
					}
					final List<BackgroundItem> items = new ArrayList<BackgroundItem>();
					items.addAll(getGroup().getItems());
					for (final BackgroundItemValue value : getValuesList())
					{
						items.remove(value.getItem());
					}
					
					final SelectionListener<BackgroundItem> action = new SelectionListener<BackgroundItem>()
					{
						@Override
						public void select(final BackgroundItem aItem)
						{
							addItem(aItem);
							addBackground.setEnabled(getValuesList().size() < BackgroundItem.MAX_BACKGROUNDS);
						}
					};
					
					SelectItemDialog.<BackgroundItem> showSelectionDialog(items, getContext().getResources().getString(R.string.add_background),
							getContext(), action);
				}
			});
			titleRow.addView(addBackground);
		}
		mBackgroundsTable.addView(titleRow);
		
		for (final BackgroundItemValue value : getValuesList())
		{
			final TableRow row = new TableRow(getContext());
			value.initRow(row);
			mBackgroundsTable.addView(row);
			value.refreshValue();
		}
		aLayout.addView(mBackgroundsTable);
		getController().updateValues(false);
	}
	
	@Override
	public void resize()
	{
		if (mBackgroundsPanel != null)
		{
			mBackgroundsPanel.startAnimation(new ResizeAnimation(mBackgroundsPanel, mBackgroundsPanel.getWidth(), ViewUtil
					.calcHeight(mBackgroundsPanel)));
		}
	}
	
	private void addValue(final BackgroundItemValue aValue)
	{
		getValuesList().add(aValue);
		getValues().put(aValue.getItem(), aValue);
		if (mBackgroundsTable != null)
		{
			final TableRow row = new TableRow(getContext());
			aValue.initRow(row);
			mBackgroundsTable.addView(row);
		}
		getUpdateAction().update();
	}
	
	private void setValue(final BackgroundItemValue aOldValue, final BackgroundItem aNewItem)
	{
		final int oldIndex = getValuesList().indexOf(aOldValue);
		final BackgroundItemValue newValue = new BackgroundItemValue(aNewItem, getContext(), getUpdateAction(), this, getCreationMode(), getPoints());
		getValuesList().set(oldIndex, newValue);
		getValues().remove(aOldValue.getItem());
		getValues().put(aNewItem, newValue);
		if (mBackgroundsTable != null)
		{
			newValue.initRow(aOldValue.getContainer());
		}
	}
}
