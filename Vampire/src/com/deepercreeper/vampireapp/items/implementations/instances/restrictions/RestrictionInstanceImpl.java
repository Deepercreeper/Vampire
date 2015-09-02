package com.deepercreeper.vampireapp.items.implementations.instances.restrictions;

import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.host.change.MessageListener;
import com.deepercreeper.vampireapp.host.change.RestrictionChange;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.ConditionInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.restrictions.RestrictionableInstance;
import com.deepercreeper.vampireapp.mechanics.Duration;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Some clans have restrictions, that define whether values or attributes have to have a specific value.<br>
 * Each restriction is represented by an instance of this class.
 * 
 * @author vrl
 */
public class RestrictionInstanceImpl implements RestrictionInstance
{
	private final String mItemName;
	
	private final LinearLayout mContainer;
	
	private final Context mContext;
	
	private final Set<ConditionInstance> mConditions = new HashSet<ConditionInstance>();
	
	private final RestrictionInstanceType mType;
	
	private final MessageListener mMessageListener;
	
	private final Duration mDuration;
	
	private final int mValue;
	
	private final int mMinimum;
	
	private final int mMaximum;
	
	private final int mIndex;
	
	private final boolean mHost;
	
	private final TextView mDurationLabel;
	
	private RestrictionableInstance mParent;
	
