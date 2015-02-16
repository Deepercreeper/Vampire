package com.deepercreeper.vampireapp.character;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.mechanics.TimeListener;
import com.deepercreeper.vampireapp.util.ItemUtil;
import com.deepercreeper.vampireapp.util.Saveable;

public class HealthController implements TimeListener, Saveable
{
	private int[]	mNegatives;
	
	private boolean	mHeavyWounds	= false;
	
	private boolean	mCanHeal		= false;
	
	private int		mValue			= 0;
	
	public HealthController(final Element aElement)
	{
		mNegatives = ItemUtil.parseValues(aElement.getAttribute("negatives"));
		mHeavyWounds = Boolean.valueOf(aElement.getAttribute("heavy"));
		mCanHeal = Boolean.valueOf(aElement.getAttribute("canHeal"));
		mValue = Integer.parseInt(aElement.getAttribute("value"));
	}
	
	public HealthController(final int[] aNegatives)
	{
		mNegatives = aNegatives;
	}
	
	public void addNegative()
	{
		final int[] negatives = new int[mNegatives.length + 1];
		negatives[0] = 0;
		for (int i = 0; i < mNegatives.length; i++ )
		{
			negatives[i + 1] = mNegatives[i];
		}
		mNegatives = negatives;
	}
	
	public int getNegative()
	{
		if (mNegatives[mValue] == Integer.MAX_VALUE)
		{
			return 0;
		}
		return mNegatives[mValue];
	}
	
	public void hurt(final int aValue, final boolean aHeavy)
	{
		mValue += aValue;
		if (mValue >= mNegatives.length)
		{
			mValue = mNegatives.length - 1;
		}
		if (aHeavy)
		{
			mHeavyWounds = true;
		}
		mCanHeal = true;
		if (mHeavyWounds)
		{
			mCanHeal = false;
		}
	}
	
	public boolean canAct()
	{
		return mNegatives[mValue] != Integer.MAX_VALUE;
	}
	
	public boolean canHeal()
	{
		return mCanHeal;
	}
	
	public void heal(final int aValue)
	{
		if ( !canHeal())
		{
			return;
		}
		mValue -= aValue;
		if (mValue < 0)
		{
			mValue = 0;
		}
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("health");
		element.setAttribute("negatives", ItemUtil.parseValues(mNegatives));
		element.setAttribute("value", "" + mValue);
		element.setAttribute("canHeal", "" + mCanHeal);
		element.setAttribute("heavy", "" + mHeavyWounds);
		return element;
	}
	
	@Override
	public void day()
	{
		mCanHeal = true;
	}
	
	@Override
	public void hour()
	{}
	
	@Override
	public void round()
	{}
}
