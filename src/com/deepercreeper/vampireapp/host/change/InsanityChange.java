package com.deepercreeper.vampireapp.host.change;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.util.CodingUtil;

/**
 * A change at insanity properties.
 * 
 * @author vrl
 */
public class InsanityChange implements CharacterChange
{
	/**
	 * The XML tag for insanity changes.
	 */
	public static final String TAG_NAME = "insanity-change";
	
	private final String mInsanity;
	
	private final Duration mDuration;
	
	/**
	 * Creates a new insanity change for adding the given one.
	 * 
	 * @param aInsanity
	 *            The added insanity.
	 * @param aDuration
	 *            The duration of the added insanity.
	 */
	public InsanityChange(String aInsanity, Duration aDuration)
	{
		mInsanity = aInsanity;
		mDuration = aDuration;
	}
	
	/**
	 * Creates a new insanity change for removing the given one.
	 * 
	 * @param aInsanity
	 *            The removed insanity.
	 */
	public InsanityChange(String aInsanity)
	{
		mInsanity = aInsanity;
		mDuration = null;
	}
	
	/**
	 * Creates a new insanity change out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 */
	public InsanityChange(Element aElement)
	{
		mInsanity = CodingUtil.decode(aElement.getAttribute("insanity"));
		NodeList children = aElement.getElementsByTagName("duration");
		if (children.getLength() > 0)
		{
			mDuration = Duration.create((Element) children.item(0));
		}
		else
		{
			mDuration = null;
		}
	}
	
	@Override
	public void applyChange(CharacterInstance aCharacter)
	{
		if (mDuration == null)
		{
			aCharacter.getInsanities().removeInsanity(mInsanity, true);
		}
		else
		{
			aCharacter.getInsanities().addInsanity(mInsanity, mDuration, true);
		}
	}
	
	@Override
	public Element asElement(Document aDoc)
	{
		Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("insanity", mInsanity);
		if (mDuration != null)
		{
			element.appendChild(mDuration.asElement(aDoc));
		}
		return element;
	}
	
	@Override
	public String getType()
	{
		return TAG_NAME;
	}
}
