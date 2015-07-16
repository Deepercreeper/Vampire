package com.deepercreeper.vampireapp.character;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.InventoryControllerInstance;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.ItemFinder;
import com.deepercreeper.vampireapp.util.interfaces.Saveable;
import com.deepercreeper.vampireapp.util.interfaces.Viewable;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An inventory item has several properties and can be stored inside the inventory.<br>
 * The properties are at least the name and the weight of this item.
 * 
 * @author vrl
 */
public class InventoryItem implements Saveable, Viewable
{
	private static final String ITEM_TYPE = "item";
	
	private final int mWeight;
	
	private final String mName;
	
	private final Context mContext;
	
	private final LinearLayout mContainer;
	
	private TextView mItemName;
	
	private int mQuantity = 1;
	
	private InventoryControllerInstance mController;
	
	private boolean mInitialized = false;
	
	private InventoryItem(final Element aElement, final Context aContext, final InventoryControllerInstance aController)
	{
		mContext = aContext;
		mController = aController;
		mName = CodingUtil.decode(aElement.getAttribute("name"));
		mWeight = Integer.parseInt(aElement.getAttribute("weight"));
		mQuantity = Integer.parseInt(aElement.getAttribute("quantity"));
		
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
	 * @param aQuantity
	 *            The quantity of this item.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            the parent inventory controller.
	 */
	public InventoryItem(final String aName, final int aWeight, final int aQuantity, final Context aContext,
			final InventoryControllerInstance aController)
	{
		mContext = aContext;
		mController = aController;
		mName = aName;
		mWeight = aWeight;
		mQuantity = aQuantity;
		
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
		this(aName, aWeight, 1, aContext, aController);
	}
	
	/**
	 * Increases the amount of this items by the given amount.
	 * 
	 * @param aAmount
	 *            The amount.
	 */
	public void increase(final int aAmount)
	{
		mQuantity += aAmount;
		updateValue();
		getController().updateWeight();
	}
	
	/**
	 * @return how many items this stack contains.
	 */
	public int getQuantity()
	{
		return mQuantity;
	}
	
	/**
	 * Removes one of this item stack.<br>
	 * If there is no one left, this item is removed from the controller.
	 */
	public void decrease()
	{
		mQuantity-- ;
		updateValue();
		if (mQuantity > 0)
		{
			getController().updateWeight();
		}
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
			mItemName = (TextView) getContainer().findViewById(R.id.view_inv_item_name_label);
			final ImageButton infoButton = (ImageButton) getContainer().findViewById(R.id.view_inv_item_info_button);
			final ImageButton removeButton = (ImageButton) getContainer().findViewById(R.id.view_remove_inv_item_button);
			
			infoButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					Toast.makeText(mContext, getInfo(true), Toast.LENGTH_LONG).show();
				}
			});
			
			removeButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View aV)
				{
					getController().removeItem(InventoryItem.this, false);
				}
			});
			
			mInitialized = true;
		}
		
		updateValue();
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
	 * @param aQuantity
	 *            Whether the item quantity should be added.
	 * @return a summary of this item.
	 */
	public String getInfo(final boolean aQuantity)
	{
		return getName() + (aQuantity ? getQuantitySuffix() : "") + ": " + getWeight() + " " + getContext().getString(R.string.weight_unit);
	}
	
	/**
	 * @return the underlying context.
	 */
	public Context getContext()
	{
		return mContext;
	}
	
	protected String getQuantitySuffix()
	{
		if (getQuantity() > 1)
		{
			return " (" + getQuantity() + ")";
		}
		return "";
	}
	
	/**
	 * Updates the name value.
	 */
	public void updateValue()
	{
		mItemName.setText(getName() + getQuantitySuffix());
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		result = prime * result + mWeight;
		return result;
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
		if ( !(obj instanceof InventoryItem))
		{
			return false;
		}
		final InventoryItem other = (InventoryItem) obj;
		if (mName == null)
		{
			if (other.mName != null)
			{
				return false;
			}
		}
		else if ( !mName.equals(other.mName))
		{
			return false;
		}
		if (mWeight != other.mWeight)
		{
			return false;
		}
		return true;
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
	
	protected String getType()
	{
		return ITEM_TYPE;
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("item");
		element.setAttribute("type", getType());
		element.setAttribute("name", CodingUtil.encode(getName()));
		element.setAttribute("weight", "" + getWeight());
		element.setAttribute("quantity", "" + getQuantity());
		return element;
	}
	
	/**
	 * @return an array of strings, that are used to display this item inside a message.
	 */
	public String[] getInfoArray()
	{
		return new String[] { getName() + getQuantitySuffix() + ": " + getWeight() + " ", getContext().getString(R.string.weight_unit) };
	}
	
	/**
	 * @return a boolean array that determines which info array entries should be translated.
	 */
	public boolean[] getInfoTranslatedArray()
	{
		return new boolean[] { false, true };
	}
	
	/**
	 * Deserializes the given item depending on the type attribute.
	 * 
	 * @param aElement
	 *            The element-
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The inventory controller.
	 * @param aItems
	 *            An item finder.
	 * @return the new created item.
	 */
	public static InventoryItem deserialize(Element aElement, Context aContext, InventoryControllerInstance aController, ItemFinder aItems)
	{
		String type = aElement.getAttribute("type");
		if (type.equals(ITEM_TYPE))
		{
			return new InventoryItem(aElement, aContext, aController);
		}
		if (type.equals(Weapon.ITEM_TYPE))
		{
			return new Weapon(aElement, aItems, aContext, aController);
		}
		return null;
	}
}
