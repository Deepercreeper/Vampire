package com.deepercreeper.vampireapp.host.change;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;

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
	public static final String	TAG_NAME	= "item-change";
	
	private final String		mName;
	
	private final int			mValue;
	
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
	}
	
	@Override
	public void applyChange(final CharacterInstance aCharacter)
	{
		aCharacter.findItem(mName).updateValue(mValue);
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("name", mName);
		element.setAttribute("value", "" + mValue);
		return element;
	}
	
	@Override
	public String getType()
	{
		return TAG_NAME;
	}
}
