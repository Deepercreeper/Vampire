package com.deepercreeper.vampireapp.items.implementations.instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.host.Message.Builder;
import com.deepercreeper.vampireapp.host.Message.ButtonAction;
import com.deepercreeper.vampireapp.host.Message.MessageGroup;
import com.deepercreeper.vampireapp.host.Message.MessageType;
import com.deepercreeper.vampireapp.host.change.ItemChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.implementations.instances.dependencies.DependencyInstanceImpl;
import com.deepercreeper.vampireapp.items.implementations.instances.dependencies.RestrictionableDependableInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance.RestrictionInstanceType;
import com.deepercreeper.vampireapp.mechanics.Action;
import com.deepercreeper.vampireapp.mechanics.ActionInstance;
import com.deepercreeper.vampireapp.mechanics.ActionInstanceImpl;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateStringDialog;
import com.deepercreeper.vampireapp.util.view.dialogs.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.listeners.ItemSelectionListener;
import com.deepercreeper.vampireapp.util.view.listeners.StringCreationListener;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An item instance implementation.
 * 
 * @author vrl
 */
public class ItemInstanceImpl extends RestrictionableDependableInstanceImpl implements ItemInstance
{
	private static final String TAG = "ItemInstance";
	
	private static final int VALUE_MULTIPLICATOR = 20;
	
	private final List<ItemInstance> mChildrenList;
	
	private final Map<String, ItemInstance> mChildren;
	
	private final Set<ActionInstance> mActions = new HashSet<ActionInstance>();
	
	private final List<ItemValueListener> mValueListeners = new ArrayList<ItemValueListener>();
	
	private final Item mItem;
	
	private final Context mContext;
	
	private final ItemGroupInstance mItemGroup;
	
	private final LinearLayout mContainer;
	
	private final ItemInstance mParentItem;
	
	private final String mDescription;
	
	private final ValueAnimator mAnimator;
	
	private final MessageListener mMessageListener;
	
	private final LinearLayout mChildrenContainer;
	
	private final ImageButton mIncreaseButton;
	
	private final ImageButton mDecreaseButton;
	
	private final ImageButton mAddButton;
	
	private final ProgressBar mValueBar;
	
	private final TextView mValueText;
	
	private final boolean mHost;
	
	private int mValueId = 0;
	
