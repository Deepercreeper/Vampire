package com.deepercreeper.vampireapp.host.change;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;

/**
 * A change at the experience value.
 * 
 * @author Vincent
 */
public class EPChange implements CharacterChange
{
	/**
	 * The XML tag for experience changes.
	 */
	public static final String	TAG_NAME	= "ep-change";
	
	private final int			mValue;
	
	/**
	 * Creates a new experience change.
	 * 
	 * @param aValue
	 *            The new experience value.
	 */
	public EPChange(final int aValue)
	{
		mValue = aValue;
	}
	
	/**
	 * Creates a new experience change out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 */
	public EPChange(final Element aElement)
	{
		mValue = Integer.parseInt(aElement.getAttribute("value"));
	}
	
	@Override
	public void applyChange(final CharacterInstance aCharacter)
	{
		aCharacter.getEPController().updateValue(mValue);
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("value", "" + mValue);
		return element;
	}
	
	@Override
	public String getType()
	{
		return TAG_NAME;
	}
}
