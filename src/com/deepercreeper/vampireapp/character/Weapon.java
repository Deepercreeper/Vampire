package com.deepercreeper.vampireapp.character;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.content.Context;
import com.deepercreeper.vampireapp.R;
import com.deepercreeper.vampireapp.character.controllers.InventoryController;
import com.deepercreeper.vampireapp.items.interfaces.instances.ItemInstance;
import com.deepercreeper.vampireapp.mechanics.Action.ItemFinder;

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
	
	public Weapon(final String aName, final int aWeight, final int aDifficulty, final int aDamage, final String aAdditionalDamage,
			final String aStash, final int aDistance, final int aReloadTime, final int aMagazine, final String aAmmo, final ItemFinder aItems,
			final Context aContext, final InventoryController aController)
	{
		super(aName, aWeight, aContext, aController);
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
	
	public Weapon(final String aName, final int aWeight, final int aDifficulty, final int aDamage, final String aAdditionalDamage,
			final String aStash, final ItemFinder aItems, final Context aContext, final InventoryController aController)
	{
		this(aName, aWeight, aDifficulty, aDamage, aAdditionalDamage, aStash, -1, -1, -1, null, aItems, aContext, aController);
	}
	
	public Weapon(final Element aElement, final ItemFinder aItems, final Context aContext, final InventoryController aController)
	{
		super(aElement.getAttribute("name"), Integer.parseInt(aElement.getAttribute("weight")), aContext, aController);
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
	
	public int getDifficulty()
	{
		return mDifficulty;
	}
	
	public int getAdditionalDamage()
	{
		return mAdditionalDamage.getValue();
	}
	
	public int getDamage()
	{
		return mDamage;
	}
	
	public String getStash()
	{
		return mStash;
	}
	
	public String getAmmo()
	{
		return mAmmo;
	}
	
	public int getDistance()
	{
		return mDistance;
	}
	
	public int getMagazine()
	{
		return mMagazine;
	}
	
	public int getReloadTime()
	{
		return mReloadTime;
	}
	
	public void setAmmo(final String aAmmo)
	{
		mAmmo = aAmmo;
	}
	
	public void setDamage(final int aDamage)
	{
		mDamage = aDamage;
	}
	
	public void setDifficulty(final int aDifficulty)
	{
		mDifficulty = aDifficulty;
	}
	
	public void setDistance(final int aDistance)
	{
		mDistance = aDistance;
	}
	
	public void setMagazine(final int aMagazine)
	{
		mMagazine = aMagazine;
	}
	
	public void setReloadTime(final int aReloadTime)
	{
		mReloadTime = aReloadTime;
	}
	
	public void setStash(final String aStash)
	{
		mStash = aStash;
	}
	
	@Override
	public String getInfo()
	{
		final StringBuilder info = new StringBuilder();
		info.append(getName() + ": ");
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
}
