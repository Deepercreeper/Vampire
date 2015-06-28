package com.deepercreeper.vampireapp.items.implementations.creations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.creation.CreationMode;
import com.deepercreeper.vampireapp.items.implementations.creations.restrictions.CreationRestrictionableImpl;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemControllerCreation.PointHandler;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction;
import com.deepercreeper.vampireapp.items.interfaces.creations.restrictions.CreationRestriction.CreationRestrictionType;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.dialogs.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.dialogs.SelectItemDialog.SelectionListener;

/**
 * An item creation implementation.
 * 
 * @author vrl
 */
public class ItemCreationImpl extends CreationRestrictionableImpl implements ItemCreation
{
	/**
	 * The change action tells, what to change, when increasing or decreasing an item.
	 * 
	 * @author vrl
	 */
	public interface ChangeAction
	{
		/**
		 * Invoked, when the item is decreased.
		 */
		void decrease();
		
		/**
		 * Invoked, when the item is increased.
		 */
		void increase();
	}
	
	private static final String				TAG					= "ItemCreation";
	
	private static final int				VALUE_MULTIPLICATOR	= 20;
	
	private final ChangeAction				mChangeValue		= new ChangeAction()
																{
																	@Override
																	public void decrease()
																	{
																		mValueId-- ;
																	}
																	
																	@Override
																	public void increase()
																	{
																		mValueId++ ;
																	}
																};
	
	private final ChangeAction				mChangeTempPoints	= new ChangeAction()
																{
																	@Override
																	public void decrease()
																	{
																		mTempPoints-- ;
																	}
																	
																	@Override
																	public void increase()
																	{
																		mTempPoints++ ;
																	}
																};
	
	private final Item						mItem;
	
	private final Context					mContext;
	
	private final ItemGroupCreation			mItemGroup;
	
	private final ValueAnimator				mAnimator;
	
	private ImageButton						mIncreaseButton;
	
	private ImageButton						mDecreaseButton;
	
	private ProgressBar						mValueBar;
	
	private TextView						mValueText;
	
	private final LinearLayout				mContainer;
	
	private final List<ItemCreation>		mChildrenList;
	
	private final Map<String, ItemCreation>	mChildren;
	
	private final ItemCreation				mParentItem;
	
	private LinearLayout					mChildrenContainer;
	
	private ImageButton						mAddButton;
	
	private ImageButton						mEditButton;
	
	private ImageButton						mRemoveButton;
	
	private TextView						mNameText;
	
	private boolean							mInitialized		= false;
	
	private CreationMode					mMode;
	
	private PointHandler					mPoints;
	
	private String							mDescription;
	
	private int								mValueId;
	
	private int								mTempPoints;
	
