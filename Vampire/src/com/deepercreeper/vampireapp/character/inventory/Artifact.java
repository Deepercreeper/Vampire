package com.deepercreeper.vampireapp.character.inventory;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.InventoryControllerInstance;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.LanguageUtil;
import com.deepercreeper.vampireapp.util.ViewUtil;
import com.deepercreeper.vampireapp.util.interfaces.ItemFinder;
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
public class Artifact implements InventoryItem
{
	/**
	 * The type that defines items to be artifacts.
	 */
	public static final String ARTIFACT_ITEM_TYPE = "item";
	
	private final int mWeight;
	
	private final String mName;
	
	private final Context mContext;
	
	private final LinearLayout mContainer;
	
	private final ImageButton mRemoveButton;
	
	private final TextView mItemName;
	
	private int mQuantity = 1;
	
	private InventoryControllerInstance mController;
	
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
	public Artifact(final String aName, final int aWeight, final Context aContext, final InventoryControllerInstance aController)
	{
		this(aName, aWeight, 1, aContext, aController);
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
	public Artifact(final String aName, final int aWeight, final int aQuantity, final Context aContext, final InventoryControllerInstance aController)
	{
		mContext = aContext;
		mController = aController;
		mName = aName;
		mWeight = aWeight;
		mQuantity = aQuantity;
		
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_inventory_item, null);
		
		mItemName = (TextView) getContainer().findViewById(R.id.view_inv_item_name_label);
		final ImageButton infoButton = (ImageButton) getContainer().findViewById(R.id.view_inv_item_info_button);
		mRemoveButton = (ImageButton) getContainer().findViewById(R.id.view_remove_inv_item_button);
		
		infoButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Toast.makeText(mContext, getInfo(), Toast.LENGTH_LONG).show();
			}
		});
		
		mRemoveButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				getController().removeItem(Artifact.this, false);
			}
		});
	}
	
	private Artifact(final Element aElement, final Context aContext, final InventoryControllerInstance aController)
	{
		mContext = aContext;
		mController = aController;
		mName = CodingUtil.decode(aElement.getAttribute("name"));
		mWeight = Integer.parseInt(aElement.getAttribute("weight"));
		mQuantity = Integer.parseInt(aElement.getAttribute("quantity"));
		
		mContainer = (LinearLayout) View.inflate(mContext, R.layout.view_inventory_item, null);
		
		mItemName = (TextView) getContainer().findViewById(R.id.view_inv_item_name_label);
		final ImageButton infoButton = (ImageButton) getContainer().findViewById(R.id.view_inv_item_info_button);
		mRemoveButton = (ImageButton) getContainer().findViewById(R.id.view_remove_inv_item_button);
		
		infoButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				Toast.makeText(mContext, getInfo(), Toast.LENGTH_LONG).show();
			}
		});
		
		mRemoveButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View aV)
			{
				getController().removeItem(Artifact.this, false);
			}
		});
	}
	
	@Override
	public final void addTo(final InventoryControllerInstance aController)
	{
		mController = aController;
		updateUI();
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
	
	@Override
	public final void decrease()
	{
		mQuantity-- ;
		updateUI();
		if (mQuantity > 0)
		{
			getController().updateWeight();
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
		if ( !(obj instanceof Artifact))
		{
			return false;
		}
		final Artifact other = (Artifact) obj;
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
	
	@Override
	public final LinearLayout getContainer()
	{
		return mContainer;
	}
	
	@Override
	public final Context getContext()
	{
		return mContext;
	}
	
	@Override
	public final InventoryControllerInstance getController()
	{
		return mController;
	}
	
	@Override
	public final String getInfo()
	{
		return DataUtil.buildMessage("{x}", LanguageUtil.instance().translateArray(getInfoArray(true), getInfoTranslatedArray(true)));
	}
	
	@Override
	public String[] getInfoArray(final boolean aQuantity)
	{
		final List<String> list = new ArrayList<String>();
		list.add(getName() + (aQuantity ? getQuantitySuffix() : "") + ": ");
		list.add("" + R.string.weight);
		list.add(": " + getWeight() + " ");
		list.add("" + R.string.weight_unit);
		return list.toArray(new String[list.size()]);
	}
	
	@Override
	public boolean[] getInfoTranslatedArray(final boolean aQuantity)
	{
		return DataUtil.parseFlags("0101");
	}
	
	@Override
	public final String getName()
	{
		return mName;
	}
	
	@Override
	public final int getQuantity()
	{
		return mQuantity;
	}
	
	@Override
	public String getType()
	{
		return ARTIFACT_ITEM_TYPE;
	}
	
	@Override
	public final int getWeight()
	{
		return mWeight;
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
	public final void increase(final int aAmount)
	{
		mQuantity += aAmount;
		updateUI();
		getController().updateWeight();
	}
	
	@Override
	public final void release()
	{
		ViewUtil.release(getContainer());
	}
	
	@Override
	public void updateUI()
	{
		mItemName.setText(getName() + getQuantitySuffix());
		if (getController() != null)
		{
			ViewUtil.setEnabled(mRemoveButton, getController().isHost() || getController().getCharacter().getMode().getMode().canUseAction());
		}
	}
	
	protected final String getQuantitySuffix()
	{
		if (getQuantity() > 1)
		{
			return " (" + getQuantity() + ")";
		}
		return "";
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
	public static Artifact deserialize(final Element aElement, final Context aContext, final InventoryControllerInstance aController,
			final ItemFinder aItems)
	{
		final String type = aElement.getAttribute("type");
		if (type.equals(ARTIFACT_ITEM_TYPE))
		{
			return new Artifact(aElement, aContext, aController);
		}
		if (type.equals(Weapon.WEAPON_ITEM_TYPE))
		{
			return new Weapon(aElement, aItems, aContext, aController);
		}
		if (type.equals(Armor.ARMOR_ITEM_TYPE))
		{
			return new Armor(aElement, aContext, aController);
		}
		return null;
	}
}
