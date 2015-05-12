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

/**
 * A deprecated item provider implementation, that uses the device file,<br>
 * by loading it every time, the application starts.
 * 
 * @author vrl
 */
@Deprecated
public class ItemProviderImpl implements ItemProvider
{
	private final ClanController		mClans;
	
	private final DescriptionController	mDescriptions;
	
	private final NatureController		mNatures;
	
	private final List<ItemController>	mControllers;
	
	private final Health				mHealth;
	
	private final Money					mMoney;
	
	private final Inventory				mInventory;
	
	private final String				mGenerationItem;
	
	/**
	 * Creates a new item provider.
	 * 
	 * @param aContext
	 *            The underlying context.
	 */
	public ItemProviderImpl(final Context aContext)
	{
		LanguageUtil.init(aContext);
		mClans = DataUtil.loadClans(aContext);
		mControllers = DataUtil.loadItems(aContext);
		mNatures = new NatureController(aContext.getResources());
		mDescriptions = new DescriptionController(aContext.getResources());
		mHealth = DataUtil.loadHealth(aContext);
		mMoney = DataUtil.loadMoney(aContext);
		mInventory = DataUtil.loadInventory(aContext);
		mGenerationItem = DataUtil.loadGenerationItem(aContext);
	}
	
	@Override
	public ClanController getClans()
	{
		return mClans;
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
	public String getGenerationItem()
	{
		return mGenerationItem;
	}
	
	@Override
	public Health getHealth()
	{
		return mHealth;
	}
	
	@Override
	public Inventory getInventory()
	{
		return mInventory;
	}
	
	@Override
	public Money getMoney()
	{
		return mMoney;
	}
	
	@Override
	public NatureController getNatures()
	{
		return mNatures;
	}
}