	/**
	 * Creates a new restriction out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aContext
	 *            The underlying context.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided restriction.
	 */
	public RestrictionInstanceImpl(final Element aElement, Context aContext, MessageListener aMessageListener, boolean aHost)
	{
		mType = RestrictionInstanceType.get(aElement.getAttribute("type"));
		if (aElement.hasAttribute("itemName"))
		{
			mItemName = aElement.getAttribute("itemName");
		}
		else
		{
			mItemName = null;
		}
		mMinimum = Integer.parseInt(aElement.getAttribute("minimum"));
		mMaximum = Integer.parseInt(aElement.getAttribute("maximum"));
		mIndex = Integer.parseInt(aElement.getAttribute("index"));
		mValue = Integer.parseInt(aElement.getAttribute("value"));
		mDuration = Duration.create(DataUtil.getElement(aElement, "duration"));
		mDuration.addListener(this);
		mMessageListener = aMessageListener;
		mContext = aContext;
		mHost = aHost;
		
		int id = mHost ? R.layout.host_restriction : R.layout.client_restriction;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		
		int nameId = mHost ? R.id.h_restriction_name_label : R.id.c_restriction_name_label;
		int durationId = mHost ? R.id.h_restriction_duration_label : R.id.c_restriction_duration_label;
		((TextView) getContainer().findViewById(nameId)).setText(getDescription());
		mDurationLabel = (TextView) getContainer().findViewById(durationId);
		
		if (mHost)
		{
			((Button) getContainer().findViewById(R.id.h_remove_restriction_button)).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View aV)
				{
					clear();
				}
			});
		}
		
		updateUI();
	}
	
	/**
	 * Creates a new restriction.
	 * 
	 * @param aType
	 *            The restriction type.
	 * @param aItemName
	 *            The item name.
	 * @param aMinimum
	 *            The minimum value.
	 * @param aMaximum
	 *            The maximum value.
	 * @param aIndex
	 *            The index.
	 * @param aValue
	 *            The value.
	 * @param aDuration
	 *            The restriction duration.
	 * @param aContext
	 *            The underlying context.
	 * @param aMessageListener
	 *            The message listener.
	 * @param aHost
	 *            Whether this is a host sided restriction.
	 */
	public RestrictionInstanceImpl(final RestrictionInstanceType aType, final String aItemName, final int aMinimum, final int aMaximum,
			final int aIndex, final int aValue, final Duration aDuration, Context aContext, MessageListener aMessageListener, boolean aHost)
	{
		mType = aType;
		mItemName = aItemName;
		mMinimum = aMinimum;
		mMaximum = aMaximum;
		mIndex = aIndex;
		mValue = aValue;
		mDuration = aDuration;
		mDuration.addListener(this);
		mMessageListener = aMessageListener;
		mContext = aContext;
		mHost = aHost;
		
		int id = mHost ? R.layout.host_restriction : R.layout.client_restriction;
		mContainer = (LinearLayout) View.inflate(mContext, id, null);
		
		int nameId = mHost ? R.id.h_restriction_name_label : R.id.c_restriction_name_label;
		int durationId = mHost ? R.id.h_restriction_duration_label : R.id.c_restriction_duration_label;
		((TextView) getContainer().findViewById(nameId)).setText(getDescription());
		mDurationLabel = (TextView) getContainer().findViewById(durationId);
		
		if (mHost)
		{
			((Button) getContainer().findViewById(R.id.h_remove_restriction_button)).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View aV)
				{
					clear();
				}
			});
		}
		
		updateUI();
	}
	
	@Override
	public void addCondition(final ConditionInstance aCondition)
	{
		mConditions.add(aCondition);
	}
	
	@Override
	public LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public void release()
	{
		ViewUtil.release(getContainer());
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
		element.setAttribute("minimum", "" + getMinimum());
		element.setAttribute("maximum", "" + getMaximum());
		element.setAttribute("index", "" + getIndex());
		element.setAttribute("value", "" + getValue());
		element.appendChild(mDuration.asElement(aDoc));
		return element;
	}
	
	private String getDescription()
	{
		StringBuilder description = new StringBuilder();
		description.append(mType.getName(mContext));
		if (mType.hasItemName())
		{
			description.append(", ").append(mContext.getString(R.string.item_name)).append(": ").append(mItemName);
		}
		if (mType.hasIndex())
		{
			description.append(", ").append(mContext.getString(R.string.index)).append(": ").append(mIndex);
		}
		if (mType.hasRange())
		{
			description.append(", ").append(mContext.getString(R.string.minimum)).append(": ").append(mMinimum);
			description.append(", ").append(mContext.getString(R.string.maximum)).append(": ").append(mMaximum);
		}
		if (mType.hasValue())
		{
			description.append(", ").append(mContext.getString(R.string.value)).append(": ").append(mValue);
		}
		return description.toString();
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
		if (mHost)
		{
			mMessageListener.sendChange(new RestrictionChange(this, false));
		}
	}
	
	@Override
	public boolean equals(final Object aObj)
	{
		if (aObj instanceof RestrictionInstance)
		{
			RestrictionInstance restriction = (RestrictionInstance) aObj;
			if ( !getType().equals(restriction.getType()))
			{
				return false;
			}
			if (getType().hasItemName() && !getItemName().equals(restriction.getItemName()))
			{
				return false;
			}
			if (getType().hasIndex() && getIndex() != restriction.getIndex())
			{
				return false;
			}
			if (getType().hasValue() && getValue() != restriction.getValue())
			{
				return false;
			}
			if (getType().hasRange() && (getMinimum() != restriction.getMinimum() || getMaximum() != restriction.getMaximum()))
			{
				return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public Set<ConditionInstance> getConditions()
	{
		return mConditions;
	}
	
	@Override
	public Duration getDuration()
	{
		return mDuration;
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
	public RestrictionableInstance getParent()
	{
		return mParent;
	}
	
	@Override
	public RestrictionInstanceType getType()
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
		result = prime * result + mMaximum;
		result = prime * result + mMinimum;
		result = prime * result + mValue;
		result = prime * result + ((mType == null) ? 0 : mType.hashCode());
		return result;
	}
	
	@Override
	public boolean isActive(final ItemControllerInstance aController)
	{
		for (final ConditionInstance condition : mConditions)
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
	
	@Override
	public void onDue()
	{
		clear();
	}
	
	@Override
	public void setParent(final RestrictionableInstance aParent)
	{
		mParent = aParent;
	}
	
	@Override
	public void time(final Type aType, final int aAmount)
	{
		mDuration.time(aType, aAmount);
	}
	
	@Override
	public void timeUpdated()
	{
		updateUI();
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
		if ( !first)
		{
			string.append(", ");
		}
		string.append("Minimum = " + getMinimum()).append(", Maximum = " + getMaximum());
		string.append(", Index = " + getIndex()).append(", Value = " + getValue());
		string.append(", " + getDuration());
		return string.toString();
	}
	
	@Override
	public void updateUI()
	{
		mDurationLabel.setText(mDuration.getName(mContext));
	}
}
