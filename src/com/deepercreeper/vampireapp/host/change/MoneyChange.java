package com.deepercreeper.vampireapp.host.change;

import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.MoneyDepot;
import com.deepercreeper.vampireapp.util.DataUtil;

/**
 * A change at money properties.
 * 
 * @author Vincent
 */
public class MoneyChange implements CharacterChange
{
	/**
	 * The XML tag for money changes.
	 */
	public static final String			TAG_NAME	= "money-change";
	
	private final String				mDepot;
	
	private final Map<String, Integer>	mValues;
	
	private final boolean				mCreated;
	
	/**
	 * Creates a money change out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 */
	public MoneyChange(final Element aElement)
	{
		mDepot = aElement.getAttribute("depot");
		if (aElement.hasAttribute("values"))
		{
			mValues = DataUtil.parseMap(aElement.getAttribute("values"));
			mCreated = false;
		}
		else
		{
			mCreated = Boolean.valueOf(aElement.getAttribute("created"));
			mValues = null;
		}
	}
	
	/**
	 * Creates a new money change, that indicates, which depot was removed or created.
	 * 
	 * @param aDepot
	 *            The depot name.
	 * @param aCreated
	 *            Whether the depot was created.
	 */
	public MoneyChange(final String aDepot, final boolean aCreated)
	{
		mDepot = aDepot;
		mCreated = aCreated;
		mValues = null;
	}
	
	/**
	 * Creates a new money change.
	 * 
	 * @param aDepot
	 *            The depot name.
	 * @param aValues
	 *            The new depot values.
	 */
	public MoneyChange(final String aDepot, final Map<String, Integer> aValues)
	{
		mDepot = aDepot;
		mValues = aValues;
		mCreated = false;
	}
	
	@Override
	public void applyChange(final CharacterInstance aCharacter)
	{
		if (mValues == null)
		{
			if (mCreated)
			{
				aCharacter.getMoney().addDepot(new MoneyDepot(mDepot, aCharacter.getContext(), aCharacter.isHost(), false, aCharacter.getMoney()),
						true);
			}
			else
			{
				aCharacter.getMoney().removeDepot(mDepot, true);
			}
		}
		else
		{
			aCharacter.getMoney().getDepot(mDepot).updateValues(mValues);
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("depot", mDepot);
		if (mValues != null)
		{
			element.setAttribute("values", "" + DataUtil.parseMap(mValues));
		}
		else
		{
			element.setAttribute("created", "" + mCreated);
		}
		return element;
	}
	
	@Override
	public String getType()
	{
		return TAG_NAME;
	}
}