	/**
	 * Creates a new item out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aItemGroup
	 *            The parent item group.
	 * @param aContext
	 *            The underlying context.
	 * @param aParentItem
	 *            The parent item.
	 * @param aCharacter
	 *            The character.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided item.
	 */
	public ItemInstanceImpl(final Element aElement, final ItemGroupInstance aItemGroup, final Context aContext, final ItemInstance aParentItem,
			final CharacterInstance aCharacter, final MessageListener aMessageListener, final boolean aHost)
	{
		super(aCharacter, aItemGroup.getItemController());
		if (aParentItem == null)
		{
			mItem = aItemGroup.getItemGroup().getItem(aElement.getAttribute("name"));
		}
		else
		{
			mItem = aParentItem.getItem().getChild(aElement.getAttribute("name"));
		}
		mItemGroup = aItemGroup;
		mContext = aContext;
		mHost = aHost;
		if (getItem().needsDescription())
		{
			mDescription = CodingUtil.decode(aElement.getAttribute("description"));
		}
		else
		{
			mDescription = null;
		}
		final int id = mHost ? R.layout.host_item_instance : R.layout.client_item_instance;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		mMessageListener = aMessageListener;
		
		if (isValueItem())
		{
			mValueId = Integer.parseInt(aElement.getAttribute("value"));
			mAnimator = new ValueAnimator();
			mAnimator.setInterpolator(new AccelerateInterpolator());
			mAnimator.addUpdateListener(this);
		}
		else
		{
			mAnimator = null;
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
		
		for (final Action action : getItem().getActions())
		{
			mActions.add(new ActionInstanceImpl(action, getContext(), getCharacter(), mMessageListener, this));
		}
		for (final Dependency dependency : getItem().getDependencies())
		{
			if ( !dependency.isCreationDependency())
			{
				addDependency(new DependencyInstanceImpl(dependency, getCharacter()));
			}
		}
		
		mChildrenContainer = (LinearLayout) getContainer()
				.findViewById(mHost ? R.id.h_item_instance_children_list : R.id.c_item_instance_children_list);
		mValueText = (TextView) getContainer().findViewById(mHost ? R.id.h_item_instance_value_text : R.id.c_item_instance_value_text);
		mValueBar = (ProgressBar) getContainer().findViewById(mHost ? R.id.h_item_instance_value_bar : R.id.c_item_instance_value_bar);
		mIncreaseButton = (ImageButton) getContainer()
				.findViewById(mHost ? R.id.h_increase_item_instance_button : R.id.c_increase_item_instance_button);
		mDecreaseButton = mHost ? (ImageButton) getContainer().findViewById(R.id.h_decrease_item_instance_button) : null;
		mAddButton = mHost ? (ImageButton) getContainer().findViewById(R.id.h_add_item_instance_child_button) : null;
		final TextView name = (TextView) getContainer().findViewById(mHost ? R.id.h_item_instance_name_label : R.id.c_item_instance_name_label);
		final ImageButton remove = mHost ? (ImageButton) getContainer().findViewById(R.id.h_remove_item_instance_button) : null;
		
		name.setText(getItem().getDisplayName());
		name.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Toast.makeText(getContext(), getItem().getDescription(), Toast.LENGTH_LONG).show();
			}
		});
		
		if (isValueItem())
		{
			mIncreaseButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					increase(true, true);
				}
			});
			if (mHost)
			{
				mDecreaseButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						decrease(false);
					}
				});
			}
		}
		if (mHost && getItemGroup().isMutable())
		{
			remove.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					if (hasParentItem())
					{
						getParentItem().removeChild(getItem(), false);
					}
					else
					{
						getItemGroup().removeItem(getItem(), false);
					}
				}
			});
			if (getItem().isMutableParent())
			{
				mAddButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						addChild();
					}
				});
			}
		}
		
		for (final Element item : DataUtil.getChildren(aElement, "item"))
		{
			int pos = -1;
			if (hasOrder())
			{
				pos = Integer.parseInt(item.getAttribute("order"));
			}
			addChildSilent(new ItemInstanceImpl(item, getItemGroup(), getContext(), this, getCharacter(), mMessageListener, mHost), pos);
		}
		if (hasChildren() && !hasOrder())
		{
			sortChildren();
		}
	}
	
	/**
	 * Creates a new item instance.
	 * 
	 * @param aItem
	 *            The item type.
	 * @param aDescription
	 *            The item description if needed. May be null for non described items.
	 * @param aItemGroup
	 *            The parent item group.
	 * @param aContext
	 *            The underlying context.
	 * @param aCharacter
	 *            The character.
	 * @param aParentItem
	 *            The parent item.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided item.
	 */
	public ItemInstanceImpl(final Item aItem, final String aDescription, final ItemGroupInstance aItemGroup, final Context aContext,
			final CharacterInstance aCharacter, final ItemInstance aParentItem, final MessageListener aMessageListener, final boolean aHost)
	{
		super(aCharacter, aItemGroup.getItemController());
		mItem = aItem;
		mItemGroup = aItemGroup;
		mContext = aContext;
		mHost = aHost;
		if (getItem().needsDescription())
		{
			mDescription = aDescription;
		}
		else
		{
			mDescription = null;
		}
		final int id = mHost ? R.layout.host_item_instance : R.layout.client_item_instance;
		mContainer = (LinearLayout) View.inflate(getContext(), id, null);
		mMessageListener = aMessageListener;
		
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
		
		if (isValueItem())
		{
			mValueId = getStartValue();
			mAnimator = new ValueAnimator();
			mAnimator.setInterpolator(new AccelerateInterpolator());
			mAnimator.addUpdateListener(this);
		}
		else
		{
			mAnimator = null;
		}
		
		for (final Action action : getItem().getActions())
		{
			mActions.add(new ActionInstanceImpl(action, getContext(), getCharacter(), mMessageListener, this));
		}
		for (final Dependency dependency : getItem().getDependencies())
		{
			if ( !dependency.isCreationDependency())
			{
				addDependency(new DependencyInstanceImpl(dependency, getCharacter()));
			}
		}
		
		mChildrenContainer = (LinearLayout) getContainer()
				.findViewById(mHost ? R.id.h_item_instance_children_list : R.id.c_item_instance_children_list);
		mValueText = (TextView) getContainer().findViewById(mHost ? R.id.h_item_instance_value_text : R.id.c_item_instance_value_text);
		mValueBar = (ProgressBar) getContainer().findViewById(mHost ? R.id.h_item_instance_value_bar : R.id.c_item_instance_value_bar);
		mIncreaseButton = (ImageButton) getContainer()
				.findViewById(mHost ? R.id.h_increase_item_instance_button : R.id.c_increase_item_instance_button);
		mDecreaseButton = mHost ? (ImageButton) getContainer().findViewById(R.id.h_decrease_item_instance_button) : null;
		mAddButton = mHost ? (ImageButton) getContainer().findViewById(R.id.h_add_item_instance_child_button) : null;
		final TextView name = (TextView) getContainer().findViewById(mHost ? R.id.h_item_instance_name_label : R.id.c_item_instance_name_label);
		final ImageButton remove = mHost ? (ImageButton) getContainer().findViewById(R.id.h_remove_item_instance_button) : null;
		
		name.setText(getItem().getDisplayName());
		name.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Toast.makeText(getContext(), getItem().getDescription(), Toast.LENGTH_LONG).show();
			}
		});
		
		if (isValueItem())
		{
			mIncreaseButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					increase(true, true);
				}
			});
			if (mHost)
			{
				mDecreaseButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						decrease(false);
					}
				});
			}
		}
		if (mHost && ( !hasParentItem() && getItemGroup().isMutable() || hasParentItem() && getParentItem().getItem().isMutableParent()))
		{
			remove.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					if (hasParentItem())
					{
						getParentItem().removeChild(getItem(), false);
					}
					else
					{
						getItemGroup().removeItem(getItem(), false);
					}
				}
			});
			if (getItem().isMutableParent())
			{
				mAddButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						addChild();
					}
				});
			}
		}
	}
	
	/**
	 * Creates a new item out of the given item creation.
	 * 
	 * @param aItem
	 *            The item creation.
	 * @param aItemGroup
	 *            The parent item group.
	 * @param aParentItem
	 *            The parent item.
	 * @param aCharacter
	 *            The character.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided item.
	 */
	public ItemInstanceImpl(final ItemCreation aItem, final ItemGroupInstance aItemGroup, final ItemInstance aParentItem,
			final CharacterInstance aCharacter, final MessageListener aMessageListener, final boolean aHost)
	{
		super(aCharacter, aItemGroup.getItemController());
		mItem = aItem.getItem();
		mItemGroup = aItemGroup;
		mContext = aItem.getContext();
		mHost = aHost;
		mDescription = aItem.getDescription();
		final int id = mHost ? R.layout.host_item_instance : R.layout.client_item_instance;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		mMessageListener = aMessageListener;
		
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
		
		if (isValueItem())
		{
			mValueId = aItem.getValueId();
			mAnimator = new ValueAnimator();
			mAnimator.setInterpolator(new AccelerateInterpolator());
			mAnimator.addUpdateListener(this);
		}
		else
		{
			mAnimator = null;
		}
		
		for (final Action action : getItem().getActions())
		{
			mActions.add(new ActionInstanceImpl(action, getContext(), getCharacter(), mMessageListener, this));
		}
		for (final Dependency dependency : getItem().getDependencies())
		{
			if ( !dependency.isCreationDependency())
			{
				addDependency(new DependencyInstanceImpl(dependency, getCharacter()));
			}
		}
		
		mChildrenContainer = (LinearLayout) getContainer()
				.findViewById(mHost ? R.id.h_item_instance_children_list : R.id.c_item_instance_children_list);
		mValueText = (TextView) getContainer().findViewById(mHost ? R.id.h_item_instance_value_text : R.id.c_item_instance_value_text);
		mValueBar = (ProgressBar) getContainer().findViewById(mHost ? R.id.h_item_instance_value_bar : R.id.c_item_instance_value_bar);
		mIncreaseButton = (ImageButton) getContainer()
				.findViewById(mHost ? R.id.h_increase_item_instance_button : R.id.c_increase_item_instance_button);
		mDecreaseButton = mHost ? (ImageButton) getContainer().findViewById(R.id.h_decrease_item_instance_button) : null;
		mAddButton = mHost ? (ImageButton) getContainer().findViewById(R.id.h_add_item_instance_child_button) : null;
		final TextView name = (TextView) getContainer().findViewById(mHost ? R.id.h_item_instance_name_label : R.id.c_item_instance_name_label);
		final ImageButton remove = mHost ? (ImageButton) getContainer().findViewById(R.id.h_remove_item_instance_button) : null;
		
		name.setText(getItem().getDisplayName());
		name.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Toast.makeText(getContext(), getItem().getDescription(), Toast.LENGTH_LONG).show();
			}
		});
		
		if (isValueItem())
		{
			mIncreaseButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					increase(true, true);
				}
			});
			if (mHost)
			{
				mDecreaseButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						decrease(false);
					}
				});
			}
		}
		if (mHost && getItemGroup().isMutable())
		{
			remove.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					if (hasParentItem())
					{
						getParentItem().removeChild(getItem(), false);
					}
					else
					{
						getItemGroup().removeItem(getItem(), false);
					}
				}
			});
			if (getItem().isMutableParent())
			{
				mAddButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						addChild();
					}
				});
			}
		}
		
		if (aItem.hasChildren())
		{
			for (final ItemCreation item : aItem.getChildrenList())
			{
				if ( !aItem.isMutableParent() || item.isImportant())
				{
					addChildSilent(new ItemInstanceImpl(item, getItemGroup(), this, getCharacter(), aMessageListener, mHost), -1);
				}
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
		if ( !getItem().isMutableParent())
		{
			Log.w(TAG, "Tried to add a child to a non mutable item.");
			return;
		}
		if (SelectItemDialog.isDialogOpen())
		{
			return;
		}
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
				addChild(aChoosenItem, false);
			}
		};
		SelectItemDialog.showSelectionDialog(items, getContext().getString(R.string.add_item), getContext(), action);
	}
	
	@Override
	public void addChild(final Item aItem, final boolean aSilent)
	{
		if ( !getItem().isMutableParent())
		{
			Log.w(TAG, "Tried to add a child to a non parent or non mutable item.");
			return;
		}
		if (getChildrenList().contains(aItem))
		{
			Log.w(TAG, "Tried to add a already existing child.");
			return;
		}
		
		final List<ItemInstance> itemContainer = new ArrayList<ItemInstance>();
		if (aItem.needsDescription())
		{
			final StringCreationListener listener = new StringCreationListener()
			{
				@Override
				public void create(final String aString)
				{
					itemContainer.add(new ItemInstanceImpl(aItem, aString, getItemGroup(), getContext(), getCharacter(), ItemInstanceImpl.this,
							mMessageListener, mHost));
				}
			};
			CreateStringDialog.showCreateStringDialog("<EMPTY>", "Please enter the item description", getContext(), listener);
		}
		else
		{
			itemContainer.add(
					new ItemInstanceImpl(aItem, null, getItemGroup(), getContext(), getCharacter(), ItemInstanceImpl.this, mMessageListener, mHost));
		}
		final ItemInstance item = itemContainer.get(0);
		if (item == null)
		{
			return;
		}
		
		item.initActions();
		getChildrenList().add(item);
		mChildren.put(item.getName(), item);
		mChildrenContainer.addView(item.getContainer());
		getItemGroup().getItemController().addItem(item);
		getItemGroup().getItemController().resize();
		getItemGroup().getItemController().updateUI();
		if ( !mHost)
		{
			getItemGroup().getItemController().getCharacter().getActions().addActions(item.getActions());
		}
		
		if ( !aSilent)
		{
			final Builder builder = new Builder(R.string.added_item, getContext());
			builder.setArguments(aItem.getName()).setTranslated(true);
			mMessageListener.sendMessage(builder.create());
			mMessageListener.sendChange(new ItemChange(getName(), aItem.getName(), true));
		}
	}
	
	@Override
	public void addValueListener(final ItemValueListener aListener)
	{
		mValueListeners.add(aListener);
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
			item.setAttribute("description", CodingUtil.encode(getDescription()));
		}
		
		if (hasChildren())
		{
			if (hasOrder())
			{
				for (int i = 0; i < getChildrenList().size(); i++ )
				{
					final Element childElement = getChildrenList().get(i).asElement(aDoc);
					childElement.setAttribute("order", "" + i);
					item.appendChild(childElement);
				}
			}
			else
			{
				for (final ItemInstance child : getChildrenList())
				{
					item.appendChild(child.asElement(aDoc));
				}
			}
		}
		return item;
	}
	
	@Override
	public int calcEPCost()
	{
		if (getValue() == 0)
		{
			return getEPCostNew();
		}
		return getEPCost() + getValue() * getEPCostMulti();
	}
	
	@Override
	public boolean canDecrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to ask whether a non value item can be decreased.");
			return false;
		}
		return mValueId > Math.max(0, getMinValue(RestrictionInstanceType.ITEM_VALUE));
	}
	
	@Override
	public boolean canEPIncrease()
	{
		return getEPCost() != 0 || getEPCostMulti() != 0 || getEPCostNew() != 0;
	}
	
	@Override
	public boolean canIncrease()
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to ask whether a non value item can be increased.");
			return false;
		}
		if (mHost)
		{
			return mValueId < Math.min(getValues().length - 1, getMaxValue(RestrictionInstanceType.ITEM_VALUE));
		}
		if ( !hasEnoughEP())
		{
			return false;
		}
		if ( !getCharacter().isOnline())
		{
			return false;
		}
		if ( !getCharacter().getMode().getMode().canIncreaseItems())
		{
			return false;
		}
		if (getCharacter().isLowLevel())
		{
			return mValueId < Math.min(getMaxLowLevelValue(), getMaxValue(RestrictionInstanceType.ITEM_VALUE));
		}
		return mValueId < getMaxValue(RestrictionInstanceType.ITEM_VALUE);
	}
	
	@Override
	public int compareTo(final ItemInstance aAnother)
	{
		if (aAnother == null)
		{
			return getItem().compareTo(null);
		}
		return getItem().compareTo(aAnother.getItem());
	}
	
	@Override
	public void decrease(final boolean aSilent)
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to decrease a non value item.");
			return;
		}
		if (mValueId > 0)
		{
			mValueId-- ;
		}
		updateCharacter();
		updateValueListeners();
		if ( !aSilent)
		{
			mMessageListener.sendChange(new ItemChange(getName(), mValueId));
			if (mHost)
			{
				final Builder builder = new Builder(R.string.host_decreased, getContext());
				builder.setArguments(getName(), "" + getValue()).setTranslated(true, false);
				mMessageListener.sendMessage(builder.create());
			}
		}
	}
	
	@Override
	public int getAbsoluteValue()
	{
		return Math.abs(getValue());
	}
	
	@Override
	public Set<ActionInstance> getActions()
	{
		return mActions;
	}
	
	@Override
	public void initActions()
	{
		for (final ActionInstance action : getActions())
		{
			action.initDices();
		}
	}
	
	@Override
	public int getMaxDecreasure()
	{
		int valueId = mValueId;
		final int minValue = getMinValue(RestrictionInstanceType.ITEM_VALUE);
		while (valueId > 0 && getValues()[valueId] > minValue)
		{
			valueId-- ;
		}
		return mValueId - valueId;
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
	public String getDisplayName()
	{
		return getItem().getDisplayName();
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
	public int getEPCost()
	{
		if (hasRestrictions(RestrictionInstanceType.ITEM_EP_COST))
		{
			for (final RestrictionInstance restriction : getRestrictions(RestrictionInstanceType.ITEM_EP_COST))
			{
				return restriction.getValue();
			}
		}
		return getItem().getEPCost();
	}
	
	@Override
	public int getEPCostMulti()
	{
		if (hasRestrictions(RestrictionInstanceType.ITEM_EP_COST_MULTI))
		{
			for (final RestrictionInstance restriction : getRestrictions(RestrictionInstanceType.ITEM_EP_COST_MULTI))
			{
				return restriction.getValue();
			}
		}
		if (hasParentItem() && getParentItem().hasRestrictions(RestrictionInstanceType.ITEM_CHILD_EP_COST_MULTI))
		{
			for (final RestrictionInstance restriction : getParentItem().getRestrictions(RestrictionInstanceType.ITEM_CHILD_EP_COST_MULTI))
			{
				return restriction.getValue();
			}
		}
		return getItem().getEPCostMultiplicator();
	}
	
	@Override
	public int getEPCostNew()
	{
		if (hasRestrictions(RestrictionInstanceType.ITEM_EP_COST_NEW))
		{
			for (final RestrictionInstance restriction : getRestrictions(RestrictionInstanceType.ITEM_EP_COST_NEW))
			{
				return restriction.getValue();
			}
		}
		if (hasParentItem() && getParentItem().hasRestrictions(RestrictionInstanceType.ITEM_CHILD_EP_COST_NEW))
		{
			for (final RestrictionInstance restriction : getParentItem().getRestrictions(RestrictionInstanceType.ITEM_CHILD_EP_COST_NEW))
			{
				return restriction.getValue();
			}
		}
		return getItem().getEPCostNew();
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
	public int getMaxLowLevelValue()
	{
		int maxLowLevelValue = getItemGroup().getItemGroup().getMaxLowLevelValue();
		if (getItem().hasMaxLowLevelValue())
		{
			maxLowLevelValue = getItem().getMaxLowLevelValue();
		}
		return maxLowLevelValue;
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
	public ItemInstance getParentItem()
	{
		return mParentItem;
	}
	
	@Override
	public int getValue()
	{
		final int[] values = getValues();
		if (mValueId >= values.length)
		{
			mValueId = values.length - 1;
		}
		return values[mValueId];
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
		return getCharacter().getEPController().getExperience() >= calcEPCost();
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
	public void increase(final boolean aAsk, final boolean aCostEP)
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to increase a non value item.");
			return;
		}
		if ( !mHost && aCostEP && !hasEnoughEP())
		{
			return;
		}
		if ((mHost || !aAsk) && mValueId < getValues().length - 1)
		{
			mValueId++ ;
			updateCharacter();
			updateValueListeners();
			mMessageListener.sendChange(new ItemChange(getName(), mValueId));
		}
		if ( !aAsk && aCostEP)
		{
			getCharacter().getEPController().decreaseBy(calcEPCost());
		}
		if ( !aAsk)
		{
			return;
		}
		if (mHost)
		{
			final Builder builder = new Builder(R.string.host_increased, getContext());
			builder.setArguments(getName(), "" + getValue()).setTranslated(true, false);
			mMessageListener.sendMessage(builder.create());
		}
		else
		{
			final Builder builder = new Builder(R.string.ask_increase, getContext());
			builder.setGroup(MessageGroup.ITEM).setSender(getCharacter().getName()).setArguments(getName(), "" + getValues()[mValueId + 1]);
			builder.setTranslated(true, false).setType(MessageType.YES_NO).setYesAction(ButtonAction.ACCEPT_INCREASE);
			builder.setNoAction(ButtonAction.DENY_INCREASE).setSaveables(getName());
			mMessageListener.sendMessage(builder.create());
		}
	}
	
	@Override
	public int indexOfChild(final ItemInstance aItem)
	{
		return getChildrenList().indexOf(aItem);
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
	public void removeChild(final Item aItem, final boolean aSilent)
	{
		if ( !getItem().isMutableParent())
		{
			Log.w(TAG, "Tried to remove a child from a non parent or non mutable item.");
			return;
		}
		final ItemInstance item = mChildren.get(aItem.getName());
		item.release();
		getChildrenList().remove(item);
		mChildren.remove(item.getName());
		getItemGroup().getItemController().removeItem(aItem.getName());
		getItemGroup().getItemController().resize();
		getItemGroup().getItemController().updateUI();
		if ( !mHost)
		{
			getItemGroup().getItemController().getCharacter().getActions().removeActions(item.getActions());
		}
		
		if ( !aSilent)
		{
			final Builder builder = new Builder(R.string.removed_item, getContext());
			builder.setArguments(aItem.getName()).setTranslated(true);
			mMessageListener.sendMessage(builder.create());
			mMessageListener.sendChange(new ItemChange(getName(), aItem.getName(), false));
		}
	}
	
	@Override
	public void removeValueListener(final ItemValueListener aListener)
	{
		mValueListeners.remove(aListener);
	}
	
	@Override
	public void updateCharacter()
	{
		getCharacter().updateUI();
	}
	
	@Override
	public void updateUI()
	{
		if (isValueItem())
		{
			if (hasRestrictions(RestrictionInstanceType.ITEM_VALUE) && !isValueOk(getValue(), RestrictionInstanceType.ITEM_VALUE))
			{
				while (canIncrease() && getValue() < getMinValue(RestrictionInstanceType.ITEM_VALUE))
				{
					increase(false, false);
				}
				while (canDecrease() && getValue() > getMaxValue(RestrictionInstanceType.ITEM_VALUE))
				{
					decrease(true);
				}
			}
			while (canDecrease() && getValue() > getMaxValue())
			{
				decrease(true);
			}
		}
		
		final ImageButton remove = mHost ? (ImageButton) getContainer().findViewById(R.id.h_remove_item_instance_button) : null;
		final int buttonWidth = (int) getContext().getResources().getDimension(R.dimen.button_width);
		
		if (isValueItem())
		{
			if (mHost || canEPIncrease())
			{
				ViewUtil.setPxWidth(mIncreaseButton, buttonWidth);
			}
			else
			{
				ViewUtil.hideWidth(mIncreaseButton);
			}
			
			if (mAnimator.isRunning())
			{
				mAnimator.cancel();
			}
			mValueBar.setMax(VALUE_MULTIPLICATOR * Math.abs(getMaxValue()));
			mAnimator.setIntValues(mValueBar.getProgress(), VALUE_MULTIPLICATOR * getAbsoluteValue());
			mAnimator.start();
			mValueText.setText("" + getValue());
			ViewUtil.setEnabled(mIncreaseButton, canIncrease());
			if (mHost)
			{
				ViewUtil.setEnabled(mDecreaseButton, canDecrease());
			}
		}
		else
		{
			ViewUtil.hideHeight(mValueBar);
			ViewUtil.hideWidth(mIncreaseButton);
			if (mHost)
			{
				ViewUtil.hideWidth(mDecreaseButton);
			}
		}
		if (mHost)
		{
			if (getItemGroup().isMutable())
			{
				ViewUtil.setPxWidth(remove, buttonWidth);
				if (getItem().isMutableParent())
				{
					ViewUtil.setPxWidth(mAddButton, buttonWidth);
				}
				else
				{
					ViewUtil.hideWidth(mAddButton);
				}
			}
			else
			{
				ViewUtil.hideWidth(remove);
				ViewUtil.hideWidth(mAddButton);
			}
		}
		if (isParent())
		{
			if (mHost && getItemGroup().isMutable() && getItem().isMutableParent())
			{
				ViewUtil.setEnabled(mAddButton, !getAddableItems().isEmpty());
			}
		}
		
		if (hasChildren() && !hasOrder())
		{
			sortChildren();
		}
		
		if (isParent())
		{
			for (final ItemInstance item : getChildrenList())
			{
				item.updateUI();
			}
		}
	}
	
	@Override
	public void updateValue(final int aValue)
	{
		if ( !isValueItem())
		{
			Log.w(TAG, "Tried to update a non value item.");
			return;
		}
		mValueId = aValue;
		updateCharacter();
		updateValueListeners();
	}
	
	private void addChildSilent(final ItemInstance aItem, final int aPos)
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
		mChildren.put(aItem.getName(), aItem);
		if (aPos != -1)
		{
			getChildrenList().add(aPos, aItem);
			mChildrenContainer.addView(aItem.getContainer(), aPos);
		}
		else
		{
			getChildrenList().add(aItem);
			mChildrenContainer.addView(aItem.getContainer());
		}
		getItemGroup().getItemController().addItem(aItem);
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
		for (final ItemInstance item : getChildrenList())
		{
			item.release();
		}
		Collections.sort(getChildrenList());
		for (final ItemInstance item : getChildrenList())
		{
			item.updateUI();
			mChildrenContainer.addView(item.getContainer());
		}
	}
	
	private void updateValueListeners()
	{
		for (final ItemValueListener listener : mValueListeners)
		{
			listener.valueChanged();
		}
	}
}