	/**
	 * Creates a new item creation.
	 * 
	 * @param aItem
	 *            The item type.
	 * @param aContext
	 *            The underlying context.
	 * @param aGroup
	 *            The parent item group.
	 * @param aMode
	 *            The creation mode.
	 * @param aPoints
	 *            The points handler.
	 * @param aParentItem
	 *            The parent item.
	 */
	public ItemCreationImpl(final Item aItem, final Context aContext, final ItemGroupCreation aGroup, final CreationMode aMode,
			final PointHandler aPoints, final ItemCreation aParentItem)
	{
		super(aGroup.getItemController());
		mItem = aItem;
		mContext = aContext;
		mItemGroup = aGroup;
		mMode = aMode;
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.item_creation, null);
		if (isValueItem())
		{
			mAnimator = new ValueAnimator();
			mAnimator.addUpdateListener(this);
			mValueId = getItem().getStartValue();
			mPoints = aPoints;
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
		if (getItem().hasParentItem() && aParentItem == null || !getItem().hasParentItem() && aParentItem != null)
		{
			Log.w(TAG, "Tried to create an item with different parent item state and parent item.");
			throw new IllegalArgumentException("ItemCreation error!");
		}
		mParentItem = aParentItem;
		
		init();
		
		if (isParent() && !isMutableParent())
		{
			for (final Item item : getItem().getChildrenList())
			{
				addChildSilent(new ItemCreationImpl(item, getContext(), getItemGroup(), getCreationMode(), getItemGroup().getPoints(), this));
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
		final SelectionListener<Item> action = new SelectionListener<Item>()
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
		final ItemCreation item = new ItemCreationImpl(aItem, getContext(), getItemGroup(), getCreationMode(), getItemGroup().getPoints(), this);
		getChildrenList().add(item);
		mChildren.put(item.getName(), item);
		mChildrenContainer.addView(item.getContainer());
		getItemGroup().getItemController().addItemName(item);
		getItemGroup().getItemController().resize();
		updateController();
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
		if ( !getCreationMode().isFreeMode())
		{
			canDecreaseItemValue &= mValueId > getMinValue(CreationRestrictionType.ITEM_VALUE) && mValueId > getItem().getStartValue();
		}
		final boolean canDecreaseItemTempPoints = mTempPoints > 0;
		boolean canDecreaseChild = true;
		
		if (hasParentItem())
		{
			for (final CreationRestriction restriction : getParentItem().getRestrictions(CreationRestrictionType.ITEM_CHILD_VALUE_AT))
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
			for (final CreationRestriction restriction : getItemGroup().getRestrictions(CreationRestrictionType.GROUP_ITEM_VALUE_AT))
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
		
		return getCreationMode().canDecreaseItem(this, canDecreaseItemValue && canDecreaseChild, canDecreaseItemTempPoints && canDecreaseChild);
	}
	
	@Override
	public boolean canIncrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to ask whether a non value item can be increased.");
			return false;
		}
		boolean canIncreaseItem = mValueId + mTempPoints < getItem().getMaxValue();
		if ( !getCreationMode().isFreeMode())
		{
			canIncreaseItem &= mValueId + mTempPoints < getMaxValue(CreationRestrictionType.ITEM_VALUE)
					&& mValueId + mTempPoints < getItem().getMaxLowLevelValue();
		}
		boolean canIncreaseChild = true;
		
		if (hasParentItem())
		{
			for (final CreationRestriction restriction : getParentItem().getRestrictions(CreationRestrictionType.ITEM_CHILD_VALUE_AT))
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
			for (final CreationRestriction restriction : getItemGroup().getRestrictions(CreationRestrictionType.GROUP_ITEM_VALUE_AT))
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
		
		return getCreationMode().canIncreaseItem(this, canIncreaseItem && canIncreaseChild);
	}
	
	@Override
	public void clear()
	{
		if (getItem().isValueItem())
		{
			mValueId = getItem().getStartValue();
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
		getCreationMode().decreaseItem(this);
		refreshValue();
		updateController();
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
		final SelectionListener<Item> action = new SelectionListener<Item>()
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
	public ChangeAction getChangeTempPoints()
	{
		return mChangeTempPoints;
	}
	
	@Override
	public ChangeAction getChangeValue()
	{
		return mChangeValue;
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
	public CreationMode getCreationMode()
	{
		return mMode;
	}
	
	@Override
	public int getDecreasedValue()
	{
		if ( !getItem().isValueItem())
		{
			Log.w(TAG, "Tried to get the decreased value of a non value item.");
			return 0;
		}
		return getItem().getValues()[Math.max(mValueId + mTempPoints - 1, 0)];
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
		return getItem().getValues()[Math.min(getItem().getMaxValue(), mValueId + mTempPoints + 1)];
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
	public PointHandler getPoints()
	{
		return mPoints;
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
		return getItem().getValues()[mValueId + mTempPoints];
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
		return getItem().getItemGroup().getFreePointsCost() <= getPoints().getPoints();
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
		getCreationMode().increaseItem(this);
		refreshValue();
		updateController();
	}
	
	@Override
	public int indexOfChild(final ItemCreation aItem)
	{
		return getChildrenList().indexOf(aItem);
	}
	
	@Override
	public void init()
	{
		final boolean canEditItem = getCreationMode().canEditItem(this);
		final boolean canAddChildren = getCreationMode().canAddChild(this, false);
		
		if ( !mInitialized)
		{
			mEditButton = (ImageButton) getContainer().findViewById(R.id.item_edit_button);
			mRemoveButton = (ImageButton) getContainer().findViewById(R.id.item_remove_button);
			mNameText = (TextView) getContainer().findViewById(R.id.item_name);
			mDecreaseButton = (ImageButton) getContainer().findViewById(R.id.item_decrease_button);
			mValueText = (TextView) getContainer().findViewById(R.id.item_value);
			mValueBar = (ProgressBar) getContainer().findViewById(R.id.item_value_bar);
			mIncreaseButton = (ImageButton) getContainer().findViewById(R.id.item_increase_button);
			mAddButton = (ImageButton) getContainer().findViewById(R.id.item_add_button);
			mChildrenContainer = (LinearLayout) getContainer().findViewById(R.id.children_container);
			
			mNameText.setText(getItem().getDisplayName());
			mNameText.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					Toast.makeText(getContext(), getItem().getDescription(), Toast.LENGTH_LONG).show();
				}
			});
			
			if (canEditItem)
			{
				mEditButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						if (hasParentItem())
						{
							if (getCreationMode().canRemoveChild(ItemCreationImpl.this))
							{
								getParentItem().editChild(getItem());
							}
						}
						else
						{
							if (getCreationMode().canRemoveItem(ItemCreationImpl.this))
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
							if (getCreationMode().canRemoveChild(ItemCreationImpl.this))
							{
								getParentItem().removeChild(getItem());
							}
						}
						else
						{
							if (getCreationMode().canRemoveItem(ItemCreationImpl.this))
							{
								getItemGroup().removeItem(getItem());
							}
						}
					}
				});
				
				updateEditRemoveButtons();
			}
			
			if (isValueItem())
			{
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
				
				refreshValue();
			}
			
			if (canAddChildren)
			{
				mAddButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						addChild();
					}
				});
				
