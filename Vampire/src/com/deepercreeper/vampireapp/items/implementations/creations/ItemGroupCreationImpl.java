package com.deepercreeper.vampireapp.items.implementations.creations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.implementations.creations.dependencies.DependencyCreationImpl;
import com.deepercreeper.vampireapp.items.implementations.creations.dependencies.RestrictionableDependableCreationImpl;
import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.dialogs.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.listeners.ItemSelectionListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A group creation implementation.
 * 
 * @author vrl
 */
public class ItemGroupCreationImpl extends RestrictionableDependableCreationImpl implements ItemGroupCreation
{
	private static final String TAG = "ItemGroupCreation";
	
	private final ItemGroup mItemGroup;
	
	private final LinearLayout mContainer;
	
	private final Context mContext;
	
	private final ItemControllerCreation mItemController;
	
	private final List<ItemCreation> mItemsList = new ArrayList<ItemCreation>();
	
	private final Map<Item, ItemCreation> mItems = new HashMap<Item, ItemCreation>();
	
	private final LinearLayout mItemsContainer;
	
	private final Button mAddButton;
	
	private final CharacterCreation mChar;
	
	/**
	 * Creates a new group creation.
	 * 
	 * @param aGroup
	 *            The group type.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The item controller.
	 * @param aChar
	 *            The parent character.
	 */
	public ItemGroupCreationImpl(final ItemGroup aGroup, final Context aContext, final ItemControllerCreation aController,
			final CharacterCreation aChar)
	{
		super(aController);
		mItemGroup = aGroup;
		mContext = aContext;
		mItemController = aController;
		mChar = aChar;
		mContainer = (LinearLayout) View.inflate(getContext(), R.layout.view_item_group, null);
		mAddButton = (Button) getContainer().findViewById(R.id.view_item_group_add_button);
		mItemsContainer = (LinearLayout) getContainer().findViewById(R.id.view_item_group_items_list);
		
		for (final Dependency dependency : getItemGroup().getDependencies())
		{
			addDependency(new DependencyCreationImpl(dependency, aChar));
		}
		
		((TextView) getContainer().findViewById(R.id.view_item_group_name_label)).setText(getItemGroup().getDisplayName());
		mAddButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				addItem();
			}
		});
		
		if ( !isMutable())
		{
			for (final Item item : getItemGroup().getItemsList())
			{
				if (isItemOk(item))
				{
					addItemSilent(new ItemCreationImpl(item, getContext(), this, mChar, null));
				}
			}
			if ( !hasOrder())
			{
				sortItems();
			}
		}
		
		updateUI();
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
		final ItemSelectionListener<Item> action = new ItemSelectionListener<Item>()
		{
			@Override
			public void cancel()
			{}
			
			@Override
			public void select(final Item aChoosenItem)
			{
				addItem(aChoosenItem);
			}
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
		final ItemCreation item = new ItemCreationImpl(aItem, getContext(), this, mChar, null);
		getItemsList().add(item);
		mItems.put(aItem, item);
		mItemsContainer.addView(item.getContainer());
		getItemController().resize();
		getItemController().addItem(item);
		updateControllerUI();
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
		updateUI();
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
		final ItemSelectionListener<Item> action = new ItemSelectionListener<Item>()
		{
			@Override
			public void cancel()
			{}
			
			@Override
			public void select(final Item aChoosenItem)
			{
				setItemAt(index, aChoosenItem);
			}
		};
		SelectItemDialog.showSelectionDialog(items, getContext().getString(R.string.edit_item) + " " + aItem.getName(), getContext(), action);
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
	public int getMaxValue()
	{
		int maxValue = getItemGroup().getMaxValue();
		if (hasDependency(Type.MAX_VALUE))
		{
			maxValue = getDependency(Type.MAX_VALUE).getValue(maxValue);
		}
		return maxValue;
	}
	
	@Override
	public String getName()
	{
		return getItemGroup().getName();
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
	public int hashCode()
	{
		return getItemGroup().hashCode();
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
	public boolean hasOrder()
	{
		return getItemGroup().hasOrder();
	}
	
	@Override
	public int indexOfItem(final ItemCreation aItem)
	{
		return getItemsList().indexOf(aItem);
	}
	
	@Override
	public boolean isMutable()
	{
		if (mChar.getMode().isFreeMode())
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
		ViewUtil.release(getContainer());
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
			getItemController().removeItem(aItem.getName());
			item.clear();
			mItems.remove(aItem);
			getItemsList().remove(item);
			getItemController().resize();
			updateControllerUI();
		}
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
		final ItemCreation item = new ItemCreationImpl(aItem, getContext(), this, mChar, null);
		getItemController().removeItem(oldItem.getName());
		oldItem.clear();
		mItems.remove(oldItem.getItem());
		getItemsList().set(aIndex, item);
		mItems.put(aItem, item);
		mItemsContainer.addView(item.getContainer(), aIndex);
		getItemController().addItem(item);
		updateControllerUI();
	}
	
	@Override
	public String toString()
	{
		return getItemGroup().getDisplayName() + ": " + getValue();
	}
	
	@Override
	public void updateControllerUI()
	{
		mChar.updateUI();
	}
	
	@Override
	public void updateUI()
	{
		if (hasRestrictions(CreationRestrictionType.GROUP_CHILDREN))
		{
			final Set<ItemCreation> prohibitedItems = new HashSet<ItemCreation>();
			for (final ItemCreation item : getItemsList())
			{
				if ( !isItemOk(item.getItem()))
				{
					prohibitedItems.add(item);
				}
			}
			for (final ItemCreation item : prohibitedItems)
			{
				removeItemSilent(item);
			}
			if ( !isMutable())
			{
				for (final Item item : getItemGroup().getItemsList())
				{
					if ( !hasItem(item) && isItemOk(item))
					{
						addItemSilent(new ItemCreationImpl(item, getContext(), this, mChar, null));
					}
				}
			}
		}
		
		if (mChar.getMode().canAddItem(this))
		{
			ViewUtil.wrapHeight(mAddButton);
			ViewUtil.setEnabled(mAddButton, canAddItem());
		}
		else
		{
			ViewUtil.hideHeight(mAddButton);
		}
		if ( !hasOrder())
		{
			sortItems();
		}
		
		for (final ItemCreation item : getItemsList())
		{
			item.updateUI();
		}
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
		mItemsContainer.addView(aItem.getContainer());
		getItemController().addItem(aItem);
		getItemController().resize();
		updateControllerUI();
	}
	
	private boolean canAddItem()
	{
		if ( !mChar.getMode().canAddItem(this))
		{
			return false;
		}
		if (getAddableItems().isEmpty())
		{
			return false;
		}
		if (getItemsList().size() >= getMaxValue(CreationRestrictionType.GROUP_CHILDREN_COUNT))
		{
			return false;
		}
		if (getItemsList().size() >= getItemGroup().getMaxItems())
		{
			return false;
		}
		return true;
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
		for (final RestrictionCreation restriction : getRestrictions(CreationRestrictionType.GROUP_CHILDREN))
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
		getItemController().removeItem(aItem.getName());
		aItem.clear();
		mItems.remove(aItem.getItem());
		getItemsList().remove(aItem);
		getItemController().resize();
		updateControllerUI();
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
			mItemsContainer.addView(item.getContainer());
		}
	}
}
