package com.deepercreeper.vampireapp.host.change;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemGroupInstance;

/**
 * An item addition or removal change.
 * 
 * @author Vincent
 */
public class ItemGroupChange implements CharacterChange
{
	/**
	 * The XML tag for item group changes.
	 */
	public static final String TAG_NAME = "item-group-change";
	
	private final String mItemName;
	
	private final String mGroupName;
	
	private final boolean mAdded;
	
	/**
	 * Creates a new item group change.
	 * 
	 * @param aItemName
	 *            The item name.
	 * @param aGroupName
	 *            The group name.
	 * @param aAdded
	 *            Whether the item was added or removed.
	 */
	public ItemGroupChange(final String aItemName, final String aGroupName, final boolean aAdded)
	{
		mItemName = aItemName;
		mGroupName = aGroupName;
		mAdded = aAdded;
	}
	
	/**
	 * Creates a new item group change out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 */
	public ItemGroupChange(final Element aElement)
	{
		mItemName = aElement.getAttribute("item");
		mGroupName = aElement.getAttribute("group");
		mAdded = Boolean.valueOf(aElement.getAttribute("added"));
	}
	
	@Override
	public void applyChange(final CharacterInstance aCharacter)
	{
		final ItemGroupInstance group = aCharacter.findGroupInstance(mGroupName);
		if (mAdded)
		{
			group.addItem(aCharacter.findItem(mItemName), true);
		}
		else
		{
			group.removeItem(aCharacter.findItem(mItemName), true);
		}
	}
	
	@Override
	public String getType()
	{
		return TAG_NAME;
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("item", mItemName);
		element.setAttribute("group", mGroupName);
		element.setAttribute("added", "" + mAdded);
		return element;
	}
}
