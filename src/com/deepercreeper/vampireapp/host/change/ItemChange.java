package com.deepercreeper.vampireapp.host.change;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.interfaces.Item;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;

/**
 * An item change.
 * 
 * @author Vincent
 */
public class ItemChange implements CharacterChange
{
	/**
	 * The XML tag for item changes.
	 */
	public static final String TAG_NAME = "item-change";
	
	private final String mName;
	
	private final String mChild;
	
	private final int mValue;
	
	private final boolean mAdded;
	
	/**
	 * Creates a new item change.
	 * 
	 * @param aName
	 *            The item name.
	 * @param aValue
	 *            The new item value.
	 */
	public ItemChange(final String aName, final int aValue)
	{
		mName = aName;
		mValue = aValue;
		mChild = null;
		mAdded = false;
	}
	
	/**
	 * Creates a new item change that tells that a child was added or removed.
	 * 
	 * @param aName
	 *            The parent item name.
	 * @param aChild
	 *            The child item name.
	 * @param aAdded
	 *            Whether the child was added or removed.
	 */
	public ItemChange(final String aName, final String aChild, final boolean aAdded)
	{
		mName = aName;
		mValue = -1;
		mChild = aChild;
		mAdded = aAdded;
	}
	
	/**
	 * Creates a new item change out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 */
	public ItemChange(final Element aElement)
	{
		mName = aElement.getAttribute("name");
		mValue = Integer.parseInt(aElement.getAttribute("value"));
		if (mValue == -1)
		{
			mChild = aElement.getAttribute("child");
			mAdded = Boolean.valueOf(aElement.getAttribute("added"));
		}
		else
		{
			mChild = null;
			mAdded = false;
		}
	}
	
	@Override
	public void applyChange(final CharacterInstance aCharacter)
	{
		if (mValue != -1)
		{
			aCharacter.findItemInstance(mName).updateValue(mValue);
		}
		else
		{
			final ItemInstance item = aCharacter.findItemInstance(mName);
			final Item child = aCharacter.findItem(mName).getChild(mChild);
			if (mAdded)
			{
				item.addChild(child, true);
			}
			else
			{
				item.removeChild(child, true);
			}
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("name", mName);
		element.setAttribute("value", "" + mValue);
		if (mChild != null)
		{
			element.setAttribute("child", mChild);
			element.setAttribute("added", "" + mAdded);
		}
		return element;
	}
	
	@Override
	public String getType()
	{
		return TAG_NAME;
	}
}
