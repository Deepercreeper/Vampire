package com.deepercreeper.vampireapp.character.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.InventoryControllerInstance;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import android.content.Context;

/**
 * Each player is able to wear armor. By default just one item. That has several properties and effects to the player.
 * 
 * @author vrl
 */
public class Armor extends Artifact
{
	/**
	 * The type that defines items to be armor items.
	 */
	public static final String ARMOR_ITEM_TYPE = "armor";
	
	private final int mArmor;
	
	protected Armor(final Element aElement, final Context aContext, final InventoryControllerInstance aController)
	{
		super(CodingUtil.decode(aElement.getAttribute("name")), Integer.parseInt(aElement.getAttribute("weight")),
				Integer.parseInt(aElement.getAttribute("quantity")), aContext, aController);
		mArmor = Integer.parseInt(aElement.getAttribute("armor"));
	}
	
	/**
	 * Creates a new armor item.
	 * 
	 * @param aName
	 *            The item name.
	 * @param aWeight
	 *            The item weight.
	 * @param aQuantity
	 *            The number of items, that are created.
	 * @param aArmor
	 *            The armor value.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The parent controller.
	 */
	public Armor(final String aName, final int aWeight, final int aQuantity, final int aArmor, final Context aContext,
			final InventoryControllerInstance aController)
	{
		super(aName, aWeight, aQuantity, aContext, aController);
		mArmor = aArmor;
	}
	
	/**
	 * Creates a new armor item.
	 * 
	 * @param aName
	 *            The item name.
	 * @param aWeight
	 *            The item weight.
	 * @param aArmor
	 *            The armor value.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The parent controller.
	 */
	public Armor(final String aName, final int aWeight, final int aArmor, final Context aContext, final InventoryControllerInstance aController)
	{
		super(aName, aWeight, aContext, aController);
		mArmor = aArmor;
	}
	
	/**
	 * @return the armor value.
	 */
	public int getArmor()
	{
		return mArmor;
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = super.asElement(aDoc);
		element.setAttribute("armor", "" + getArmor());
		return element;
	}
	
	@Override
	public String[] getInfoArray(final boolean aQuantity)
	{
		final List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(super.getInfoArray(aQuantity)));
		list.add(", ");
		list.add("" + R.string.armor);
		list.add(": " + getArmor());
		return list.toArray(new String[list.size()]);
	}
	
	@Override
	public boolean[] getInfoTranslatedArray(final boolean aQuantity)
	{
		final StringBuilder flags = new StringBuilder();
		flags.append(DataUtil.parseFlags(super.getInfoTranslatedArray(aQuantity)));
		flags.append("010");
		return DataUtil.parseFlags(flags.toString());
	}
	
	@Override
	public String getType()
	{
		return ARMOR_ITEM_TYPE;
	}
}
