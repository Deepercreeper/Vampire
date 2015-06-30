package com.deepercreeper.vampireapp.character;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.InventoryControllerInstance;
import com.deepercreeper.vampireapp.util.Saveable;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.view.Viewable;

/**
 * An inventory item has several properties and can be stored inside the inventory.<br>
 * The properties are at least the name and the weight of this item.
 * 
 * @author vrl
 */
public class InventoryItem implements Saveable, Viewable
{
	private final int					mWeight;
	
	private final String				mName;
	
	private final Context				mContext;
	
	private final LinearLayout			mContainer;
	
	private InventoryControllerInstance	mController;
	
	private boolean						mInitialized	= false;
	
	/**
	 * Creates a new inventory item out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The parent inventory controller.
	 */
	public InventoryItem(final Element aElement, final Context aContext, final InventoryControllerInstance aController)
	{
		mContext = aContext;
		mController = aController;
		mName = aElement.getAttribute("name");
		mWeight = Integer.parseInt(aElement.getAttribute("weight"));
		
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.inventory_item_view, null);
		
		init();
	}
	
	/**
	 * Creates a new inventory item.
	 * 
	 * @param aName
	 *            The item name.
	 * @param aWeight
	 *            The item weight.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            the parent inventory controller.
	 */
	public InventoryItem(final String aName, final int aWeight, final Context aContext, final InventoryControllerInstance aController)
	{
		mContext = aContext;
		mController = aController;
		mName = aName;
		mWeight = aWeight;
		
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.inventory_item_view, null);
		
		init();
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
	public void init()
	{
		if ( !mInitialized)
		{
			final TextView itemName = (TextView) getContainer().findViewById(R.id.view_inv_item_name_label);
			final ImageButton infoButton = (ImageButton) getContainer().findViewById(R.id.view_inv_item_info_button);
			final ImageButton removeButton = (ImageButton) getContainer().findViewById(R.id.view_remove_inv_item_button);
			
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
	
	/**
	 * @return The parent inventory controller.
	 */
	public InventoryControllerInstance getController()
	{
		return mController;
	}
	
	/**
	 * @return the item name.
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * @return the item weight.
	 */
	public int getWeight()
	{
		return mWeight;
	}
	
	/**
	 * @return a summary of this item.
	 */
	public String getInfo()
	{
		return getName() + ": " + getWeight() + " " + getContext().getString(R.string.weight_unit);
	}
	
	/**
	 * @return the underlying context.
	 */
	public Context getContext()
	{
		return mContext;
	}
	
	/**
	 * Sets the parent inventory controller.
	 * 
	 * @param aController
	 *            The new parent inventory controller.
	 */
	public void addTo(final InventoryControllerInstance aController)
	{
		mController = aController;
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
