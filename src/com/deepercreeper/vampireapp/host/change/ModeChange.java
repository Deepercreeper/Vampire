package com.deepercreeper.vampireapp.host.change;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.Mode;

/**
 * A change in mode properties.
 * 
 * @author Vincent
 */
public class ModeChange implements CharacterChange
{
	/**
	 * The XML tag for mode changes.
	 */
	public static final String TAG_NAME = "mode-change";
	
	private final Mode mMode;
	
	/**
	 * Creates a new mode change out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 */
	public ModeChange(final Element aElement)
	{
		mMode = Mode.valueOf(aElement.getAttribute("mode"));
	}
	
	/**
	 * Creates a new mode change.
	 * 
	 * @param aMode
	 *            The mode.
	 */
	public ModeChange(final Mode aMode)
	{
		mMode = aMode;
	}
	
	@Override
	public void applyChange(final CharacterInstance aCharacter)
	{
		aCharacter.getMode().setMode(mMode, true);
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("mode", mMode.name());
		return element;
	}
	
	@Override
	public String getType()
	{
		return TAG_NAME;
	}
}
