package com.deepercreeper.vampireapp.host.change;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import com.deepercreeper.vampireapp.character.InventoryItem;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.util.FilesUtil;

/**
 * A inventory change.
 * 
 * @author Vincent
 */
public class InventoryChange implements CharacterChange
{
	/**
	 * The XML tag for inventory changes.
	 */
	public static final String	TAG_NAME	= "inventory-change";
	
	private final InventoryItem	mItem;
	
	private final boolean		mAdded;
	
	/**
	 * The inventory has added or removed an item.
	 * 
	 * @param aItem
	 *            The item.
	 * @param aAdded
	 *            Whether the item was added or removed.
	 */
	public InventoryChange(final InventoryItem aItem, final boolean aAdded)
	{
		mItem = aItem;
		mAdded = aAdded;
	}
	
	/**
	 * Creates an inventory change out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 * @param aContext
	 *            The underlying context.
	 */
	public InventoryChange(final Element aElement, final Context aContext)
	{
		mAdded = Boolean.valueOf(aElement.getAttribute("added"));
		final Element itemElement = (Element) FilesUtil.loadDocument(aElement.getAttribute("item")).getElementsByTagName("item").item(0);
		mItem = new InventoryItem(itemElement, aContext, null);
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("added", "" + mAdded);
		element.setAttribute("item", FilesUtil.serialize(mItem));
		return element;
	}
	
	@Override
	public void applyChange(final CharacterInstance aCharacter)
	{
		if (mAdded)
		{
			aCharacter.getInventory().addItem(mItem, true);
		}
		else
		{
			aCharacter.getInventory().removeItem(mItem, true);
		}
	}
	
	@Override
	public String getType()
	{
		return TAG_NAME;
	}
	
}
