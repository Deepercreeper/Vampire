package com.deepercreeper.vampireapp.character.controllers;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import com.deepercreeper.vampireapp.character.InventoryItem;
import com.deepercreeper.vampireapp.character.Weapon;
import com.deepercreeper.vampireapp.mechanics.Action.ItemFinder;
import com.deepercreeper.vampireapp.util.Log;
import com.deepercreeper.vampireapp.util.Saveable;

public class InventoryController implements Saveable
{
	private static final String			TAG			= "InventoryController";
	
	private final List<InventoryItem>	mItemsList	= new ArrayList<InventoryItem>();
	
	private final ItemFinder			mItems;
	
	private final Context				mContext;
	
	private int							mWeight		= 0;
	
	private int							mMaxWeight;
	
	public InventoryController(final int aMaxWeight, final ItemFinder aItems, final Context aContext)
	{
		mMaxWeight = aMaxWeight;
		mItems = aItems;
		mContext = aContext;
	}
	
	public InventoryController(final Element aElement, final ItemFinder aItems, final Context aContext)
	{
		mMaxWeight = Integer.parseInt(aElement.getAttribute("maxWeight"));
		mItems = aItems;
		mContext = aContext;
		
		for (int i = 0; i < aElement.getChildNodes().getLength(); i++ )
		{
			if (aElement.getChildNodes().item(i) instanceof Element)
			{
				final Element child = (Element) aElement.getChildNodes().item(i);
				if (child.getTagName().equals("item"))
				{
					addItem(new InventoryItem(child, mContext));
				}
				else if (child.getTagName().equals("weapon"))
				{
					addItem(new Weapon(child, mItems, mContext));
				}
			}
		}
	}
	
	public void setMaxWeight(final int aMaxWeight)
	{
		mMaxWeight = aMaxWeight;
	}
	
	public int getMaxWeight()
	{
		return mMaxWeight;
	}
	
	public boolean canAddItem(final InventoryItem aItem)
	{
		return getWeight() + aItem.getWeight() <= getMaxWeight();
	}
	
	public void addItem(final InventoryItem aItem)
	{
		if ( !canAddItem(aItem))
		{
			Log.w(TAG, "Tried to add an item that weights too much.");
		}
		mItemsList.add(aItem);
		mWeight += aItem.getWeight();
	}
	
	public void removeItem(final InventoryItem aItem)
	{
		mItemsList.remove(aItem);
		mWeight -= aItem.getWeight();
	}
	
	public int getWeight()
	{
		return mWeight;
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("inventory");
		element.setAttribute("maxWeight", "" + getMaxWeight());
		for (final InventoryItem item : mItemsList)
		{
			element.appendChild(item.asElement(aDoc));
		}
		return element;
	}
}
