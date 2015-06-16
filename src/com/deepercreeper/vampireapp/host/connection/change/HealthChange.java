package com.deepercreeper.vampireapp.host.connection.change;

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
	
	private boolean				mBooleanValue	= false;
	
	private int[]				mSteps			= null;
	
	private enum Type
	{
		VALUE, CAN_HEAL, HEAVY_WOUNDS, STEPS
	}
	
	/**
	 * Creates a health value change.
	 * 
	 * @param aValue
	 *            The new health value.
	 */
	public HealthChange(int aValue)
	{
		mType = Type.VALUE;
		mIntegerValue = aValue;
	}
	
	@Override
	public String getType()
	{
		return TAG_NAME;
	}
	
	/**
	 * Creates a heavy wounds or can heal health change.
	 * 
	 * @param aWoundsOrHeal
	 *            Whether heavy wounds or can heal has changed.
	 * @param aValue
	 *            The new value.
	 */
	public HealthChange(boolean aWoundsOrHeal, boolean aValue)
	{
		mType = aWoundsOrHeal ? Type.HEAVY_WOUNDS : Type.CAN_HEAL;
		mBooleanValue = aValue;
	}
	
	/**
	 * Creates a health steps change.
	 * 
	 * @param aSteps
	 *            The new health steps.
	 */
	public HealthChange(int[] aSteps)
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
	public HealthChange(Element aElement)
	{
		mType = Type.valueOf(aElement.getAttribute("type"));
		String value = aElement.getAttribute("value");
		switch (mType)
		{
			case CAN_HEAL :
			case HEAVY_WOUNDS :
				mBooleanValue = Boolean.valueOf(value);
				break;
			case VALUE :
				mIntegerValue = Integer.parseInt(value);
			case STEPS :
				mSteps = DataUtil.parseValues(value);
			default :
				break;
		}
	}
	
	@Override
	public void applyChange(CharacterInstance aCharacter)
	{
		HealthControllerInstance health = aCharacter.getHealth();
		switch (mType)
		{
			case CAN_HEAL :
				health.updateCanHeal(mBooleanValue);
				break;
			case HEAVY_WOUNDS :
				health.updateHeavyWounds(mBooleanValue);
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
	public Element asElement(Document aDoc)
	{
		Element element = aDoc.createElement(TAG_NAME);
		element.setAttribute("type", mType.name());
		String value = "";
		switch (mType)
		{
			case VALUE :
				value = "" + mIntegerValue;
				break;
			case CAN_HEAL :
			case HEAVY_WOUNDS :
				value = "" + mBooleanValue;
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