				updateAddButton();
			}
			mInitialized = true;
		}
		
		if (canEditItem)
		{
			ViewUtil.setWidth(mEditButton, 30);
			ViewUtil.setWidth(mRemoveButton, 30);
		}
		else
		{
			ViewUtil.hideWidth(mEditButton);
			ViewUtil.hideWidth(mRemoveButton);
		}
		if (canAddChildren)
		{
			ViewUtil.setWidth(mAddButton, 30);
		}
		else
		{
			ViewUtil.hideWidth(mAddButton);
		}
		
		if (isValueItem())
		{
			// TODO Use cases needed?
			ViewUtil.setWidth(mDecreaseButton, 30);
			ViewUtil.setWidth(mIncreaseButton, 30);
			ViewUtil.setWidth(mValueText, 30);
		}
		else
		{
			// TODO Do this in another way
			ViewUtil.hideWidth(mValueBar);
			ViewUtil.hideWidth(mDecreaseButton);
			ViewUtil.hideWidth(mIncreaseButton);
			ViewUtil.hideWidth(mValueText);
		}
		
		if (hasChildren())
		{
			if ( !hasOrder())
			{
				sortChildren();
			}
			else
			{
				for (final ItemCreation item : getChildrenList())
				{
					item.init();
					mChildrenContainer.addView(item.getContainer());
				}
			}
		}
	}
	
	@Override
	public boolean isImportant()
	{
		// TODO Add if this is only a non value item?
		if (isValueItem() && getValue() != 0)
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
	public void refreshValue()
	{
		if ( !isValueItem() && !isParent())
		{
			Log.w(TAG, "Tried to refresh the value of a non value item.");
			return;
		}
		if (isParent())
		{
			for (final ItemCreation item : getChildrenList())
			{
				item.refreshValue();
			}
		}
		if (isValueItem())
		{
			if (mAnimator.isRunning())
			{
				mAnimator.cancel();
			}
			mValueBar.setMax(VALUE_MULTIPLICATOR * Math.abs(getItem().getValues()[getItem().getMaxValue()]));
			mAnimator.setIntValues(mValueBar.getProgress(), VALUE_MULTIPLICATOR * getAbsoluteValue());
			mAnimator.start();
			mValueText.setText("" + getValue());
		}
	}
	
	@Override
	public void release()
	{
		if (isParent())
		{
			for (final ItemCreation item : getChildrenList())
			{
				item.release();
			}
		}
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
		getItemGroup().getItemController().removeItemName(aItem.getName());
		getItemGroup().getItemController().resize();
		updateController();
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
			refreshValue();
		}
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
		getItemGroup().getItemController().removeItemName(oldItem.getName());
		oldItem.clear();
		mChildren.remove(oldItem.getName());
		final ItemCreation item = new ItemCreationImpl(aItem, getContext(), getItemGroup(), getCreationMode(), getItemGroup().getPoints(), this);
		getChildrenList().set(aIndex, item);
		mChildren.put(aItem.getName(), item);
		mChildrenContainer.addView(item.getContainer(), aIndex + 1);
		getItemGroup().getItemController().addItemName(item);
		getItemGroup().getItemController().resize();
		updateController();
	}
	
	@Override
	public void setCreationMode(final CreationMode aMode)
	{
		mMode = aMode;
		if (isParent())
		{
			for (final ItemCreation item : getChildrenList())
			{
				item.setCreationMode(getCreationMode());
			}
		}
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
	public void setPoints(final PointHandler aPoints)
	{
		mPoints = aPoints;
		if (isParent())
		{
			for (final ItemCreation item : getChildrenList())
			{
				item.setPoints(mPoints);
			}
		}
	}
	
	@Override
	public String toString()
	{
		return getName() + ": " + getValue();
	}
	
	@Override
	public void updateButtons()
	{
		if (isValueItem())
		{
			setIncreasable();
			setDecreasable();
		}
		updateEditRemoveButtons();
		updateAddButton();
		if (isParent())
		{
			for (final ItemCreation child : getChildrenList())
			{
				child.updateButtons();
			}
		}
	}
	
	@Override
	public void updateController()
	{
		getItemGroup().getItemController().updateGroups();
	}
	
	@Override
	public void updateRestrictions()
	{
		if (isValueItem())
		{
			resetTempPoints();
			if (hasRestrictions())
			{
				if ( !isValueOk(getValue(), CreationRestrictionType.ITEM_VALUE))
				{
					while (mValueId < getMinValue(CreationRestrictionType.ITEM_VALUE))
					{
						increase();
					}
					while (mValueId > getMaxValue(CreationRestrictionType.ITEM_VALUE))
					{
						decrease();
					}
				}
			}
			else
			{
				mValueId = getItem().getStartValue();
				refreshValue();
				updateController();
			}
		}
		if (isParent())
		{
			for (final ItemCreation child : getChildrenList())
			{
				child.updateRestrictions();
			}
		}
		updateButtons();
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
			item.init();
			mChildrenContainer.addView(item.getContainer());
		}
	}
	
	private void updateAddButton()
	{
		if (isParent() && isMutableParent())
		{
			ViewUtil.setEnabled(mAddButton, getCreationMode().canAddChild(this, true) && !getAddableItems().isEmpty());
		}
	}
	
	private void updateEditRemoveButtons()
	{
		if (getCreationMode().canEditItem(this))
		{
			boolean canEditRemove;
			if (hasParentItem())
			{
				canEditRemove = getCreationMode().canRemoveChild(this);
			}
			else
			{
				canEditRemove = getCreationMode().canRemoveItem(this);
			}
			ViewUtil.setEnabled(mEditButton, canEditRemove);
			ViewUtil.setEnabled(mRemoveButton, canEditRemove);
		}
	}
}
