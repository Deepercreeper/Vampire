package com.deepercreeper.vampireapp.items.implementations.creations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.CreationMode;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.CreationRestrictionableImpl;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation.PointHandler;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.SelectItemDialog.NamableSelectionListener;

public class ItemGroupCreationImpl extends CreationRestrictionableImpl implements ItemGroupCreation
{
	private static final String				TAG				= "ItemGroupCreation";
	
	private final ItemGroup					mItemGroup;
	
	private final LinearLayout				mContainer;
	
	private final Context					mContext;
	
	private final ItemControllerCreation	mItemController;
	
	private final List<ItemCreation>		mItemsList		= new ArrayList<ItemCreation>();
	
	private final Map<Item, ItemCreation>	mItems			= new HashMap<Item, ItemCreation>();
	
	private final TextView					mTitleText;
	
	private final Button					mAddButton;
	
	private boolean							mInitialized	= false;
	
	private CreationMode					mMode;
	
	private PointHandler					mPoints;
	
	public ItemGroupCreationImpl(final ItemGroup aGroup, final Context aContext, final ItemControllerCreation aController, final CreationMode aMode,
			final PointHandler aPoints)
	{
		mItemGroup = aGroup;
		mContext = aContext;
		mItemController = aController;
		setController(mItemController);
		mMode = aMode;
		mPoints = aPoints;
		mContainer = new LinearLayout(getContext());
		mTitleText = new TextView(getContext());
		mAddButton = new Button(getContext());
		init();
		if ( !isMutable())
		{
			for (final Item item : getItemGroup().getItemsList())
			{
				if (isItemOk(item))
				{
					addItemSilent(new ItemCreationImpl(item, getContext(), this, getCreationMode(), getPoints(), null));
				}
			}
			if ( !hasOrder())
			{
				sortItems();
			}
		}
	}
	
	@Override
	public boolean hasOrder()
	{
		return getItemGroup().hasOrder();
	}
	
	@Override
	public void addItem()
	{
		if ( !isMutable())
		{
			Log.w(TAG, "Tried to add an item to a non mutable group.");
			return;
		}
		if (SelectItemDialog.isDialogOpen())
		{
			return;
		}
		final List<Item> items = getAddableItems();
		if (items.isEmpty() || getMaxValue(CreationRestrictionType.GROUP_CHILDREN_COUNT) <= getItemsList().size())
		{
			return;
		}
		final NamableSelectionListener<Item> action = new NamableSelectionListener<Item>()
		{
			@Override
			public void select(final Item aChoosenItem)
			{
				addItem(aChoosenItem);
			}
			
			@Override
			public void cancel()
			{}
		};
		SelectItemDialog.showSelectionDialog(items, getContext().getString(R.string.add_item), getContext(), action);
	}
	
	@Override
	public void addItem(final Item aItem)
	{
		if ( !isMutable())
		{
			Log.w(TAG, "Tried to change a non mutable group.");
			return;
		}
		if (mItems.keySet().contains(aItem))
		{
			Log.w(TAG, "Tried to add an already existing item.");
			return;
		}
		if (getMaxValue(CreationRestrictionType.GROUP_CHILDREN_COUNT) <= getItemsList().size())
		{
			return;
		}
		final ItemCreation item = new ItemCreationImpl(aItem, getContext(), this, getCreationMode(), getPoints(), null);
		getItemsList().add(item);
		mItems.put(aItem, item);
		getContainer().addView(item.getContainer());
		getItemController().resize();
		getItemController().addItemName(item);
		updateController();
	}
	
