package com.deepercreeper.vampireapp.items.implementations.instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.EPControllerInstance;
import com.deepercreeper.vampireapp.character.instance.Mode;
import com.deepercreeper.vampireapp.host.change.ItemGroupChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.ItemGroup;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemCreation;
import com.deepercreeper.vampireapp.items.interfaces.creations.ItemGroupCreation;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
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
 * A group instance implementation.
 * 
 * @author vrl
 */
public class ItemGroupInstanceImpl implements ItemGroupInstance
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
	
	private LinearLayout mItemsContainer;
	
	private boolean mInitialized = false;
	
	private Mode mMode;
	
	private final EPControllerInstance mEP;
	
	/**
	 * Creates a new group out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aItemController
	 *            The item controller.
	 * @param aContext
	 *            The underlying context.
	 * @param aMode
	 *            The character mode.
	 * @param aEP
	 *            The experience controller.
	 * @param aCharacter
	 *            The character.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided group.
	 */
	public ItemGroupInstanceImpl(final Element aElement, final ItemControllerInstance aItemController, final Context aContext, final Mode aMode,
			final EPControllerInstance aEP, final CharacterInstance aCharacter, final MessageListener aMessageListener, final boolean aHost)
	{
		mItemGroup = aItemController.getItemController().getGroup(aElement.getAttribute("name"));
		mItemController = aItemController;
		mContext = aContext;
		mMode = aMode;
		mHost = aHost;
		mEP = aEP;
		mMessageListener = aMessageListener;
		mCharacter = aCharacter;
		mContainer = (LinearLayout) View.inflate(getContext(), R.layout.item_group_view, null);
		
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
				int pos = -1;
				if (hasOrder())
				{
					pos = Integer.parseInt(item.getAttribute("order"));
				}
				addItemSilent(new ItemInstanceImpl(item, this, getContext(), getMode(), getEP(), null, getCharacter(), mMessageListener, mHost), pos);
			}
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
	 * @param aMode
	 *            The character mode.
	 * @param aEP
	 *            The experience controller.
	 * @param aCharacter
	 *            The character.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            whether this is a host sided group.
	 */
	public ItemGroupInstanceImpl(final ItemGroupCreation aItemGroup, final ItemControllerInstance aItemController, final Context aContext,
			final Mode aMode, final EPControllerInstance aEP, final CharacterInstance aCharacter, final MessageListener aMessageListener,
			final boolean aHost)
	{
		mItemGroup = aItemGroup.getItemGroup();
		mContext = aContext;
		mItemController = aItemController;
		mMode = aMode;
		mHost = aHost;
		mEP = aEP;
		mMessageListener = aMessageListener;
		mCharacter = aCharacter;
		mContainer = (LinearLayout) View.inflate(getContext(), R.layout.item_group_view, null);
		
		init();
		
		for (final ItemCreation item : aItemGroup.getItemsList())
		{
			if ( !getItemGroup().isMutable() || item.isImportant())
			{
				addItemSilent(new ItemInstanceImpl(item, this, getMode(), getEP(), null, getCharacter(), mMessageListener, mHost), -1);
			}
		}
		if ( !hasOrder())
		{
			sortItems();
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
	public boolean hasOrder()
	{
		return getItemGroup().hasOrder();
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
			updateController();
			
			if ( !aSilent)
			{
				mMessageListener.sendChange(new ItemGroupChange(aItem.getName(), getName(), false));
			}
		}
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
		final ItemInstance item = new ItemInstanceImpl(aItem, this, getContext(), getCharacter(), null, mMessageListener, mHost);
		getItemsList().add(item);
		mItems.put(aItem, item);
		mItemsContainer.addView(item.getContainer());
		getItemController().resize();
		getItemController().addItem(item);
		updateController();
		if ( !aSilent)
		{
			mMessageListener.sendChange(new ItemGroupChange(aItem.getName(), getName(), true));
		}
	}
	
	@Override
	public boolean isMutable()
	{
		return getItemGroup().isHostMutable();
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
	public EPControllerInstance getEP()
	{
		return mEP;
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
	public Mode getMode()
	{
		return mMode;
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
	public int indexOfItem(final ItemInstance aItem)
	{
		return getItemsList().indexOf(aItem);
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
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
			
			mInitialized = true;
		}
		
		if ( !hasOrder())
		{
			sortItems();
		}
		else
		{
			for (final ItemInstance item : getItemsList())
			{
				item.init();
				mItemsContainer.addView(item.getContainer());
			}
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
	public boolean isValueGroup()
	{
		return getItemGroup().isValueGroup();
	}
	
	@Override
	public void release()
	{
		for (final ItemInstance item : getItemsList())
		{
			item.release();
		}
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void setMode(final Mode aMode)
	{
		mMode = aMode;
		for (final ItemInstance item : getItemsList())
		{
			item.setMode(aMode);
		}
	}
	
	@Override
	public String toString()
	{
		return getItemGroup().getDisplayName();
	}
	
	@Override
	public void updateController()
	{
		getItemController().updateGroups();
	}
	
	@Override
	public void updateItems()
	{
		for (final ItemInstance item : getItemsList())
		{
			if (item.isValueItem() || item.isParent())
			{
				item.updateButtons();
			}
		}
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
			item.init();
			mItemsContainer.addView(item.getContainer());
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
		updateController();
	}
}
