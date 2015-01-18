package com.deepercreeper.vampireapp.controllers.dynamic.implementations.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.character.CharacterInstance;
import com.deepercreeper.vampireapp.character.EPHandler;
import com.deepercreeper.vampireapp.character.Mode;
import com.deepercreeper.vampireapp.controllers.actions.Action;
import com.deepercreeper.vampireapp.controllers.actions.Action.ItemFinder;
import com.deepercreeper.vampireapp.controllers.dynamic.implementations.instances.restrictions.InstanceRestrictionableImpl;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.restrictions.InstanceRestriction;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.instances.restrictions.InstanceRestriction.InstanceRestrictionType;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class ItemInstanceImpl extends InstanceRestrictionableImpl implements ItemInstance
{
	public static final String				TAG				= "ItemInstance";
	
	private final Item						mItem;
	
	private final Context					mContext;
	
	private final ItemGroupInstance			mItemGroup;
	
	private final ImageButton				mIncreaseButton;
	
	private final ImageButton				mDecreaseButton;
	
	private final ProgressBar				mValueBar;
	
	private final TextView					mValueText;
	
	private final LinearLayout				mContainer;
	
	private final List<ItemInstance>		mChildrenList;
	
	private final Map<String, ItemInstance>	mChildren;
	
	private final ItemInstance				mParentItem;
	
	private final RelativeLayout			mRelativeContainer;
	
	private final TextView					mNameText;
	
	private final String					mDescription;
	
	private final Set<Action>				mActions		= new HashSet<Action>();
	
	private final CharacterInstance			mCharacter;
	
	private final boolean					mInitialized	= false;
	
	private EPHandler						mEP;
	
	private Mode							mMode;
	
	private int								mValueId		= 0;
	
	public ItemInstanceImpl(final ItemInstance aItem, final ItemGroupInstance aItemGroup, final Mode aMode, final EPHandler aEP,
			final ItemInstance aParentItem, final CharacterInstance aCharacter)
	{
		mItem = aItem.getItem();
		mCharacter = aCharacter;
		mItemGroup = aItemGroup;
		mContext = aItem.getContext();
		mDescription = aItem.getDescription();
		setController(aItemGroup.getItemController());
		mMode = aMode;
		mContainer = new LinearLayout(getContext());
		mRelativeContainer = new RelativeLayout(getContext());
		mNameText = new TextView(getContext());
		
		if (isValueItem())
		{
			mIncreaseButton = new ImageButton(getContext());
			mDecreaseButton = new ImageButton(getContext());
			mValueBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
			mValueText = new TextView(getContext());
			mValueId = getItem().getStartValue();
			mEP = aEP;
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
			mChildrenList = new ArrayList<ItemInstance>();
			mChildren = new HashMap<String, ItemInstance>();
		}
		else
		{
			mChildrenList = null;
			mChildren = null;
		}
		if (getItem().hasParentItem() && aParentItem == null || !getItem().hasParentItem() && aParentItem != null)
		{
			Log.w(TAG, "Tried to create an item with different parent item state and parent item.");
			throw new IllegalArgumentException("ItemInstance error!");
		}
		mParentItem = aParentItem;
		
		init();
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public boolean canIncrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to ask whether a non value item can be increased.");
			return false;
		}
		final boolean canIncreaseItem;
		if (mCharacter.isLowLevel())
		{
			canIncreaseItem = mValueId < Math.min(getItem().getMaxLowLevelValue(), getMaxValue(InstanceRestrictionType.ITEM_VALUE));
		}
		else
		{
			canIncreaseItem = mValueId < getMaxValue(InstanceRestrictionType.ITEM_VALUE);
		}
		boolean canIncreaseChild = true;
		
		if (hasParentItem())
		{
			for (final InstanceRestriction restriction : getParentItem().getRestrictions(InstanceRestrictionType.ITEM_CHILD_VALUE_AT))
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
			for (final InstanceRestriction restriction : getItemGroup().getRestrictions(InstanceRestrictionType.GROUP_ITEM_VALUE_AT))
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
		
		return canIncreaseItem && canIncreaseChild && hasEnoughEP();
	}
	
	@Override
	public boolean canDecrease()
	{
		return false;
	}
	
	@Override
	public void clear()
	{
		if (getItem().isValueItem())
		{
			mValueId = getItem().getStartValue();
		}
		release();
	}
	
	@Override
	public int getAbsoluteValue()
	{
		return Math.abs(getValue());
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
			for (final ItemInstance item : getChildrenList())
			{
				values += item.getValue();
			}
		}
		return values;
	}
	
	@Override
	public CharacterInstance getCharacter()
	{
		return mCharacter;
	}
	
	@Override
	public String getDescription()
	{
		return mDescription;
	}
	
	@Override
	public String getName()
	{
		return getItem().getName();
	}
	
	@Override
	public List<ItemInstance> getChildrenList()
	{
		return mChildrenList;
	}
	
	@Override
	public ItemInstance getParentItem()
	{
		return mParentItem;
	}
	
	@Override
	public List<ItemInstance> getDescriptionItems()
	{
		if ( !isParent())
		{
			Log.w(TAG, "Tried to get the description items of a non parent item.");
			return null;
		}
		final List<ItemInstance> items = new ArrayList<ItemInstance>();
		for (final ItemInstance child : getChildrenList())
		{
			if (child.hasDescription())
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
	public ItemGroupInstance getItemGroup()
	{
		return mItemGroup;
	}
	
	@Override
	public boolean hasEnoughEP()
	{
		return getEP().getExperience() >= getEPCost();
	}
	
	@Override
	public int getEPCost()
	{
		return getItem().getEPCost() + getValue() * getItem().getEPCostMultiplicator();
	}
	
	@Override
	public void setMode(final Mode aMode)
	{
		mMode = aMode;
		if (isParent())
		{
			for (final ItemInstance child : getChildrenList())
			{
				child.setMode(getMode());
			}
		}
	}
	
	@Override
	public Mode getMode()
	{
		return mMode;
	}
	
	@Override
	public void setEP(final EPHandler aEP)
	{
		mEP = aEP;
		if (isParent())
		{
			for (final ItemInstance child : getChildrenList())
			{
				child.setEP(getEP());
			}
		}
	}
	
	@Override
	public EPHandler getEP()
	{
		return mEP;
	}
	
	@Override
	public Item getItem()
	{
		return mItem;
	}
	
	@Override
	public int getValue()
	{
		return getItem().getValues()[mValueId];
	}
	
	@Override
	public Set<Action> getActions()
	{
		return mActions;
	}
	
	@Override
	public void initActions(final ItemFinder aFinder)
	{
		for (final Action action : getActions())
		{
			action.init(aFinder);
		}
	}
	
	@Override
	public void updateRestrictions()
	{
		if (isValueItem())
		{
			if (hasRestrictions())
			{
				while ( !isValueOk(getValue(), InstanceRestrictionType.ITEM_VALUE))
				{
					// TODO Implement
				}
			}
		}
	}
	
	@Override
	public boolean hasDescription()
	{
		return mDescription != null;
	}
	
	@Override
	public void decrease()
	{	
		
	}
	
	@Override
	public void init()
	{}
	
	@Override
	public void updateButtons()
	{
		if (isValueItem())
		{
			setIncreasable();
			setDecreasable();
		}
		if (isParent())
		{
			for (final ItemInstance child : getChildrenList())
			{
				child.updateButtons();
			}
		}
	}
	
	@Override
	public ItemInstance getChildAt(final int aIndex)
	{
		return getChildrenList().get(aIndex);
	}
	
	@Override
	public int indexOfChild(final ItemInstance aItem)
	{
		return getChildrenList().indexOf(aItem);
	}
	
	@Override
	public boolean hasChildAt(final int aIndex)
	{
		return getChildrenList().size() > aIndex;
	}
	
	@Override
	public Context getContext()
	{
		return mContext;
	}
	
	@Override
	public boolean hasChild(final Item aItem)
	{
		return mChildren.containsKey(aItem.getName());
	}
	
	@Override
	public boolean hasChildren()
	{
		return !getChildrenList().isEmpty();
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
		if ( !hasEnoughEP())
		{
			return;
		}
		if (mValueId < getItem().getValues().length - 1)
		{
			mValueId++ ;
		}
		getEP().decreaseBy(getEPCost());
		refreshValue();
		updateCharacter();
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
	public void updateCharacter()
	{
		getCharacter().update();
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
			for (final ItemInstance item : getChildrenList())
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
			for (final ItemInstance item : getChildrenList())
			{
				item.release();
			}
		}
		ViewUtil.release(getContainer());
		ViewUtil.release(mNameText);
		ViewUtil.release(mRelativeContainer);
		ViewUtil.release(mDecreaseButton);
		ViewUtil.release(mValueText);
		ViewUtil.release(mValueBar);
		ViewUtil.release(mIncreaseButton);
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
	public void setIncreasable()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to change whether a non value item can be increased.");
			return;
		}
		mIncreaseButton.setEnabled(canIncrease());
	}
}
