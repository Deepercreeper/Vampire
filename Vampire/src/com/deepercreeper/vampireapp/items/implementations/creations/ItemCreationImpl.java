package com.deepercreeper.vampireapp.items.implementations.creations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.CharacterCreation;
import com.deepercreeper.vampireapp.items.implementations.creations.dependencies.DependencyCreationImpl;
import com.deepercreeper.vampireapp.items.implementations.creations.dependencies.RestrictionableDependableCreationImpl;
import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.RestrictionCreation.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.dialogs.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.listeners.ItemSelectionListener;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An item creation implementation.
 * 
 * @author vrl
 */
public class ItemCreationImpl extends RestrictionableDependableCreationImpl implements ItemCreation
{
	private static final String TAG = "ItemCreation";
	
	private static final int VALUE_MULTIPLICATOR = 20;
	
	private final Item mItem;
	
	private final Context mContext;
	
	private final ItemGroupCreation mItemGroup;
	
	private final ValueAnimator mAnimator;
	
	private final ImageButton mIncreaseButton;
	
	private final ImageButton mDecreaseButton;
	
	private final ProgressBar mValueBar;
	
	private final TextView mValueText;
	
	private final LinearLayout mContainer;
	
	private final List<ItemCreation> mChildrenList;
	
	private final Map<String, ItemCreation> mChildren;
	
	private final ItemCreation mParentItem;
	
	private final CharacterCreation mChar;
	
	private final LinearLayout mChildrenContainer;
	
	private final ImageButton mAddButton;
	
	private final ImageButton mEditButton;
	
	private final ImageButton mRemoveButton;
	
	private final TextView mNameText;
	
	private final int mButtonWidth;
	
	private String mDescription;
	
	private int mValueId;
	
	private int mTempPoints;
	
	/**
	 * Creates a new item creation.
	 * 
	 * @param aItem
	 *            The item type.
	 * @param aContext
	 *            The underlying context.
	 * @param aGroup
	 *            The parent item group.
	 * @param aChar
	 *            The parent character.
	 * @param aParentItem
	 *            The parent item.
	 */
	public ItemCreationImpl(final Item aItem, final Context aContext, final ItemGroupCreation aGroup, final CharacterCreation aChar,
			final ItemCreation aParentItem)
	{
		super(aGroup.getItemController());
		mItem = aItem;
		mContext = aContext;
		mButtonWidth = (int) getContext().getResources().getDimension(R.dimen.button_width);
		mItemGroup = aGroup;
		mChar = aChar;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_item_creation, null);
		if (getItem().hasParentItem() && aParentItem == null || !getItem().hasParentItem() && aParentItem != null)
		{
			Log.e(TAG, "Tried to create an item with different parent item state and parent item.");
		}
		if (isValueItem())
		{
			mAnimator = new ValueAnimator();
			mAnimator.addUpdateListener(this);
			mValueId = getStartValue();
		}
		else
		{
			mAnimator = null;
		}
		if (isParent())
		{
			mChildrenList = new ArrayList<ItemCreation>();
			mChildren = new HashMap<String, ItemCreation>();
		}
		else
		{
			mChildrenList = null;
			mChildren = null;
		}
		mParentItem = aParentItem;
		mEditButton = (ImageButton) getContainer().findViewById(R.id.view_edit_item_creation_button);
		mRemoveButton = (ImageButton) getContainer().findViewById(R.id.view_remove_item_creation_button);
		mNameText = (TextView) getContainer().findViewById(R.id.view_item_creation_name_label);
		mDecreaseButton = (ImageButton) getContainer().findViewById(R.id.view_decrease_item_creation_button);
		mValueText = (TextView) getContainer().findViewById(R.id.view_item_creation_value_text);
		mValueBar = (ProgressBar) getContainer().findViewById(R.id.view_item_creation_value_bar);
		mIncreaseButton = (ImageButton) getContainer().findViewById(R.id.view_increase_item_creation_button);
		mAddButton = (ImageButton) getContainer().findViewById(R.id.view_add_item_creation_child_button);
		mChildrenContainer = (LinearLayout) getContainer().findViewById(R.id.view_item_creation_children_list);
		
		for (final Dependency dependency : getItem().getDependencies())
		{
			addDependency(new DependencyCreationImpl(dependency, aChar));
		}
		
