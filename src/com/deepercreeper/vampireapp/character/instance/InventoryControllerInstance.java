package com.deepercreeper.vampireapp.character.instance;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.Inventory;
import com.deepercreeper.vampireapp.character.InventoryItem;
import com.deepercreeper.vampireapp.character.Weapon;
import com.deepercreeper.vampireapp.host.Message;
import com.deepercreeper.vampireapp.host.Message.ButtonAction;
import com.deepercreeper.vampireapp.host.change.InventoryChange;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance.ItemValueListener;
import com.deepercreeper.vampireapp.util.FilesUtil;
import com.deepercreeper.vampireapp.util.ItemFinder;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Expander;
import com.deepercreeper.vampireapp.util.view.ResizeListener;
import com.deepercreeper.vampireapp.util.view.Viewable;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateInventoryItemDialog;
import com.deepercreeper.vampireapp.util.view.dialogs.CreateInventoryItemDialog.ItemCreationListener;

/**
 * This controller is used to control the whole character inventory.<br>
 * It contains weight management, an items list and their usability.
 * 
 * @author vrl
 */
public class InventoryControllerInstance implements Saveable, ItemValueListener, Viewable
{
	private static final String			TAG				= "InventoryController";
	
	private final Inventory				mInventory;
	
	private final List<InventoryItem>	mItemsList		= new ArrayList<InventoryItem>();
	
	private final ItemFinder			mItems;
	
	private final Context				mContext;
	
	private final LinearLayout			mContainer;
	
	private final ItemInstance			mMaxWeightItem;
	
	private final boolean				mHost;
	
	private final ResizeListener		mResizeListener;
	
	private final MessageListener		mMessageListener;
	
	private final CharacterInstance		mChar;
	
	private final Expander				mExpander;
	
	private LinearLayout				mInventoryList;
	
	private TextView					mWeightLabel;
	
	private TextView					mMaxWeightLabel;
	
	private ImageButton					mAddItemButton;
	
	private int							mWeight			= 0;
	
	private int							mMaxWeight;
	
	private boolean						mInitialized	= false;
	
	/**
	 * Creates a new inventory controller from the given XML document.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aInventory
	 *            The default inventory settings.
	 * @param aItems
	 *            An item finder, that provides items by search name.
	 * @param aContext
	 *            The underlying context.
	 * @param aResizeListener
	 *            The parent resize listener.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aChar
	 *            The character.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 */
	public InventoryControllerInstance(final Element aElement, final Inventory aInventory, final ItemFinder aItems, final Context aContext,
			final ResizeListener aResizeListener, final MessageListener aMessageListener, final CharacterInstance aChar, final boolean aHost)
	{
		mInventory = aInventory;
		mItems = aItems;
		mContext = aContext;
		mResizeListener = aResizeListener;
		mMessageListener = aMessageListener;
		mChar = aChar;
		mHost = aHost;
		
		mMaxWeightItem = mItems.findItem(mInventory.getMaxWeightItem());
		mMaxWeightItem.addValueListener(this);
		
		updateMaxWeight();
		
		final int id = mHost ? R.layout.host_inventory : R.layout.client_inventory;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		
		mExpander = Expander.handle(mHost ? R.id.h_inventory_button : R.id.c_inventory_button, mHost ? R.id.h_inventory_panel
				: R.id.c_inventory_panel, mContext, mResizeListener);
		
		init();
		
		for (int i = 0; i < aElement.getChildNodes().getLength(); i++ )
		{
			if (aElement.getChildNodes().item(i) instanceof Element)
			{
				final Element child = (Element) aElement.getChildNodes().item(i);
				if (child.getTagName().equals("item"))
				{
					addItem(new InventoryItem(child, mContext, this), true);
				}
				else if (child.getTagName().equals("weapon"))
				{
					addItem(new Weapon(child, mItems, mContext, this), true);
				}
			}
		}
	}
	
	/**
	 * Creates a new inventory controller.
	 * 
	 * @param aInventory
	 *            The default inventory settings.
	 * @param aItems
	 *            An item finder, that provides all items by their name.
	 * @param aContext
	 *            The underlying context.
	 * @param aResizeListener
	 *            The parent resize listener.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aChar
	 *            The character.
	 * @param aHost
	 *            Whether this is a host sided controller.
	 */
	public InventoryControllerInstance(final Inventory aInventory, final ItemFinder aItems, final Context aContext,
			final ResizeListener aResizeListener, final MessageListener aMessageListener, final CharacterInstance aChar, final boolean aHost)
	{
		mInventory = aInventory;
		mItems = aItems;
		mContext = aContext;
		mResizeListener = aResizeListener;
		mMessageListener = aMessageListener;
		mChar = aChar;
		mHost = aHost;
		
		mMaxWeightItem = mItems.findItem(mInventory.getMaxWeightItem());
		mMaxWeightItem.addValueListener(this);
		
		updateMaxWeight();
		
		final int id = mHost ? R.layout.host_inventory : R.layout.client_inventory;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		
		mExpander = Expander.handle(mHost ? R.id.h_inventory_button : R.id.c_inventory_button, mHost ? R.id.h_inventory_panel
				: R.id.c_inventory_panel, mContext, mResizeListener);
		
		init();
	}
	
