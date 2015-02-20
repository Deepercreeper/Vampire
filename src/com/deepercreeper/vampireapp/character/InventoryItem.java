package com.deepercreeper.vampireapp.character;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.controllers.InventoryController;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;

public class InventoryItem implements Saveable
{
	private final InventoryController	mController;
	
	private final int					mWeight;
	
	private final String				mName;
	
	private final Context				mContext;
	
	private final RelativeLayout		mContainer;
	
	private boolean						mInitialized	= false;
	
	public InventoryItem(final Element aElement, final Context aContext, final InventoryController aController)
	{
		mContext = aContext;
		mController = aController;
		mName = aElement.getAttribute("name");
		mWeight = Integer.parseInt(aElement.getAttribute("weight"));
		
		mContainer = (RelativeLayout) View.inflate(mContext, R.layout.inventory_item, null);
		
		init();
	}
	
	public InventoryItem(final String aName, final int aWeight, final Context aContext, final InventoryController aController)
	{
		mContext = aContext;
		mController = aController;
		mName = aName;
		mWeight = aWeight;
		
		mContainer = (RelativeLayout) View.inflate(mContext, R.layout.inventory_item, null);
		
		init();
	}
	
	public RelativeLayout getContainer()
	{
		return mContainer;
	}
	
	public void release()
	{
		ViewUtil.release(getContainer());
	}
	
	public void init()
	{
		if ( !mInitialized)
		{
			final TextView itemName = (TextView) getContainer().findViewById(R.id.item_name);
			final ImageButton infoButton = (ImageButton) getContainer().findViewById(R.id.item_info);
			final ImageButton removeButton = (ImageButton) getContainer().findViewById(R.id.remove_item);
			
			itemName.setText(getName());
			
			infoButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					Toast.makeText(mContext, getInfo(), Toast.LENGTH_LONG).show();
				}
			});
			
			removeButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					getController().removeItem(InventoryItem.this);
				}
			});
			
			mInitialized = true;
		}
	}
	
	public InventoryController getController()
	{
		return mController;
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