		mNameText.setText(getItem().getDisplayName());
		mNameText.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Toast.makeText(getContext(), getItem().getDescription(), Toast.LENGTH_LONG).show();
			}
		});
		
		mEditButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				if (hasParentItem())
				{
					if (mChar.getMode().canRemoveChild(ItemCreationImpl.this))
					{
						getParentItem().editChild(getItem());
					}
				}
				else
				{
					if (mChar.getMode().canRemoveItem(ItemCreationImpl.this))
					{
						getItemGroup().editItem(getItem());
					}
				}
			}
		});
		mRemoveButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				if (hasParentItem())
				{
					if (mChar.getMode().canRemoveChild(ItemCreationImpl.this))
					{
						getParentItem().removeChild(getItem());
					}
				}
				else
				{
					if (mChar.getMode().canRemoveItem(ItemCreationImpl.this))
					{
						getItemGroup().removeItem(getItem());
					}
				}
			}
		});
		mAddButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				addChild();
			}
		});
		mDecreaseButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				decrease();
			}
		});
		
		mIncreaseButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				increase();
			}
		});
		
		if (isParent() && !isMutableParent())
		{
			for (final Item item : getItem().getChildrenList())
			{
				addChildSilent(new ItemCreationImpl(item, getContext(), getItemGroup(), mChar, this));
			}
			if ( !hasOrder())
			{
				sortChildren();
			}
		}
	}
	
	@Override
	public void addChild()
	{
		if ( !isMutableParent())
		{
			Log.w(TAG, "Tried to add a child to a non mutable item.");
			return;
		}
		if (SelectItemDialog.isDialogOpen())
		{
			return;
		}
		final List<Item> items = getAddableItems();
		if (items.isEmpty() || getMaxValue(CreationRestrictionType.ITEM_CHILDREN_COUNT) <= getChildrenList().size())
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
				addChild(aChoosenItem);
			}
		};
		SelectItemDialog.showSelectionDialog(items, getContext().getString(R.string.add_item), getContext(), action);
	}
	
	@Override
	public void addChild(final Item aItem)
	{
		if ( !isMutableParent())
		{
			Log.w(TAG, "Tried to add a child to a non parent or non mutable item.");
			return;
		}
		if (getChildrenList().contains(aItem))
		{
			Log.w(TAG, "Tried to add a already existing child.");
			return;
		}
		if (getMaxValue(CreationRestrictionType.ITEM_CHILDREN_COUNT) <= getChildrenList().size())
		{
			return;
		}
		final ItemCreation item = new ItemCreationImpl(aItem, getContext(), getItemGroup(), mChar, this);
		getChildrenList().add(item);
		mChildren.put(item.getName(), item);
		mChildrenContainer.addView(item.getContainer());
		getItemGroup().getItemController().addItem(item);
		getItemGroup().getItemController().resize();
		updateControllerUI();
	}
	
	@Override
	public boolean canDecrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to ask whether a non value item can be decreased.");
			return false;
		}
		boolean canDecreaseItemValue = mValueId > 0;
		if ( !mChar.getMode().isFreeMode())
		{
			canDecreaseItemValue &= mValueId > getMinValue(CreationRestrictionType.ITEM_VALUE) && mValueId > getStartValue();
		}
		final boolean canDecreaseItemTempPoints = mTempPoints > 0;
		boolean canDecreaseChild = true;
		
		if (hasParentItem())
		{
			for (final RestrictionCreation restriction : getParentItem().getRestrictions(CreationRestrictionType.ITEM_CHILD_VALUE_AT))
			{
				if (getParentItem().indexOfChild(this) == restriction.getIndex())
				{
					if (restriction.isActive(getItemGroup().getItemController()) && restriction.getMinimum() >= getValue())
					{
						canDecreaseChild = false;
						break;
					}
				}
			}
		}
		else
		{
			for (final RestrictionCreation restriction : getItemGroup().getRestrictions(CreationRestrictionType.GROUP_ITEM_VALUE_AT))
			{
				if (getItemGroup().indexOfItem(this) == restriction.getIndex())
				{
					if (restriction.isActive(getItemGroup().getItemController()) && restriction.getMinimum() >= getValue())
					{
						canDecreaseChild = false;
						break;
					}
				}
			}
		}
		
		return mChar.getMode().canDecreaseItem(this, canDecreaseItemValue && canDecreaseChild, canDecreaseItemTempPoints && canDecreaseChild);
	}
	
	@Override
	public boolean canIncrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to ask whether a non value item can be increased.");
			return false;
		}
		boolean canIncreaseItem = mValueId + mTempPoints < getMaxValue();
		if ( !mChar.getMode().isFreeMode())
		{
			canIncreaseItem &= mValueId + mTempPoints < getMaxValue(CreationRestrictionType.ITEM_VALUE)
					&& ( !mChar.isLowLevel() || mValueId + mTempPoints < getItem().getMaxLowLevelValue());
		}
		boolean canIncreaseChild = true;
		
		if (hasParentItem())
		{
			for (final RestrictionCreation restriction : getParentItem().getRestrictions(CreationRestrictionType.ITEM_CHILD_VALUE_AT))
			{
				if (getParentItem().indexOfChild(this) == restriction.getIndex())
				{
					if (restriction.isActive(getItemGroup().getItemController()) && restriction.getMaximum() <= getValue())
					{
						canIncreaseChild = false;
						break;
					}
				}
			}
		}
		else
		{
			for (final RestrictionCreation restriction : getItemGroup().getRestrictions(CreationRestrictionType.GROUP_ITEM_VALUE_AT))
			{
				if (getItemGroup().indexOfItem(this) == restriction.getIndex())
				{
					if (restriction.isActive(getItemGroup().getItemController()) && restriction.getMaximum() <= getValue())
					{
						canIncreaseChild = false;
						break;
					}
				}
			}
		}
		
		return mChar.getMode().canIncreaseItem(this, canIncreaseItem && canIncreaseChild);
	}
	
	@Override
	public void clear()
	{
		if (getItem().isValueItem())
		{
			mValueId = getStartValue();
			resetTempPoints();
		}
		release();
	}
	
	@Override
	public int compareTo(final ItemCreation aAnother)
	{
		if (aAnother == null)
		{
			return getItem().compareTo(null);
		}
		return getItem().compareTo(aAnother.getItem());
	}
	
	@Override
	public void decrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to decrease a non value item.");
			return;
		}
		if ( !canDecrease())
		{
			return;
		}
		if (mChar.getMode().isValueMode())
		{
			mValueId-- ;
		}
		else if (mChar.getMode().isTempPointsMode())
		{
			mTempPoints-- ;
			getCharacter().increaseFreePoints(getFreePointsCost());
		}
		updateControllerUI();
	}
	
	@Override
	public void editChild(final Item aItem)
	{
		if ( !isMutableParent())
		{
			Log.w(TAG, "Tried to edit a child of a non mutable parent item.");
			return;
		}
		if (SelectItemDialog.isDialogOpen())
		{
			return;
		}
		final int index = getChildrenList().indexOf(mChildren.get(aItem.getName()));
		final List<Item> children = new ArrayList<Item>();
		for (final Item item : getItem().getChildrenList())
		{
			if ( !hasChild(item))
			{
				children.add(item);
			}
		}
		if (children.isEmpty())
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
				setChildAt(index, aChoosenItem);
			}
		};
		SelectItemDialog.showSelectionDialog(children, getContext().getString(R.string.edit_item) + aItem.getName(), getContext(), action);
	}
	
	@Override
	public boolean equals(final Object aO)
	{
		if (aO instanceof ItemCreation)
		{
			final ItemCreation item = (ItemCreation) aO;
			return getItem().equals(item.getItem());
		}
		return false;
	}
	
	@Override
	public int getAbsoluteValue()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to get the absolute value of a non value item.");
			return 0;
		}
		return Math.abs(getValue());
	}
	
	@Override
	public int getAllTempPoints()
	{
		int tempPoints = mTempPoints;
		for (final ItemCreation item : getChildrenList())
		{
			tempPoints += item.getAllTempPoints();
		}
		return tempPoints;
	}
	
	@Override
	public int getAllValues()
	{
		int values = 0;
		if (isValueItem())
		{
			values += getValue();
		}
		if (isParent())
		{
			for (final ItemCreation item : getChildrenList())
			{
				values += item.getValue();
			}
		}
		return values;
	}
	
	@Override
	public CharacterCreation getCharacter()
	{
		return mChar;
	}
	
	@Override
	public ItemCreation getChildAt(final int aIndex)
	{
		if ( !getItem().isParent())
		{
			Log.w(TAG, "Tried to get a child of a non parent item.");
			return null;
		}
		return getChildrenList().get(aIndex);
	}
	
	@Override
	public List<ItemCreation> getChildrenList()
	{
		if ( !getItem().isParent())
		{
			Log.w(TAG, "Tried to get the children of a non parent item.");
			return null;
		}
		return mChildrenList;
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
	public int getDecreasedValue()
	{
		if ( !getItem().isValueItem())
		{
			Log.w(TAG, "Tried to get the decreased value of a non value item.");
			return 0;
		}
		return getValues()[Math.max(mValueId + mTempPoints - 1, 0)];
	}
	
	@Override
	public String getDescription()
	{
		return mDescription;
	}
	
	@Override
	public List<ItemCreation> getDescriptionItems()
	{
		if ( !isParent())
		{
			Log.w(TAG, "Tried to get the description items of a non parent item.");
			return null;
		}
		final List<ItemCreation> items = new ArrayList<ItemCreation>();
		for (final ItemCreation child : getChildrenList())
		{
			if (child.needsDescription() && ( !child.isValueItem() || child.getValue() != 0))
			{
				items.add(child);
			}
			if (child.isParent())
			{
				items.addAll(child.getDescriptionItems());
			}
		}
		return items;
	}
	
	@Override
	public int getFreePointsCost()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to get the free points cost of a non value item.");
			return 0;
		}
		return getItem().getFreePointsCost();
	}
	
	@Override
	public int getIncreasedValue()
	{
		if ( !getItem().isValueItem())
		{
			Log.w(TAG, "Tried to get the increased value of a non value item.");
			return 0;
		}
		return getValues()[Math.min(getMaxValue(), mValueId + mTempPoints + 1)];
	}
	
	@Override
	public Item getItem()
	{
		return mItem;
	}
	
	@Override
	public ItemGroupCreation getItemGroup()
	{
		return mItemGroup;
	}
	
	@Override
	public int getMaxValue()
	{
		int maxValue = getValues()[getItem().getMaxValue()];
		if (hasDependency(Type.MAX_VALUE))
		{
			final int value = getDependency(Type.MAX_VALUE).getValue(maxValue);
			if (getValues().length > value)
			{
				maxValue = getValues()[value];
			}
		}
		return maxValue;
	}
	
	@Override
	public String getName()
	{
		return getItem().getName();
	}
	
	@Override
	public ItemCreation getParentItem()
	{
		return mParentItem;
	}
	
	@Override
	public int getStartValue()
	{
		int startValue = getItemGroup().getStartValue();
		if (getItem().hasStartValue())
		{
			startValue = getItem().getStartValue();
		}
		if (hasDependency(Type.START_VALUE))
		{
			startValue = getDependency(Type.START_VALUE).getValue(startValue);
		}
		return startValue;
	}
	
	@Override
	public int getTempPoints()
	{
		if (isParent())
		{
			int tempPoints = 0;
			if (isValueItem())
			{
				tempPoints += mTempPoints;
			}
			for (final ItemCreation item : getChildrenList())
			{
				tempPoints += item.getTempPoints();
			}
			return tempPoints;
		}
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to get the temp points of a non parent and value item.");
			return 0;
		}
		return mTempPoints;
	}
	
	@Override
	public int getValue()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to get the value of a non value item.");
			return 0;
		}
		final int[] values = getValues();
		if (mValueId + mTempPoints >= values.length)
		{
			if (mTempPoints >= values.length)
			{
				resetTempPoints();
			}
			mValueId = values.length - mTempPoints - 1;
		}
		return values[mValueId + mTempPoints];
	}
	
	@Override
	public int getValueId()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to get the value id of a non valule item.");
			return 0;
		}
		return mValueId;
	}
	
	@Override
	public int[] getValues()
	{
		int[] values = getItem().getValues();
		if (hasDependency(Type.VALUES))
		{
			values = getDependency(Type.VALUES).getValues(values);
		}
		return values;
	}
	
	@Override
	public boolean hasChild(final Item aItem)
	{
		if ( !isParent())
		{
			Log.w(TAG, "Tried to get whether  non parent item has a child.");
			return false;
		}
		return mChildren.containsKey(aItem.getName());
	}
	
	@Override
	public boolean hasChildAt(final int aIndex)
	{
		return getChildrenList().size() > aIndex;
	}
	
	@Override
	public boolean hasChildren()
	{
		return isParent() && !getChildrenList().isEmpty();
	}
	
	@Override
	public boolean hasEnoughPoints()
	{
		return getItem().getItemGroup().getFreePointsCost() <= mChar.getFreePoints();
	}
	
	@Override
	public int hashCode()
	{
		return getItem().hashCode();
	}
	
	@Override
	public boolean hasOrder()
	{
		return getItem().hasOrder();
	}
	
	@Override
	public boolean hasParentItem()
	{
		return getItem().hasParentItem();
	}
	
	@Override
	public void increase()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to increase a non value item.");
			return;
		}
		if ( !canIncrease())
		{
			return;
		}
		if (getCharacter().getMode().isValueMode())
		{
			mValueId++ ;
		}
		else if (getCharacter().getMode().isTempPointsMode())
		{
			mTempPoints++ ;
			getCharacter().decreaseFreePoints(getFreePointsCost());
		}
		updateControllerUI();
	}
	
	@Override
	public int indexOfChild(final ItemCreation aItem)
	{
		return getChildrenList().indexOf(aItem);
	}
	
	@Override
	public boolean isImportant()
	{
		if (isValueItem() && getValue() != 0)
		{
			return true;
		}
		if ( !isValueItem())
		{
			return true;
		}
		if (hasChildren())
		{
			if ( !isMutableParent())
			{
				return true;
			}
			for (final ItemCreation item : getChildrenList())
			{
				if (item.isImportant())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean isMutableParent()
	{
		return getItem().isMutableParent();
	}
	
	@Override
	public boolean isParent()
	{
		return getItem().isParent();
	}
	
	@Override
	public boolean isValueItem()
	{
		return getItem().isValueItem();
	}
	
	@Override
	public boolean needsDescription()
	{
		return getItem().needsDescription();
	}
	
	@Override
	public void onAnimationUpdate(final ValueAnimator aAnimation)
	{
		mValueBar.setProgress((Integer) aAnimation.getAnimatedValue());
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void removeChild(final Item aItem)
	{
		if ( !isMutableParent())
		{
			Log.w(TAG, "Tried to remove a child from a non parent or non mutable item.");
			return;
		}
		if (getMinValue(CreationRestrictionType.ITEM_CHILDREN_COUNT) >= getChildrenList().size())
		{
			return;
		}
		final ItemCreation item = mChildren.get(aItem.getName());
		item.clear();
		getChildrenList().remove(item);
		mChildren.remove(item.getName());
		getItemGroup().getItemController().removeItem(aItem.getName());
		getItemGroup().getItemController().resize();
		updateControllerUI();
	}
	
	@Override
	public void resetTempPoints()
	{
		if ( !isValueItem() && !isParent())
		{
			Log.w(TAG, "Tried to reset the temp points of a non value and non parent item.");
			return;
		}
		if (isValueItem())
		{
			mTempPoints = 0;
		}
		updateUI();
		if (isParent())
		{
			for (final ItemCreation item : getChildrenList())
			{
				item.resetTempPoints();
			}
		}
	}
	
	@Override
	public void setChildAt(final int aIndex, final Item aItem)
	{
		if ( !isMutableParent())
		{
			Log.w(TAG, "Tried to set a child for a non parent or non mutable item.");
			return;
		}
		
		final ItemCreation oldItem = getChildrenList().get(aIndex);
		if (oldItem.getItem().equals(aItem))
		{
			return;
		}
		if (mChildren.containsKey(aItem.getName()))
		{
			removeChild(oldItem.getItem());
			return;
		}
		getItemGroup().getItemController().removeItem(oldItem.getName());
		oldItem.clear();
		mChildren.remove(oldItem.getName());
		final ItemCreation item = new ItemCreationImpl(aItem, getContext(), getItemGroup(), mChar, this);
		getChildrenList().set(aIndex, item);
		mChildren.put(aItem.getName(), item);
		mChildrenContainer.addView(item.getContainer(), aIndex + 1);
		getItemGroup().getItemController().addItem(item);
		getItemGroup().getItemController().resize();
		updateControllerUI();
	}
	
	@Override
	public void setDecreasable()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to change whether a non value item can be decreased.");
			return;
		}
		ViewUtil.setEnabled(mDecreaseButton, canDecrease());
	}
	
	@Override
	public void setDescription(final String aDescription)
	{
		mDescription = aDescription;
	}
	
	@Override
	public void setIncreasable()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to change whether a non value item can be increased.");
			return;
		}
		ViewUtil.setEnabled(mIncreaseButton, canIncrease());
	}
	
	@Override
	public String toString()
	{
		return getName() + ": " + getValue();
	}
	
	@Override
	public void updateControllerUI()
	{
		mChar.updateUI();
	}
	
	@Override
	public void updateUI()
	{
		if (isValueItem())
		{
			if (hasRestrictions() && !isValueOk(getValue(), CreationRestrictionType.ITEM_VALUE))
			{
				while (canIncrease() && getValue() < getMinValue(CreationRestrictionType.ITEM_VALUE))
				{
					increase();
				}
				while (canDecrease() && getValue() > getMaxValue(CreationRestrictionType.ITEM_VALUE))
				{
					decrease();
				}
			}
			if ((getCharacter().getMode().isValueMode() || getCharacter().getMode().isTempPointsMode()) && !getCharacter().getMode().isFreeMode())
			{
				while (canIncrease() && getValue() < getStartValue())
				{
					increase();
				}
			}
			while (canDecrease() && getValue() > getMaxValue())
			{
				decrease();
			}
		}
		
		if (hasParentItem() && getParentItem().isMutableParent() || !hasParentItem() && getItemGroup().isMutable())
		{
			ViewUtil.setPxWidth(mEditButton, mButtonWidth);
			ViewUtil.setPxWidth(mRemoveButton, mButtonWidth);
			if (hasParentItem())
			{
				ViewUtil.setEnabled(mRemoveButton, mChar.getMode().canRemoveChild(this));
			}
			else
			{
				ViewUtil.setEnabled(mRemoveButton, mChar.getMode().canRemoveItem(this));
			}
			ViewUtil.setEnabled(mEditButton, mChar.getMode().canEditItem(this));
		}
		else
		{
			ViewUtil.hideWidth(mEditButton);
			ViewUtil.hideWidth(mRemoveButton);
		}
		if (isParent() && isMutableParent())
		{
			ViewUtil.setPxWidth(mAddButton, mButtonWidth);
			ViewUtil.setEnabled(mAddButton, mChar.getMode().canAddChild(this, true) && !getAddableItems().isEmpty());
		}
		else
		{
			ViewUtil.hideWidth(mAddButton);
		}
		if (isValueItem())
		{
			ViewUtil.setPxWidth(mDecreaseButton, mButtonWidth);
			ViewUtil.setPxWidth(mIncreaseButton, mButtonWidth);
			ViewUtil.wrapWidth(mValueText);
			ViewUtil.matchHeight(mValueBar);
			setIncreasable();
			setDecreasable();
			if (mAnimator.isRunning())
			{
				mAnimator.cancel();
			}
			mValueBar.setMax(VALUE_MULTIPLICATOR * Math.abs(getMaxValue()));
			mAnimator.setIntValues(mValueBar.getProgress(), VALUE_MULTIPLICATOR * getAbsoluteValue());
			mAnimator.start();
			mValueText.setText("" + getValue());
		}
		else
		{
			ViewUtil.hideHeight(mValueBar);
			ViewUtil.hideWidth(mValueBar);
			ViewUtil.hideWidth(mDecreaseButton);
			ViewUtil.hideWidth(mIncreaseButton);
			ViewUtil.hideWidth(mValueText);
		}
		if (hasChildren() && !hasOrder())
		{
			sortChildren();
		}
		if (hasChildren())
		{
			for (final ItemCreation child : getChildrenList())
			{
				child.updateUI();
			}
		}
	}
	
	private void addChildSilent(final ItemCreation aItem)
	{
		if ( !isParent())
		{
			Log.w(TAG, "Tried to add a child to a non parent item.");
			return;
		}
		if (getChildrenList().contains(aItem))
		{
			Log.w(TAG, "Tried to add a child to a parent item twice.");
			return;
		}
		getChildrenList().add(aItem);
		mChildren.put(aItem.getName(), aItem);
		mChildrenContainer.addView(aItem.getContainer());
	}
	
	private List<Item> getAddableItems()
	{
		final List<Item> children = new ArrayList<Item>();
		for (final Item item : getItem().getChildrenList())
		{
			if ( !hasChild(item))
			{
				children.add(item);
			}
		}
		return children;
	}
	
	private void sortChildren()
	{
		for (final ItemCreation item : getChildrenList())
		{
			item.release();
		}
		Collections.sort(getChildrenList());
		for (final ItemCreation item : getChildrenList())
		{
			mChildrenContainer.addView(item.getContainer());
		}
	}
}