	@Override
	public boolean canChangeBy(final int aValue)
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to get whether a non value group can be changed by a value.");
			return false;
		}
		return getItemController().canChangeGroupBy(this, aValue);
	}
	
	@Override
	public void clear()
	{
		for (final ItemCreation item : getItemsList())
		{
			item.clear();
			item.release();
		}
		mItems.clear();
		getItemsList().clear();
		updateAddButton();
	}
	
	@Override
	public int compareTo(final ItemGroupCreation aAnother)
	{
		if (aAnother == null)
		{
			return getItemGroup().compareTo(null);
		}
		return getItemGroup().compareTo(aAnother.getItemGroup());
	}
	
	@Override
	public void editItem(final Item aItem)
	{
		if ( !isMutable())
		{
			Log.w(TAG, "Tried to edit an item of a non mutable group.");
			return;
		}
		if (SelectItemDialog.isDialogOpen())
		{
			return;
		}
		final int index = getItemsList().indexOf(mItems.get(aItem));
		final List<Item> items = getAddableItems();
		if (items.isEmpty())
		{
			return;
		}
		final NamableSelectionListener<Item> action = new NamableSelectionListener<Item>()
		{
			@Override
			public void select(final Item aChoosenItem)
			{
				setItemAt(index, aChoosenItem);
			}
			
			@Override
			public void cancel()
			{}
		};
		SelectItemDialog.showSelectionDialog(items, getContext().getString(R.string.edit_item) + aItem.getName(), getContext(), action);
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof ItemGroupCreation)
		{
			final ItemGroupCreation item = (ItemGroupCreation) aO;
			return getItemGroup().equals(item.getItemGroup());
		}
		return false;
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public Context getContext()
	{
		return mContext;
	}
	
	@Override
	public CreationMode getCreationMode()
	{
		return mMode;
	}
	
	@Override
	public List<ItemCreation> getDescriptionItems()
	{
		final List<ItemCreation> items = new ArrayList<ItemCreation>();
		for (final ItemCreation item : getItemsList())
		{
			if (item.needsDescription() && ( !item.isValueItem() || item.getValue() != 0))
			{
				items.add(item);
			}
			if (item.isParent())
			{
				items.addAll(item.getDescriptionItems());
			}
		}
		return items;
	}
	
	@Override
	public ItemCreation getItem(final Item aItem)
	{
		return mItems.get(aItem);
	}
	
	@Override
	public ItemCreation getItem(final String aName)
	{
		return getItem(getItemGroup().getItem(aName));
	}
	
	@Override
	public ItemControllerCreation getItemController()
	{
		return mItemController;
	}
	
	@Override
	public ItemGroup getItemGroup()
	{
		return mItemGroup;
	}
	
	@Override
	public List<ItemCreation> getItemsList()
	{
		return mItemsList;
	}
	
	@Override
	public int getItemValue(final String aName)
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to get the value of an item inside a non value group.");
			return 0;
		}
		return getItem(getItemGroup().getItem(aName)).getValue();
	}
	
	@Override
	public String getName()
	{
		return getItemGroup().getName();
	}
	
	@Override
	public PointHandler getPoints()
	{
		return mPoints;
	}
	
	@Override
	public int getTempPoints()
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to get the temp points of a non value group.");
			return 0;
		}
		int tempPoints = 0;
		for (final ItemCreation item : getItemsList())
		{
			tempPoints += item.getTempPoints();
		}
		return tempPoints;
	}
	
	@Override
	public int getValue()
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to get the value of a non value group.");
			return 0;
		}
		int value = 0;
		for (final ItemCreation item : getItemsList())
		{
			value += item.getAllValues();
		}
		return value;
	}
	
	@Override
	public boolean hasItem(final Item aItem)
	{
		return mItems.containsKey(aItem);
	}
	
	@Override
	public boolean hasItem(final ItemCreation aItem)
	{
		return mItems.containsKey(aItem.getItem());
	}
	
	@Override
	public boolean hasItem(final String aName)
	{
		if (getItemGroup().hasItem(aName))
		{
			return hasItem(getItemGroup().getItem(aName));
		}
		return false;
	}
	
	@Override
	public int indexOfItem(final ItemCreation aItem)
	{
		return getItemsList().indexOf(aItem);
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			getContainer().setLayoutParams(ViewUtil.getWrapHeight());
			getContainer().setOrientation(LinearLayout.VERTICAL);
			
			mTitleText.setLayoutParams(ViewUtil.getWrapHeight());
			mTitleText.setText(getItemGroup().getDisplayName());
			mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			mTitleText.setGravity(Gravity.CENTER);
		}
		getContainer().addView(mTitleText);
		
		if (getCreationMode().canAddItem(this))
		{
			mAddButton.setLayoutParams(ViewUtil.getWrapHeight());
			if ( !mInitialized)
			{
				mAddButton.setText(R.string.add);
				mAddButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						addItem();
					}
				});
			}
			updateAddButton();
			getContainer().addView(mAddButton);
		}
		
		if ( !hasOrder())
		{
			sortItems();
		}
		else
		{
			for (final ItemCreation item : getItemsList())
			{
				item.init();
				getContainer().addView(item.getContainer());
			}
		}
		
		if ( !mInitialized)
		{
			mInitialized = true;
		}
	}
	
	@Override
	public boolean isMutable()
	{
		if (getCreationMode().isFreeMode())
		{
			return getItemGroup().isMutable() || getItemGroup().isFreeMutable();
		}
		return getItemGroup().isMutable();
	}
	
	@Override
	public boolean isValueGroup()
	{
		return getItemGroup().isValueGroup();
	}
	
	@Override
	public void release()
	{
		for (final ItemCreation item : getItemsList())
		{
			item.release();
		}
		ViewUtil.release(getContainer());
		ViewUtil.release(mTitleText);
		ViewUtil.release(mAddButton);
	}
	
	@Override
	public void removeItem(final Item aItem)
	{
		if ( !isMutable())
		{
			Log.w(TAG, "Tried to change a non mutable group.");
			return;
		}
		if (getMinValue(CreationRestrictionType.GROUP_CHILDREN_COUNT) >= getItemsList().size())
		{
			return;
		}
		if (mItems.containsKey(aItem))
		{
			final ItemCreation item = mItems.get(aItem);
			getItemController().removeItemName(aItem.getName());
			item.clear();
			mItems.remove(aItem);
			getItemsList().remove(item);
		}
		getItemController().resize();
		updateController();
	}
	
	@Override
	public void resetTempPoints()
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to reset the temp points of a non value group.");
			return;
		}
		for (final ItemCreation item : getItemsList())
		{
			item.resetTempPoints();
		}
	}
	
	@Override
	public void setCreationMode(final CreationMode aMode)
	{
		mMode = aMode;
		for (final ItemCreation item : getItemsList())
		{
			item.setCreationMode(aMode);
		}
	}
	
	@Override
	public void setItemAt(final int aIndex, final Item aItem)
	{
		if ( !isMutable())
		{
			Log.w(TAG, "Tried to change a non mutable group.");
			return;
		}
		final ItemCreation oldItem = getItemsList().get(aIndex);
		if (oldItem.getItem().equals(aItem))
		{
			return;
		}
		if (mItems.containsKey(aItem))
		{
			removeItem(oldItem.getItem());
			return;
		}
		final ItemCreation item = new ItemCreationImpl(aItem, getContext(), this, getCreationMode(), getPoints(), null);
		getItemController().removeItemName(oldItem.getName());
		oldItem.clear();
		mItems.remove(oldItem.getItem());
		getItemsList().set(aIndex, item);
		mItems.put(aItem, item);
		getContainer().addView(item.getContainer(), aIndex + 1 + (isMutable() ? 1 : 0));
		getItemController().addItemName(item);
		updateController();
	}
	
	@Override
	public void setPoints(final PointHandler aPoints)
	{
		mPoints = aPoints;
		for (final ItemCreation item : getItemsList())
		{
			item.setPoints(mPoints);
		}
	}
	
	@Override
	public String toString()
	{
		return getItemGroup().getDisplayName() + ": " + getValue();
	}
	
	@Override
	public void updateAddButton()
	{
		if (isMutable())
		{
			mAddButton.setEnabled( !getAddableItems().isEmpty() && getItemsList().size() < getMaxValue(CreationRestrictionType.GROUP_CHILDREN_COUNT)
					&& getItemsList().size() < getItemGroup().getMaxItems());
		}
	}
	
	@Override
	public void updateController()
	{
		getItemController().updateGroups();
	}
	
	@Override
	public void updateItems()
	{
		for (final ItemCreation item : getItemsList())
		{
			if (item.isValueItem() || item.isParent())
			{
				item.updateButtons();
			}
		}
		updateAddButton();
	}
	
	@Override
	public void updateRestrictions()
	{
		final Set<ItemCreation> removableItems = new HashSet<ItemCreation>();
		for (final ItemCreation item : getItemsList())
		{
			if ( !isItemOk(item.getItem()))
			{
				removableItems.add(item);
			}
		}
		for (final ItemCreation item : removableItems)
		{
			removeItemSilent(item);
		}
		if ( !isMutable())
		{
			for (final Item item : getItemGroup().getItemsList())
			{
				if ( !hasItem(item) && isItemOk(item))
				{
					addItemSilent(new ItemCreationImpl(item, getContext(), this, getCreationMode(), getPoints(), null));
				}
			}
		}
		updateAddButton();
	}
	
	private void addItemSilent(final ItemCreation aItem)
	{
		if (getItemsList().contains(aItem))
		{
			Log.w(TAG, "Tried to add a child to a group twice.");
			return;
		}
		getItemsList().add(aItem);
		mItems.put(aItem.getItem(), aItem);
		getContainer().addView(aItem.getContainer());
		getItemController().addItemName(aItem);
		getItemController().resize();
		updateController();
	}
	
	private void sortItems()
	{
		for (final ItemCreation item : getItemsList())
		{
			item.release();
		}
		Collections.sort(getItemsList());
		for (final ItemCreation item : getItemsList())
		{
			item.init();
			getContainer().addView(item.getContainer());
		}
	}
	
	private List<Item> getAddableItems()
	{
		final List<Item> items = new ArrayList<Item>();
		for (final Item item : getItemGroup().getItemsList())
		{
			if ( !hasItem(item) && isItemOk(item))
			{
				items.add(item);
			}
		}
		return items;
	}
	
	private boolean isItemOk(final Item aItem)
	{
		for (final CreationRestriction restriction : getRestrictions(CreationRestrictionType.GROUP_CHILDREN))
		{
			if ( !restriction.getItems().contains(aItem.getName()))
			{
				return false;
			}
		}
		return true;
	}
	
	private void removeItemSilent(final ItemCreation aItem)
	{
		if ( !getItemsList().contains(aItem))
		{
			Log.w(TAG, "Tried to remove a non added item.");
			return;
		}
		getItemController().removeItemName(aItem.getName());
		aItem.clear();
		mItems.remove(aItem.getItem());
		getItemsList().remove(aItem);
		getItemController().resize();
		updateController();
	}
}
