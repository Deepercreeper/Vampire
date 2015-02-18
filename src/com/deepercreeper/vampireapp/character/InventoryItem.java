package com.deepercreeper.vampireapp.character;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.util.Saveable;

public class InventoryItem implements Saveable
{
	private final int		mWeight;
	
	private final String	mName;
	
	private final Context	mContext;
	
	public InventoryItem(final Element aElement, final Context aContext)
	{
		mContext = aContext;
		mName = aElement.getAttribute("name");
		mWeight = Integer.parseInt(aElement.getAttribute("weight"));
	}
	
	public InventoryItem(final String aName, final int aWeight, final Context aContext)
	{
		mContext = aContext;
		mName = aName;
		mWeight = aWeight;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public int getWeight()
	{
		return mWeight;
	}
	
	public String getInfo()
	{
		return getName() + ": " + getWeight() + " " + getContext().getString(R.string.weight_unit);
	}
	
	public Context getContext()
	{
		return mContext;
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("item");
		element.setAttribute("name", mName);
		element.setAttribute("weight", "" + mWeight);
		return element;
	}
}