	/**
	 * Asks the user to create a new inventory item.
	 */
	public void addItem()
	{
		final ItemCreationListener listener = new ItemCreationListener()
		{
			@Override
			public void itemCreated(final InventoryItem aItem)
			{
				mMessageListener.sendMessage(new Message(mChar.getName(), R.string.got_item, new String[] { aItem.getInfo(true) }, mContext, null,
						ButtonAction.TAKE_ITEM, ButtonAction.IGNORE_ITEM, FilesUtil.serialize(aItem)));
			}
		};
		CreateInventoryItemDialog.showCreateInventoryItemDialog(mContext.getString(R.string.create_item), mContext, listener);
	}
	
	/**
	 * Adds a new inventory item to this inventory, if possible.
	 * 
	 * @param aItem
	 *            The item to add.
	 * @param aSilent
	 *            Whether this is a silent addition.
	 */
	public void addItem(final InventoryItem aItem, final boolean aSilent)
	{
		if ( !canAddItem(aItem))
		{
			Log.w(TAG, "Tried to add an item that weights too much.");
		}
		if (mItemsList.contains(aItem))
		{
			final InventoryItem existingItem = mItemsList.get(mItemsList.indexOf(aItem));
			existingItem.increase(aItem.getQuantity());
		}
		else
		{
			mItemsList.add(aItem);
			mWeight += aItem.getWeight() * aItem.getQuantity();
			setWeight();
			aItem.init();
			mInventoryList.addView(aItem.getContainer());
			aItem.addTo(this);
			if (mExpander != null)
			{
				mExpander.resize();
			}
		}
		if ( !aSilent)
		{
			mMessageListener.sendChange(new InventoryChange(aItem, true));
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("inventory");
		for (final InventoryItem item : mItemsList)
		{
			element.appendChild(item.asElement(aDoc));
		}
		return element;
	}
	
	/**
	 * @param aItem
	 *            The item to add.
	 * @return whether the given item can be added to this inventory.<br>
	 *         Referring to the weight properties.
	 */
	public boolean canAddItem(final InventoryItem aItem)
	{
		return getWeight() + aItem.getWeight() * aItem.getQuantity() <= getMaxWeight();
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	/**
	 * @return the current maximum weight that can be ported.
	 */
	public int getMaxWeight()
	{
		return mMaxWeight;
	}
	
	/**
	 * @return the current weight of items that is ported inside this inventory.
	 */
	public int getWeight()
	{
		return mWeight;
	}
	
	@Override
	public void init()
	{
		if ( !mInitialized)
		{
			mExpander.init();
			
			mInventoryList = (LinearLayout) getContainer().findViewById(mHost ? R.id.h_inventory_list : R.id.c_inventory_list);
			mWeightLabel = (TextView) getContainer().findViewById(mHost ? R.id.h_weight_label : R.id.c_weight_label);
			mMaxWeightLabel = (TextView) getContainer().findViewById(mHost ? R.id.h_max_weight_label : R.id.c_max_weight_label);
			mAddItemButton = mHost ? (ImageButton) getContainer().findViewById(R.id.h_add_item_button) : null;
			
			if (mHost)
			{
				mAddItemButton.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View aV)
					{
						addItem();
					}
				});
			}
			
			setWeight();
			
			mInitialized = true;
		}
		
		for (final InventoryItem item : mItemsList)
		{
			item.release();
		}
		for (final InventoryItem item : mItemsList)
		{
			item.init();
			mInventoryList.addView(item.getContainer());
		}
		
		mExpander.close();
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
		for (final InventoryItem item : mItemsList)
		{
			item.release();
		}
	}
	
	/**
	 * Removes the given item from this inventory.
	 * 
	 * @param aItem
	 *            The item to remove.
	 * @param aSilent
	 *            Whether this is a silent remove.
	 */
	public void removeItem(final InventoryItem aItem, final boolean aSilent)
	{
		final InventoryItem existingItem = mItemsList.get(mItemsList.indexOf(aItem));
		if (existingItem.getQuantity() > 1)
		{
			existingItem.decrease();
		}
		else
		{
			mItemsList.remove(existingItem);
			mWeight -= existingItem.getWeight();
			setWeight();
			existingItem.release();
			mExpander.resize();
		}
		if ( !aSilent)
		{
			mMessageListener.sendChange(new InventoryChange(aItem, false));
		}
		if ( !mHost)
		{
			mMessageListener.sendMessage(new Message(mChar.getName(), R.string.left_item, new String[] { aItem.getInfo(false) }, mContext, null,
					ButtonAction.NOTHING));
		}
	}
	
	/**
	 * Sets the new maximum weight that can be ported.
	 * 
	 * @param aMaxWeight
	 *            The new maximum weight.
	 */
	public void setMaxWeight(final int aMaxWeight)
	{
		mMaxWeight = aMaxWeight;
		setWeight();
	}
	
	/**
	 * Recalculates the maximum weight out of the weight defining item of the character.
	 */
	public void updateMaxWeight()
	{
		mMaxWeight = mInventory.getMaxWeightOf(mMaxWeightItem.getValue());
	}
	
	@Override
	public void valueChanged()
	{
		updateMaxWeight();
		setWeight();
	}
	
	/**
	 * Recalculates the current weight of all items.
	 */
	public void updateWeight()
	{
		mWeight = 0;
		for (final InventoryItem item : mItemsList)
		{
			mWeight += item.getWeight() * item.getQuantity();
		}
		setWeight();
	}
	
	private void setWeight()
	{
		mWeightLabel.setText(mContext.getString(R.string.weight) + ": " + mWeight + " " + mContext.getString(R.string.weight_unit));
		mMaxWeightLabel.setText(mContext.getString(R.string.max_weight) + ": " + mMaxWeight + " " + mContext.getString(R.string.weight_unit));
	}
}
