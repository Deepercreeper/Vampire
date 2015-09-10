package com.deepercreeper.vampireapp.items.implementations.instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.host.Message.Builder;
import com.deepercreeper.vampireapp.host.change.ItemGroupChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.implementations.instances.dependencies.DependableInstanceImpl;
import com.deepercreeper.vampireapp.items.implementations.instances.dependencies.DependencyInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.Dependency;
import com.deepercreeper.vampireapp.items.interfaces.Dependency.Type;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateStringDialog;
import com.deepercreeper.vampireapp.util.view.dialogs.SelectItemDialog;
import com.deepercreeper.vampireapp.util.view.listeners.ItemSelectionListener;
import com.deepercreeper.vampireapp.util.view.listeners.StringCreationListener;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A group instance implementation.
 * 
 * @author vrl
 */
public class ItemGroupInstanceImpl extends DependableInstanceImpl implements ItemGroupInstance
{
	private static final String TAG = "ItemGroupInstance";
	
	private final ItemGroup mItemGroup;
	
	private final LinearLayout mContainer;
	
	private final Context mContext;
	
	private final ItemControllerInstance mItemController;
	
	private final List<ItemInstance> mItemsList = new ArrayList<ItemInstance>();
	
	private final Map<Item, ItemInstance> mItems = new HashMap<Item, ItemInstance>();
	
	private final CharacterInstance mCharacter;
	
	private final boolean mHost;
	
	private final MessageListener mMessageListener;
	
	private final LinearLayout mItemsContainer;
	
