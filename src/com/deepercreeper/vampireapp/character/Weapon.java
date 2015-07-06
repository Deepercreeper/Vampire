package com.deepercreeper.vampireapp.character;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.InventoryControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.util.ItemFinder;

/**
 * This is a sub type of an inventory item and represents a weapon.
 * 
 * @author vrl
 */
public class Weapon extends InventoryItem
{
	private final ItemFinder	mItems;
	
	private int					mDifficulty;
	
	private int					mDamage;
	
	private final ItemInstance	mAdditionalDamage;
	
	private String				mStash;
	
	private int					mDistance;
	
	private int					mReloadTime;
	
	private int					mMagazine;
	
	private String				mAmmo;
	
	/**
	 * Creates a weapon out of the given XML data.
	 * 
	 * @param aElement
	 *            The XML data.
	 * @param aItems
	 *            The item finder.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The parent inventory controller.
	 */
	public Weapon(final Element aElement, final ItemFinder aItems, final Context aContext, final InventoryControllerInstance aController)
	{
		super(aElement.getAttribute("name"), Integer.parseInt(aElement.getAttribute("weight")), Integer.parseInt(aElement.getAttribute("quantity")),
				aContext, aController);
		mItems = aItems;
		mDifficulty = Integer.parseInt(aElement.getAttribute("difficulty"));
		mDamage = Integer.parseInt(aElement.getAttribute("damage"));
		mStash = aElement.getAttribute("stash");
		mDistance = Integer.parseInt(aElement.getAttribute("distance"));
		mReloadTime = Integer.parseInt(aElement.getAttribute("reloadTime"));
		mMagazine = Integer.parseInt(aElement.getAttribute("magazine"));
		mAmmo = aElement.getAttribute("ammo");
		if (aElement.hasAttribute("additionalDamage"))
		{
			mAdditionalDamage = mItems.findItem(aElement.getAttribute("additionalDamage"));
		}
		else
		{
			mAdditionalDamage = null;
		}
	}
	
	/**
	 * Creates a new weapon.
	 * 
	 * @param aName
	 *            The weapon name.
	 * @param aWeight
	 *            The weapon weight.
	 * @param aQuantity
	 *            The quantity of this weapon.
	 * @param aDifficulty
	 *            the difficulty of this weapon.
	 * @param aDamage
	 *            The damage this weapon takes.
	 * @param aAdditionalDamage
	 *            Additional damage, that is dependent on an item..
	 * @param aStash
	 *            The place, this weapon can be stored.
	 * @param aDistance
	 *            The maximum distance. {@code -1} for melee weapons.
	 * @param aReloadTime
	 *            The number of rounds it takes to reload this weapon. {@code -1} for melee weapons.
	 * @param aMagazine
	 *            The number of bullets or arrows etc. that can be fired before reload is necessary. {@code -1} for melee weapons.
	 * @param aAmmo
	 *            The ammo type of this weapon. {@code null} for melee weapons.
	 * @param aItems
	 *            The item finder.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The parent inventory controller.
	 */
	public Weapon(final String aName, final int aWeight, final int aQuantity, final int aDifficulty, final int aDamage,
			final String aAdditionalDamage, final String aStash, final int aDistance, final int aReloadTime, final int aMagazine, final String aAmmo,
			final ItemFinder aItems, final Context aContext, final InventoryControllerInstance aController)
	{
		super(aName, aWeight, aQuantity, aContext, aController);
		mDifficulty = aDifficulty;
		mDamage = aDamage;
		mStash = aStash;
		mDistance = aDistance;
		mReloadTime = aReloadTime;
		mMagazine = aMagazine;
		mAmmo = aAmmo;
		mItems = aItems;
		if (aAdditionalDamage != null)
		{
			mAdditionalDamage = mItems.findItem(aAdditionalDamage);
		}
		else
		{
			mAdditionalDamage = null;
		}
	}
	
