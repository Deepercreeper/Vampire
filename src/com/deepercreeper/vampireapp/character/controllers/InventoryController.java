package com.deepercreeper.vampireapp.character.controllers;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.Inventory;
import com.deepercreeper.vampireapp.character.InventoryItem;
import com.deepercreeper.vampireapp.character.Weapon;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance.ItemValueListener;
import com.deepercreeper.vampireapp.mechanics.Action.ItemFinder;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.ResizeHeightAnimation;

public class InventoryController implements Saveable, ItemValueListener
{
	private static final String			TAG				= "InventoryController";
	
	private final Inventory				mInventory;
	
	private final List<InventoryItem>	mItemsList		= new ArrayList<InventoryItem>();
	
	private final ItemFinder			mItems;
	
	private final Context				mContext;
	
	private final LinearLayout			mContainer;
	
	private final ItemInstance			mMaxWeightItem;
	
	private TextView					mWeightLabel;
	
	private TextView					mMaxWeightLabel;
	
	private Button						mInventoryButton;
	
	private LinearLayout				mInventoryContainer;
	
	private int							mWeight			= 0;
	
	private int							mMaxWeight;
	
	private boolean						mOpen			= false;
	
	private boolean						mInitialized	= false;
	
	public InventoryController(final Element aElement, final Inventory aInventory, final ItemFinder aItems, final Context aContext)
	{
		mInventory = aInventory;
		mItems = aItems;
		mContext = aContext;
		
		mMaxWeightItem = mItems.findItem(mInventory.getMaxWeightItem());
		mMaxWeightItem.addValueListener(this);
		
		updateMaxWeight();
		
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.inventory, null);
		
		init();
		
		for (int i = 0; i < aElement.getChildNodes().getLength(); i++ )
		{
			if (aElement.getChildNodes().item(i) instanceof Element)
			{
				final Element child = (Element) aElement.getChildNodes().item(i);
				if (child.getTagName().equals("item"))
				{
					addItem(new InventoryItem(child, mContext, this));
				}
				else if (child.getTagName().equals("weapon"))
				{
					addItem(new Weapon(child, mItems, mContext, this));
				}
			}
		}
	}
	
	public InventoryController(final Inventory aInventory, final ItemFinder aItems, final Context aContext)
	{
		mInventory = aInventory;
		mItems = aItems;
		mContext = aContext;
		
		mMaxWeightItem = mItems.findItem(mInventory.getMaxWeightItem());
		mMaxWeightItem.addValueListener(this);
		
		updateMaxWeight();
		
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.inventory, null);
		
		init();
	}
	
	public void addItem(final InventoryItem aItem)
	{
		if ( !canAddItem(aItem))
		{
			Log.w(TAG, "Tried to add an item that weights too much.");
		}
		mItemsList.add(aItem);
		mWeight += aItem.getWeight();
		setWeight();
		aItem.init();
		mInventoryContainer.addView(aItem.getContainer());
		resize();
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
	
	public boolean canAddItem(final InventoryItem aItem)
	{
		return getWeight() + aItem.getWeight() <= getMaxWeight();
	}
	
	public void close()
	{
		mOpen = false;
		mInventoryButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		resize();
	}
	
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	public int getMaxWeight()
	{
		return mMaxWeight;
	}
	
	public int getWeight()
	{
		return mWeight;
	}
	
	public void init()
	{
		if ( !mInitialized)
		{
			getContainer().setLayoutParams(ViewUtil.getWrapHeight());
			
			mInventoryButton = (Button) getContainer().findViewById(R.id.inventory_button);
			mWeightLabel = (TextView) getContainer().findViewById(R.id.weight);
			mMaxWeightLabel = (TextView) getContainer().findViewById(R.id.max_weight);
			mInventoryContainer = (LinearLayout) getContainer().findViewById(R.id.inventory_panel);
			
			mInventoryButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					toggleOpen();
				}
			});
			
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
			mInventoryContainer.addView(item.getContainer());
		}
		
		close();
	}
	
	public void release()
	{
		ViewUtil.release(getContainer());
		for (final InventoryItem item : mItemsList)
		{
			item.release();
		}
	}
	
	public void removeItem(final InventoryItem aItem)
	{
		mItemsList.remove(aItem);
		mWeight -= aItem.getWeight();
		setWeight();
		aItem.release();
		resize();
	}
	
	public void resize()
	{
		if (mInventoryContainer.getAnimation() != null && !mInventoryContainer.getAnimation().hasEnded())
		{
			mInventoryContainer.getAnimation().cancel();
		}
		int height = 0;
		if (mOpen)
		{
			height = ViewUtil.calcHeight(mInventoryContainer);
		}
		if (height != mInventoryContainer.getHeight())
		{
			mInventoryContainer.startAnimation(new ResizeHeightAnimation(mInventoryContainer, height));
		}
	}
	
	public void setMaxWeight(final int aMaxWeight)
	{
		mMaxWeight = aMaxWeight;
		setWeight();
	}
	
	public void toggleOpen()
	{
		mOpen = !mOpen;
		if (mOpen)
		{
			mInventoryButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
		}
		else
		{
			mInventoryButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
		}
		resize();
	}
	
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
	
	private void setWeight()
	{
		mWeightLabel.setText(mContext.getString(R.string.weight) + ": " + mWeight + " " + mContext.getString(R.string.weight_unit));
		mMaxWeightLabel.setText(mContext.getString(R.string.max_weight) + ": " + mMaxWeight + " " + mContext.getString(R.string.weight_unit));
	}
}
