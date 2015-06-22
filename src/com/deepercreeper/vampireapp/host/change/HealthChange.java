package com.deepercreeper.vampireapp.host.change;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.character.instance.CharacterInstance;
import com.deepercreeper.vampireapp.character.instance.HealthControllerInstance;
import com.deepercreeper.vampireapp.util.DataUtil;

/**
 * A change at health properties.
 * 
 * @author vrl
 */
public class HealthChange implements CharacterChange
{
	/**
	 * The XML tag for health changes.
	 */
	public static final String	TAG_NAME		= "health-change";
	
	private final Type			mType;
	
	private int					mIntegerValue	= -1;
	
	private int[]				mSteps			= null;
	
	private enum Type
	{
		VALUE, HEAVY_WOUNDS, STEPS
	}
	
	/**
	 * Creates a health value change.
	 * 
	 * @param aWoundsOrValue
	 *            Whether this is a heavy wounds count or health value change.
	 * @param aValue
	 *            The new health value.
	 */
	public HealthChange(final boolean aWoundsOrValue, final int aValue)
	{
		mType = aWoundsOrValue ? Type.HEAVY_WOUNDS : Type.VALUE;
		mIntegerValue = aValue;
	}
	
	@Override
	public String getType()
	{
		return TAG_NAME;
	}
	
	/**
	 * Creates a health steps change.
	 * 
	 * @param aSteps
	 *            The new health steps.
	 */
	public HealthChange(final int[] aSteps)
	{
		mType = Type.STEPS;
		mSteps = aSteps;
	}
	
	/**
	 * Creates a health change out of the given XML data.
	 * 
	 * @param aElement
	 *            The data.
	 */
	public HealthChange(final Element aElement)
	{
		mType = Type.valueOf(aElement.getAttribute("type"));
		final String value = aElement.getAttribute("value");
		switch (mType)
		{
			case HEAVY_WOUNDS :
			case VALUE :
				mIntegerValue = Integer.parseInt(value);
			case STEPS :
				mSteps = DataUtil.parseValues(value);
			default :
				break;
		}
	}
	
	@Override
	public void applyChange(final CharacterInstance aCharacter)
	{
		final HealthControllerInstance health = aCharacter.getHealth();
		switch (mType)
		{
			case HEAVY_WOUNDS :
				health.updateHeavyWounds(mIntegerValue);
				break;
			case STEPS :
				health.updateSteps(mSteps);
				break;
			case VALUE :
				health.updateValue(mIntegerValue);
			default :
				break;
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("type", mType.name());
		String value = "";
		switch (mType)
		{
			case VALUE :
			case HEAVY_WOUNDS :
				value = "" + mIntegerValue;
				break;
			case STEPS :
				value = DataUtil.parseValues(mSteps);
			default :
				break;
		}
		element.setAttribute("value", value);
		return element;
	}
}
