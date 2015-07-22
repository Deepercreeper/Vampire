package com.deepercreeper.vampireapp.character.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.instance.InventoryControllerInstance;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.util.CodingUtil;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.interfaces.ItemFinder;
import android.content.Context;

/**
 * This is a sub type of an inventory item and represents a weapon.
 * 
 * @author vrl
 */
public class Weapon extends Artifact
{
	/**
	 * The type that defines items to be weapons.
	 */
	public static final String WEAPON_ITEM_TYPE = "weapon";
	
	private final ItemFinder mItems;
	
	private final ItemInstance mAdditionalDamage;
	
	private final boolean mDistanceWeapon;
	
	private final int mDifficulty;
	
	private final int mDamage;
	
	private final String mStash;
	
	private final int mDistance;
	
	private final int mReloadTime;
	
	private final int mMagazine;
	
	private final String mAmmo;
	
	protected Weapon(final Element aElement, final ItemFinder aItems, final Context aContext, final InventoryControllerInstance aController)
	{
		super(CodingUtil.decode(aElement.getAttribute("name")), Integer.parseInt(aElement.getAttribute("weight")),
				Integer.parseInt(aElement.getAttribute("quantity")), aContext, aController);
		mItems = aItems;
		mDistanceWeapon = Boolean.valueOf(aElement.getAttribute("distance-weapon"));
		mDifficulty = Integer.parseInt(aElement.getAttribute("difficulty"));
		mDamage = Integer.parseInt(aElement.getAttribute("damage"));
		mStash = CodingUtil.decode(aElement.getAttribute("stash"));
		if (mDistanceWeapon)
		{
			mDistance = Integer.parseInt(aElement.getAttribute("distance"));
			mReloadTime = Integer.parseInt(aElement.getAttribute("reloadTime"));
			mMagazine = Integer.parseInt(aElement.getAttribute("magazine"));
			mAmmo = CodingUtil.decode(aElement.getAttribute("ammo"));
		}
		else
		{
			mDistance = mReloadTime = mMagazine = -1;
			mAmmo = null;
		}
		if (aElement.hasAttribute("additionalDamage"))
		{
			mAdditionalDamage = mItems.findItemInstance(CodingUtil.decode(aElement.getAttribute("additionalDamage")));
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
		mDistanceWeapon = mDistance != -1 && mReloadTime != -1 && mMagazine != -1 && mAmmo != null;
		mItems = aItems;
		if (aAdditionalDamage != null)
		{
			mAdditionalDamage = mItems.findItemInstance(aAdditionalDamage);
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
		final Element element = super.asElement(aDoc);
		element.setAttribute("difficulty", "" + getDifficulty());
		element.setAttribute("damage", "" + getDamage());
		if (mAdditionalDamage != null)
		{
			element.setAttribute("additionalDamage", CodingUtil.encode(mAdditionalDamage.getName()));
		}
		element.setAttribute("stash", CodingUtil.encode(getStash()));
		element.setAttribute("distance-weapon", "" + mDistanceWeapon);
		if (mDistanceWeapon)
		{
			element.setAttribute("distance", "" + getDistance());
			element.setAttribute("reloadTime", "" + getReloadTime());
			element.setAttribute("magazine", "" + getMagazine());
			element.setAttribute("ammo", CodingUtil.encode(getAmmo()));
		}
		return element;
	}
	
	@Override
	public String[] getInfoArray()
	{
		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(super.getInfoArray()));
		list.add(", ");
		list.add("" + R.string.difficulty);
		list.add(": " + getDifficulty() + ", ");
		list.add("" + R.string.damage);
		list.add(": " + getDamage() + ", ");
		if (mAdditionalDamage != null)
		{
			list.add("" + R.string.additional_damage);
			list.add(": ");
			list.add(mAdditionalDamage.getName());
			list.add(", ");
		}
		list.add("" + R.string.stash);
		list.add(": " + getStash());
		if (mDistanceWeapon)
		{
			list.add(", ");
			list.add("" + R.string.distance);
			list.add(": " + getDistance() + ", ");
			list.add("" + R.string.magazine);
			list.add(": " + getMagazine() + ", ");
			list.add("" + R.string.reload_time);
			list.add(": " + getReloadTime() + ", ");
			list.add("" + R.string.ammo);
			list.add(": " + getAmmo());
		}
		return list.toArray(new String[list.size()]);
	}
	
	@Override
	public boolean[] getInfoTranslatedArray()
	{
		StringBuilder flags = new StringBuilder();
		flags.append(DataUtil.parseFlags(super.getInfoTranslatedArray()));
		flags.append("01010");
		if (mAdditionalDamage != null)
		{
			flags.append("1010");
		}
		flags.append("10");
		if (mDistanceWeapon)
		{
			flags.append("010101010");
		}
		return DataUtil.parseFlags(flags.toString());
	}
	
	@Override
	public String getType()
	{
		return WEAPON_ITEM_TYPE;
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
}