	/**
	 * Creates a new melee weapon.
	 * 
	 * @param aName
	 *            The weapon name.
	 * @param aWeight
	 *            The weapon weight.
	 * @param aQuantity
	 *            The quantity of this weapon.
	 * @param aDifficulty
	 *            the difficulty of this weapon.
	 * @param aDamage
	 *            The damage this weapon takes.
	 * @param aAdditionalDamage
	 *            Additional damage that is dependent on an item.
	 * @param aStash
	 *            The place, this weapon can be stored.
	 * @param aItems
	 *            The item finder.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The parent inventory controller.
	 */
	public Weapon(final String aName, final int aWeight, final int aQuantity, final int aDifficulty, final int aDamage,
			final String aAdditionalDamage, final String aStash, final ItemFinder aItems, final Context aContext,
			final InventoryControllerInstance aController)
	{
		this(aName, aWeight, aQuantity, aDifficulty, aDamage, aAdditionalDamage, aStash, -1, -1, -1, null, aItems, aContext, aController);
	}
	
	/**
	 * Creates a new weapon.
	 * 
	 * @param aName
	 *            The weapon name.
	 * @param aWeight
	 *            The weapon weight.
	 * @param aDifficulty
	 *            the difficulty of this weapon.
	 * @param aDamage
	 *            The damage this weapon takes.
	 * @param aAdditionalDamage
	 *            Additional damage, that is dependent on an item..
	 * @param aStash
	 *            The place, this weapon can be stored.
	 * @param aDistance
	 *            The maximum distance. {@code -1} for melee weapons.
	 * @param aReloadTime
	 *            The number of rounds it takes to reload this weapon. {@code -1} for melee weapons.
	 * @param aMagazine
	 *            The number of bullets or arrows etc. that can be fired before reload is necessary. {@code -1} for melee weapons.
	 * @param aAmmo
	 *            The ammo type of this weapon. {@code null} for melee weapons.
	 * @param aItems
	 *            The item finder.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The parent inventory controller.
	 */
	public Weapon(final String aName, final int aWeight, final int aDifficulty, final int aDamage, final String aAdditionalDamage,
			final String aStash, final int aDistance, final int aReloadTime, final int aMagazine, final String aAmmo, final ItemFinder aItems,
			final Context aContext, final InventoryControllerInstance aController)
	{
		this(aName, aWeight, 1, aDifficulty, aDamage, aAdditionalDamage, aStash, aDistance, aReloadTime, aMagazine, aAmmo, aItems, aContext,
				aController);
	}
	
	/**
	 * Creates a new melee weapon.
	 * 
	 * @param aName
	 *            The weapon name.
	 * @param aWeight
	 *            The weapon weight.
	 * @param aDifficulty
	 *            the difficulty of this weapon.
	 * @param aDamage
	 *            The damage this weapon takes.
	 * @param aAdditionalDamage
	 *            Additional damage that is dependent on an item.
	 * @param aStash
	 *            The place, this weapon can be stored.
	 * @param aItems
	 *            The item finder.
	 * @param aContext
	 *            The underlying context.
	 * @param aController
	 *            The parent inventory controller.
	 */
	public Weapon(final String aName, final int aWeight, final int aDifficulty, final int aDamage, final String aAdditionalDamage,
			final String aStash, final ItemFinder aItems, final Context aContext, final InventoryControllerInstance aController)
	{
		this(aName, aWeight, 1, aDifficulty, aDamage, aAdditionalDamage, aStash, aItems, aContext, aController);
	}
	
	@Override
	public Element asElement(final Document aDoc)
	{
		final Element element = aDoc.createElement("weapon");
		element.setAttribute("name", getName());
		element.setAttribute("weight", "" + getWeight());
		element.setAttribute("difficulty", "" + getDifficulty());
		element.setAttribute("damage", "" + getDamage());
		if (mAdditionalDamage != null)
		{
			element.setAttribute("additionalDamage", mAdditionalDamage.getName());
		}
		element.setAttribute("stash", getStash());
		element.setAttribute("distance", "" + getDistance());
		element.setAttribute("reloadTime", "" + getReloadTime());
		element.setAttribute("magazine", "" + getMagazine());
		element.setAttribute("ammo", getAmmo());
		return element;
	}
	
	/**
	 * @return the current value of the item, that causes additional damage or {@code 0} if there is no additional damage.
	 */
	public int getAdditionalDamage()
	{
		if (mAdditionalDamage == null)
		{
			return 0;
		}
		return mAdditionalDamage.getValue();
	}
	
	/**
	 * @return The type of ammo this weapon needs or {@code null} if this is a melee weapon.
	 */
	public String getAmmo()
	{
		return mAmmo;
	}
	
