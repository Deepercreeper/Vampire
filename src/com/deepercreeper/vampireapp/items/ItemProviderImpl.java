package com.deepercreeper.vampireapp.items;

import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.character.Health;
import com.deepercreeper.vampireapp.character.Inventory;
import com.deepercreeper.vampireapp.character.Money;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.lists.controllers.ClanController;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionController;
import com.deepercreeper.vampireapp.lists.controllers.NatureController;
import com.deepercreeper.vampireapp.util.DataUtil;
import com.deepercreeper.vampireapp.util.LanguageUtil;

public class ItemProviderImpl implements ItemProvider
{
	private final ClanController		mClans;
	
	private final DescriptionController	mDescriptions;
	
	private final NatureController		mNatures;
	
	private final List<ItemController>	mControllers;
	
	private final Health				mHealth;
	
	private final Money					mMoney;
	
	private final Inventory				mInventory;
	
	public ItemProviderImpl(final Context aContext)
	{
		LanguageUtil.init(aContext);
		mClans = DataUtil.createClans(aContext);
		mControllers = DataUtil.createItems(aContext);
		mNatures = new NatureController(aContext.getResources());
		mDescriptions = new DescriptionController(aContext.getResources());
		mHealth = DataUtil.loadHealth(aContext);
		mMoney = DataUtil.loadMoney(aContext);
		mInventory = DataUtil.loadInventory(aContext);
	}
	
	@Override
	public ClanController getClans()
	{
		return mClans;
	}
	
	@Override
	public List<ItemController> getControllers()
	{
		return mControllers;
	}
	
	@Override
	public DescriptionController getDescriptions()
	{
		return mDescriptions;
	}
	
	@Override
	public NatureController getNatures()
	{
		return mNatures;
	}
	
	@Override
	public Health getHealth()
	{
		return mHealth;
	}
	
	@Override
	public Money getMoney()
	{
		return mMoney;
	}
	
	@Override
	public Inventory getInventory()
	{
		return mInventory;
	}
	
	@Override
	public ItemController getController(final String aName)
	{
		for (final ItemController controller : getControllers())
		{
			if (controller.getName().equals(aName))
			{
				return controller;
			}
		}
		return null;
	}
}
