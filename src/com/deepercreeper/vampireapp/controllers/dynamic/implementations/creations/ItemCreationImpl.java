package com.deepercreeper.vampireapp.controllers.dynamic.implementations.creations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.controllers.dialog.SelectItemDialog;
import com.deepercreeper.vampireapp.controllers.dialog.SelectItemDialog.SelectionListener;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemControllerCreation.PointHandler;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.controllers.restrictions.Restriction;
import com.deepercreeper.vampireapp.controllers.restrictions.Restriction.RestrictionType;
import com.deepercreeper.vampireapp.controllers.restrictions.RestrictionableImpl;
import com.deepercreeper.vampireapp.creation.CreationMode;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class ItemCreationImpl extends RestrictionableImpl implements ItemCreation
{
	private static final String	TAG	= "ItemCreation";
	
	public interface ChangeAction
	{
		void decrease();
		
		void increase();
	}
	
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
	
	private final ImageButton				mIncreaseButton;
	
	private final ImageButton				mDecreaseButton;
	
	private final ProgressBar				mValueBar;
	
	private final TextView					mValueText;
	
	private final LinearLayout				mContainer;
	
	private final List<ItemCreation>		mChildrenList;
	
	private final Map<String, ItemCreation>	mChildren;
	
	private final ItemCreation				mParentItem;
	
	private final RelativeLayout			mRelativeContainer;
	
	private final ImageButton				mAddButton;
	
	private final ImageButton				mEditButton;
	
	private final ImageButton				mRemoveButton;
	
	private final TextView					mNameText;
	
	private boolean							mInitialized		= false;
	
	private CreationMode					mMode;
	
	private PointHandler					mPoints;
	
	private String							mDescription;
	
	private int								mValueId;
	
	private int								mTempPoints;
	
	public ItemCreationImpl(final Item aItem, final Context aContext, final ItemGroupCreation aGroup, final CreationMode aMode,
			final PointHandler aPoints, final ItemCreation aParentItem)
	{
		mItem = aItem;
		mContext = aContext;
		mItemGroup = aGroup;
		setController(aGroup.getItemController());
		mMode = aMode;
		mContainer = new LinearLayout(getContext());
		mRelativeContainer = new RelativeLayout(getContext());
		mAddButton = new ImageButton(getContext());
		mEditButton = new ImageButton(getContext());
		mRemoveButton = new ImageButton(getContext());
		mNameText = new TextView(getContext());
		if (isValueItem())
		{
			mIncreaseButton = new ImageButton(getContext());
			mDecreaseButton = new ImageButton(getContext());
			mValueBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
			mValueText = new TextView(getContext());
			mValueId = getItem().getStartValue();
			mPoints = aPoints;
		}
		else
		{
			mIncreaseButton = null;
			mDecreaseButton = null;
			mValueBar = null;
			mValueText = null;
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
				addChild(new ItemCreationImpl(item, getContext(), getItemGroup(), getCreationMode(), getItemGroup().getPoints(), this));
			}
		}
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
		if (getMaxValue(RestrictionType.ITEM_CHILDREN_COUNT) <= getChildrenList().size())
		{
			return;
		}
		final ItemCreation item = new ItemCreationImpl(aItem, getContext(), getItemGroup(), getCreationMode(), getItemGroup().getPoints(), this);
		getChildrenList().add(item);
		mChildren.put(item.getName(), item);
		getContainer().addView(item.getContainer());
		getItemGroup().getItemController().addItemName(item);
		getItemGroup().getItemController().resize();
		updateController();
	}
	
	@Override
	public void updateController()
	{
		getItemGroup().getItemController().updateGroups();
	}
	
	@Override
	public boolean canDecrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to ask whether a non value item can be decreased.");
			return false;
		}
		final boolean canDecreaseItemValue = mValueId > Math.max(getItem().getStartValue(), getMinValue(RestrictionType.ITEM_VALUE)) && mValueId > 0;
		final boolean canDecreaseItemTempPoints = mTempPoints > 0;
		boolean canDecreaseChild = true;
		
		if (hasParentItem())
		{
			for (final Restriction restriction : getParentItem().getRestrictions(RestrictionType.ITEM_CHILD_VALUE_AT))
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
		final boolean canIncreaseItem = mValueId + mTempPoints < Math.min(getItem().getMaxLowLevelValue(), getMaxValue(RestrictionType.ITEM_VALUE));
		boolean canIncreaseChild = true;
		
		if (hasParentItem())
		{
			for (final Restriction restriction : getParentItem().getRestrictions(RestrictionType.ITEM_CHILD_VALUE_AT))
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
		
		// TODO Add all old resource files to the new data file
		
		// TODO Implement the same thing for the group child value at restrictionType
		
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
		final SelectionListener action = new SelectionListener()
		{
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
	public boolean hasChildAt(final int aIndex)
	{
		return getChildrenList().size() > aIndex;
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
		getCreationMode().increaseItem(this);
		refreshValue();
		updateController();
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
			mValueBar.setMax(Math.abs(getItem().getValues()[getItem().getMaxValue()]));
			mValueBar.setProgress(getAbsoluteValue());
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
		ViewUtil.release(mEditButton);
		ViewUtil.release(mRemoveButton);
		ViewUtil.release(mAddButton);
		ViewUtil.release(mNameText);
		ViewUtil.release(mRelativeContainer);
		ViewUtil.release(mDecreaseButton);
		ViewUtil.release(mValueText);
		ViewUtil.release(mValueBar);
		ViewUtil.release(mIncreaseButton);
	}
	
	@Override
	public void removeChild(final Item aItem)
	{
		if ( !isMutableParent())
		{
			Log.w(TAG, "Tried to remove a child from a non parent or non mutable item.");
			return;
		}
		if (getMinValue(RestrictionType.ITEM_CHILDREN_COUNT) >= getChildrenList().size())
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
		getContainer().addView(item.getContainer(), aIndex + 1);
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
		mDecreaseButton.setEnabled(canDecrease());
	}
	
	@Override
	public void setDescription(final String aDescription)
	{
		mDescription = aDescription;
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
	public void setIncreasable()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to change whether a non value item can be increased.");
			return;
		}
		mIncreaseButton.setEnabled(canIncrease());
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
		if (items.isEmpty() || getMaxValue(RestrictionType.ITEM_CHILDREN_COUNT) <= getChildrenList().size())
		{
			return;
		}
		final SelectionListener action = new SelectionListener()
		{
			@Override
			public void select(final Item aChoosenItem)
			{
				addChild(aChoosenItem);
			}
		};
		SelectItemDialog.showSelectionDialog(items, getContext().getString(R.string.add_item), getContext(), action);
	}
	
	@Override
	public void updateRestrictions()
	{
		if (isValueItem() || isParent())
		{
			resetTempPoints();
			if (hasRestrictions())
			{
				if ( !isValueOk(getValue(), RestrictionType.ITEM_VALUE))
				{
					while (mValueId < getMinValue(RestrictionType.ITEM_VALUE))
					{
						increase();
					}
					while (mValueId > getMaxValue(RestrictionType.ITEM_VALUE))
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
			if (isParent())
			{
				for (final ItemCreation child : getChildrenList())
				{
					child.updateRestrictions();
				}
			}
			updateButtons();
		}
	}
	
	@Override
	public int indexOfChild(final ItemCreation aItem)
	{
		return getChildrenList().indexOf(aItem);
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
	
	private void addChild(final ItemCreation aItem)
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
		getContainer().addView(aItem.getContainer());
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			getContainer().setLayoutParams(ViewUtil.getWrapHeight());
			getContainer().setOrientation(LinearLayout.VERTICAL);
			
			mRelativeContainer.setLayoutParams(ViewUtil.getWrapAll());
		}
		
		RelativeLayout.LayoutParams params;
		View leftView = null;
		final boolean canEditItem = getCreationMode().canEditItem(this);
		final boolean canAddChildren = getCreationMode().canAddChild(this, false);
		
		if (canEditItem)
		{
			params = ViewUtil.getRelativeButtonSize();
			mEditButton.setLayoutParams(params);
			if ( !mInitialized)
			{
				mEditButton.setContentDescription("Edit");
				mEditButton.setImageResource(android.R.drawable.ic_menu_edit);
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
			}
			mRelativeContainer.addView(mEditButton);
			
			params = ViewUtil.getRelativeButtonSize();
			ViewUtil.generateId(mEditButton);
			params.addRule(RelativeLayout.RIGHT_OF, mEditButton.getId());
			mRemoveButton.setLayoutParams(params);
			if ( !mInitialized)
			{
				mRemoveButton.setContentDescription("Remove");
				mRemoveButton.setImageResource(android.R.drawable.ic_menu_delete);
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
			}
			leftView = mRemoveButton;
			mRelativeContainer.addView(mRemoveButton);
			updateEditRemoveButtons();
		}
		
		params = ViewUtil.getRelativeNameLong();
		if (leftView != null)
		{
			ViewUtil.generateId(leftView);
			params.addRule(RelativeLayout.RIGHT_OF, leftView.getId());
		}
		mNameText.setLayoutParams(params);
		if ( !mInitialized)
		{
			mNameText.setText(getItem().getName());
			mNameText.setGravity(Gravity.CENTER_VERTICAL);
			mNameText.setSingleLine();
			mNameText.setEllipsize(TruncateAt.END);
		}
		leftView = mNameText;
		mRelativeContainer.addView(mNameText);
		
		if (isValueItem())
		{
			params = ViewUtil.getRelativeButtonSize();
			if (leftView != null)
			{
				ViewUtil.generateId(leftView);
				params.addRule(RelativeLayout.RIGHT_OF, leftView.getId());
			}
			mDecreaseButton.setLayoutParams(params);
			if ( !mInitialized)
			{
				mDecreaseButton.setImageResource(android.R.drawable.ic_media_previous);
				mDecreaseButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						decrease();
					}
				});
			}
			leftView = mDecreaseButton;
			mRelativeContainer.addView(mDecreaseButton);
			
			params = ViewUtil.getRelativeValueTextSize();
			if (leftView != null)
			{
				ViewUtil.generateId(leftView);
				params.addRule(RelativeLayout.RIGHT_OF, leftView.getId());
			}
			mValueText.setLayoutParams(params);
			if ( !mInitialized)
			{
				mValueText.setGravity(Gravity.CENTER_VERTICAL);
				mValueText.setPadding(mValueText.getPaddingLeft(), mValueText.getPaddingTop(), ViewUtil.calcPx(5), mValueText.getPaddingBottom());
				mValueText.setSingleLine();
				mValueText.setEllipsize(TruncateAt.END);
			}
			leftView = mValueText;
			mRelativeContainer.addView(mValueText);
			
			int additionalBarSize = 0;
			if ( !canEditItem)
			{
				additionalBarSize += 60;
			}
			if ( !canAddChildren)
			{
				additionalBarSize += 30;
			}
			params = ViewUtil.getRelativeValueBarSize(80 + additionalBarSize);
			if (leftView != null)
			{
				ViewUtil.generateId(leftView);
				params.addRule(RelativeLayout.RIGHT_OF, leftView.getId());
			}
			mValueBar.setLayoutParams(params);
			leftView = mValueBar;
			mRelativeContainer.addView(mValueBar);
			
			params = ViewUtil.getRelativeButtonSize();
			if (leftView != null)
			{
				ViewUtil.generateId(leftView);
				params.addRule(RelativeLayout.RIGHT_OF, leftView.getId());
			}
			mIncreaseButton.setLayoutParams(params);
			if ( !mInitialized)
			{
				mIncreaseButton.setImageResource(android.R.drawable.ic_media_next);
				mIncreaseButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						increase();
					}
				});
			}
			leftView = mIncreaseButton;
			mRelativeContainer.addView(mIncreaseButton);
			
			refreshValue();
		}
		
		if (canAddChildren)
		{
			params = ViewUtil.getRelativeButtonSize();
			if (leftView != null)
			{
				ViewUtil.generateId(leftView);
				params.addRule(RelativeLayout.RIGHT_OF, leftView.getId());
			}
			mAddButton.setLayoutParams(params);
			if ( !mInitialized)
			{
				mAddButton.setContentDescription("Add");
				mAddButton.setImageResource(android.R.drawable.ic_menu_add);
				mAddButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						addChild();
					}
				});
			}
			leftView = mAddButton;
			mRelativeContainer.addView(mAddButton);
		}
		
		getContainer().addView(mRelativeContainer);
		
		if (hasChildren())
		{
			for (final ItemCreation child : getChildrenList())
			{
				child.init();
				getContainer().addView(child.getContainer());
			}
		}
		updateAddButton();
		mInitialized = true;
	}
	
	private void updateAddButton()
	{
		if (isParent() && isMutableParent())
		{
			mAddButton.setEnabled(getCreationMode().canAddChild(this, true));
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
			mEditButton.setEnabled(canEditRemove);
			mRemoveButton.setEnabled(canEditRemove);
		}
	}
	
	@Override
	public String toString()
	{
		return getName() + ": " + getValue();
	}
}
