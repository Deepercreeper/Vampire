package com.deepercreeper.vampireapp.host.change;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;

/**
 * A change at the generation value.
 * 
 * @author Vincent
 */
public class GenerationChange implements CharacterChange
{
	/**
	 * The XML tag for generation changes.
	 */
	public static final String	TAG_NAME	= "generation-change";
	
	private final int			mValue;
	
	/**
	 * Creates a generation change out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 */
	public GenerationChange(final Element aElement)
	{
		mValue = Integer.parseInt(aElement.getAttribute("value"));
	}
	
	/**
	 * Creates a new generation change.
	 * 
	 * @param aValue
	 *            The new generation.
	 */
	public GenerationChange(final int aValue)
	{
		mValue = aValue;
	}
	
	@Override
	public void applyChange(final CharacterInstance aCharacter)
	{
		aCharacter.getGenerationController().updateGeneration(mValue);
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
