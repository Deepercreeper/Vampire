package com.deepercreeper.vampireapp.host.change;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.items.implementations.instances.restrictions.RestrictionInstanceImpl;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance;
import com.deepercreeper.vampireapp.util.DataUtil;
import android.content.Context;

/**
 * A change at restriction properties.
 * 
 * @author vrl
 */
public class RestrictionChange implements CharacterChange
{
	/**
	 * The XML tag for restriction changes.
	 */
	public static final String TAG_NAME = "restriction-change";
	
	private final RestrictionInstance mRestriction;
	
	private final boolean mAdded;
	
	/**
	 * Creates a restriction change that handles restriction addition and removal.
	 * 
	 * @param aRestriction
	 *            The added or removed restriction.
	 * @param aAdded
	 *            Whether the restriction was added or not.
	 */
	public RestrictionChange(RestrictionInstance aRestriction, boolean aAdded)
	{
		mRestriction = aRestriction;
		mAdded = aAdded;
	}
	
	/**
	 * Creates a new restriction change out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 * @param aContext
	 *            The underlying context.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided change.
	 */
	public RestrictionChange(Element aElement, Context aContext, MessageListener aMessageListener, boolean aHost)
	{
		mRestriction = new RestrictionInstanceImpl(DataUtil.getElement(aElement, "restriction"), aContext, aMessageListener, aHost);
		mAdded = Boolean.parseBoolean(aElement.getAttribute("added"));
	}
	
	@Override
	public void applyChange(CharacterInstance aCharacter)
	{
		if (mAdded)
		{
			aCharacter.getRestrictions().addRestriction(mRestriction, true);
		}
		else
		{
			aCharacter.getRestrictions().getRestriction(mRestriction).clear();
		}
	}
	
	@Override
	public Element asElement(Document aDoc)
	{
		Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("added", "" + mAdded);
		element.appendChild(mRestriction.asElement(aDoc));
		return element;
	}
	
	@Override
	public String getType()
	{
		return TAG_NAME;
	}
}
