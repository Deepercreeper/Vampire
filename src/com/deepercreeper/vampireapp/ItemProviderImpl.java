package com.deepercreeper.vampireapp;

import java.util.List;
import android.content.Context;
import com.deepercreeper.vampireapp.controllers.descriptions.DescriptionController;
import com.deepercreeper.vampireapp.controllers.dynamic.Creator;
import com.deepercreeper.vampireapp.controllers.dynamic.interfaces.ItemController;
import com.deepercreeper.vampireapp.controllers.lists.ClanController;
import com.deepercreeper.vampireapp.controllers.lists.NatureController;
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
		mClans = Creator.createClans(aContext);
		mControllers = Creator.createItems(aContext);
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