	/**
	 * Creates a new group out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aItemController
	 *            The item controller.
	 * @param aContext
	 *            The underlying context.
	 * @param aCharacter
	 *            The character.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided group.
	 */
	public ItemGroupInstanceImpl(final Element aElement, final ItemControllerInstance aItemController, final Context aContext,
			final CharacterInstance aCharacter, final MessageListener aMessageListener, final boolean aHost)
	{
		mItemGroup = aItemController.getItemController().getGroup(aElement.getAttribute("name"));
		mItemController = aItemController;
		mContext = aContext;
		mHost = aHost;
		mMessageListener = aMessageListener;
		mCharacter = aCharacter;
		mContainer = (LinearLayout) View.inflate(getContext(), R.layout.view_item_group, null);
		
		for (final Dependency dependency : getItemGroup().getDependencies())
		{
			if ( !dependency.isCreationDependency())
			{
				addDependency(new DependencyInstanceImpl(dependency, getCharacter()));
			}
		}
		
		((TextView) getContainer().findViewById(R.id.view_item_group_name_label)).setText(getItemGroup().getDisplayName());
		final Button addButton = (Button) getContainer().findViewById(R.id.view_item_group_add_button);
		mItemsContainer = (LinearLayout) getContainer().findViewById(R.id.view_item_group_items_list);
		
		if (mHost && isMutable())
		{
			addButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					addItem();
				}
			});
		}
		else
		{
			ViewUtil.hideHeight(addButton);
		}
		
		for (final Element item : DataUtil.getChildren(aElement, "item"))
		{
			int pos = -1;
			if (hasOrder())
			{
				pos = Integer.parseInt(item.getAttribute("order"));
			}
			addItemSilent(new ItemInstanceImpl(item, this, getContext(), null, getCharacter(), mMessageListener, mHost), pos);
		}
		if ( !hasOrder())
		{
			sortItems();
		}
	}
	
	/**
	 * Creates a new group out of the given group creation.
	 * 
	 * @param aItemGroup
	 *            The group creation.
	 * @param aItemController
	 *            The item controller.
	 * @param aContext
	 *            The underlying context.
	 * @param aCharacter
	 *            The character.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            whether this is a host sided group.
	 */
	public ItemGroupInstanceImpl(final ItemGroupCreation aItemGroup, final ItemControllerInstance aItemController, final Context aContext,
			final CharacterInstance aCharacter, final MessageListener aMessageListener, final boolean aHost)
	{
		mItemGroup = aItemGroup.getItemGroup();
		mContext = aContext;
		mItemController = aItemController;
		mHost = aHost;
		mMessageListener = aMessageListener;
		mCharacter = aCharacter;
		mContainer = (LinearLayout) View.inflate(getContext(), R.layout.view_item_group, null);
		
		for (final Dependency dependency : getItemGroup().getDependencies())
		{
			if ( !dependency.isCreationDependency())
			{
				addDependency(new DependencyInstanceImpl(dependency, getCharacter()));
			}
		}
		
		((TextView) getContainer().findViewById(R.id.view_item_group_name_label)).setText(getItemGroup().getDisplayName());
		final Button addButton = (Button) getContainer().findViewById(R.id.view_item_group_add_button);
		mItemsContainer = (LinearLayout) getContainer().findViewById(R.id.view_item_group_items_list);
		
		if (mHost && isMutable())
		{
			addButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					addItem();
				}
			});
		}
		else
		{
			ViewUtil.hideHeight(addButton);
		}
		
		for (final ItemCreation item : aItemGroup.getItemsList())
		{
			if ( !getItemGroup().isMutable() || item.isImportant())
			{
				addItemSilent(new ItemInstanceImpl(item, this, null, getCharacter(), mMessageListener, mHost), -1);
			}
		}
		if ( !hasOrder())
		{
			sortItems();
		}
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
				addItem(aChoosenItem, false);
			}
		};
		SelectItemDialog.showSelectionDialog(items, getContext().getString(R.string.add_item), getContext(), action);
	}
	
	@Override
	public void addItem(final Item aItem, final boolean aSilent)
	{
		if ( !isMutable())
		{
			Log.w(TAG, "Tried to add an item to a non mutable group.");
			return;
		}
		if (mItems.containsKey(aItem))
		{
			Log.w(TAG, "Tried to add an already existing item.");
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
					itemContainer.add(new ItemInstanceImpl(aItem, aString, ItemGroupInstanceImpl.this, getContext(), getCharacter(), null,
							mMessageListener, mHost));
				}
			};
			CreateStringDialog.showCreateStringDialog("<EMPTY>", "Please enter the item description", getContext(), listener);
		}
		else
		{
			itemContainer
					.add(new ItemInstanceImpl(aItem, null, ItemGroupInstanceImpl.this, getContext(), getCharacter(), null, mMessageListener, mHost));
		}
		final ItemInstance item = itemContainer.get(0);
		if (item == null)
		{
			return;
		}
		
		item.initActions();
		getItemsList().add(item);
		mItems.put(aItem, item);
		mItemsContainer.addView(item.getContainer());
		getItemController().resize();
		getItemController().addItem(item);
		updateCharacterUI();
		if ( !mHost)
		{
			getItemController().getCharacter().getActions().addActions(item.getActions());
		}
		if ( !aSilent)
		{
			final Builder builder = new Builder(R.string.added_item, getContext());
			builder.setArguments(aItem.getName()).setTranslated(true);
			mMessageListener.sendMessage(builder.create());
			mMessageListener.sendChange(new ItemGroupChange(aItem.getName(), getName(), true));
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element group = aDoc.createElement("group");
		group.setAttribute("name", getName());
		for (int i = 0; i < getItemsList().size(); i++ )
		{
			final Element element = getItemsList().get(i).asElement(aDoc);
			if (hasOrder())
			{
				element.setAttribute("order", "" + i);
			}
			group.appendChild(element);
		}
		return group;
	}
	
	@Override
	public int compareTo(final ItemGroupInstance aAnother)
	{
		if (aAnother == null)
		{
			return getItemGroup().compareTo(null);
		}
		return getItemGroup().compareTo(aAnother.getItemGroup());
	}
	
	@Override
	public List<Item> getAddableItems()
	{
		final List<Item> items = new ArrayList<Item>();
		for (final Item item : getItemGroup().getItemsList())
		{
			if ( !hasItem(item))
			{
				items.add(item);
			}
		}
		return items;
	}
	
	@Override
	public CharacterInstance getCharacter()
	{
		return mCharacter;
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
	public List<ItemInstance> getDescriptionItems()
	{
		final List<ItemInstance> items = new ArrayList<ItemInstance>();
		for (final ItemInstance item : getItemsList())
		{
			if (item.hasDescription())
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
	public ItemInstance getItem(final Item aItem)
	{
		return mItems.get(aItem);
	}
	
	@Override
	public ItemInstance getItem(final String aName)
	{
		return getItem(getItemGroup().getItem(aName));
	}
	
	@Override
	public ItemControllerInstance getItemController()
	{
		return mItemController;
	}
	
	@Override
	public ItemGroup getItemGroup()
	{
		return mItemGroup;
	}
	
	@Override
	public List<ItemInstance> getItemsList()
	{
		return mItemsList;
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
	public int getValue()
	{
		if ( !isValueGroup())
		{
			Log.w(TAG, "Tried to get the value of a non value group.");
			return 0;
		}
		int value = 0;
		for (final ItemInstance item : getItemsList())
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
	public boolean hasItem(final ItemInstance aItem)
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
	public int indexOfItem(final ItemInstance aItem)
	{
		return getItemsList().indexOf(aItem);
	}
	
	@Override
	public boolean isMutable()
	{
		return getItemGroup().isHostMutable();
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
	public void removeItem(final Item aItem, final boolean aSilent)
	{
		if ( !isMutable())
		{
			Log.w(TAG, "Tried to add an item to a non mutable group.");
			return;
		}
		if (mItems.containsKey(aItem))
		{
			final ItemInstance item = mItems.get(aItem);
			getItemController().removeItem(aItem.getName());
			item.release();
			mItems.remove(aItem);
			getItemsList().remove(item);
			getItemController().resize();
			updateCharacterUI();
			if ( !mHost)
			{
				getItemController().getCharacter().getActions().removeActions(item.getActions());
			}
			
			if ( !aSilent)
			{
				final Builder builder = new Builder(R.string.removed_item, getContext());
				builder.setArguments(aItem.getName()).setTranslated(true);
				mMessageListener.sendMessage(builder.create());
				mMessageListener.sendChange(new ItemGroupChange(aItem.getName(), getName(), false));
			}
		}
	}
	
	@Override
	public String toString()
	{
		return getItemGroup().getDisplayName();
	}
	
	@Override
	public void updateCharacterUI()
	{
		getItemController().updateUI();
	}
	
	@Override
	public void updateUI()
	{
		if ( !hasOrder())
		{
			sortItems();
		}
		for (final ItemInstance item : getItemsList())
		{
			item.updateUI();
		}
	}
	
	private void addItemSilent(final ItemInstance aItem, final int aPos)
	{
		if (getItemsList().contains(aItem))
		{
			Log.w(TAG, "Tried to add a child to a group twice.");
			return;
		}
		mItems.put(aItem.getItem(), aItem);
		if (aPos != -1)
		{
			getItemsList().add(aPos, aItem);
			mItemsContainer.addView(aItem.getContainer(), aPos + 1);
		}
		else
		{
			getItemsList().add(aItem);
			mItemsContainer.addView(aItem.getContainer(), aPos + 1);
		}
		getItemController().resize();
		getItemController().addItem(aItem);
		updateCharacterUI();
	}
	
	private void sortItems()
	{
		for (final ItemInstance item : getItemsList())
		{
			item.release();
		}
		Collections.sort(getItemsList());
		for (final ItemInstance item : getItemsList())
		{
			mItemsContainer.addView(item.getContainer());
		}
	}
}
