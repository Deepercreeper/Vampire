package com.deepercreeper.vampireapp.items.implementations.instances.restrictions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceCondition;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestriction;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.InstanceRestrictionable;
import com.deepercreeper.vampireapp.util.DataUtil;

/**
 * Some clans have restrictions, that define whether values or attributes have to have a specific value.<br>
 * Each restriction is represented by an instance of this class.
 * 
 * @author vrl
 */
public class InstanceRestrictionImpl implements InstanceRestriction
{
	private final String					mItemName;
	
	private final List<String>				mItems;
	
	private final InstanceRestrictionType	mType;
	
	private final int						mValue;
	
	private final int						mMinimum;
	
	private final int						mMaximum;
	
	private final int						mIndex;
	
	private final Set<InstanceCondition>	mConditions	= new HashSet<InstanceCondition>();
	
	private InstanceRestrictionable			mParent;
	
	public InstanceRestrictionImpl(final Element aElement)
	{
		mType = InstanceRestrictionType.get(aElement.getAttribute("type"));
		if (aElement.hasAttribute("itemName"))
		{
			mItemName = aElement.getAttribute("itemName");
		}
		else
		{
			mItemName = null;
		}
		if (aElement.hasAttribute("items"))
		{
			mItems = DataUtil.parseList(aElement.getAttribute("items"));
		}
		else
		{
			mItems = null;
		}
		mMinimum = Integer.parseInt(aElement.getAttribute("minimum"));
		mMaximum = Integer.parseInt(aElement.getAttribute("maximum"));
		mIndex = Integer.parseInt(aElement.getAttribute("index"));
		mValue = Integer.parseInt(aElement.getAttribute("value"));
	}
	
	public InstanceRestrictionImpl(final InstanceRestrictionType aType, final String aItemName, final int aMinimum, final int aMaximum,
			final List<String> aItems, final int aIndex, final int aValue)
	{
		mType = aType;
		mItemName = aItemName;
		mItems = aItems;
		mMinimum = aMinimum;
		mMaximum = aMaximum;
		mIndex = aIndex;
		mValue = aValue;
	}
	
	@Override
	public void addCondition(final InstanceCondition aCondition)
	{
		mConditions.add(aCondition);
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("restriction");
		element.setAttribute("type", getType().getName());
		if (getItemName() != null)
		{
			element.setAttribute("itemName", getItemName());
		}
		if (getItems() != null)
		{
			element.setAttribute("items", DataUtil.parseList(getItems()));
		}
		element.setAttribute("minimum", "" + getMinimum());
		element.setAttribute("maximum", "" + getMaximum());
		element.setAttribute("index", "" + getIndex());
		element.setAttribute("value", "" + getValue());
		return element;
	}
	
	/**
	 * Removes this restriction from each restricted parent.
	 */
	@Override
	public void clear()
	{
		if (mParent != null)
		{
			mParent.removeRestriction(this);
			mParent = null;
		}
	}
	
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if ( !(obj instanceof InstanceRestrictionImpl))
		{
			return false;
		}
		final InstanceRestrictionImpl other = (InstanceRestrictionImpl) obj;
		if (mConditions == null)
		{
			if (other.mConditions != null)
			{
				return false;
			}
		}
		else if ( !mConditions.equals(other.mConditions))
		{
			return false;
		}
		if (mIndex != other.mIndex)
		{
			return false;
		}
		if (mItemName == null)
		{
			if (other.mItemName != null)
			{
				return false;
			}
		}
		else if ( !mItemName.equals(other.mItemName))
		{
			return false;
		}
		if (mItems == null)
		{
			if (other.mItems != null)
			{
				return false;
			}
		}
		else if ( !mItems.equals(other.mItems))
		{
			return false;
		}
		if (mMaximum != other.mMaximum)
		{
			return false;
		}
		if (mMinimum != other.mMinimum)
		{
			return false;
		}
		if (mValue != other.mValue)
		{
			return false;
		}
		if (mType == null)
		{
			if (other.mType != null)
			{
				return false;
			}
		}
		else if ( !mType.equals(other.mType))
		{
			return false;
		}
		return true;
	}
	
	@Override
	public Set<InstanceCondition> getConditions()
	{
		return mConditions;
	}
	
	@Override
	public int getIndex()
	{
		return mIndex;
	}
	
	@Override
	public String getItemName()
	{
		return mItemName;
	}
	
	@Override
	public List<String> getItems()
	{
		return mItems;
	}
	
	@Override
	public int getMaximum()
	{
		return mMaximum;
	}
	
	@Override
	public int getMinimum()
	{
		return mMinimum;
	}
	
	@Override
	public InstanceRestrictionable getParent()
	{
		return mParent;
	}
	
	@Override
	public InstanceRestrictionType getType()
	{
		return mType;
	}
	
	@Override
	public int getValue()
	{
		return mValue;
	}
	
	@Override
	public boolean hasConditions()
	{
		return !mConditions.isEmpty();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mConditions == null) ? 0 : mConditions.hashCode());
		result = prime * result + mIndex;
		result = prime * result + ((mItemName == null) ? 0 : mItemName.hashCode());
		result = prime * result + ((mItems == null) ? 0 : mItems.hashCode());
		result = prime * result + mMaximum;
		result = prime * result + mMinimum;
		result = prime * result + mValue;
		result = prime * result + ((mType == null) ? 0 : mType.hashCode());
		return result;
	}
	
	@Override
	public boolean isActive(final ItemControllerInstance aController)
	{
		for (final InstanceCondition condition : mConditions)
		{
			if ( !condition.complied(aController))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean isInRange(final int aValue)
	{
		return mMinimum <= aValue && aValue <= mMaximum;
	}
	
	/**
	 * Adds a restricted parent to this restriction. That makes sure,<br>
	 * that the removal of restrictions is done for each restricted value of this restriction.
	 * 
	 * @param aParent
	 *            The parent.
	 */
	@Override
	public void setParent(final InstanceRestrictionable aParent)
	{
		mParent = aParent;
	}
	
	@Override
	public void update()
	{
		getParent().updateRestrictions();
	}
	
	@Override
	public String toString()
	{
		final StringBuilder string = new StringBuilder();
		string.append(getType().getName() + ": ");
		boolean first = true;
		if (getItemName() != null)
		{
			if ( !first)
			{
				string.append(", ");
			}
			else
			{
				first = false;
			}
			string.append("ItemName = " + getItemName());
		}
		if (getItems() != null)
		{
			if ( !first)
			{
				string.append(", ");
			}
			else
			{
				first = false;
			}
			string.append("Items = " + getItems());
		}
		if ( !first)
		{
			string.append(", ");
		}
		string.append("Minimum = " + getMinimum()).append(", Maximum = " + getMaximum());
		string.append(", Index = " + getIndex()).append(", Value = " + getValue());
		return string.toString();
	}
}
