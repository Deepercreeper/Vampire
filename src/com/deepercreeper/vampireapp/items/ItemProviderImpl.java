package com.deepercreeper.vampireapp.items;

import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.items.interfaces.ItemController;
import com.deepercreeper.vampireapp.lists.controllers.ClanController;
import com.deepercreeper.vampireapp.lists.controllers.DescriptionController;
import com.deepercreeper.vampireapp.lists.controllers.NatureController;
import com.deepercreeper.vampireapp.util.ItemUtil;
import com.deepercreeper.vampireapp.util.LanguageUtil;

public class ItemProviderImpl implements ItemProvider
{
	private final ClanController		mClans;
	
	private final DescriptionController	mDescriptions;
	
	private final NatureController		mNatures;
	
	private final List<ItemController>	mControllers;
	
	public ItemProviderImpl(final Context aContext)
	{
		LanguageUtil.init(aContext);
		mClans = ItemUtil.createClans(aContext);
		mControllers = ItemUtil.createItems(aContext);
		mNatures = new NatureController(aContext.getResources());
		mDescriptions = new DescriptionController(aContext.getResources());
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