	/**
	 * @return the default damage, the weapon takes.
	 */
	public int getDamage()
	{
		return mDamage;
	}
	
	/**
	 * @return The difficulty of using this weapon.
	 */
	public int getDifficulty()
	{
		return mDifficulty;
	}
	
	/**
	 * @return the maximum distance of this weapon or {@code -1} if this i a melee weapon.
	 */
	public int getDistance()
	{
		return mDistance;
	}
	
	@Override
	public String getInfo(final boolean aQuantity)
	{
		final StringBuilder info = new StringBuilder();
		info.append(getName() + (aQuantity ? getQuantity() : "") + ": ");
		info.append(getContext().getString(R.string.weight) + ": " + getWeight() + " " + getContext().getString(R.string.weight_unit) + ", ");
		info.append(getContext().getString(R.string.difficulty) + ": " + getDifficulty() + ", ");
		info.append(getContext().getString(R.string.damage) + ": " + getDamage() + ", ");
		if (mAdditionalDamage != null)
		{
			info.append(getContext().getString(R.string.additional_damage) + ": " + mAdditionalDamage.getName() + ", ");
		}
		info.append(getContext().getString(R.string.stash) + ": " + getStash() + ", ");
		
		if (getDistance() != -1)
		{
			info.append(getContext().getString(R.string.distance) + ": " + getDistance() + ", ");
		}
		if (getReloadTime() != -1)
		{
			info.append(getContext().getString(R.string.reload_time) + ": " + getReloadTime() + ", ");
		}
		if (getMagazine() != -1)
		{
			info.append(getContext().getString(R.string.magazine) + ": " + getMagazine() + ", ");
		}
		if (getDistance() != -1)
		{
			info.append(getContext().getString(R.string.ammo) + ": " + getAmmo() + ", ");
		}
		info.delete(info.length() - 2, info.length());
		return info.toString();
	}
	
	/**
	 * @return the maximum number of bullets that can be stored, before reload is necessary or {@code -1} if this is a melee weapon.
	 */
	public int getMagazine()
	{
		return mMagazine;
	}
	
	/**
	 * @return the number of rounds necessary to reload this weapon or {@code -1} if this is a melee weapon.
	 */
	public int getReloadTime()
	{
		return mReloadTime;
	}
	
	/**
	 * @return the place, this weapon can be stored.
	 */
	public String getStash()
	{
		return mStash;
	}
	
	/**
	 * Sets the ammo type for non melee weapons.
	 * 
	 * @param aAmmo
	 *            The new ammo type or {@code null} if this is a melee weapon.
	 */
	public void setAmmo(final String aAmmo)
	{
		mAmmo = aAmmo;
	}
	
	/**
	 * Sets the damage this weapon takes.
	 * 
	 * @param aDamage
	 *            The new number of instant damage points this weapon takes.
	 */
	public void setDamage(final int aDamage)
	{
		mDamage = aDamage;
	}
	
	/**
	 * Sets the weapons difficulty.
	 * 
	 * @param aDifficulty
	 *            The new minimum value each dice has to count to add a hit.
	 */
	public void setDifficulty(final int aDifficulty)
	{
		mDifficulty = aDifficulty;
	}
	
	/**
	 * Sets the new maximum distance for non melee weapons.
	 * 
	 * @param aDistance
	 *            The new distance in meters or {@code -1} if this is a melee weapon.
	 */
	public void setDistance(final int aDistance)
	{
		mDistance = aDistance;
	}
	
	/**
	 * Sets the magazine size for non melee weapons.
	 * 
	 * @param aMagazine
	 *            The new number of bullets, arrows, etc. a character can shoot before reloading<br>
	 *            or {@code -1} if this is a melee weapon.
	 */
	public void setMagazine(final int aMagazine)
	{
		mMagazine = aMagazine;
	}
	
	/**
	 * Sets the reload time for non melee weapons.
	 * 
	 * @param aReloadTime
	 *            the new number of rounds used to reload or {@code -1} if this is a melee weapon.
	 */
	public void setReloadTime(final int aReloadTime)
	{
		mReloadTime = aReloadTime;
	}
	
	/**
	 * Sets the new weapon storing place.
	 * 
	 * @param aStash
	 *            The place where to store this weapon.
	 */
	public void setStash(final String aStash)
	{
		mStash = aStash;
	}
}
