package com.deepercreeper.vampireapp.controllers.dynamic.implementations.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.deepercreeper.vampireapp.character.CharacterInstance;
import com.deepercreeper.vampireapp.character.EPHandler;
import com.deepercreeper.vampireapp.character.Mode;
import com.deepercreeper.vampireapp.controllers.actions.Action;
import com.deepercreeper.vampireapp.controllers.actions.Action.ItemFinder;
import com.deepercreeper.vampireapp.controllers.dynamic.implementations.instances.restrictions.InstanceRestrictionableImpl;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.Item;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.creations.ItemCreation;
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
	
	private boolean							mInitialized	= false;
	
	private EPHandler						mEP;
	
	private Mode							mMode;
	
	private int								mValueId		= 0;
	
	public ItemInstanceImpl(final Element aElement, final ItemGroupInstance aItemGroup, final Context aContext, final Mode aMode,
			final EPHandler aEP, final ItemInstance aParentItem, final CharacterInstance aCharacter)
	{
		mItem = aItemGroup.getItemGroup().getItem(aElement.getAttribute("name"));
		mCharacter = aCharacter;
		mItemGroup = aItemGroup;
		mContext = aContext;
		if (getItem().needsDescription())
		{
			mDescription = aElement.getAttribute("description");
		}
		else
		{
			mDescription = null;
		}
		setController(getItemGroup().getItemController());
		mMode = aMode;
		mContainer = new LinearLayout(getContext());
		mRelativeContainer = new RelativeLayout(getContext());
		mNameText = new TextView(getContext());
		
		if (isValueItem())
		{
			mIncreaseButton = new ImageButton(getContext());
			mValueBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
			mValueText = new TextView(getContext());
			mEP = aEP;
		}
		else
		{
			mIncreaseButton = null;
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
		mValueId = Integer.parseInt(aElement.getAttribute("value"));
		
		init();
		
		final NodeList children = aElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++ )
		{
			if (children.item(i) instanceof Element)
			{
				final Element item = (Element) children.item(i);
				if ( !item.getTagName().equals("item"))
				{
					continue;
				}
				addChildSilent(new ItemInstanceImpl(item, getItemGroup(), getContext(), getMode(), getEP(), this, getCharacter()));
			}
		}
	}
	
	public ItemInstanceImpl(final ItemCreation aItem, final ItemGroupInstance aItemGroup, final Mode aMode, final EPHandler aEP,
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
			mValueBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
			mValueText = new TextView(getContext());
			mEP = aEP;
		}
		else
		{
			mIncreaseButton = null;
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
		mValueId = aItem.getValueId();
		
		init();
		
		if (aItem.hasChildren())
		{
			for (final ItemCreation item : aItem.getChildrenList())
			{
				if ( !aItem.isMutableParent() || item.isImportant())
				{
					addChildSilent(new ItemInstanceImpl(item, getItemGroup(), getMode(), getEP(), this, getCharacter()));
				}
			}
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element item = aDoc.createElement("item");
		item.setAttribute("name", getName());
		if (isValueItem())
		{
			item.setAttribute("value", "" + mValueId);
		}
		if (hasDescription())
		{
			item.setAttribute("description", getDescription());
		}
		
		if (hasChildren())
		{
			for (final ItemInstance child : getChildrenList())
			{
				item.appendChild(child.asElement(aDoc));
			}
		}
		return item;
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
	public int getAbsoluteValue()
	{
		return Math.abs(getValue());
	}
	
	@Override
	public Set<Action> getActions()
	{
		return mActions;
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
	public ItemInstance getChildAt(final int aIndex)
	{
		return getChildrenList().get(aIndex);
	}
	
	@Override
	public List<ItemInstance> getChildrenList()
	{
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
	public String getDescription()
	{
		return mDescription;
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
	public EPHandler getEP()
	{
		return mEP;
	}
	
	@Override
	public int getEPCost()
	{
		return getItem().getEPCost() + getValue() * getItem().getEPCostMultiplicator();
	}
	
	@Override
	public Item getItem()
	{
		return mItem;
	}
	
	@Override
	public ItemGroupInstance getItemGroup()
	{
		return mItemGroup;
	}
	
	@Override
	public Mode getMode()
	{
		return mMode;
	}
	
	@Override
	public String getName()
	{
		return getItem().getName();
	}
	
	@Override
	public ItemInstance getParentItem()
	{
		return mParentItem;
	}
	
	@Override
	public int getValue()
	{
		return getItem().getValues()[mValueId];
	}
	
	@Override
	public boolean hasChild(final Item aItem)
	{
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
	public boolean hasDescription()
	{
		return mDescription != null;
	}
	
	@Override
	public boolean hasEnoughEP()
	{
		return getEP().getExperience() >= getEPCost();
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
	public int indexOfChild(final ItemInstance aItem)
	{
		return getChildrenList().indexOf(aItem);
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
		
		params = ViewUtil.getRelativeNameLong();
		mNameText.setLayoutParams(params);
		if ( !mInitialized)
		{
			mNameText.setText(getItem().getDisplayName());
			mNameText.setClickable(true);
			mNameText.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					Toast.makeText(getContext(), getItem().getDescription(), Toast.LENGTH_LONG).show();
				}
			});
			mNameText.setGravity(Gravity.CENTER_VERTICAL);
			mNameText.setSingleLine();
			mNameText.setEllipsize(TruncateAt.END);
		}
		leftView = mNameText;
		mRelativeContainer.addView(mNameText);
		
		if (isValueItem())
		{
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
			
			params = ViewUtil.getRelativeValueBarSize(200);
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
		
		getContainer().addView(mRelativeContainer);
		
		if (hasChildren())
		{
			for (final ItemInstance child : getChildrenList())
			{
				child.init();
				getContainer().addView(child.getContainer());
			}
		}
		mInitialized = true;
	}
	
	@Override
	public void initActions(final ItemFinder aFinder)
	{
		// TODO Invoke
		for (final Action action : getActions())
		{
			action.init(aFinder);
		}
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
	public boolean masterCanDecrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to ask whether a non value item can be decreased.");
			return false;
		}
		final boolean canDecreaseItem = mValueId > Math.max(0, getMinValue(InstanceRestrictionType.ITEM_VALUE));
		// TODO Implement
		return canDecreaseItem;
	}
	
	@Override
	public boolean masterCanIncrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to ask whether a non value item can be increased.");
			return false;
		}
		// TODO Implement
		return false;
	}
	
	@Override
	public void masterDecrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to decrease a non value item.");
			return;
		}
		// TODO Implement
		return;
	}
	
	@Override
	public void masterIncrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to increase a non value item.");
			return;
		}
		// TODO Implement
		return;
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
		ViewUtil.release(mValueText);
		ViewUtil.release(mValueBar);
		ViewUtil.release(mIncreaseButton);
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
	public void updateButtons()
	{
		if (isValueItem())
		{
			setIncreasable();
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
	public void updateCharacter()
	{
		getCharacter().update();
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
	
	private void addChildSilent(final ItemInstance aItem)
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
}
